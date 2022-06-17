package com.example.demo.Domain.UserModel;

import com.example.demo.DataAccess.Entity.DataShoppingBag;
import com.example.demo.DataAccess.Entity.DataShoppingCart;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Store;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    static Logger logger= Logger.getLogger(ShoppingBag.class);
    ConcurrentHashMap<Integer, ShoppingBag> shoppingBagHash; //<store id , shopping bag>

    public ShoppingCart(){
        shoppingBagHash=new ConcurrentHashMap<>();
    }

    public DResponseObj<Boolean> addNewProductToShoppingBag(int ProductId, Store store, int quantity){
        logger.debug("ShoppingCart addNewProductToShoppingBag");
        if(!shoppingBagHash.containsKey(store.getStoreId().value)){
            shoppingBagHash.put(store.getStoreId().value,new ShoppingBag(store));
        }
        ShoppingBag shoppingBag = shoppingBagHash.get(store.getStoreId().value);
        return new DResponseObj<Boolean>( shoppingBag.addProduct(ProductId,quantity).value);
    }


    public DResponseObj<Boolean>  isCartExist(int storeId){
        return new DResponseObj<>( shoppingBagHash.containsKey(storeId));

    }

    public DResponseObj<Boolean>  setProductQuantity(int storeId,int productId, int quantity)
    {
        logger.debug("ShoppingCart setProductQuantity");
        if(shoppingBagHash.containsKey(storeId)){
            ShoppingBag bag = shoppingBagHash.get(storeId);
            Store store = bag.getStore().value;
            DResponseObj<Boolean> hasQuantity = store.isProductExistInStock(productId,quantity);
            return hasQuantity.errorOccurred() ? new DResponseObj<>(hasQuantity.value,hasQuantity.errorMsg) : bag.setProductQuantity(productId,quantity);
        }
        else {
            return new DResponseObj<>( false, ErrorCode.NO_STORE_IN_BAG);
        }
    }



    public DResponseObj<Boolean> removeProductFromShoppingBag(int storeId, int productId){
        logger.debug("ShoppingCart removeProductFromShoppingBag");
        if(shoppingBagHash.containsKey(storeId)) {
            DResponseObj<Boolean> res = shoppingBagHash.get(storeId).removeProductFromShoppingBag(productId);
            if(res.errorOccurred())
                return new DResponseObj<>(res.value,res.errorMsg);
            if(shoppingBagHash.get(storeId).isEmpty().value)
                shoppingBagHash.remove(storeId);
            return new DResponseObj<>(true,-1);
        }
        else {
            return new DResponseObj<>( false, ErrorCode.NOTVALIDINPUT);
        }
    }

    public DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>> getHashShoppingCart(){
        return new DResponseObj<>( shoppingBagHash,-1);
    }


    public DResponseObj<Boolean>  removeShoppingCart(int StoreID)
    {
        if(shoppingBagHash.containsKey(StoreID)){
            shoppingBagHash.remove(StoreID);
            return new DResponseObj<>( true);
        }
        else {
            return new DResponseObj<>( false);



        }
    }
}
