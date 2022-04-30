package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.StoreModel.Store;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    UserManager userManager;
    Store store ;
    String[] emails = {"yosi@gmail.com","kobi@gmail.com","shalom@gmai.com","aaaa"};
    String[] passwords = {"Yosi123$","Kobi123$","Shalom123$","11111"};
    String[] PhoneNum = {"0538265477","0538265477","0538265477","0538265477"};
    String[] CreditCard = {"1234567891234567","1234567891234567","1234567891234567","1234567891234567"};
    String[] CreditDate = {"1234","1234","1234","1234"};
    @BeforeEach
    void setUp() {
        userManager = new UserManager();
        store = new Store(1,"Best Store", null, null, "dor@gmail.com");


    }

    @Test
    void addFounderUserNotLooged() {
        Assertions.assertFalse(userManager.addFounder(UUID.randomUUID(),store).value);
    }
    @Test
    void addFounder() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        userManager.addFounder(uuid,store);
        User a = userManager.getLoginUsers().value.get(uuid);


    }

    @Test
    void addNewStoreOwner() {
    }

    @Test
    void addNewStoreManager() {
    }

    @Test
    void setManagerPermissions() {
    }

    @Test
    void getRolesInStore() {
    }

    @Test
    void isPasswordEquals() {
    }
}