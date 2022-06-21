package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleStoreDiscountRuleSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property ="SimpleStoreDiscountRule")
@JsonSubTypes({
        @JsonSubTypes.Type(value= ConditionStoreDiscountRule.class, name="ConditionStoreDiscountRule")
})
public class SimpleStoreDiscountRule extends LeafDiscountRule{

    @JsonCreator
    public SimpleStoreDiscountRule(@JsonProperty("percentDiscount") double percentDiscount){
        super(percentDiscount);
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        double dis = 0.0;
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            dis += (e.getKey().getPrice().value* e.getValue() * (percentDiscount/100));
        }
        return new DResponseObj<>(dis);
    }


/*    @Override
    public DResponseObj<String> getDiscountRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Simple Store Discount Rule #"+id + ":\n\t";
        stringRule += "All the products in store have a "+percentDiscount+"% discount";
        return new DResponseObj<>(stringRule);
    }*/

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        return new DResponseObj<>(new SimpleStoreDiscountRuleSL(percentDiscount,id));
    }


}
