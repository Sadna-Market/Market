package com.example.demo.Domain.AlertService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private static final Logger logger = Logger.getLogger(AlertService.class);

    private NotificationDispatcher dispatcher;


    ConcurrentHashMap<String, List<Notification>> delayedNotification; //username-notifications
    ConcurrentHashMap<UUID, String> sessionMapper; //uuid-sessionID

    //TODO: maybe to autowire dispatcher here
    public AlertService() {
        delayedNotification = new ConcurrentHashMap<>();
        sessionMapper = new ConcurrentHashMap<>();
    }

    @Autowired
    public void setDispatcher(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * used to send can be either Guest or Logged in Member.
     *
     * @param uuid this is the sessionID when a user connects to the system
     * @param msg  the message to pop to the client through the websocket
     * @return true if success, else false
     */
    public void notifyUser(UUID uuid, String msg) {
        //for test
//        writeToFile(uuid, msg);
//        return;

        var notification = new Notification(msg);
        if (sessionMapper.containsKey(uuid)) {
            String sessionID = sessionMapper.get(uuid);
            if (!dispatcher.addNotification(sessionID, notification)) {
                logger.error(String.format("failed to notify %s", uuid));
                return;
            }
        }
        logger.error("didnt find sessionID in sessionMapper");
    }


    /**
     * used to send for e member that is not connected to the system yet.
     * <p>
     * Note: in the future this will go to DB
     *
     * @param username
     * @param msg
     * @return
     */
    public void notifyUser(String username, String msg) {
        var notification = new Notification(msg);
        if (delayedNotification.containsKey(username)) {
            delayedNotification.get(username).add(notification);
        } else {
            List<Notification> lst = new ArrayList<>();
            lst.add(notification);
            delayedNotification.put(username, lst);
        }
    }

    /**
     * called from NotificationController when start() was called.
     *
     * @param uuid      the id that the user sent
     * @param sessionID the sessionID that WebSocket generated
     */
    public void addListener(UUID uuid, String sessionID) {
        sessionMapper.put(uuid, sessionID);
        dispatcher.addNewSession(sessionID);
    }

    /**
     * called from NotificationController when stop() was called.
     *
     * @param uuid      the id that the user sent
     * @param sessionID
     */
    public void removeListener(UUID uuid, String sessionID) {
        sessionMapper.remove(uuid);
        dispatcher.removeSession(sessionID);
    }

    /**
     * this function is called when a member is logged in
     *
     * @param username
     * @param uuid
     */
    public void modifyDelayIfExist(String username, UUID uuid) {
        if (delayedNotification.containsKey(username)) {
            //for testing only
//            writeToFile(uuid,delayedNotification.get(username).get(0).text);

            String sessionID = sessionMapper.get(uuid);
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
        for (Map.Entry<UUID, String> entry : sessionMapper.entrySet()) {
            if (entry.getValue().equals(sessionId)) {
                sessionMapper.remove(entry.getKey());
                logger.info("removed session from disconnect event");
                return;
            }
        }
        logger.error("didn't find session on disconnect event");
    }


    /**
     * getter for test only
     *
     * @return
     */
    public ConcurrentHashMap<String, List<String>> getDelayedNotification() {
        ConcurrentHashMap<String, List<String>> out = new ConcurrentHashMap<>();
        for (Map.Entry<String, List<Notification>> entry : delayedNotification.entrySet()) {
            List<String> msgs = entry.getValue().stream().map(Notification::getText).collect(Collectors.toList());
            out.put(entry.getKey(), msgs);
        }
        return out;
    }

    private void writeToFile(UUID uuid, String msg) {
        String path = System.getProperty("user.dir").concat("\\src\\main\\Alerts\\").concat(uuid.toString()).concat(".txt");
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(msg);
        } catch (Exception e) {
            logger.error("couldn't write to file");
        }
    }

}
