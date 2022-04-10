/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.GroupEntitySessionBeanLocal;
import entities.GroupEntity;
import entities.MessageEntity;
import entities.StudentEntity;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
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

    private GroupEntitySessionBeanLocal groupEntitySessionBean;
    
    private Map<Long,Session> sessions = new HashMap<>();
    private Map<Long,Long> groups = new HashMap<>();
    private Map<Long,List<MessageEntity>> activeChats = new HashMap<>();

    public MessageSessionHandler() {
        this.groupEntitySessionBean = new SessionBeanLookup().lookupGroupEntitySessionBeanLocal();
    }
    
    public void addSession(Long studentId, Long groupId, Session session) {
        sessions.put(studentId, session);
        groups.put(groupId, groupId);
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
                Session ss = sessions.get(studentId);
                Long stuGroupId = groups.get(studentId);
                if (ss != null && groupId.equals(stuGroupId)) {
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
            String response = new ObjectMapper().writeValueAsString(message);
            session.getBasicRemote().sendText(response);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            removeSession(studentId);
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
