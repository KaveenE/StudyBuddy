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
    
@ApplicationScoped
@ServerEndpoint("/messages")
public class MessageWebsocketServer {

    @Inject
    private MessageSessionHandler messageSessionHandler;

    @OnOpen
    public void open(Session session) {
    }

    @OnClose
    public void close(Session session) {
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(MessageWebsocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("register".equals(jsonMessage.getString("type"))) {
                Long studentId = Long.valueOf(jsonMessage.getString("studentId"));
                Long groupId = Long.valueOf(jsonMessage.getString("groupId"));
                messageSessionHandler.addSession(studentId, groupId, session);
            }
            if ("close".equals(jsonMessage.getString("type"))) {
                Long studentId = Long.valueOf(jsonMessage.getString("studentId"));
                messageSessionHandler.removeSession(studentId);
            } else if ("send".equals(jsonMessage.getString("type"))) {
                MessageEntity messageEntity = new MessageEntity();
                GroupEntity group = new GroupEntity();
                StudentEntity sender = new StudentEntity();
                group.setGroupId(Long.valueOf(jsonMessage.getJsonObject("group").getString("groupId")));
                sender.setAccountId(Long.valueOf(jsonMessage.getJsonObject("sender").getString("accountId")));

                messageEntity.setGroup(group);
                messageEntity.setSender(sender);

                messageEntity.setContent(jsonMessage.getString("content"));

                messageSessionHandler.addMessage(messageEntity);
            }
        }
    }

}
