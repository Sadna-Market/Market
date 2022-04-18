package main.System.Server.Domain.Market;

import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    ConcurrentHashMap<Integer,ShoppingBag> shoppingBagHash; //<store id , shopping bag>
    //todo is it ok for the assosiatin class

    public boolean addNewProductToShoppingBag(int ProductId, Store Store, int quantity){
        if(!shoppingBagHash.containsKey(Store.getStoreId())){
            shoppingBagHash.put(Store.getStoreId(),new ShoppingBag());
        }
        ShoppingBag shoppingBag = shoppingBagHash.get(Store.getStoreId());
        return shoppingBag.addProduct(ProductId,quantity);
    }

    public boolean setProductQuantity(int storeId,int productId, int quantity)
    {
        return shoppingBagHash.get(storeId).setProductQuantity(productId,quantity);
    }

    public boolean removeProductFromShoppingBag(int storeId,int productId){
        return shoppingBagHash.get(storeId).removeProductFromShoppingBag(productId);
    }

}
