package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import java.util.List;

public abstract class CompositionDiscountRuleSL implements DiscountRuleSL {
    public List<DiscountRuleSL> rules;
    public double discount;
    public int id;

    public CompositionDiscountRuleSL(List<DiscountRuleSL> rules, double dis){
        this.rules = rules;
        discount = dis;
    }

    //for convert
    public CompositionDiscountRuleSL(List<DiscountRuleSL> rules, double dis,int id){
        this.rules = rules;
        discount = dis;
        this.id = id;
    }

    public abstract SLResponseOBJ<String> getDiscountRule();

    public abstract SLResponseOBJ<DiscountRule> convertToDiscountRuleDL();

}
