package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserPred implements Predicate{
    private final String username;  //can't buy in this store

    public UserPred(String username){
        this.username = username;
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age , ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        return username.equals(user) ? new DResponseObj<>(false, ErrorCode.USER_CAN_NOT_BUY_IN_THIS_STORE) : new DResponseObj<>(true);
    }

    @Override
    public String getPredicateBuyRule() {
        return "user email: " + username + " can't buy";
    }

    @Override
    public String getPredicateDiscountRule() {
        return null;
    }


}
