package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleCategoryDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleStoreDiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class SimpleCategoryDiscountRuleSL extends LeafDiscountRuleSL {

    public int categoryId;

    public SimpleCategoryDiscountRuleSL(double percentDiscount, int categoryId){
        super(percentDiscount);
        this.categoryId = categoryId;
    }

    @Override
    public SLResponseOBJ<String> getDiscountRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Simple Category Discount Rule #"+id + ":\n\t";
        stringRule += "All products in the category "+categoryId+ " have a "+percentDiscount+"% discount";
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<DiscountRule> convertToDiscountRuleDL() {
        return new SLResponseOBJ<>(new SimpleCategoryDiscountRule(percentDiscount,categoryId));
    }
}
