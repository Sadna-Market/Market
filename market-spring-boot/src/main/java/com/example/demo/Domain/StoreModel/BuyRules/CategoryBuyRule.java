package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.concurrent.ConcurrentHashMap;

public class CategoryBuyRule extends LeafBuyRule{

    public CategoryBuyRule(CategoryPred pred){
        super(pred);
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        return pred.passRule(user,age,shoppingBag);
    }

    @Override
    public DResponseObj<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Category Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new DResponseObj<>(stringRule);
    }
}