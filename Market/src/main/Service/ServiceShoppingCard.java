package main.Service;

import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceShoppingCard {
    ConcurrentHashMap<Integer, ShoppingBag> shoppingBagHash; //<store id , shopping bag>
    public ServiceShoppingCard(ShoppingCart shoppingCart){
        shoppingBagHash = new ConcurrentHashMap<>();
    }
    public List<List<ServiceItem>> get(){
        List<List<ServiceItem>> lst = new LinkedList<>();
        shoppingBagHash.forEach((i,bag)->{
            List<ServiceItem> l = new LinkedList<>();
            //TODO: convert shopping bag to list of ServiceItems.
            lst.add(l);
        });
        return lst;
    }
}
