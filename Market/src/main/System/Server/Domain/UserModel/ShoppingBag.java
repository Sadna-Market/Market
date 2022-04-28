package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.StoreModel.Store;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class ShoppingBag {
    static Logger logger= Logger.getLogger(ShoppingBag.class);
    private Store store;
    private ConcurrentHashMap<Integer, Integer> productQuantity ;


    public ShoppingBag(Store store){
        this.productQuantity=new ConcurrentHashMap<>();
        this.store = store;
    }

    public boolean isContainProduct(int pid){
        return productQuantity.containsKey(pid);
    }

    public int getProductQuantity(int pid){
        return productQuantity.get(pid);
    }

    public Store getStore() {
        logger.debug("ShoppingBag getStore");
        return store;
    }


    public boolean addProduct(int productId , int quantity){
        logger.debug(" ShoppingBag addProduct");
        if(productQuantity.containsKey(productId)){return false;}
        else {
            productQuantity.put(productId,quantity);
            return true;
        }
    }

    public boolean setProductQuantity(int productId, int quantity)
    {
        logger.debug("ShoppingBag setProductQuantity");
        if(!productQuantity.containsKey(productId)){
            return false;
        }
        else {
            productQuantity.replace(productId,quantity);
            return true;
        }
    }

    public boolean removeProductFromShoppingBag(int productId){
        logger.debug("ShoppingBag removeProductFromShoppingBag");
        if(!productQuantity.containsKey(productId)){
            return false;
        }
        else {
            productQuantity.remove(productId);
            return true;
        }
    }

    public ConcurrentHashMap<Integer, Integer> getProductQuantity() {
        return productQuantity;
    }
}
