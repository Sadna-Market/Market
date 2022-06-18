package com.example.demo.Service.ServiceObj.DiscountRules;


import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleProductDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleStoreDiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class SimpleProductDiscountRuleSL extends LeafDiscountRuleSL {

    public String kind = "Simple Proudct Discount Rule";

    public int productId;

    public SimpleProductDiscountRuleSL(double percentDiscount, int productId){
        super(percentDiscount);
        this.productId = productId;
    }

    //for convert
    public SimpleProductDiscountRuleSL(double percentDiscount, int productId,int id){
        super(percentDiscount,id);
        this.productId = productId;
    }

    @Override
    public SLResponseOBJ<String> getDiscountRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Simple Product Discount Rule #"+id + ":\n\t";
        stringRule += "productID "+productId +" have a "+percentDiscount+"% discount";
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<DiscountRule> convertToDiscountRuleDL() {
        return new SLResponseOBJ<>(new SimpleProductDiscountRule(percentDiscount,productId));
    }
}
