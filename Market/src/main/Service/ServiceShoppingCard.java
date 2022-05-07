package main.Service;

import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceShoppingCard {
    ConcurrentHashMap<Integer, ShoppingBag> shoppingBagHash; //<store id , shopping bag>
    public ServiceShoppingCard(ShoppingCart shoppingCart){
        shoppingBagHash = shoppingCart.getHashShoppingCart().value;
    }
    public List<List<Integer>> get(){
        List<List<Integer>> lst = new LinkedList<>();
        shoppingBagHash.forEach((i,bag)->{
            List<Integer> l = new LinkedList<>(bag.getProductQuantity().value.values());
            lst.add(l);
        });
        return lst;
    }
}
