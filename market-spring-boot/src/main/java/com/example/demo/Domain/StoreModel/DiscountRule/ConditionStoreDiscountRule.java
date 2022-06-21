package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.ConditionStoreDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleStoreDiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;

import java.util.concurrent.ConcurrentHashMap;

public class ConditionStoreDiscountRule extends SimpleStoreDiscountRule{
    protected ShoppingBagPred pred;

    public ConditionStoreDiscountRule(ShoppingBagPred pred,double percentDiscount) {
        super(percentDiscount);
        this.pred = pred;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username, int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        DResponseObj<Boolean> res = pred.passRule(username,age,shoppingBag);
        if(!res.errorOccurred()){
            return super.howMuchDiscount(username,age,shoppingBag);
        }
        return new DResponseObj<>(0.0, res.getErrorMsg());
    }

/*    @Override
    public DResponseObj<String> getDiscountRule() {
        String stringRule = "";
        if (id == 0) stringRule += pred.getPredicateDiscountRule();
        else {
            stringRule += "Conditional Store Discount Rule #" + id + ":\n\t";
            stringRule += pred.getPredicateDiscountRule();
            stringRule += " so all products in store have a " + percentDiscount + "% discount";
        }
        return new DResponseObj<>(stringRule);
    }*/

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        return new DResponseObj<>(new ConditionStoreDiscountRuleSL(new ShoppingBagPredicateSL(pred),percentDiscount,id));
    }
}
