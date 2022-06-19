package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleProductDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleStoreDiscountRuleSL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleProductDiscountRule extends LeafDiscountRule{

    protected int productId;

    public SimpleProductDiscountRule(double percentDiscount, int productId){
        super(percentDiscount);
        this.productId = productId;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            if(e.getKey().getProductType().getProductID().value == productId){
                double dis = e.getKey().getPrice().value * e.getValue() * (percentDiscount/100);
                return new DResponseObj<> (dis);
            }
        }
        return new DResponseObj<>(0.0);
    }


    @Override
    public DResponseObj<String> getDiscountRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Simple Product Discount Rule #"+id + ":\n\t";
        stringRule += "productID "+productId +" have a "+percentDiscount+"% discount";
        return new DResponseObj<>(stringRule);
    }

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        return new DResponseObj<>(new SimpleProductDiscountRuleSL(percentDiscount,productId));
    }

}
