package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.DiscountRule.ConditionCategoryDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.ConditionProductDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Service.ServiceObj.Predicate.CategoryPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ConditionCategoryDiscountRuleSL extends SimpleCategoryDiscountRuleSL {

    public CategoryPredicateSL pred;

    public ConditionCategoryDiscountRuleSL(CategoryPredicateSL pred, double percentDiscount) {
        super(percentDiscount, pred.getCategory());
        this.pred = pred;
    }

    @Override
    public SLResponseOBJ<String> getDiscountRule() {
        String stringRule = "";
        if (id == 0) stringRule += pred.getPredicateDiscountRule();
        else {
            stringRule += "Conditional Category Discount Rule #" + id + ":\n\t";
            stringRule += pred.getPredicateDiscountRule();
            stringRule += " so all products from Category " + categoryId + " have a " + percentDiscount + "% discount";
        }

        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<DiscountRule> convertToDiscountRuleDL() {
        SLResponseOBJ<Predicate> predicate = pred.convertToPredicateDL();
        if(predicate.errorOccurred()) return new SLResponseOBJ<>(predicate.getErrorMsg());
        return new SLResponseOBJ<>(new ConditionCategoryDiscountRule(predicate.value,percentDiscount,pred.category));
    }
}
