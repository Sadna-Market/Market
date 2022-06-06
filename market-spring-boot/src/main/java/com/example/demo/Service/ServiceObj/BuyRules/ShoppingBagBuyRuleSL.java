package com.example.demo.Service.ServiceObj.BuyRules;

import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ShoppingBagBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.UserBuyRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ShoppingBagBuyRuleSL extends LeafBuyRuleSL {

    public ShoppingBagBuyRuleSL(ShoppingBagPredicateSL pred) {
        super(pred);
    }

    @Override
    public SLResponseOBJ<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "ShoppingBag Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<BuyRule> convertToBuyRuleDL() {
        SLResponseOBJ<Predicate> predicate = pred.convertToPredicateDL();
        if(predicate.errorOccurred()) return new SLResponseOBJ<>(predicate.getErrorMsg());
        return new SLResponseOBJ<>(new ShoppingBagBuyRule((ShoppingBagPred) predicate.value));
    }
}
