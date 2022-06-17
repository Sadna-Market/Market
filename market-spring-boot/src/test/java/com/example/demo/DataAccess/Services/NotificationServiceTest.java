package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataNotification;
import com.example.demo.Domain.AlertService.Notification;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    void insertNotification() {
        String message = "Good job niv!";
        String sendTo = "niv@gmail.com";
        Notification notification = new Notification(message, sendTo);
        DataNotification dataNotification = notification.getDataObject();
        //action
        assertTrue(notificationService.insertNotification(dataNotification));
        //check
        List<DataNotification> dataNotificationList = notificationService.getAllUserNotifications(sendTo);
        assertFalse(dataNotificationList.isEmpty());
        assertEquals(message, dataNotificationList.get(0).getMessage());
    }

    @Test
    void insertNotifications() {
        String message = "Good job niv!";
        String sendTo = "niv@gmail.com";
        int numOfmessages = 10;
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < numOfmessages; i++) {
            notifications.add(new Notification(message+i,sendTo));
        }
        List<DataNotification> dataNotificationList = notifications.stream().map(Notification::getDataObject).collect(Collectors.toList());
        //action
        assertTrue(notificationService.insertNotifications(dataNotificationList));
        //check
        List<DataNotification> afterNotifications = notificationService.getAllUserNotifications(sendTo);
        assertNotNull(afterNotifications);
        assertFalse(afterNotifications.isEmpty());
        assertEquals(numOfmessages,afterNotifications.size());
        for (int i = 0; i <numOfmessages ; i++) {
            assertEquals(notifications.get(i).getText(),afterNotifications.get(i).getMessage());
        }
    }

    @Test
    void getAllUserNotifications() {
        String message = "Good job niv!";
        String sendTo = "niv@gmail.com";
        int numOfmessages = 10;
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < numOfmessages; i++) {
            notifications.add(new Notification(message+i,sendTo));
        }
        List<DataNotification> dataNotificationList = notifications.stream().map(Notification::getDataObject).collect(Collectors.toList());
        //action
        assertTrue(notificationService.insertNotifications(dataNotificationList));
        //check
        notificationService.getAllUserNotifications(sendTo);
        List<DataNotification> afterNotifications = notificationService.getAllUserNotifications(sendTo);
        assertTrue(afterNotifications.isEmpty());
    }
}