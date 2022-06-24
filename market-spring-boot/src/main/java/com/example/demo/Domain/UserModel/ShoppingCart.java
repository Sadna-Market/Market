package com.example.demo.Domain.UserModel;

import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Store;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    static Logger logger= Logger.getLogger(ShoppingBag.class);
    ConcurrentHashMap<Integer, ShoppingBag> shoppingBagHash; //<store id , shopping bag>
    String username;

    private static DataServices dataServices;

    public ShoppingCart(String username){
        shoppingBagHash=new ConcurrentHashMap<>();
        this.username = username;
    }
    public ShoppingCart(String username,ConcurrentHashMap<Integer,ShoppingBag> shoppingBagHash){
        this.shoppingBagHash= shoppingBagHash;
        this.username = username;
    }

    public DResponseObj<Boolean> addNewProductToShoppingBag(int ProductId, Store store, int quantity){
        logger.debug("ShoppingCart addNewProductToShoppingBag");
        if(!shoppingBagHash.containsKey(store.getStoreId().value)){
            shoppingBagHash.put(store.getStoreId().value,new ShoppingBag(store,username));
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

    public static void setDataServices(DataServices dataServices) {
        ShoppingCart.dataServices = dataServices;
    }


//    public void load() {
//        var dataShoppingBags = dataServices.getShoppingBagService().getUserShoppingBags(username);
//        dataShoppingBags.forEach(shoppingBag -> {
//            shoppingBagHash.put(
//                shoppingBag.getShoppingBagId().getStoreId(),
//                new ShoppingBag().fromData(shoppingBag)
//            );
//        });
//    }

//    public DataShoppingCart getDataObject() {
//        DataShoppingCart dataShoppingCart = new DataShoppingCart();
//        Set<DataShoppingBag> dataShoppingBags = this.shoppingBagHash.values()
//                .stream()
//                .map(ShoppingBag::getDataObject).collect(Collectors.toSet());
//        dataShoppingCart.setShoppingBags(dataShoppingBags);
//        return dataShoppingCart;
//    }
}
