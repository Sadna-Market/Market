package com.example.demo.Domain.AlertService;

import org.apache.log4j.Logger;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class AlertServiceDemo implements IAlertService {
    private static final Logger logger = Logger.getLogger(AlertServiceDemo.class);


    ConcurrentHashMap<String, List<Notification>> delayedNotification; //username-notifications
    ConcurrentHashMap<UUID, String> sessionMapper; //uuid-sessionID

    public AlertServiceDemo() {
        delayedNotification = new ConcurrentHashMap<>();
        sessionMapper = new ConcurrentHashMap<>();
    }

    public void notifyUser(UUID uuid, String msg,String username) {
        writeToFile(uuid, msg);
    }

//    public void notifyUser(String username, String msg) {
//        var notification = new Notification(msg);
//        if (delayedNotification.containsKey(username)) {
//            delayedNotification.get(username).add(notification);
//        } else {
//            List<Notification> lst = new ArrayList<>();
//            lst.add(notification);
//            delayedNotification.put(username, lst);
//        }
//    }
    //use db
    public void notifyUsers(List<Notification> toPersist) {
        toPersist.forEach(notification -> {
            writeToFile(notification.getSentTo(),notification.getText());
        });
    }

    public void modifyDelayIfExist(String username, UUID uuid) {
        if (delayedNotification.containsKey(username)) {
            writeToFile(uuid, delayedNotification.get(username).get(0).getText());
        }
    }

    private void writeToFile(UUID uuid, String msg) {
        String path = System.getProperty("user.dir").concat("\\src\\main\\Alerts\\").concat(uuid.toString()).concat(".txt");
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(msg);
        } catch (Exception e) {
            logger.error("couldn't write to file");
        }
    }

    private void writeToFile(String email, String msg) {
        String path = System.getProperty("user.dir").concat("\\src\\main\\Alerts\\").concat(email).concat(".txt");
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(msg);
        } catch (Exception e) {
            logger.error("couldn't write to file");
        }
    }
}
