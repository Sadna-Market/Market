package Acceptance.Tests;

import Acceptance.Bridge.MarketBridge;
import Acceptance.Bridge.ProxyMarket;
import Acceptance.Obj.Address;
import Acceptance.Obj.ItemDetail;
import Acceptance.Obj.PasswordGenerator;
import Acceptance.Obj.User;

import java.util.List;

public class MarketTests {
    protected final MarketBridge market;
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
        sysManager = new User("SysManager", "sysManager@gmail.com", "Shalom123$", new Address("Tel-Aviv", "Nordau", 2), "0523111110");
        member = new User("member", "member@gmail.com", "Shalom123$", new Address("Tel-Aviv", "Nordau", 3), "0523111111");
        System.out.println("---------MarketTests-----------");
        initMarketWithSysManagerAndItems();
        System.out.println("---------MarketTests-----------");

        registerMemberData();
        System.out.println("---------MarketTests-----------");

        populateItemsAndStore();
        System.out.println("---------MarketTests-----------");

    }
    public User generateUser() {
        int suffix = counter++;
        return new User("User" + suffix,
                "user" + suffix + "@gmail.com",
                PasswordGenerator.generateStrongPassword(),
                new Address("Tel-Aviv", "Nordau", suffix),
                "052311111" + suffix);
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
        System.out.println("initMarketWithSysManagerAndItems");

        String uuid = market.initSystem(sysManager).value;
        //add items to system
        System.out.println("initMarketWithSysManagerAndItems");
        uuid = market.login(uuid, sysManager).value;
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3, 10, List.of("TV"), "screen");
        ItemDetail item4  = new ItemDetail("galaxyS10", 1, 10, List.of("phone"), "phone");
        System.out.println("initMarketWithSysManagerAndItems");
        System.out.println("initMarketWithSysManagerAndItems");

        IPHONE_5_ID = market.addProductType(uuid, item1).value;
        System.out.println("initMarketWithSysManagerAndItems");

        IPHONE_6_ID = market.addProductType(uuid, item2).value;
        SCREEN_FULL_HD_ID = market.addProductType(uuid, item3).value;
        GALAXY_ID = market.addProductType(uuid, item4).value;
        System.out.println("initMarketWithSysManagerAndItems");

        uuid = market.logout(uuid).value;
        market.exitSystem(uuid);
        System.out.println("initMarketWithSysManagerAndItems");

    }

    protected void registerMemberData(){
        String uuid = market.guestVisit();
        market.register(uuid, member.username, member.password);
        market.exitSystem(uuid);
    }
    protected void roleBackAfterReset(){
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
    }
}
