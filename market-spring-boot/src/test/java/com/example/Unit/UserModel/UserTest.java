package com.example.Unit.UserModel;

import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.UserManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

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
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0], LocalDate.of(1998,11,15));
        uuid=userManager.Login(uuid,emails[0],passwords[0]).value;
        userManager.addFounder(uuid,store);
        boolean A=userManager.isFounder(store,userManager.getOnlineUser(uuid).value.getEmail().value).getValue();
        Assertions.assertTrue(A);



    }

    @Test
    void addNewStoreOwner() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0], LocalDate.of(1998,11,15));
        userManager.AddNewMember(uuid,emails[1],passwords[1],PhoneNum[1], LocalDate.of(1998,11,15));
        uuid=userManager.Login(uuid,emails[0],passwords[0]).value;
        userManager.addFounder(uuid,store);
        userManager.addNewStoreOwner(uuid,store,emails[1]);
        uuid = userManager.GuestVisit().value;
        uuid=userManager.Login(uuid,emails[1],passwords[1]).value;
        boolean A=userManager.isOwner(uuid,store).getValue();
        Assertions.assertTrue(A);

    }
    @Test

    void addNewStoreOwnerWithUserThatNotOuner() {
        UUID uuid = userManager.GuestVisit().value;
        userManager.AddNewMember(uuid,emails[0],passwords[0],PhoneNum[0], LocalDate.of(1998,11,15));
        userManager.AddNewMember(uuid,emails[1],passwords[1],PhoneNum[1], LocalDate.of(1998,11,15));
        uuid=userManager.Login(uuid,emails[0],passwords[0]).value;
        Assertions.assertFalse(userManager.addNewStoreOwner(uuid,store,emails[1]).value);


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