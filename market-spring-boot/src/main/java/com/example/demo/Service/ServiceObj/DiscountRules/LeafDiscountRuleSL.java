package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public abstract class LeafDiscountRuleSL implements DiscountRuleSL {
    public double percentDiscount;
    public int id;

    public LeafDiscountRuleSL(double percentDiscount){
        this.percentDiscount = percentDiscount;
    }

    //for convert
    public LeafDiscountRuleSL(double percentDiscount,int id){
        this.percentDiscount = percentDiscount;
        this.id = id;
    }

    public abstract SLResponseOBJ<String> getDiscountRule();

    public abstract SLResponseOBJ<DiscountRule> convertToDiscountRuleDL();
}
