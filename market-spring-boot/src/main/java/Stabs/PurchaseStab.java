package Stabs;

import com.example.demo.Domain.Market.DetailsPurchase;
import com.example.demo.Domain.Market.Purchase;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.UserModel.User;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseStab extends Purchase {
    @Override
    public DResponseObj<DetailsPurchase> order(User user, String city , String Street, int apartment,
                                               String card, String exp, String pin){
        return new DResponseObj<>(new DetailsPurchase(1,2, List.of(1)));
    }
}
