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
import entities.StudentEntity;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Map<Long, Session> sessions = new HashMap<>();
    private final Map<Long, Long> groups = new HashMap<>();
    private final Map<Long, List<MessageEntity>> activeChats = new HashMap<>();

    public MessageSessionHandler() {
        this.groupEntitySessionBean = new SessionBeanLookup().lookupGroupEntitySessionBeanLocal();
    }

    public void addSession(Long studentId, Long groupId, Session session) {
        sessions.put(studentId, session);
        groups.put(studentId, groupId);
    }

    public void removeSession(Long studentId) {
        sessions.remove(studentId);
        groups.remove(studentId);
    }

    public void addMessage(MessageEntity message) {
        try {
            Long messageId = groupEntitySessionBean.addNewMessage(message);
            Long groupId = message.getGroup().getGroupId();
            message.setMessageId(messageId);
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);
            for (StudentEntity student : group.getGroupMembers()) {
                Long studentId = student.getAccountId();
                System.out.printf("group[%d] with student[%d]%n", groupId, studentId);
                Session ss = sessions.get(studentId);
                Long stuGroupId = groups.get(studentId);
                System.out.printf("This student is subcribing to group[%d] %n", stuGroupId);
                System.out.println("This student's session: " + ss);
                if (ss != null && groupId.equals(stuGroupId)) {
                    System.out.printf("Message forwarding to student[%d] %n", studentId);
                    sendMessage(studentId, ss, message);
                }
            }
        } catch (DoesNotExistException ex) {
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendMessage(Long studentId, Session session, MessageEntity message) {
        try {
            JsonObject sender = Json.createObjectBuilder()
                    .add("accountId", message.getSender().getAccountId())
                    .build();
            JsonObject group = Json.createObjectBuilder()
                    .add("groupId", message.getGroup().getGroupId())
                    .build();
            
            System.out.println(message);
            
            String response = Json.createObjectBuilder()
                    .add("messageId", message.getMessageId())
                    .add("action", "response")
                    .add("sender", sender)
                    .add("groupId", group)
                    .add("content", message.getContent())
                    .build().toString();
            
//            String response = new ObjectMapper().writeValueAsString(message);
            
            session.getBasicRemote().sendText(response);
            System.out.println(response);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            removeSession(studentId);
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            removeSession(studentId);
        }
    }

}
