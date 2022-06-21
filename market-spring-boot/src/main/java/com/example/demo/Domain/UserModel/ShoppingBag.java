package com.example.demo.Domain.UserModel;

import com.example.demo.DataAccess.Entity.DataShoppingBag;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Domain.StoreModel.Store;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class ShoppingBag {
    static Logger logger = Logger.getLogger(ShoppingBag.class);
    private Store store;
    private ConcurrentHashMap<Integer, Integer> productQuantity;


    public ShoppingBag(Store store) {
        this.productQuantity = new ConcurrentHashMap<>();
        this.store = store;
    }

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
        return new DResponseObj<>(true);

    }

    public DResponseObj<Boolean> setProductQuantity(int productId, int quantity) {
        logger.debug("ShoppingBag setProductQuantity");
        if (!productQuantity.containsKey(productId)) {
            return new DResponseObj<>(false);
        } else {
            productQuantity.replace(productId, quantity);
            return new DResponseObj<>(true);
        }
    }

    public DResponseObj<Boolean> removeProductFromShoppingBag(int productId) {
        logger.debug("ShoppingBag removeProductFromShoppingBag");
        if (!productQuantity.containsKey(productId)) {
            return new DResponseObj<>(false,-1);
        } else {
            productQuantity.remove(productId);
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
        dataShoppingBag.setStore(this.store.getDataObject());
        dataShoppingBag.setProductQuantity(this.productQuantity);
        return dataShoppingBag;
    }
}
