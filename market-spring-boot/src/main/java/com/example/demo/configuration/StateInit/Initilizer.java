package com.example.demo.configuration.StateInit;

import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import com.example.demo.configuration.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Initilizer {
    private Facade facade;
    public Initilizer( Facade facade) {
        this.facade=facade;

    }

    public  void initialization(String adminEmail,String adminPassword) throws IOException {
        JsonReader readJson = config.get_instance().getJsonReaderState();
        Map<String, Integer> userName_id = new HashMap<>();
        Map<String, Integer> pname_to_pid = adminaddSystemProducts(adminEmail, adminPassword, readJson);




    }
    public void register (JsonReader readJson){
        SLResponseOBJ<String> uuidSlr;
        String uuid;
        for (Map<String, String> reg : readJson.register) {
            uuidSlr = facade.guestVisit();
            if (uuidSlr.errorOccurred()) throw new IllegalArgumentException();
            uuid = uuidSlr.value;
            String email = reg.get("email");
            String password = reg.get("password");
            String phoneNumber = reg.get("phoneNumber");
            String date = reg.get("dateOfBirth");
            SLResponseOBJ<Boolean> res = facade.addNewMember(uuid, email, password, phoneNumber, date);
            if (res.errorOccurred()) throw new IllegalArgumentException("");
            res = facade.guestLeave(uuid);
            if (res.errorOccurred()) throw new IllegalArgumentException();
        }
    }

    public void login(JsonReader reader)
    {
        SLResponseOBJ<String> uuidSlr;
        String uuid;
        Map<String,String> uuidlogins =new HashMap<>();
        for(Map<String,String> log :reader.login){
            uuidSlr = facade.guestVisit();
            if (uuidSlr.errorOccurred()) throw new IllegalArgumentException();

        }

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
        if(readJson.stores.containsKey(email)) {
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
        if(checkJsonItemInStore(readJson.add_item, email, name)) {
            List<Map<String, Object>> storeItems = readJson.add_item.get(email).get(name);
            for (Map<String, Object> item : storeItems) {
                double price = (Double) item.get("price");
                int quantity = (int) item.get("quantity");
                SLResponseOBJ<Boolean> pres=facade.addNewProductToStore(u, StoreId, pname_to_pid.get((String) item.get("name")), price, quantity);
                throwIfError(pres);
            }
        }
    }

    private void logoutAndExit(String u){
        SLResponseOBJ<String> su=facade.logout(u);
        throwIfError(su);
        throwIfError(facade.guestLeave(su.value));
    }

    private void throwIfError(SLResponseOBJ slr){
        if(slr.errorOccurred()) throw new IllegalArgumentException("eror: "+ slr.errorMsg);
    }

    private boolean checkJsonItemInStore(Map<String, Map<String, List<Map<String, Object>>>> products, String email, String store){
        return (products.containsKey(email)&&products.get(email).containsKey(store));
    }
}
