package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public interface DiscountRuleSL {
    SLResponseOBJ<String> getDiscountRule();
    SLResponseOBJ<DiscountRule> convertToDiscountRuleDL();
}
