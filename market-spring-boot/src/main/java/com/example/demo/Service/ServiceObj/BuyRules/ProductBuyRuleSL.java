package com.example.demo.Service.ServiceObj.BuyRules;


import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ProductBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.UserBuyRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ProductBuyRuleSL extends LeafBuyRuleSL {

    public ProductBuyRuleSL(ProductPredicateSL pred) {
        super(pred);
    }

    @Override
    public SLResponseOBJ<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Product Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<BuyRule> convertToBuyRuleDL() {
        SLResponseOBJ<Predicate> predicate = pred.convertToPredicateDL();
        if(predicate.errorOccurred()) return new SLResponseOBJ<>(predicate.getErrorMsg());
        return new SLResponseOBJ<>(new ProductBuyRule(predicate.value));
    }
}
