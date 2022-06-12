package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.concurrent.ConcurrentHashMap;

public interface Predicate {

    DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag);
    String getPredicateBuyRule();
    String getPredicateDiscountRule();

}
