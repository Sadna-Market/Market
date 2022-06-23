package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Qualifier("UserPred")
public class UserPred implements Predicate{
    private final String username;  //can't buy in this store

    @JsonCreator
    public UserPred(@JsonProperty("username") String username){
        this.username = username;
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age , ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        return username.equals(user) ? new DResponseObj<>(false, ErrorCode.USER_CAN_NOT_BUY_IN_THIS_STORE) : new DResponseObj<>(true);
    }

   /* @Override
    public String getPredicateBuyRule() {
        return "user email: " + username + " can't buy";
    }

    @Override
    public String getPredicateDiscountRule() {
        return null;
    }*/

    public String getUsername() {
        return username;
    }
}
