package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;
import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class Purchase {

    Market market;
    public boolean order(ShoppingCart shoppingCart){
        //catch
        //supply
        //payment


        //for (ShoppingBag sb: shoppingCart.)
            return false;
    }

    public ATResponseObj<Boolean> order(User user){
         ConcurrentHashMap<Integer,ShoppingBag> bags =user.getShoppingCart().getHashShoppingCart();
         for (Integer i: bags.keySet()){
             ShoppingBag bag=bags.get(i);
             Store store=bag.getStore();

             for (Integer j: bag.getProductQuantity().keySet()){

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
