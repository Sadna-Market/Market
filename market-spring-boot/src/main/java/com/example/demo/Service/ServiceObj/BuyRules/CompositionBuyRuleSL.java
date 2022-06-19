package com.example.demo.Service.ServiceObj.BuyRules;

import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import java.util.List;

public abstract class CompositionBuyRuleSL implements BuyRuleSL {


    public List<BuyRuleSL> rules;
    public int id;

    public CompositionBuyRuleSL(List<BuyRuleSL> rules){
        this.rules = rules;
    }

    @Override
    public abstract SLResponseOBJ<String> getBuyRule();

    public abstract SLResponseOBJ<BuyRule> convertToBuyRuleDL();

}
