package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.BuyRules.ShoppingBagBuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.ConditionStoreDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleStoreDiscountRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ConditionStoreDiscountRuleSL extends SimpleStoreDiscountRuleSL {

    public String kind = "Condition Store Discount Rule";

    public ShoppingBagPredicateSL pred;

    public ConditionStoreDiscountRuleSL(ShoppingBagPredicateSL pred, double percentDiscount) {
        super(percentDiscount);
        this.pred = pred;
    }

    //for convert
    public ConditionStoreDiscountRuleSL(ShoppingBagPredicateSL pred, double percentDiscount,int id) {
        super(percentDiscount,id);
        this.pred = pred;
    }

    //use when this rule is inside composite rule
    public ConditionStoreDiscountRuleSL(ShoppingBagPredicateSL pred) {
        super(1.0);
        this.pred = pred;
    }
    @Override
    public SLResponseOBJ<String> getDiscountRule() {
        String stringRule = "";
        if (id == 0) stringRule += pred.getPredicateDiscountRule();
        else {
            stringRule += "Conditional Store Discount Rule #" + id + ":\n\t";
            stringRule += pred.getPredicateDiscountRule();
            stringRule += " so all products in store have a " + percentDiscount + "% discount";
        }
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<DiscountRule> convertToDiscountRuleDL() {
        SLResponseOBJ<Predicate> predicate = pred.convertToPredicateDL();
        if(predicate.errorOccurred()) return new SLResponseOBJ<>(predicate.getErrorMsg());
        return new SLResponseOBJ<>(new ConditionStoreDiscountRule((ShoppingBagPred) predicate.value,percentDiscount));
    }
}
