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

    public static int IPHONE_5;
    public static int IPHONE_6;
    public static int SCREEN_FULL_HD;
    public static int GALAXY;

    public MarketTests() {
        market = new ProxyMarket();
        initStoreAndItem();
    }
    public User generateUser() {
        int suffix = counter++;
        return new User("User" + suffix, "user" + suffix + "@gmail.com", PasswordGenerator.generateStrongPassword(), new Address("Tel-Aviv", "Nordau", suffix), "052311111" + suffix);
    }

    protected void initStoreAndItem() {
        counter = 1;
        sysManager = new User("SysManager", "sysManager@gamil.com", "123qweASD!", new Address("Tel-Aviv", "Nordau", 2), "0523111110");
        member = new User("member", "member@gmail.com", "123qweASD!", new Address("Tel-Aviv", "Nordau", 3), "0523111111");

        String uuid = market.initSystem(sysManager).value;
        //add items to system
        uuid = market.login(uuid, sysManager).value;
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3, 10, List.of("TV"), "screen");
        ItemDetail item4  = new ItemDetail("galaxyS10", 1, 10, List.of("phone"), "phone");
        IPHONE_5 = market.addProductType(uuid, item1).value;
        IPHONE_6 = market.addProductType(uuid, item2).value;
        SCREEN_FULL_HD = market.addProductType(uuid, item3).value;
        GALAXY = market.addProductType(uuid, item4).value;
        uuid = market.logout(uuid).value;

        market.register(uuid, member.username, member.password);
        uuid = market.login(uuid, member).value;
        int existing_storeID = market.addStore(uuid, member).value;
        market.addItemToStore(uuid, existing_storeID, item1);
        market.addItemToStore(uuid, existing_storeID, item2);
        market.addItemToStore(uuid, existing_storeID, item3);
        uuid = market.logout(uuid).value;
        market.exitSystem(uuid);
    }
}
