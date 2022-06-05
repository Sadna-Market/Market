package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.DiscountRule.ConditionProductDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.ConditionStoreDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ConditionProductDiscountRuleSL extends SimpleProductDiscountRuleSL {

    public ProductPredicateSL pred;

    public ConditionProductDiscountRuleSL(ProductPredicateSL pred, double percentDiscount) {
        super(percentDiscount,pred.getProductID());
        this.pred = pred;
    }

    @Override
    public SLResponseOBJ<String> getDiscountRule() {
        String stringRule = "";
        if (id == 0) stringRule += pred.getPredicateDiscountRule();
        else {
            stringRule += "Conditional Product Discount Rule #" + id + ":\n\t";
            stringRule += pred.getPredicateDiscountRule();
            stringRule += " so productID " + productId + " have a " + percentDiscount + "% discount";
        }
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<DiscountRule> convertToDiscountRuleDL() {
        SLResponseOBJ<Predicate> predicate = pred.convertToPredicateDL();
        if(predicate.errorOccurred()) return new SLResponseOBJ<>(predicate.getErrorMsg());
        return new SLResponseOBJ<>(new ConditionProductDiscountRule(predicate.value,percentDiscount,pred.productID));
    }

}
