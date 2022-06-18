package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.DiscountRule.ConditionProductDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.ConditionStoreDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ConditionProductDiscountRuleSL extends SimpleProductDiscountRuleSL {

    public String kind = "Condition Product Discount Rule";

    public ProductPredicateSL pred;

    public ConditionProductDiscountRuleSL(ProductPredicateSL pred, double percentDiscount) {
        super(percentDiscount,pred.getProductID());
        this.pred = pred;
    }

    //for convert
    public ConditionProductDiscountRuleSL(ProductPredicateSL pred, double percentDiscount,int id) {
        super(percentDiscount,pred.getProductID(),id);
        this.pred = pred;
    }

    //use when this rule is inside composite rule
    public ConditionProductDiscountRuleSL(ProductPredicateSL pred) {
        super(1.0,pred.getProductID());
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
        return new SLResponseOBJ<>(new ConditionProductDiscountRule((ProductPred) predicate.value,percentDiscount));
    }

}
