package com.example.demo.Service.ServiceObj.BuyRules;


import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ProductBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.UserBuyRule;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ProductBuyRuleSL extends LeafBuyRuleSL {

    public String kind = "Product Buy Rule";

    public ProductBuyRuleSL(ProductPredicateSL pred) {
        super(pred,-1);
    }

    //for convert
    public ProductBuyRuleSL(ProductPredicateSL pred,int id) {
        super(pred,id);
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
        return new SLResponseOBJ<>(new ProductBuyRule((ProductPred) predicate.value));
    }
}
