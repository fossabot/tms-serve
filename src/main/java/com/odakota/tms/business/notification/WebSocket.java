package com.odakota.tms.business.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionPool = new HashMap<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            webSockets.add(this);
            sessionPool.put(userId, session);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message) {
    }

    // this is a broadcast message
    public void sendAllMessage(String message) {
        for (WebSocket webSocket : webSockets) {
            try {
                if (webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    // this is a single point message
    public void sendOneMessage(String userId, String message) {
        Session ses = sessionPool.get(userId);
        if (ses != null && ses.isOpen()) {
            try {
                ses.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    // this is a single point message multiple
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session ses = sessionPool.get(userId);
            if (ses != null && ses.isOpen()) {
                try {
                    ses.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
