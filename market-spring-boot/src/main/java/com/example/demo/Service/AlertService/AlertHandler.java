package com.example.demo.Service.AlertService;

import com.example.demo.Service.IMarket;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import com.example.demo.api.apiObjects.apiUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class AlertHandler {
    private static final Logger logger = Logger.getLogger(AlertHandler.class);

    private final NotificationDispatcher dispatcher;
    ConcurrentHashMap<String, List<Notification>> delayedNotification; //username-notifications
    ConcurrentHashMap<String, String> sessionMapper; //uuid-sessionID

    @Autowired
    public AlertHandler(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        delayedNotification = new ConcurrentHashMap<>();
        sessionMapper = new ConcurrentHashMap<>();
    }

    /**
     * used to send can be either Guest or Logged in Member.
     * @param uuid         this is the sessionID when a user connects to the system
     * @param notification the message to pop to the client through the websocket
     * @return true if success, else false
     */
    public boolean notifyUser(UUID uuid, Notification notification) {
        if (sessionMapper.containsKey(uuid.toString())) {
            String sessionID = sessionMapper.get(uuid.toString());
            if (!dispatcher.addNotification(sessionID, notification)) {
                logger.error(String.format("failed to notify %s", uuid));
                return false;
            }
            return true;
        }
        logger.error("didnt find sessionID in sessionMapper");
        return false;
    }

    /**
     * used to send for e member that is not connected to the system yet.
     *
     * Note: in the future this will go to DB
     * @param username
     * @param notification
     * @return
     */
    public boolean notifyUser(String username, Notification notification) {
        if (delayedNotification.containsKey(username)) {
            delayedNotification.get(username).add(notification);
        } else {
            delayedNotification.put(username, List.of(notification));
        }
        return true;
    }

    /**
     * called from NotificationController when start() was called.
     *
     * @param uuid      the id that the user sent
     * @param sessionID the sessionID that WebSocket generated
     */
    public void addListener(String uuid, String sessionID) {
        sessionMapper.put(uuid, sessionID);
        dispatcher.addNewSession(sessionID);
    }

    /**
     * called from NotificationController when stop() was called.
     *
     * @param uuid      the id that the user sent
     * @param sessionID
     */
    public void removeListener(String uuid, String sessionID) {
        sessionMapper.remove(uuid);
        dispatcher.removeSession(sessionID);
    }

    /**
     * this function is called when a member is logged in
     * @param username
     * @param uuid
     */
    public void modifyDelayIfExist(String username, UUID uuid) {
        if (delayedNotification.containsKey(username)) {
            String sessionID = sessionMapper.get(uuid.toString());
            dispatcher.importDelayedNotifications(sessionID, delayedNotification.remove(username));
            logger.info(String.format("imported all delayed notifications of %s", username));
            return;
        }
        logger.info(String.format("user %s didn't have pending notifications", username));
    }

    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        logger.info("Disconnecting " + sessionId + "!");
        boolean res = dispatcher.removeSession(sessionId);
        if (!res) {
            logger.error(String.format("didn't remove sessionID %s from disconnect event", sessionId));
        }
        logger.info(String.format("ended session %s on disconnect event", sessionId));
        for (Map.Entry<String, String> entry : sessionMapper.entrySet()) {
            if (entry.getValue().equals(sessionId)) {
                sessionMapper.remove(entry.getKey());
                logger.info("removed session from disconnect event");
                return;
            }
        }
        logger.error("didn't find session on disconnect event");
    }

    public void add(String a){
        System.out.printf("add : %s%n",a);
    }
    public void remove(String a){
        System.out.printf("remove : %s%n",a);
    }

}
