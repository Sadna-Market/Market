package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.userTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionManagerTest {
    PermissionManager permissionManager;
    User founder;
    User owner1;
    User owner2;
    User manager;
    User manager2;
    User member;

    Store store1;

    @BeforeEach
    void setUp() {
        founder = new User("founder","abc123D!","0678987655","1234567891234567","1234");
        owner1 = new User("owner1","abc123D!","0678987655","1234567891234567","1234");
        owner2 = new User("owner2","abc123D!","0678987655","1234567891234567","1234");
        manager = new User("manager","abc123D!","0678987655","1234567891234567","1234");
        manager2 = new User("manager2","abc123D!","0678987655","1234567891234567","1234");
        member = new User("member","abc123D!","0678987655","1234567891234567","1234");
        store1 = new Store(null,null,null,null);
        permissionManager = PermissionManager.getInstance();

    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void createPermission() {
        assertFalse((permissionManager.createPermission(owner1, store1, owner1, userTypes.owner, userTypes.owner)).value);//try to give myself permision

        assertTrue((permissionManager.createPermission(founder, store1, null, userTypes.owner, userTypes.system)).value);//open new store
        assertTrue((permissionManager.createPermission(owner1, store1, founder, userTypes.owner, userTypes.owner)).value);//owner(founder)->owner
        assertTrue((permissionManager.createPermission(owner2, store1, owner1, userTypes.owner, userTypes.owner)).value);//owner->owner
        assertTrue((permissionManager.createPermission(manager, store1, owner2, userTypes.manager, userTypes.owner)).value);//owner->manager
        assertTrue((permissionManager.createPermission(manager, store1, owner2, userTypes.owner, userTypes.owner)).value);//owner->manager
        assertTrue((permissionManager.createPermission(manager2, store1, owner2, userTypes.manager, userTypes.owner)).value);//owner->manager

        assertFalse((permissionManager.createPermission(owner1, store1, founder, userTypes.owner, userTypes.owner)).value);//try to create twice owner


    }

    @Test
    void addManagerPermissionType() {
        permissionManager.createPermission(founder, store1, null, userTypes.owner, userTypes.system);//open new store
        permissionManager.createPermission(owner1, store1, founder, userTypes.owner, userTypes.owner);//owner(founder)->owner
        permissionManager.createPermission(owner2, store1, owner1, userTypes.owner, userTypes.owner);//owner->owner
        permissionManager.createPermission(manager2, store1, owner2, userTypes.manager, userTypes.owner);//owner->manager

        assertFalse(permissionManager.addManagerPermissionType(permissionType.permissionEnum.addNewProductToStore, manager2, store1, owner1));
        assertTrue(permissionManager.addManagerPermissionType(permissionType.permissionEnum.addNewProductToStore, manager2, store1, owner2));

    }

    @Test
    void removeManagerPermissionType() {
        permissionManager.createPermission(founder, store1, null, userTypes.owner, userTypes.system);//open new store
        permissionManager.createPermission(owner1, store1, founder, userTypes.owner, userTypes.owner);//owner(founder)->owner
        permissionManager.createPermission(owner2, store1, owner1, userTypes.owner, userTypes.owner);//owner->owner
        permissionManager.createPermission(manager2, store1, owner2, userTypes.manager, userTypes.owner);//owner->manager
        permissionManager.addManagerPermissionType(permissionType.permissionEnum.addNewProductToStore, manager2, store1, owner2);//add Manager Permission Type


        //remove permission type that not exist in this manager permission
        assertFalse(permissionManager.removeManagerPermissionType(permissionType.permissionEnum.deleteStore, manager2, store1, owner2));

        //try to remove permission type but with another owner that not appointed this manager
        assertFalse(permissionManager.removeManagerPermissionType(permissionType.permissionEnum.addNewProductToStore, manager2, store1, owner1));

        User user1 = new User("user11","abc123D!","0678987655","1234567891234567","1234");
        User user2 = new User("user22","abc123D!","0678987655","1234567891234567","1234");

        //try to remove permission type with users that not owner or manager in this store
        assertFalse(permissionManager.removeManagerPermissionType(permissionType.permissionEnum.addNewProductToStore, user1, store1, user2));

        //remove permission type
        assertTrue(permissionManager.removeManagerPermissionType(permissionType.permissionEnum.addNewProductToStore, manager2, store1, owner2));

    }

    @Test
    void removeManagerPermissionCompletely() {
        permissionManager.createPermission(founder, store1, null, userTypes.owner, userTypes.system);//open new store
        permissionManager.createPermission(manager2, store1, founder, userTypes.manager, userTypes.owner);//owner->manager

        //delete manager permission
        assertTrue(permissionManager.removeManagerPermissionCompletely(manager2, store1, founder));

        //delete manager permission that not already manager in this store
        assertFalse(permissionManager.removeManagerPermissionCompletely(manager2, store1, founder));

    }


    @Test
    void hasPermission() {
        permissionManager.createPermission(founder, store1, null, userTypes.owner, userTypes.system);//open new store
        permissionManager.createPermission(manager2, store1, founder, userTypes.manager, userTypes.owner);//owner->manager


        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.addNewProductToStore, founder, store1));
        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.Logout, founder, store1));
        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.AddProductToShoppingBag, founder, store1));
        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.getStoreRoles, founder, store1));
        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.setManagerPermissions, founder, store1));


        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.GetStoreInfo, manager, store1));
        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.OpenNewStore, manager, store1));
        assertTrue(permissionManager.hasPermission(permissionType.permissionEnum.Logout, manager, store1));

        assertFalse(permissionManager.hasPermission(permissionType.permissionEnum.addNewStoreManager, manager, store1));
        assertFalse(permissionManager.hasPermission(permissionType.permissionEnum.deleteStore, manager, store1));

    }

    @Test
    void getGranteeUserType() {
        permissionManager.createPermission(founder, store1, null, userTypes.owner, userTypes.system);//open new store
        permissionManager.createPermission(owner1, store1, founder, userTypes.owner, userTypes.owner);//owner(founder)->owner
        permissionManager.createPermission(owner2, store1, owner1, userTypes.owner, userTypes.owner);//owner->owner
        permissionManager.createPermission(manager2, store1, owner2, userTypes.manager, userTypes.owner);//owner->manager

        assertEquals(permissionManager.getGranteeUserType(founder, store1), userTypes.owner);
        assertEquals(permissionManager.getGranteeUserType(owner2, store1), userTypes.owner);
        assertEquals(permissionManager.getGranteeUserType(owner1, store1), userTypes.owner);
        assertEquals(permissionManager.getGranteeUserType(manager2, store1), userTypes.manager);

        User user1 = new User("user121","abc123D!","0678987655","1234567891234567","1234");
        assertEquals(permissionManager.getGranteeUserType(user1, store1), userTypes.member);


    }
}