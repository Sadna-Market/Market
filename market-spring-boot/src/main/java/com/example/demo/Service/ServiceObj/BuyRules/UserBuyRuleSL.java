package com.example.demo.Service.ServiceObj.BuyRules;

import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.UserBuyRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Service.ServiceObj.Predicate.UserPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class UserBuyRuleSL extends LeafBuyRuleSL {

    public String kind = "User Buy Rule";

    public UserBuyRuleSL(UserPredicateSL pred) {
        super(pred,-1);
    }
    //for convert
    public UserBuyRuleSL(UserPredicateSL pred,int id) {
        super(pred,id);
    }


    @Override
    public SLResponseOBJ<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "User Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<BuyRule> convertToBuyRuleDL() {
        SLResponseOBJ<Predicate> predicate = pred.convertToPredicateDL();
        if(predicate.errorOccurred()) return new SLResponseOBJ<>(predicate.getErrorMsg());
        return new SLResponseOBJ<>(new UserBuyRule((UserPred) predicate.value));
    }

}
