package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.StoreModel.Store;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    static Logger logger= Logger.getLogger(ShoppingBag.class);
    ConcurrentHashMap<Integer, ShoppingBag> shoppingBagHash; //<store id , shopping bag>

    public ShoppingCart(){
        shoppingBagHash=new ConcurrentHashMap<>();
    }

    public boolean addNewProductToShoppingBag(int ProductId, Store Store, int quantity){
        logger.debug("ShoppingCart addNewProductToShoppingBag");
        if(!shoppingBagHash.containsKey(Store.getStoreId())){
            shoppingBagHash.put(Store.getStoreId(),new ShoppingBag(Store));
        }
        ShoppingBag shoppingBag = shoppingBagHash.get(Store.getStoreId());
        return shoppingBag.addProduct(ProductId,quantity);
    }

    public boolean isCartExist(int storeId){
        return shoppingBagHash.containsKey(storeId);

    }

    public boolean setProductQuantity(int storeId,int productId, int quantity)
    {
        logger.debug("ShoppingCart setProductQuantity");
        if(shoppingBagHash.containsKey(storeId)){
            return shoppingBagHash.get(storeId).setProductQuantity(productId,quantity);
        }
        else {
            return false;
        }
    }

    public  boolean removeProductFromShoppingBag(int storeId,int productId){
        logger.debug("ShoppingCart removeProductFromShoppingBag");
        if(shoppingBagHash.containsKey(storeId)) {
            return shoppingBagHash.get(storeId).removeProductFromShoppingBag(productId);
        }
        else {
            return false;
        }
    }

    public ConcurrentHashMap<Integer, ShoppingBag> getHashShoppingCart(){
        return shoppingBagHash;
    }


    public boolean removeShoppingCart(int StoreID)
    {
        if(shoppingBagHash.containsKey(StoreID)){
            shoppingBagHash.remove(StoreID);
            return true;
        }
        else {
            return false;



        }
    }

}
