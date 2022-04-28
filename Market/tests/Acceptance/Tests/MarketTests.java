package Acceptance.Tests;

import Acceptance.Bridge.MarketBridge;
import Acceptance.Bridge.ProxyMarket;
import Acceptance.Obj.Address;
import Acceptance.Obj.PasswordGenerator;
import Acceptance.Obj.User;

public class MarketTests {
    protected final MarketBridge market;
    private int counter;
    protected User sysManager;
    protected User member;
    protected int existing_storeID;

    public MarketTests() {
        market = new ProxyMarket();
    }
    public User generateUser() {
        int suffix = counter++;
        return new User("User" + suffix, "user" + suffix + "@gmail.com", PasswordGenerator.generateStrongPassword(), new Address("Tel-Aviv", "Nordau", suffix), "052311111" + suffix);
    }
}
