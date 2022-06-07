package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.ConditionProductDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleStoreDiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;

import java.util.concurrent.ConcurrentHashMap;

public class ConditionProductDiscountRule extends  SimpleProductDiscountRule{

    protected ProductPred pred;


    public ConditionProductDiscountRule(ProductPred pred, double percentDiscount) {
        super(percentDiscount,pred.getProductID());
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

    @Override
    public DResponseObj<String> getDiscountRule() {
        String stringRule = "";
        if (id == 0) stringRule += pred.getPredicateDiscountRule();
        else {
            stringRule += "Conditional Product Discount Rule #" + id + ":\n\t";
            stringRule += pred.getPredicateDiscountRule();
            stringRule += " so productID " + productId + " have a " + percentDiscount + "% discount";
        }
        return new DResponseObj<>(stringRule);
    }

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        return new DResponseObj<>(new ConditionProductDiscountRuleSL(new ProductPredicateSL(pred),percentDiscount));
    }

}
