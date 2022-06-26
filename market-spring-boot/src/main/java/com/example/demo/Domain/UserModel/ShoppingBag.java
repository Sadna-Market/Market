package com.example.demo.Domain.UserModel;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;
import com.example.demo.DataAccess.Entity.DataShoppingBag;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Domain.StoreModel.Store;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class ShoppingBag {
    static Logger logger = Logger.getLogger(ShoppingBag.class);
    private Store store;
    private String username;
    private ConcurrentHashMap<Integer, Integer> productQuantity;

    private static DataServices dataServices;

    public ShoppingBag(Store store, String username) {
        this.productQuantity = new ConcurrentHashMap<>();
        this.store = store;
        this.username = username;
    }
    public ShoppingBag(Store store, String username,ConcurrentHashMap<Integer, Integer> productQuantity){
        this.productQuantity = productQuantity;
        this.store = store;
        this.username = username;
    }
    public ShoppingBag(){}

    public DResponseObj<Boolean> isContainProduct(int pid) {
        return new DResponseObj<>(productQuantity.containsKey(pid),-1);
    }

    public DResponseObj<Integer> getProductQuantity(int pid) {
        return new DResponseObj<>(productQuantity.get(pid),-1);
    }

    public DResponseObj<Store> getStore() {
        logger.debug("ShoppingBag getStore");
        return new DResponseObj<>(store);
    }


    public DResponseObj<Boolean> addProduct(int productId, int quantity) {
        logger.debug(" ShoppingBag addProduct");
        productQuantity.put(productId, productQuantity.getOrDefault(productId, 0) + quantity);
        //db
        saveToDB();
        return new DResponseObj<>(true);

    }

    private void saveToDB() {
        if(dataServices != null && dataServices.getShoppingBagService() != null){
            var dataShoppingBag = getDataObject();
            dataServices.getShoppingBagService().insertShoppingBag(dataShoppingBag);
        }
    }

    public DResponseObj<Boolean> setProductQuantity(int productId, int quantity) {
        logger.debug("ShoppingBag setProductQuantity");
        if (!productQuantity.containsKey(productId)) {
            return new DResponseObj<>(false);
        } else {
            productQuantity.replace(productId, quantity);
            saveToDB();
            return new DResponseObj<>(true);
        }
    }

    public DResponseObj<Boolean> removeProductFromShoppingBag(int productId) {
        logger.debug("ShoppingBag removeProductFromShoppingBag");
        if (!productQuantity.containsKey(productId)) {
            return new DResponseObj<>(false,-1);
        } else {
            productQuantity.remove(productId);
            saveToDB();
            return new DResponseObj<>(true,-1);
        }
    }

    public DResponseObj<ConcurrentHashMap<Integer, Integer>> getProductQuantity() {
        return new DResponseObj<>(productQuantity);
    }

    public DResponseObj<ConcurrentHashMap<Integer, ProductStore>> getProducts() {
        ConcurrentHashMap<Integer,ProductStore> products = new ConcurrentHashMap<>();
        productQuantity.forEach((id,quantity) -> {
            DResponseObj<ProductStore> ps = store.getProductInStoreInfo(id);
            if(!ps.errorOccurred()){
                ProductStore productStore = ps.value;
                ProductStore productUser = new ProductStore(productStore.getProductType(),quantity,productStore.getPrice().value);
                products.put(id,productUser);
            }
        });
        return new DResponseObj<>(products);
    }
    public DResponseObj<Boolean> isEmpty(){
        return new DResponseObj<>(productQuantity.isEmpty(),-1);
    }

    public DataShoppingBag getDataObject() {
        DataShoppingBag dataShoppingBag = new DataShoppingBag();
        ShoppingBagId id = new ShoppingBagId();
        id.setStoreId(this.store.getStoreId().value);
        id.setUsername(this.username);
        dataShoppingBag.setShoppingBagId(id);
        dataShoppingBag.setStore(this.store.getDataObject());
        dataShoppingBag.setProductQuantity(this.productQuantity);
        return dataShoppingBag;
    }

    public static void setDataServices(DataServices dataServices) {
        ShoppingBag.dataServices = dataServices;
    }

    public void reset() {
        this.store = null;
        this.username = null;
        this.productQuantity.clear();
    }

//    public ShoppingBag fromData(DataShoppingBag shoppingBag) {
//        this.username = shoppingBag.getShoppingBagId().getUsername();
//        /*this.store =
//        *
//        //TODO: very big problem */
//        this.productQuantity = new ConcurrentHashMap<>(shoppingBag.getProductQuantity());
//        return this;
//    }
}
