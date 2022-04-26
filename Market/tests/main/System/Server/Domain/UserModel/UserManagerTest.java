package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.Market.PermissionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserManagerTest {
    UserManager userManager = new UserManager();
    String[] emails = {"yosi@gmail.com","kobi@gmail.com","shalom@gmai.com","aaaa"};
    String[] passwords = {"Yosi123$","Kobi123$","Shalom123$","11111"};
    String[] PhoneNum = {"0538265477","0538265477","0538265477","0538265477"};
    String[] CreditCard = {"1234567891234567","1234567891234567","1234567891234567","1234567891234567"};
    String[] CreditDate = {"1234","1234","1234","1234"};




    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @Test
    void loginExistMember() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        Assertions.assertTrue(userManager.getMembers().containsKey(emails[0]));
        userManager.Login(uuid,emails[0],passwords[0]);
        Assertions.assertTrue(userManager.getLoginUsers().containsKey(uuid));
    }

    @Test
    void loginWithLoggedUser() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        Assertions.assertFalse(userManager.Login(uuid,emails[0],passwords[0])); }

    @Test
    void loginWithWrongUUID() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        Assertions.assertFalse(userManager.Login(UUID.randomUUID(),emails[0],passwords[0])); }

    @Test
    void loginUserThatDonotHavaAmember() {
        UUID uuid = userManager.GuestVisit();
        Assertions.assertFalse(userManager.Login(uuid,emails[0],passwords[0]));}

    @Test
    void loginWithWrongPassword() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        Assertions.assertFalse(userManager.Login(UUID.randomUUID(),emails[0],"wrong")); }


    @Test
    void loginWithWrongEmail() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        Assertions.assertFalse(userManager.Login(UUID.randomUUID(),"wrong",passwords[0])); }


    @Test
    void logoutLoggedUser() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        Assertions.assertTrue(userManager.Logout(uuid));
        Assertions.assertFalse(userManager.getLoginUsers().containsKey(uuid));
    }

    @Test
    void logoutUserNotLogged() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        Assertions.assertFalse(userManager.Logout(UUID.randomUUID()));
    }

    @Test
    void guestVisit() { //check if after the user enter to the system , the obj added guest hash
        UUID uuid = userManager.GuestVisit();
        Assertions.assertTrue(userManager.getGuestVisitors().containsKey(uuid));
    }

    @Test
    void guestLeaveExisistingUserID() {
        UUID uuid = userManager.GuestVisit();
        Assertions.assertTrue(userManager.GuestLeave(uuid));
    }

    @Test
    void guestLeaveNotExisistingUserID() {
        UUID uuid =UUID.randomUUID();
        Assertions.assertFalse(userManager.GuestLeave(uuid));
    }

    @Test
    void addNewMemberInTheSystem() {
        //add new member
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        Assertions.assertTrue(userManager.getMembers().containsKey(emails[0]));

        //add member with user id that does not exist
        uuid = UUID.randomUUID();
        userManager.AddNewMember(uuid,emails[1],passwords[1],PhoneNum[1],CreditCard[1],CreditDate[1]);
        Assertions.assertFalse(userManager.getMembers().containsKey(emails[1]));

        //add member with incorrect password
        uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[1],passwords[3],PhoneNum[1],CreditCard[1],CreditDate[1]);
        Assertions.assertFalse(userManager.getMembers().containsKey(emails[1]));


        //add member with incorrect email
        uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[3],passwords[1],PhoneNum[1],CreditCard[1],CreditDate[1]);
        Assertions.assertFalse(userManager.getMembers().containsKey(emails[3]));
    }


    @Test
    void isLogin() {
        Assertions.assertFalse(userManager.isLogged(UUID.randomUUID()));
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        System.out.println(userManager.Login(uuid,emails[0],passwords[0]));
        Assertions.assertTrue(userManager.isLogged(uuid));

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
    void isLogged() {
        UUID uuid = userManager.GuestVisit();
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0],CreditCard[0],CreditDate[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        userManager.isLogged(uuid);
    }



    @Test
    void isOnline() {
        UUID uuid = userManager.GuestVisit();
        Assertions.assertTrue(userManager.isOnline(uuid));
        Assertions.assertFalse(userManager.isOnline(UUID.randomUUID()));

    }


}