package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.CompositeKeys.PermissionId;
import com.example.demo.DataAccess.Entity.DataPermission;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Enums.PermissionType;
import com.example.demo.Domain.Market.Permission;
import com.example.demo.Domain.Market.permissionType;
import com.example.demo.Domain.Market.userTypes;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PermissionServiceTest {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Test
    @Transactional
    void insertPermission() {
        //pre
        User grantee = new User("niv@gmail.com", "1qaz2wsx3edc$RFV", "0522222222", LocalDate.of(1993, 8, 13));
        User grantor = new User("dor@gmail.com", "1qaz2wsx3edc$RFV", "0522223333", LocalDate.of(1993, 8, 13));
        Store store = new Store("hanut", new DiscountPolicy(), new BuyPolicy(), "yaki@gmail.com");
        userService.insertUser(grantee.getDataObject());
        userService.insertUser(grantor.getDataObject());
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        store.setStoreId(dataStore.getStoreId());
        Permission permission = new Permission(grantee, store, grantor);
        permission.setPermissionTypes(userTypes.owner,userTypes.owner);
        DataPermission dataPermission = permission.getDataObject();
        //action
        assertTrue(permissionService.insertPermission(dataPermission));
        //check
        PermissionId id = dataPermission.getPermissionId();
        assertNotNull(id.getGranteeId());
        assertNotNull(id.getGrantorId());
        assertNotEquals(0, id.getStoreId());
    }

    @Test
    @Transactional
    void updatePermission() {
        //pre
        User grantee = new User("niv1@gmail.com", "1qaz2wsx3edc$RFV", "0522222222", LocalDate.of(1993, 8, 13));
        User grantor = new User("dor2@gmail.com", "1qaz2wsx3edc$RFV", "0522223333", LocalDate.of(1993, 8, 13));
        Store store = new Store("hanut", new DiscountPolicy(), new BuyPolicy(), "yaki@gmail.com");
        userService.insertUser(grantee.getDataObject());
        userService.insertUser(grantor.getDataObject());
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        store.setStoreId(dataStore.getStoreId());
        Permission permission = new Permission(grantee, store, grantor);
        permission.setPermissionTypes(userTypes.owner,userTypes.owner);
        DataPermission dataPermission = permission.getDataObject();
        assertTrue(permissionService.insertPermission(dataPermission));
        //action
        permission.setPermissionTypes(userTypes.manager,userTypes.owner);
        dataPermission = permission.getDataObject();
        assertTrue(permissionService.updatePermissionType(dataPermission));
        //check
        DataPermission afterDataPermission = permissionService.getPermissionById(dataPermission.getPermissionId());
        assertEquals(1, afterDataPermission.getGranteePermissionTypes().size());
    }



    @Test
    @Transactional
    void deletePermission() {
        //pre
        User grantee = new User("niv@gmail.com", "1qaz2wsx3edc$RFV", "0522222222", LocalDate.of(1993, 8, 13));
        User grantor = new User("dor@gmail.com", "1qaz2wsx3edc$RFV", "0522223333", LocalDate.of(1993, 8, 13));
        Store store = new Store("hanut", new DiscountPolicy(), new BuyPolicy(), "yaki@gmail.com");
        userService.insertUser(grantee.getDataObject());
        userService.insertUser(grantor.getDataObject());
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        store.setStoreId(dataStore.getStoreId());
        Permission permission = new Permission(grantee, store, grantor);
        permission.setPermissionTypes(userTypes.owner,userTypes.owner);
        DataPermission dataPermission = permission.getDataObject();
        assertTrue(permissionService.insertPermission(dataPermission));
        //action
        PermissionId id = permission.getPermissionId();
        assertTrue(permissionService.deletePermission(id));
        //check
        DataPermission afterDataPermission = permissionService.getPermissionById(id);
        assertNull(afterDataPermission);
    }


}