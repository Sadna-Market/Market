package com.example.demo.DataAccess.Mappers;

import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Services.StoreService;
import com.example.demo.Domain.StoreModel.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StoreMapper {
    StoreService storeService;
    Map<Integer, Store> stores;
    private static class StoreMapperWrapper {
        static StoreMapper single_instance(StoreService storeService) {

           return new StoreMapper(storeService);
        }
    }


    private StoreMapper(StoreService storeService) {
        this.storeService = storeService;


        this.stores = new ConcurrentHashMap<>();
    }

    public static StoreMapper getInstance(StoreService storeService) {
        return StoreMapperWrapper.single_instance(storeService);
    }

    public Store getStore(Integer storeId)
    {
        if(stores.containsKey(storeId)){
            return stores.get(storeId);
        }

        DataStore dataStore = storeService.getStoreById(storeId);
        if(dataStore == null){
            return null;
        }
        Store store = convertToDomainStore(dataStore);

        stores.put(storeId,store);
        return store;
    }


    public Map<Integer, Store> getAllStores() {
        List<DataStore> dataStoreList = storeService.getAllStores();
        Map<Integer, Store> res = new HashMap<>();

        if (dataStoreList == null) {
            return null;
        }
        for (DataStore ds : dataStoreList
        ) {
            Integer storeId = ds.getStoreId();
            if (stores.containsKey(storeId)) {
                res.put(storeId, stores.get(storeId));
            } else {

                DataStore dataStore = storeService.getStoreById(storeId);
                if(dataStore == null){
                    return null;
                }
                Store store = convertToDomainStore(dataStore);
                stores.put(storeId,store);
            }

        }
        return res;
    }



    private Store convertToDomainStore(DataStore dataStore){
        return null;
        //todo
    }
}
