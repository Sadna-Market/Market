package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;

import java.util.concurrent.ConcurrentHashMap;

public class Purchase {

    Market market;
    public boolean order(ShoppingCart shoppingCart){
        //catch
        //supply
        //payment


        //for (ShoppingBag sb: shoppingCart.)
            return false;
    }

    public DResponseObj<Boolean> order(User user){
         ConcurrentHashMap<Integer,ShoppingBag> bags =user.GetSShoppingCart().value.getHashShoppingCart().value;
         for (Integer i: bags.keySet()){
             ShoppingBag bag=bags.get(i);
             Store store=bag.getStore().value;

             for (Integer j: bag.getProductQuantity().value.keySet()){

             }
         }
;
        //catch
        //supply
        //payment


        //for (ShoppingBag sb: shoppingCart.)
        return null;
    }


}
