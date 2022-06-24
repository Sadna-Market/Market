package com.example.demo.Domain.AlertService;

import com.example.demo.DataAccess.Entity.DataNotification;
import com.example.demo.DataAccess.Services.NotificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class AlertService implements IAlertService {

    private static final Logger logger = Logger.getLogger(AlertService.class);

    private NotificationDispatcher dispatcher;

    @Autowired
    private final NotificationService dataNotificationService;


    //ConcurrentHashMap<String, List<Notification>> delayedNotification; //username-notifications
    ConcurrentHashMap<UUID, String> sessionMapper; //uuid-sessionID

    @Autowired
    public AlertService(NotificationDispatcher dispatcher, NotificationService dataNotificationService) {
        this.dispatcher = dispatcher;
        this.dataNotificationService = dataNotificationService;
       // delayedNotification = new ConcurrentHashMap<>();
        sessionMapper = new ConcurrentHashMap<>();
    }

    /**
     * used to send can be either Guest or Logged in Member.
     *
     * @param uuid this is the sessionID when a user connects to the system
     * @param msg  the message to pop to the client through the websocket
     * @return true if success, else false
     */
    public void notifyUser(UUID uuid, String msg,String email) {
        var notification = new Notification(msg,email);
        if (sessionMapper.containsKey(uuid)) {
            String sessionID = sessionMapper.get(uuid);
            if (!dispatcher.addNotification(sessionID, notification)) {
                logger.error(String.format("failed to notify %s", uuid));
                return;
            }
        }
        logger.error(String.format("didnt find sessionID of uuid [%s] in sessionMapper",uuid));
    }


    /**
     * gets a list of notifications to persist to db at once
     * @param toPersist
     */
    public void notifyUsers(List<Notification> toPersist) {
        List<DataNotification> dataNotificationList = toPersist.stream()
                .map(Notification::getDataObject)
                .collect(Collectors.toList());
        if(!dataNotificationService.insertNotifications(dataNotificationList)){
            logger.error("failed to persist all notification list");
        }
        logger.info("inserted list of notifications to db");
    }

    /**
     * called from NotificationController when start() was called.
     *
     * @param uuid      the id that the user sent
     * @param sessionID the sessionID that WebSocket generated
     */
    public void addListener(UUID uuid, String sessionID) {
        logger.debug(String.format("added (%s,%s) to sessionMapper",uuid,sessionID));
        sessionMapper.put(uuid, sessionID);
        if(!dispatcher.addNewSession(sessionID)){
            logger.error(String.format("failed to add session %s when adding to be listener",sessionID));
        }
    }

    /**
     * called from NotificationController when stop() was called.
     *
     * @param uuid      the id that the user sent
     * @param sessionID
     */
    public void removeListener(UUID uuid, String sessionID) {
        if(sessionMapper.remove(uuid)!=null){
            logger.debug(String.format("removed (%s,%s) to sessionMapper",uuid,sessionID));
            dispatcher.removeSession(sessionID);
        }else {
            logger.error(String.format("failed to remove (%s,%s) to sessionMapper - not found in mapper", uuid, sessionID));
        }
    }

    /**
     * this function is called when a member is logged in
     *
     * @param username
     * @param uuid
     */
//    public void modifyDelayIfExist(String username, UUID uuid) {
//        if (delayedNotification.containsKey(username)) {
//            String sessionID = sessionMapper.get(uuid);
//            dispatcher.importDelayedNotifications(sessionID, delayedNotification.remove(username));
//            logger.info(String.format("imported all delayed notifications of %s", username));
//            return;
//        }
//        logger.info(String.format("user %s didn't have pending notifications", username));
//    }

    /**
     * this function is called when a member is logged in
     *
     * @param username
     * @param uuid
     */
    public void modifyDelayIfExist(String username, UUID uuid) {
        List<DataNotification> dataNotificationList = dataNotificationService.getAllUserNotifications(username);
        if(dataNotificationList == null){
            logger.error(String.format("failed to get all notifications from db of %s",username));
            return;
        }
        if(dataNotificationList.isEmpty()){
            logger.info(String.format("user %s didn't have pending notifications", username));
            return;
        }
        List<Notification> notifications = dataNotificationList.stream()
                .map(dataNotification -> new Notification(dataNotification.getMessage(),username))
                .collect(Collectors.toList());
        var sessionID = sessionMapper.get(uuid);
        dispatcher.importDelayedNotifications(sessionID,notifications);
        logger.info(String.format("imported all delayed notifications of %s", username));
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
        for (Map.Entry<UUID, String> entry : sessionMapper.entrySet()) {
            if (entry.getValue().equals(sessionId)) {
                sessionMapper.remove(entry.getKey());
                logger.info("removed session from disconnect event");
                return;
            }
        }
        logger.error("didn't find session on disconnect event");
    }


//    /**
//     * getter for test only
//     *
//     * @return
//     */
//    public ConcurrentHashMap<String, List<String>> getDelayedNotification() {
//        ConcurrentHashMap<String, List<String>> out = new ConcurrentHashMap<>();
//        for (Map.Entry<String, List<Notification>> entry : delayedNotification.entrySet()) {
//            List<String> msgs = entry.getValue().stream().map(Notification::getText).collect(Collectors.toList());
//            out.put(entry.getKey(), msgs);
//        }
//        return out;
//    }

}
