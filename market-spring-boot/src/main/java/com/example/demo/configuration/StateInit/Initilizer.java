package com.example.demo.configuration.StateInit;

import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import com.example.demo.configuration.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Initilizer {

    private Facade facade;

    @Autowired
    public Initilizer(Facade facade) {
        this.facade = facade;

    }
    public void initialization(String adminEmail, String adminPassword) throws IOException {
        JsonReader readJson = config.get_instance().getJsonReaderState();
        Map<String, Integer> pname_to_pid = adminaddSystemProducts(adminEmail, adminPassword, readJson);
        register(readJson);
        Map<String, String> log_uuids = login(readJson);
        Map<String, Map<String, Integer>> storeids = openStores(log_uuids, readJson);
        add_items_stores(log_uuids, pname_to_pid, storeids, readJson);
        addManager(storeids, log_uuids, readJson);
        adOwners(storeids, log_uuids, readJson);
        Logoutandleave(storeids, log_uuids, readJson);
    }

    public void register(JsonReader readJson) {
        SLResponseOBJ<String> uuidSlr;
        String uuid;
        for (Map<String, Object> reg : readJson.register) {
            uuidSlr = facade.guestVisit();
            if (uuidSlr.errorOccurred()) throw new IllegalArgumentException();
            uuid = uuidSlr.value;
            String email = (String) reg.get("email");
            String password = (String) reg.get("password");
            String phoneNumber = (String) reg.get("phoneNumber");
            String date = (String) reg.get("dateOfBirth");
            SLResponseOBJ<Boolean> res = facade.addNewMember(uuid, email, password, phoneNumber, date);
            if (res.errorOccurred()) throw new IllegalArgumentException("");
            res = facade.guestLeave(uuid);
            if (res.errorOccurred()) throw new IllegalArgumentException();
        }
    }

    public Map<String, Map<String, Integer>> openStores(Map<String, String> UUIDS, JsonReader readJson) {
        System.out.println(UUIDS);
        Map<String, Map<String, Integer>> usersStores = new HashMap<>();
        SLResponseOBJ<String> uuidSlr;
        String uuid;
        for (String user : readJson.stores.keySet()) {
            Map<String, Integer> name_to_id = new HashMap<>();
            uuid = UUIDS.get(user);
            for (Map<String, String> store : readJson.stores.get(user)) {
                String name = store.get("name");
                String founder = store.get("founder");
                System.out.println(uuid + " " + name + "  " + founder);
                SLResponseOBJ<Integer> s = facade.openNewStore(uuid, name, founder, null, null, null);
                if (s.errorOccurred()) {
                    throw new IllegalArgumentException(s.errorOccurred() + "  ");
                }
                int StoreId = s.value;
                name_to_id.put(name, StoreId);
            }
            usersStores.put(user, name_to_id);
        }
        return usersStores;
    }

    public void add_items_stores(Map<String, String> uuidlogins, Map<String, Integer> itemstoid, Map<String, Map<String, Integer>> usersStores, JsonReader readJson) {
        Map<String, Map<String, List<Map<String, Object>>>> itemsinstores = readJson.add_item;
        for (String usermail : usersStores.keySet()) {
            if (itemsinstores.containsKey(usermail)) {
                Map<String, List<Map<String, Object>>> si = itemsinstores.get(usermail);
                for (String storeName : si.keySet()) {
                    if (usersStores.get(usermail).containsKey(storeName)) {
                        List<Map<String, Object>> items = itemsinstores.get(usermail).get(storeName);
                        for (Map<String, Object> item : items) {
                            System.out.println(itemstoid);
                            System.out.println(item);
                            int itemId = itemstoid.get(item.get("name"));
                            SLResponseOBJ<Boolean> res = facade.addNewProductToStore(uuidlogins.get(usermail), usersStores.get(usermail).get(storeName), itemId, (Double) item.get("price"), (int) item.get("quantity"));
                            if (res.errorOccurred()) throw new IllegalArgumentException();
                        }
                    }
                }

            }
        }
    }


    public Map<String, String> login(JsonReader reader) {
        SLResponseOBJ<String> uuidSlr;
        String uuid;
        Map<String, String> uuidlogins = new HashMap<>();
        for (Map<String, Object> log : reader.login) {
            uuidSlr = facade.guestVisit();
            if (uuidSlr.errorOccurred()) throw new IllegalArgumentException();
            uuid = uuidSlr.value;
            String password = (String) log.get("password");
            String email = (String) log.get("email");
            SLResponseOBJ<String> loguuid = facade.login(uuid, email, password);
            if (loguuid.errorOccurred()) throw new IllegalArgumentException();
            uuidlogins.put(email, loguuid.value);
        }
        return uuidlogins;
    }

    public void addManager(Map<String, Map<String, Integer>> storeids, Map<String, String> uuidlogins, JsonReader jsonReader) {
        Map<String, Map<String, List<Map<String, List<String>>>>> mem = jsonReader.addManager;
        for (String emil : mem.keySet()) {
            String uuid = getUUid(uuidlogins, emil);
            for (String sname : mem.get(emil).keySet()) {
                Integer sid = getStoreid(storeids, sname, emil);
                for (Map<String, List<String>> MEMB : mem.get(emil).get(sname)) {
                    SLResponseOBJ<Boolean> re = facade.addNewStoreManger(uuid, sid, (String) MEMB.keySet().toArray()[0]);
                    if (re.errorOccurred()) throw new IllegalArgumentException();
                    for (String perm : (List<String>) MEMB.values().toArray()[0]) {
                        re = facade.setManagerPermissions(uuid, sid, (String) MEMB.keySet().toArray()[0], perm, true);
                        if (re.errorOccurred()) throw new IllegalArgumentException();
                    }
                }
            }
        }
    }

    public void adOwners(Map<String, Map<String, Integer>> storeids, Map<String, String> uuidlogins, JsonReader jsonReader) {
        Map<String, Map<String, List<String>>> mem = jsonReader.addOwner;
        for (String emil : mem.keySet()) {
            String uuid = getUUid(uuidlogins, emil);
            for (String sname : mem.get(emil).keySet()) {
                Integer sid = getStoreid(storeids, sname, emil);
                for (String MEMB : mem.get(emil).get(sname)) {
                    facade.addNewStoreOwner(uuid, sid, MEMB);

                }
            }
        }

    }

    public void Logoutandleave(Map<String, Map<String, Integer>> storeids, Map<String, String> uuidlogins, JsonReader jsonReader) {
        List<String> lo = jsonReader.Logoutandleave;
        for (String l : lo) {
            String uuid = getUUid(uuidlogins, l);
            SLResponseOBJ<String> res = facade.logout(uuid);
            if (res.errorOccurred()) throw new IllegalArgumentException();
            SLResponseOBJ<Boolean> r = facade.guestLeave(res.value);
            if (r.errorOccurred()) throw new IllegalArgumentException();
        }

    }

    public String getUUid(Map<String, String> uuidlogins, String Email) {
        if (!uuidlogins.containsKey(Email)) throw new IllegalArgumentException();
        return uuidlogins.get(Email);
    }

    public Integer getStoreid(Map<String, Map<String, Integer>> storeids, String sname, String Email) {
        if (!storeids.containsKey(Email) || !storeids.get(Email).containsKey(sname))
            throw new IllegalArgumentException();
        return storeids.get(Email).get(sname);
    }

    private Map<String, Integer> adminaddSystemProducts(String adminEmail, String adminPassword, JsonReader readJson) {
        //        admin add items to system
        List<Map<String, Object>> sysItems = readJson.systemItems;
        Map<String, Integer> pname_to_pid = new HashMap<>();
        SLResponseOBJ<String> uuidSlr = facade.guestVisit();
        if (uuidSlr.errorOccurred()) throw new IllegalArgumentException();
        String uuid = uuidSlr.value;
        uuidSlr = facade.login(uuid, adminEmail, adminPassword);
        if (uuidSlr.errorOccurred()) throw new IllegalArgumentException();
        uuid = uuidSlr.value;
        for (Map<String, Object> item : sysItems) {
            String name = (String) item.get("name");
            String description = (String) item.get("description");
            int category = (int) item.get("category");
            System.out.println("name: " + name + " desc: " + description + " categoty : " + category);
            SLResponseOBJ<Integer> res = facade.addNewProductType(uuid, name, description, category);
            if (res.errorOccurred()) throw new IllegalArgumentException();
            pname_to_pid.put(name, res.value);
        }
        uuidSlr = facade.logout(uuid);
        if (uuidSlr.errorOccurred()) throw new IllegalArgumentException();
        uuid = uuidSlr.value;
        SLResponseOBJ<Boolean> leave = facade.guestLeave(uuid);
        if (leave.errorOccurred()) throw new IllegalArgumentException();
        return pname_to_pid;
    }

    private String addnewStores(JsonReader readJson, Map<String, Integer> pname_to_pid, String email, String u) {
        Map<String, Integer> store_to_id = new HashMap<>();
        if (readJson.stores.containsKey(email)) {
            List<Map<String, String>> userStores = readJson.stores.get(email);
            for (Map<String, String> store : userStores) {
                String name = store.get("name");
                String founder = store.get("founder");
                SLResponseOBJ<Integer> s = facade.openNewStore(u, name, founder, null, null, null);
                if (s.errorOccurred()) {
                    throw new IllegalArgumentException(s.errorOccurred() + "  ");
                }
                int StoreId = s.value;
                store_to_id.put(name, StoreId);
                //add items
                addProcuctsToStore(readJson, pname_to_pid, email, u, name, StoreId);
            }
        }
        return u;
    }

    private void addProcuctsToStore(JsonReader readJson, Map<String, Integer> pname_to_pid, String email, String u, String name, int StoreId) {
        if (checkJsonItemInStore(readJson.add_item, email, name)) {
            List<Map<String, Object>> storeItems = readJson.add_item.get(email).get(name);
            for (Map<String, Object> item : storeItems) {
                double price = (Double) item.get("price");
                int quantity = (int) item.get("quantity");
                SLResponseOBJ<Boolean> pres = facade.addNewProductToStore(u, StoreId, pname_to_pid.get((String) item.get("name")), price, quantity);
                throwIfError(pres);
            }
        }
    }

    private void logoutAndExit(String u) {
        SLResponseOBJ<String> su = facade.logout(u);
        throwIfError(su);
        throwIfError(facade.guestLeave(su.value));
    }

    private void throwIfError(SLResponseOBJ slr) {
        if (slr.errorOccurred()) throw new IllegalArgumentException("eror: " + slr.errorMsg);
    }

    private boolean checkJsonItemInStore(Map<String, Map<String, List<Map<String, Object>>>> products, String email, String store) {
        return (products.containsKey(email) && products.get(email).containsKey(store));
    }


}
