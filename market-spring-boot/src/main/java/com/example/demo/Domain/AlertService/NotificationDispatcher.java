package com.example.demo.Domain.AlertService;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationDispatcher {

    private static final Logger logger = Logger.getLogger(NotificationDispatcher.class);

    private final SimpMessagingTemplate template;

    private final ConcurrentHashMap<String,List<Notification>> realTimeListeners; //sessionID-notifications

    public NotificationDispatcher(SimpMessagingTemplate template) {
        this.template = template;
        realTimeListeners = new ConcurrentHashMap<>();
    }

    public boolean addNewSession(String sessionId) {
        if(realTimeListeners.containsKey(sessionId)){
            logger.error(String.format("sessionID %s already exists",sessionId));
            return false;
        }
        realTimeListeners.put(sessionId,new ArrayList<>());
        logger.info(String.format("added new session %s",sessionId));
        return true;
    }

    public boolean removeSession(String sessionId) {
        if(!realTimeListeners.containsKey(sessionId)){
            logger.error(String.format("sessionID %s doesn't exists",sessionId));
            return false;
        }
        realTimeListeners.remove(sessionId);
        logger.info(String.format("removed session %s",sessionId));
        return true;
    }

    @Scheduled(fixedDelay = 2000)
    public void dispatch() {
        for (Map.Entry<String,List<Notification>> entry : realTimeListeners.entrySet()) {
            List<Notification> notifications = entry.getValue();
            String sessionID = entry.getKey();
            logger.info("Sending notification to " + sessionID);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(sessionID);
            headerAccessor.setLeaveMutable(true);

            for(Notification notification : notifications){
            template.convertAndSendToUser(
                    sessionID,
                    "/notification/item",
                    notification,
                    headerAccessor.getMessageHeaders());
            }
            entry.getValue().clear(); //dispose all messages for that person ->maybe can add to history in DB
        }
    }


    public boolean addNotification(String sessionID, Notification notification) {
        if(realTimeListeners.containsKey(sessionID)){
            realTimeListeners.get(sessionID).add(notification);
            logger.info(String.format("added notification to session %s",sessionID));
            return true;
        }
        else {
            logger.error("sessionID is not in map");
            return false;
        }
    }

    public void importDelayedNotifications(String sessionID,List<Notification> notifications){
        if(!realTimeListeners.containsKey(sessionID)){
            logger.error("the user that had delayed notifications has not set up the session on connection");
            return;
        }
        realTimeListeners.get(sessionID).addAll(notifications);
        logger.info("added all delayed notifications of user to real time map");
    }


}