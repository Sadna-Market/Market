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
        market.addSystemManager(sysManager);
        member = new User("member", "member@gmail.com", "123qweASD!", new Address("Tel-Aviv", "Nordau", 3), "0523111111");

        market.initSystem();
        market.register(member.username, member.password);
        market.login(member);
        int existing_storeID = market.addStore(member).value;
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3232, 3, 10, List.of("TV"), "screen");
        market.addItemToStore(existing_storeID, item1);
        market.addItemToStore(existing_storeID, item2);
        market.addItemToStore(existing_storeID, item3);
        market.logout();
        market.exitSystem();
    }
}
