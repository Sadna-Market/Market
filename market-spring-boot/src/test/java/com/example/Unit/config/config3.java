package com.example.Unit.config;

import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceObj.ServiceProductStore;
import com.example.demo.Service.ServiceObj.ServiceStore;
import com.example.demo.Service.ServiceObj.ServiceUser;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import com.example.demo.configuration.config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class config3 {

    Facade f = new Facade();
    List<String> members = new LinkedList<>();
    List<String> membersloged = new LinkedList<>();
    List<String> Stores = new LinkedList<>();

    HashMap<String, List<Integer>> productrs = new HashMap<>();

    @BeforeEach
    void setUP() {
        members.add("u1@gmail.com");
        members.add("u2@gmail.com");
        members.add("u3@gmail.com");
        members.add("u4@gmail.com");
        members.add("u5@gmail.com");
        members.add("u6@gmail.com");
        membersloged.add("u1@gmail.com");
        membersloged.add("u2@gmail.com");
        membersloged.add("u3@gmail.com");
        membersloged.add("u4@gmail.com");
        Stores.add("s1");
        Stores.add("s2");

        List<Integer> s1prodducts = new LinkedList<>();
        s1prodducts.add(1);
        s1prodducts.add(2);
        s1prodducts.add(3);
        productrs.put("s1", s1prodducts);
    }

    @Test
    public void config() {
        config c = config.get_instance();
        if (c.get_System_state_path().equals("state_system._tests3")) {
            SLResponseOBJ<String> UUID = f.guestVisit();
            SLResponseOBJ<String> res = f.login(UUID.value, "sysManager@gmail.com", "Shalom123$");
            System.out.println(res.errorMsg);
            SLResponseOBJ<List<String>> res2 = f.getAllMembers(res.value);
            System.out.println(res2.errorMsg);
            List<String> me = res2.value;
            for (String m : members) {
                assertFalse(me.contains(m));
            }
            SLResponseOBJ<List<ServiceUser>> loges = f.getloggedInMembers(res.value);
            List<String> log = new LinkedList<>();
            for (ServiceUser s : loges.value) {
                log.add(s.email);
            }
            assertFalse(log.contains("u5@gmail.com"));
            for (String m : membersloged) {
                assertFalse(log.contains(m));
            }
            List<ServiceStore> s = f.getAllStores().value;
            List<String> STORENAMES = new LinkedList<>();
            for (ServiceStore st : s) {
                STORENAMES.add(st.name);

            }
            for (String name : Stores) {
                assertFalse(STORENAMES.contains(name));

            }
            List<ServiceStore> stores = f.getAllStores().value;
            for (ServiceStore st : stores) {
                List<ServiceProductStore> ps = f.getAllProductsInStore(st.storeId).value;
                System.out.println(ps);
                for (ServiceProductStore producr : ps) {
                    assertFalse(productrs.get(st.name).contains(producr.itemID));
                }
            }
            assertFalse(f.isOwner("u2@gmail.com", 1).value);
            assertFalse(f.isOwner("u4@gmail.com", 1).value);
            assertFalse(f.isOwner("u5@gmail.com", 1).value);
            assertFalse(f.isManager("u3@gmail.com", 1).value);

            assertFalse(f.isOwner("u3@gmail.com", 2).value);

            UUID = f.guestVisit();
            assertTrue(f.login(UUID.value, "u1@gmail.com", "abcA!123").errorOccurred());
            UUID = f.guestVisit();

            assertTrue(f.login(UUID.value, "u2@gmail.com", "abcA!123").errorOccurred());
            UUID = f.guestVisit();

            assertTrue(f.login(UUID.value, "u3@gmail.com", "abcA!123").errorOccurred());
            UUID = f.guestVisit();

            assertTrue(f.login(UUID.value, "u4@gmail.com", "abcA!123").errorOccurred());
            UUID = f.guestVisit();

            assertTrue(f.login(UUID.value, "u5@gmail.com", "abcA!123").errorOccurred());
        }
    }
}
