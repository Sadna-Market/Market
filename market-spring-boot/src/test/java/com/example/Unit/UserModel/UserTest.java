package com.example.Unit.UserModel;

import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.UserManager;
import com.example.demo.Service.Facade;
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
    void removeMangager(){
        Facade facade = new Facade();
        String uuid1 = facade.guestVisit().value;
        String uuid2 = facade.guestVisit().value;
        System.out.println(uuid1);
        System.out.println(facade.addNewMember(uuid1,emails[0],passwords[0],PhoneNum[0], "25/10/1984").errorMsg);
        System.out.println(facade.addNewMember(uuid2,emails[1],passwords[1],PhoneNum[1], "25/10/1984").errorMsg);
        uuid1=facade.login(uuid1,emails[0],passwords[0]).value;
        uuid2=facade.login(uuid2,emails[0],passwords[0]).value;
        System.out.println(uuid1);
        facade.openNewStore(uuid1,"dali",emails[0],null,null,null);
        Assertions.assertTrue(facade.isOwner(emails[0],1).value);
        Assertions.assertFalse(facade.isManager(emails[1],1).value);
        Assertions.assertTrue(facade.addNewStoreManger(uuid1,1,emails[1]).value);
        Assertions.assertTrue(facade.isManager(emails[1],1).value);
        Assertions.assertTrue(facade.removeStoreMenager(uuid1,1,emails[1]).value);
        Assertions.assertFalse(facade.isManager(emails[1],1).value);
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