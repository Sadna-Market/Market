package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleCategoryDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleStoreDiscountRuleSL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCategoryDiscountRule extends LeafDiscountRule{

    protected int categoryId;

    public SimpleCategoryDiscountRule(double percentDiscount, int categoryId){
        super(percentDiscount);
        this.categoryId = categoryId;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        double dis = 0.0;
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            if(e.getKey().getProductType().getCategory().value == categoryId)
                dis += (e.getKey().getPrice().value * e.getValue() * (percentDiscount/100));
        }
        return new DResponseObj<>(dis);
    }

    @Override
    public DResponseObj<String> getDiscountRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Simple Category Discount Rule #"+id + ":\n\t";
        stringRule += "All products in the category "+categoryId+ " have a "+percentDiscount+"% discount";
        return new DResponseObj<>(stringRule);
    }

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        return new DResponseObj<>(new SimpleCategoryDiscountRuleSL(percentDiscount,categoryId,id));
    }
}
