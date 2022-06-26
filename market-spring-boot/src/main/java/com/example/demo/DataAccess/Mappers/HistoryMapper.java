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

   HashMap<Integer,History> historyHashMap;
    DataServices dataServices;

    private static class HistoryMapperWrapper {
        static HistoryMapper single_instance = new HistoryMapper();
    }
    private HistoryMapper() {
        historyHashMap= new HashMap<>();
    }

    public static HistoryMapper getInstance() {
        return HistoryMapper.HistoryMapperWrapper.single_instance;
    }

    public void  setDataService(DataServices dataServices){
        this.dataServices = dataServices;
    }

    public List<History> getStoreHistory(Integer StoreId)
    {

        List<DataHistory>  dataHistories= dataServices.getHistoryService().getAllHistoryByStoreId(StoreId);
        if(dataHistories==null){
            return null;
        }
        List<History> storeH = new LinkedList<>();
        for(DataHistory dataHistory : dataHistories){
            if(historyHashMap.containsKey(dataHistory.getHistoryId())){
                storeH.add(historyHashMap.get(dataHistory.getHistoryId()));
            }
            else {
                storeH.add(convertToDomainHistory(dataHistory));
            }
            }

        return storeH;
    }


    public List<History> getUserHistory(String email)
    {

        List<DataHistory>  dataHistories= dataServices.getHistoryService().getAllHistoryByUsername(email);
        if(dataHistories==null){
            return null;
        }
        List<History> userH = new LinkedList<>();
        for(DataHistory dataHistory : dataHistories){
            if(historyHashMap.containsKey(dataHistory.getHistoryId())){
                userH.add(historyHashMap.get(dataHistory.getHistoryId()));
            }
            else {
                userH.add(convertToDomainHistory(dataHistory));
            }
        }
        return userH;
    }

    public History convertToDomainHistory(DataHistory dataHistory)
    {
        History history = new History(dataHistory.getTID(),dataHistory.getSupplyId(),dataHistory.getFinalPrice(),dataHistory.getUser());
        historyHashMap.put(dataHistory.getHistoryId(),history);
        // insert before

        List<ProductStore> list = getProductStores(dataHistory.getProducts());
        if(list ==null){
            throw new IllegalArgumentException();
        }
        history.setProducts(list);
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
