package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.ConditionCategoryDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.ConditionProductDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.CategoryPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.concurrent.ConcurrentHashMap;

@JsonTypeName("ConditionCategoryDiscountRule")
public class ConditionCategoryDiscountRule extends SimpleCategoryDiscountRule {

    protected CategoryPred pred;

    @JsonCreator
    public ConditionCategoryDiscountRule(@JsonProperty("pred") CategoryPred pred,@JsonProperty("percentDiscount") double percentDiscount) {
        super(percentDiscount, pred.getCategory());
        this.pred = pred;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username, int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        DResponseObj<Boolean> res = pred.passRule(username, age, shoppingBag);
        if (!res.errorOccurred()) {
            return super.howMuchDiscount(username, age, shoppingBag);
        }
        return new DResponseObj<>(0.0, res.getErrorMsg());
    }

/*    @Override
    public DResponseObj<String> getDiscountRule() {
        String stringRule = "";
        if (id == 0) stringRule += pred.getPredicateDiscountRule();
        else {
            stringRule += "Conditional Category Discount Rule #" + id + ":\n\t";
            stringRule += pred.getPredicateDiscountRule();
            stringRule += " so all products from Category " + categoryId + " have a " + percentDiscount + "% discount";
        }

        return new DResponseObj<>(stringRule);
    }*/

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        return new DResponseObj<>(new ConditionCategoryDiscountRuleSL(new CategoryPredicateSL(pred),percentDiscount,id));
    }


    public CategoryPred getPred() {
        return pred;
    }

}
