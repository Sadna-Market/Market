package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.StoreModel.Store;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    static Logger logger= Logger.getLogger(ShoppingBag.class);
    ConcurrentHashMap<Integer, ShoppingBag> shoppingBagHash; //<store id , shopping bag>

    public ShoppingCart(){
        shoppingBagHash=new ConcurrentHashMap<>();
    }

    public DResponseObj<Boolean> addNewProductToShoppingBag(int ProductId, Store Store, int quantity){
        logger.debug("ShoppingCart addNewProductToShoppingBag");
        if(!shoppingBagHash.containsKey(Store.getStoreId())){
            shoppingBagHash.put(Store.getStoreId().value,new ShoppingBag(Store));
        }
        ShoppingBag shoppingBag = shoppingBagHash.get(Store.getStoreId());
        return new DResponseObj<Boolean>( shoppingBag.addProduct(ProductId,quantity).value);
    }


    public DResponseObj<Boolean>  isCartExist(int storeId){
        return new DResponseObj<>( shoppingBagHash.containsKey(storeId));

    }

    public DResponseObj<Boolean>  setProductQuantity(int storeId,int productId, int quantity)
    {
        logger.debug("ShoppingCart setProductQuantity");
        if(shoppingBagHash.containsKey(storeId)){
            return new DResponseObj<Boolean>( shoppingBagHash.get(storeId).setProductQuantity(productId,quantity).value);
        }
        else {
            return new DResponseObj<>( false);
        }
    }



    public  DResponseObj<Boolean>  removeProductFromShoppingBag(int storeId,int productId){
        logger.debug("ShoppingCart removeProductFromShoppingBag");
        if(shoppingBagHash.containsKey(storeId)) {
            return new DResponseObj<Boolean>( shoppingBagHash.get(storeId).removeProductFromShoppingBag(productId).value);
        }
        else {
            return new DResponseObj<>( false);
        }
    }

    public DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>> getHashShoppingCart(){
        return new DResponseObj<>( shoppingBagHash);
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
