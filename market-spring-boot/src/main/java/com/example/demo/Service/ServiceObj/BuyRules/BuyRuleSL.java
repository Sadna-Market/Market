package com.example.demo.Service.ServiceObj.BuyRules;

import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;


public interface BuyRuleSL {
    SLResponseOBJ<String> getBuyRule();
    SLResponseOBJ<BuyRule> convertToBuyRuleDL();
}
