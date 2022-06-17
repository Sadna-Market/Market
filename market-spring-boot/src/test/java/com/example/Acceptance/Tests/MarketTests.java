package com.example.Acceptance.Tests;

import com.example.Acceptance.Bridge.MarketBridge;
import com.example.Acceptance.Bridge.ProxyMarket;
import com.example.Acceptance.Obj.*;
import com.example.demo.DemoApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest(classes = DemoApplication.class)
public class MarketTests {
    protected MarketBridge market;
    private int counter;
    protected User sysManager;
    protected User member;
    protected int existing_storeID;

    public static int IPHONE_5_ID;
    public static int IPHONE_6_ID;
    public static int SCREEN_FULL_HD_ID;
    public static int GALAXY_ID;

    public MarketTests() {
       market = new ProxyMarket();
        counter = 1;
        sysManager = new User("SysManager", "sysManager@gmail.com", "Shalom123$", new Address("Tel-Aviv", "Nordau", 2), "0523111110","10/4/1994");
        member = new User("member", "member@gmail.com", "Shalom123$", new Address("Tel-Aviv", "Nordau", 3), "0523111111","16/3/2012");
    }
    public User generateUser() {
        int suffix = counter++;
        return new User("User" + suffix,
                "user" + suffix + "@gmail.com",
                PasswordGenerator.generateStrongPassword(),
                new Address("Tel-Aviv", "Nordau", suffix),
                "052311111" + suffix,"25/10/1984");
    }

    protected void populateItemsAndStore() {
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        item2.itemID = IPHONE_6_ID;
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3, 10, List.of("TV"), "screen");
        item3.itemID = SCREEN_FULL_HD_ID;
        String uuid = market.guestVisit();
        uuid = market.login(uuid, member).value;
        existing_storeID = market.addStore(uuid, member).value;
        market.addItemToStore(uuid, existing_storeID, item1);
        market.addItemToStore(uuid, existing_storeID, item2);
        market.addItemToStore(uuid, existing_storeID, item3);
        uuid = market.logout(uuid).value;
        market.exitSystem(uuid);
    }
    protected void initMarketWithSysManagerAndItems(){
        String uuid = market.initSystem(sysManager).value;
        //add items to system
        uuid = market.login(uuid, sysManager).value;
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3, 10, List.of("TV"), "screen");
        ItemDetail item4  = new ItemDetail("galaxyS10", 1, 10, List.of("phone"), "phone");
        IPHONE_5_ID = market.addProductType(uuid, item1).value;
        IPHONE_6_ID = market.addProductType(uuid, item2).value;
        SCREEN_FULL_HD_ID = market.addProductType(uuid, item3).value;
        GALAXY_ID = market.addProductType(uuid, item4).value;
        uuid = market.logout(uuid).value;
        market.exitSystem(uuid);
    }

    protected void registerMemberData(){
        String uuid = market.guestVisit();
        market.register(uuid, member.username, member.password,member.dateOfBirth);
        market.exitSystem(uuid);
    }
    protected void roleBackAfterReset(){
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
    }
}
