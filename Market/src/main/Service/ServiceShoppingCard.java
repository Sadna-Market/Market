package main.Service;

import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceShoppingCard {
    ConcurrentHashMap<Integer, ShoppingBag> shoppingBagHash; //<store id , shopping bag>
    public ServiceShoppingCard(ShoppingCart shoppingCart){

    }
}
