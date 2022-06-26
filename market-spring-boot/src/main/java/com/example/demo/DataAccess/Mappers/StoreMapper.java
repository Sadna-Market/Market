package com.example.demo.DataAccess.Mappers;

import com.example.demo.DataAccess.Entity.DataHistory;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.DataAccess.Services.StoreService;
import com.example.demo.Domain.Market.Market;
import com.example.demo.Domain.Market.Permission;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.*;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class StoreMapper {

    DataServices dataServices;
    static Logger logger = Logger.getLogger(StoreMapper.class);

    Map<Integer, Store> stores;
    private static class StoreMapperWrapper {
        static StoreMapper single_instance = new StoreMapper();
    }



    private StoreMapper() {


        this.stores = new ConcurrentHashMap<>();
    }

    public void  setDataService(DataServices dataServices){
        this.dataServices = dataServices;
    }

    public static StoreMapper getInstance() {
        return StoreMapperWrapper.single_instance;
    }

    public Store getStore(Integer storeId)
    {
        if(stores.containsKey(storeId)){
            return stores.get(storeId);
        }

        DataStore dataStore = dataServices.getStoreService().getStoreById(storeId);
        if(dataStore == null){
            return null;
        }
        Store store = convertToDomainStore(dataStore);

        stores.put(storeId,store);
        return store;
    }


    public Map<Integer, Store> getAllStores() {
        List<DataStore> dataStoreList = dataServices.getStoreService().getAllStores();
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
                Store store = convertToDomainStore(ds);
                if(ds == null){
                    throw new IllegalArgumentException();
                }
                stores.put(storeId,store);

            }

        }
        return stores;
    }



    private Store convertToDomainStore(DataStore dataStore){
        Store store =new Store(dataStore.getStoreId(),dataStore.getName(),dataStore.getFounder(),dataStore.getOpen(),
                dataStore.getRate(),dataStore.getNumOfRated());


        //create products
        ConcurrentHashMap<Integer, ProductStore> products = new ConcurrentHashMap<>();
        dataStore.getProductStores().forEach(product -> {
            ProductType productType = ProductTypeMapper.getInstance().getProductType(product.getProductType().getProductTypeId());
            if(productType == null) {
                logger.error("failed to get productType - for upload inventory");
                throw new IllegalArgumentException("failed to get productType - for upload inventory");//TODO remove
            }
            ProductStore p = new ProductStore(productType,product.getQuantity(),product.getPrice());
            products.put(p.getProductType().getProductID().value,p);
        });
        store.getInventory().value.setProducts(products);


        //create histories
        List<History> h = HistoryMapper.getInstance().getStoreHistory(dataStore.getStoreId());
        ConcurrentHashMap<Integer, History> histories = new ConcurrentHashMap<>();
        for (History history :h){
            histories.put(history.getTID(),history);
        }
        store.setHistory(histories);


        ObjectMapper mapper = new ObjectMapper();
        //create buy policy
        ConcurrentHashMap<Integer, BuyRule> buyPolicy = new ConcurrentHashMap<>();
        dataStore.getBuyRules().forEach(dataBuyRule -> {
            try {
                BuyRule convertedToBR = mapper.readValue(dataBuyRule.getRule(), BuyRule.class);
                convertedToBR.setID(dataBuyRule.getBuyRuleId());
                buyPolicy.put(convertedToBR.getId(),convertedToBR);
            } catch (JsonProcessingException e) {
                logger.error("failed to upload buy rule #"+dataBuyRule.getBuyRuleId());
                e.printStackTrace();
            }
        });
        store.setBuyPolicy(new BuyPolicy(buyPolicy));

        //create discount policy
        ConcurrentHashMap<Integer, DiscountRule> discountPolicy = new ConcurrentHashMap<>();
        dataStore.getDiscountRules().forEach(dataDiscountRule -> {
            try {
                DiscountRule convertedToDR = mapper.readValue(dataDiscountRule.getRule(), DiscountRule.class);
                convertedToDR.setID(dataDiscountRule.getDiscountRuleId());
                discountPolicy.put(convertedToDR.getId(), convertedToDR);
            } catch (JsonProcessingException e) {
                logger.error("failed to upload discount rule #" + dataDiscountRule.getDiscountRuleId());
                e.printStackTrace();
            }
        });
        store.setDiscountPolicy(new DiscountPolicy(discountPolicy));


        //create permissions
        List<Permission> permissions = PermissionMapper.getInstance().getStorePermission(dataStore.getStoreId());
        store.setPermission(permissions);


        //create bids
        ConcurrentLinkedDeque<BID> bids = new ConcurrentLinkedDeque<>();
        dataStore.getBids().forEach(dataBID -> {
            ConcurrentHashMap<String,Boolean> approves = new ConcurrentHashMap<>();
            dataBID.getApproves().forEach(approves::put);
            BID bid = new BID(dataStore.getStoreId(),dataBID.getId().getUsername(),dataBID.getId().getProductTypeId(),dataBID.getProductName(),
                    dataBID.getQuantity(),dataBID.getTotalPrice(),dataBID.getLastPrice(),dataBID.getStatus(),approves);
            bids.add(bid);
        });
        store.setBids(bids);

        return store;

    }
}