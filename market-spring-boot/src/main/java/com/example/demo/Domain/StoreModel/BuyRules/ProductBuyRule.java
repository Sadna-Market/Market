package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.ProductBuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.ConditionStoreDiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;

import java.util.concurrent.ConcurrentHashMap;

public class ProductBuyRule extends LeafBuyRule{

    public ProductBuyRule(ProductPred pred) {
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
            stringRule += "Product Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new DResponseObj<>(stringRule);
    }

    @Override
    public DResponseObj<BuyRuleSL> convertToBuyRuleSL() {
        return new DResponseObj<>(new ProductBuyRuleSL(new ProductPredicateSL((ProductPred) pred)));
    }
}
