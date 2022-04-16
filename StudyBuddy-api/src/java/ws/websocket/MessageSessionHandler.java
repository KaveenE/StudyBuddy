/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import ejb.session.stateless.GroupEntitySessionBeanLocal;
import entities.GroupEntity;
import entities.MessageEntity;
import static entities.ModuleEntity_.groups;
import entities.StudentEntity;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import ws.restful.SessionBeanLookup;

/**
 *
 * @author larby
 */
@ApplicationScoped
public class MessageSessionHandler {

    private final GroupEntitySessionBeanLocal groupEntitySessionBean;

    private final Map<Long, Set<Session>> sessions = new HashMap<>();
    private final Map<Session, Long> group = new HashMap<>();
    
    private final Set<Session> sessionsToRemove = new HashSet<>();

    public MessageSessionHandler() {
        this.groupEntitySessionBean = new SessionBeanLookup().lookupGroupEntitySessionBeanLocal();
    }

    public void addSession(Long groupId, Session session) {
        this.sessions.putIfAbsent(groupId, new HashSet<>());
        Set<Session> groupSession = this.sessions.get(groupId);
        groupSession.add(session);

        this.group.put(session, groupId);
    }

    public void removeSession(Session session) {
        Long groupId = this.group.remove(session);
        this.sessions.get(groupId).remove(session);
    }

    public void addMessage(MessageEntity message) {
        try {
            Long messageId = groupEntitySessionBean.addNewMessage(message);
            message = groupEntitySessionBean.retrieveMessageEntityById(messageId);
            Long groupId = message.getGroup().getGroupId();
            message.setMessageId(messageId);

            Set<Session> target = this.sessions.getOrDefault(groupId, new HashSet<>());

            for (Session t : target) {
                System.out.println("This student's session: " + t);
                if (t != null) {
                    sendMessage(t, message);
                }
            }
        } catch (DoesNotExistException ex) {
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            sessionsToRemove.forEach((s) -> removeSession(s));
            sessionsToRemove.clear();
        }
    }

    public void broadCastFileMessage(MessageEntity message) {
        Long groupId = message.getGroup().getGroupId();
        Set<Session> target = this.sessions.getOrDefault(groupId, new HashSet<>());

        for (Session t : target) {
            System.out.println("This student's session: " + t);
            if (t != null) {
                sendMessage(t, message);
            }
        }
    }

    private void sendMessage(Session session, MessageEntity message) {
        try {
            JsonObject sender = Json.createObjectBuilder()
                    .add("accountId", message.getSender().getAccountId())
                    .build();
            JsonObject goroup = Json.createObjectBuilder()
                    .add("groupId", message.getGroup().getGroupId())
                    .build();
            JsonObject dateTimeCreated = Json.createObjectBuilder()
                    .add("hour", message.getDateTimeCreated().getHour())
                    .add("minute", message.getDateTimeCreated().getMinute())
                    .build();

            System.out.println(message);

            String response = Json.createObjectBuilder()
                    .add("messageId", message.getMessageId())
                    .add("mediaType", message.getMediaType().toString())
                    .add("action", "response")
                    .add("sender", sender)
                    .add("groupId", goroup)
                    .add("content", message.getContent())
                    .add("dateTimeCreated", dateTimeCreated)
                    .add("mediaType", message.getMediaType().toString())
                    .build().toString();

//            String response = new ObjectMapper().writeValueAsString(message);
            session.getBasicRemote().sendText(response);
            System.out.println(response);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            sessionsToRemove.add(session);
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            sessionsToRemove.add(session);
        }
    }

}
