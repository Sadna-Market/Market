package com.example.demo.DataAccess.Mappers;

import com.example.demo.DataAccess.Entity.DataHistory;
import com.example.demo.DataAccess.Entity.DataProductStoreHistory;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.History;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HistoryMapper {

    HashMap<Integer,List<History>> storeHistory;
    HashMap<String, List<History>> userHistory;

    HashMap<Integer, History> allHistories;
    DataServices dataServices;

    private static class HistoryMapperWrapper {
        static HistoryMapper single_instance = new HistoryMapper();
    }
    private HistoryMapper() {
        this.storeHistory = new HashMap<>();
        this.userHistory = new HashMap<>();
        this.allHistories = new HashMap<>();
    }

    public static HistoryMapper getInstance() {
        return HistoryMapper.HistoryMapperWrapper.single_instance;
    }

    public void  setDataService(DataServices dataServices){
        this.dataServices = dataServices;
    }

    public List<History> getStoreHistory(Integer StoreId)
    {
        if(storeHistory.containsKey(StoreId)){
            return storeHistory.get(StoreId);
        }
        List<DataHistory>  dataHistories= dataServices.getHistoryService().getAllHistoryByStoreId(StoreId);
        if(dataHistories==null){
            return null;
        }
        List<History> storeH = new LinkedList<>();
        for(DataHistory dataHistory : dataHistories){
            storeH.add(convertToDomainHistoryStore(dataHistory,StoreId));
        }
        return storeH;
    }


    public List<History> getUserHistory(String email)
    {
        if(userHistory.containsKey(email)){
            return userHistory.get(email);
        }
        List<DataHistory>  dataHistories= dataServices.getHistoryService().getAllHistoryByUsername(email);
        if(dataHistories==null){
            return null;
        }
        List<History> userH = new LinkedList<>();
        userHistory.put(email,userH);

        for(DataHistory dataHistory : dataHistories){
            convertToDomainHistoryUser(dataHistory,email);
        }
        return userH;
    }


    private History convertToDomainHistoryStore(DataHistory dataHistory, Integer storeId)
    {
        Set<DataProductStoreHistory> DA= dataHistory.getProducts();
        //insert history to the mapper befor hard case
        History history = new History(dataHistory.getHistoryId(),dataHistory.getSupplyId(),dataHistory.getFinalPrice(),dataHistory.getUser().getUsername());
        storeHistory.get(storeId).add(history);
        List<ProductStore> productStores = getProductStores(DA);
        history.setProducts(productStores);
        return history;
    }


    private History convertToDomainHistoryUser(DataHistory dataHistory, String email)
    {
        Set<DataProductStoreHistory> DA= dataHistory.getProducts();
        //insert history to the mapper befor hard case
        History history = new History(dataHistory.getHistoryId(),dataHistory.getSupplyId(),dataHistory.getFinalPrice(),dataHistory.getUser().getUsername());
        userHistory.get(email).add(history);

        List<ProductStore> productStores = getProductStores(DA);
        history.setProducts(productStores);
        return history;
    }
    private List<ProductStore> getProductStores(Set<DataProductStoreHistory> DA) {
        List<ProductStore> productStores = new LinkedList<>();
        for(DataProductStoreHistory dataProductStoreHistory : DA){
            ProductType productType = ProductTypeMapper.getInstance().getProductType(dataProductStoreHistory.getProductType().getProductTypeId());
            productStores.add(new ProductStore(productType,dataProductStoreHistory.getQuantity(),dataProductStoreHistory.getPrice()));
        }
        return productStores;
    }


}
