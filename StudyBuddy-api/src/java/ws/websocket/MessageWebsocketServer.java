/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.websocket;

import entities.GroupEntity;
import entities.MessageEntity;
import entities.StudentEntity;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import util.enumeration.MediaType;

@ApplicationScoped
@ServerEndpoint("/messages")
public class MessageWebsocketServer {

    @Inject
    private MessageSessionHandler messageSessionHandler;

    @OnOpen
    public void open(Session session) {
        System.out.println("New Session opened: " + session.getId());
    }

    @OnClose
    public void close(Session session) {
        System.out.println("Session closed: " + session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(MessageWebsocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        System.out.println("Data sent:");
        System.out.println(message);

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            
            String action = jsonMessage.getString("action");
            Long studentId = jsonMessage.getJsonObject("sender").getJsonNumber("accountId").longValue();
            Long groupId = jsonMessage.getJsonObject("group").getJsonNumber("groupId").longValue();
            
            System.out.printf("%s from student[%d] to group [%d]", action, studentId, groupId);
            
            
            if ("register".equals(action)) {
                messageSessionHandler.addSession(groupId, session);
            }
            if ("close".equals(action)) {
                messageSessionHandler.removeSession(session);
            } else if ("send".equals(action)) {
                MessageEntity messageEntity = new MessageEntity();
                GroupEntity group = new GroupEntity();
                StudentEntity sender = new StudentEntity();
                sender.setAccountId(studentId);
                group.setGroupId(groupId);

                messageEntity.setGroup(group);
                messageEntity.setSender(sender);
                
                messageEntity.setMediaType(MediaType.valueOf(jsonMessage.getString("mediaType", "TEXT")));
                messageEntity.setContent(jsonMessage.getString("content"));

                messageSessionHandler.addMessage(messageEntity);
            }
        }
    }

}
