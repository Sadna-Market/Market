package Stabs;

import main.ExternalService.CreditCard;
import main.System.Server.Domain.Market.Purchase;
import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.UserModel.User;

import java.util.concurrent.ConcurrentHashMap;

public class PurchaseStab extends Purchase {
    @Override
    public DResponseObj<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> order(User user, String city , String Street, int apartment,
                                                                                               CreditCard c){
        return new DResponseObj<>(new ConcurrentHashMap<>());
    }
}
