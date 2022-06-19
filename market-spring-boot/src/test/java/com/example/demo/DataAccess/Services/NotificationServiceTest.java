package com.example.demo.DataAccess.Services;

import com.example.Acceptance.Obj.*;
import com.example.demo.DataAccess.Entity.DataNotification;
import com.example.demo.Domain.AlertService.Notification;
import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @Autowired
    private Facade market;

    public static int IPHONE_5_ID;
    public static int IPHONE_6_ID;
    public static int SCREEN_FULL_HD_ID;
    public static int GALAXY_ID;

    private User member;
    private User sysManager;
    private int existing_storeID;
    private String uuid;

    @BeforeEach
    public void setUp(){
        sysManager = new User("SysManager", "sysManager@gmail.com", "Shalom123$", new Address("Tel-Aviv", "Nordau", 2), "0523111110","10/4/1994");
        member = new User("member", "member@gmail.com", "Shalom123$", new Address("Tel-Aviv", "Nordau", 3), "0523111111","16/3/2012");
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
        uuid = market.guestVisit().value;
    }

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
        assertEquals(notification.getText(), dataNotificationList.get(0).getMessage());
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


    /**
     * Requirement: alert realtime (connected user) - #1.5
     */
    //can be check only after store persist and product types are in domain !!

//    @Test
//    @DisplayName("req: #1.5 - success test")
//    void RealAlert_Success() {
//        String uuid = market.guestVisit().value;
//        SLResponseOBJ<String> sysId = market.login(uuid, sysManager.username,sysManager.password);
//        assertFalse(sysId.errorOccurred());
//        uuid = sysId.value;
//        assertFalse(market.closeStore(uuid, existing_storeID).errorOccurred());
//        var  dataNotificationList= notificationService.getAllUserNotifications(member.username);
//        assertFalse(dataNotificationList.isEmpty());
//    }

//    /**
//     * Requirement: alert - #1.6
//     */
//    @Test
//    @DisplayName("req: #1.6 - success test")
//    void Alert_Success() {
//        User newManager = generateUser();
//        assertTrue(market.register(uuid,newManager.username,newManager.password,newManager.dateOfBirth));
//
//        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
//        assertFalse(memberID.errorOccurred());
//        uuid = memberID.value;
//
//        assertTrue(market.assignNewManager(uuid,existing_storeID,newManager));
//        assertTrue(market.updatePermission(uuid, Permission.SET_MANAGER_PERMISSIONS,false,newManager,existing_storeID));
//        ATResponseObj<String> res = market.logout(uuid);
//        assertFalse(res.errorOccurred());
//        uuid = res.value;
//        res = market.login(uuid,newManager);
//        assertFalse(res.errorOccurred());
//        uuid = res.value;
//
//        String read = readFile(uuid);
//        assertFalse(read.isEmpty());
//    }


    protected void populateItemsAndStore() {
        String uuid = market.guestVisit().value;
        uuid = market.login(uuid, member.username,member.password).value;
        existing_storeID = market.openNewStore(uuid,"myStore",member.username,null,null,null).value;
        market.addNewProductToStore(uuid, existing_storeID, IPHONE_5_ID, 10, 1);
        market.addNewProductToStore(uuid, existing_storeID, IPHONE_6_ID, 60, 1);
        market.addNewProductToStore(uuid, existing_storeID, SCREEN_FULL_HD_ID, 10, 3);
        uuid = market.logout(uuid).value;
        market.guestLeave(uuid);
    }
    protected void initMarketWithSysManagerAndItems(){
        String uuid = market.guestVisit().value;
        uuid = market.login(uuid,sysManager.username,sysManager.password).value;
        IPHONE_5_ID = market.addNewProductType(uuid,"iphone5","phone",1).value;
        IPHONE_6_ID = market.addNewProductType(uuid,"iphone6","phone",1).value;
        SCREEN_FULL_HD_ID = market.addNewProductType(uuid,"screenFULLHD","screen",2).value;
        GALAXY_ID = market.addNewProductType(uuid,"galaxyS10","phone",1).value;
        uuid = market.logout(uuid).value;
        market.guestLeave(uuid);
    }

    protected void registerMemberData(){
        String uuid = market.guestVisit().value;
        market.addNewMember(uuid,member.username,member.password,member.phone_number,member.dateOfBirth);
        market.guestLeave(uuid);
    }
}