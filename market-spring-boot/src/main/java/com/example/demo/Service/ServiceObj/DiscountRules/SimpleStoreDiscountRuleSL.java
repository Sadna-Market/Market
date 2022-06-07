package com.example.demo.Service.ServiceObj.DiscountRules;


import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleStoreDiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class SimpleStoreDiscountRuleSL extends LeafDiscountRuleSL {

    public SimpleStoreDiscountRuleSL(double percentDiscount){
        super(percentDiscount);
    }

     @Override
    public SLResponseOBJ<String> getDiscountRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Simple Store Discount Rule #"+id + ":\n\t";
        stringRule += "All the products in store have a "+percentDiscount+"% discount";
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<DiscountRule> convertToDiscountRuleDL() {
        return new SLResponseOBJ<>(new SimpleStoreDiscountRule(percentDiscount));
    }


}
