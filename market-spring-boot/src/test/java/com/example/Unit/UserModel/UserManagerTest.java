package com.example.Unit.UserModel;


import com.example.demo.Domain.UserModel.UserManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserManagerTest {
    UserManager userManager = new UserManager();
    String[] emails = {"yosi@gmail.com","kobi@gmail.com","shalom@gmai.com","aaaa"};
    String[] passwords = {"Yosi123$","Kobi123$","Shalom123$","11111"};
    String[] PhoneNum = {"0538265477","0538265477","0538265477","0538265477"};





    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @Test
    void loginExistMember() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        Assertions.assertTrue(userManager.getMembers().value.containsKey(emails[0]));
        uuid = userManager.Login(uuid,emails[0],passwords[0]).value;
        Assertions.assertTrue(userManager.getLoginUsers().value.containsKey(uuid));
    }
    @Test
    void loginWithLoggedUser() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        Assertions.assertTrue(userManager.Login(uuid,emails[0],passwords[0]).errorOccurred()); }

    @Test
    void loginWithWrongUUID() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        Assertions.assertTrue(userManager.Login(UUID.randomUUID(),emails[0],passwords[0]).errorOccurred()); }
    @Test
    void loginUserThatDonotHavaAmember() {
        UUID uuid = userManager.GuestVisit().value;
        Assertions.assertTrue(userManager.Login(uuid,emails[0],passwords[0]).errorOccurred());}

    @Test
    void loginWithWrongPassword() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        Assertions.assertTrue(userManager.Login(UUID.randomUUID(),emails[0],"wrong").errorOccurred()); }


    @Test
    void loginWithWrongEmail() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        Assertions.assertTrue(userManager.Login(UUID.randomUUID(),"wrong",passwords[0]).errorOccurred()); }


    @Test
    void logoutLoggedUser() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        uuid=userManager.Login(uuid,emails[0],passwords[0]).value;
        uuid = userManager.Logout(uuid).value;
        Assertions.assertFalse(userManager.getLoginUsers().value.containsKey(uuid));
    }

    @Test
    void logoutUserNotLogged() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        Assertions.assertTrue(userManager.Logout(UUID.randomUUID()).errorOccurred());
    }

    @Test
    void guestVisit() { //check if after the user enter to the system , the obj added guest hash
        UUID uuid = userManager.GuestVisit().value;
        Assertions.assertTrue(userManager.getGuestVisitors().value.containsKey(uuid));
    }

    @Test
    void guestLeaveExisistingUserID() {
        UUID uuid = userManager.GuestVisit().value;
        Assertions.assertTrue(userManager.GuestLeave(uuid).value);
    }

    @Test
    void guestLeaveNotExisistingUserID() {
        UUID uuid =UUID.randomUUID();
        Assertions.assertFalse(userManager.GuestLeave(uuid).value);
    }

    @Test
    void addNewMemberInTheSystem() {
        //add new member
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        Assertions.assertTrue(userManager.getMembers().value.containsKey(emails[0]));

        //add member with user id that does not exist
        uuid = UUID.randomUUID();
        userManager.AddNewMember(uuid,emails[1],passwords[1],PhoneNum[1]);
        Assertions.assertFalse(userManager.getMembers().value.containsKey(emails[1]));

        //add member with incorrect password
        uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[1],passwords[3],PhoneNum[1]);
        Assertions.assertFalse(userManager.getMembers().value.containsKey(emails[1]));


        //add member with incorrect email
        uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[3],passwords[1],PhoneNum[1]);
        Assertions.assertFalse(userManager.getMembers().value.containsKey(emails[3]));
    }


    @Test
    void isLogin() {
        Assertions.assertFalse(userManager.isLogged(UUID.randomUUID()).value);
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        uuid = userManager.Login(uuid,emails[0],passwords[0]).value;
        Assertions.assertTrue(userManager.isLogged(uuid).value);

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
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0]);
        userManager.Login(uuid,emails[0],passwords[0]);
        userManager.isLogged(uuid);
    }


    @Test
    void isOnline() {
        UUID uuid = userManager.GuestVisit().value;
        Assertions.assertTrue(userManager.isOnline(uuid).value);
        Assertions.assertFalse(userManager.isOnline(UUID.randomUUID()).value);

    }
}