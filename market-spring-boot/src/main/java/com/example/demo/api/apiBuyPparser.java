package com.example.demo.api;

import com.example.demo.Service.ServiceObj.BuyRules.*;
import com.example.demo.Service.ServiceObj.Predicate.CategoryPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.UserPredicateSL;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class apiBuyPparser {

    public  BuyRuleSL BuyRuleParse(Map<String,Object> buyRuleMap){
        if(buyRuleMap.containsKey("combine"))
        {
           return BuyRuleParseCombine(buyRuleMap.get("combine"));
        }
        String BuyRuleArr[]= {"and","or","condition"};
        if(buyRuleMap.containsKey("and")){

            return BuyRuleParseAnd(buyRuleMap.get("and"));

        }
        if(buyRuleMap.containsKey("or"))
        {
            return BuyRuleParseOr(buyRuleMap.get("or"));
        }
       if(buyRuleMap.containsKey("condition"))
       {
            return BuyRuleParseCondition(buyRuleMap.get("condition"));
       }
       return BuyRuleParseSimple(buyRuleMap);
    }
    private BuyRuleSL BuyRuleParseCondition(Object or) {
        List<Map<String,Object>> cl= (List<Map<String, Object>>) or;
        Map<String,Object> mif = cl.get(0);
        Map<String,Object> mthen = cl.get(1);
        BuyRuleSL Bif = BuyRuleParse(mif);
        BuyRuleSL Bthen =BuyRuleParse(mthen);
        ConditioningBuyRuleSL c = new ConditioningBuyRuleSL(Bif,Bthen);
        return c;

    }
    private BuyRuleSL BuyRuleParseOr(Object or){
        List<Map<String,Object>> cl= (List<Map<String, Object>>) or;
        List<BuyRuleSL> simpleBuyRules = new LinkedList<>();
        for(Map<String,Object> m : cl) {
            BuyRuleSL b=BuyRuleParseSimple(m);
            simpleBuyRules.add(b);
        }
        return new OrBuyRuleSL(simpleBuyRules);
    }

    private BuyRuleSL BuyRuleParseAnd(Object and){
        List<Map<String,Object>> cl= (List<Map<String, Object>>) and;
        List<BuyRuleSL> simpleBuyRules = new LinkedList<>();
        for(Map<String,Object> m : cl) {
            BuyRuleSL b=BuyRuleParseSimple(m);
            simpleBuyRules.add(b);
        }
        return new AndBuyRuleSL(simpleBuyRules);
    }

    private BuyRuleSL BuyRuleParseCombine(Object combine){
        List<Integer> cl= (List<Integer>) combine;
        //todo not have yet implemented b dor
        return null;
    }

    private BuyRuleSL BuyRuleParseCategory(Object category){
        Map<String,Object> cat = (Map<String, Object>) category;
        switch (cat.keySet().size()-2) {
            case 4:
                return new CategoryBuyRuleSL(new CategoryPredicateSL(Integer.valueOf((String) cat.get("categoryID")), Integer.valueOf((String) cat.get("minAge")), Integer.valueOf((String) cat.get("minHour")), Integer.valueOf((String) cat.get("maxHour"))));
            case 2:
                return new CategoryBuyRuleSL(new CategoryPredicateSL(Integer.valueOf((String) cat.get("categoryID")), Integer.valueOf((String) cat.get("minAge"))));
            case 1:
                return new CategoryBuyRuleSL(new CategoryPredicateSL(Integer.valueOf((String) cat.get("categoryID"))));
        }
        throw new IllegalArgumentException();
    }

    private BuyRuleSL BuyRuleParseProduct(Object PRODUCT) {
        Map<String,Object> cat = (Map<String, Object>) PRODUCT;
        switch (cat.keySet().size()-2) {
            case 4:
                return new ProductBuyRuleSL(new ProductPredicateSL(Integer.valueOf((String) cat.get("productID")), Integer.valueOf((String) cat.get("minQuantity")), Integer.valueOf((String) cat.get("maxQuantity")), Boolean.valueOf((String) cat.get("canBuy"))));//todo
            case 3:
                return new ProductBuyRuleSL(new ProductPredicateSL(Integer.valueOf((String) cat.get("productID")), Integer.valueOf((String) cat.get("minQuantity")), Integer.valueOf((String) cat.get("maxQuantity"))));
        }
        throw new IllegalArgumentException();
    }

    private BuyRuleSL BuyRuleParseShoppingCart(Object ShoppingCart) {
        Map<String,Object> cat = (Map<String, Object>) ShoppingCart;
        switch (cat.keySet().size()-2) {
            case 2:
                return new ShoppingBagBuyRuleSL(new ShoppingBagPredicateSL(Integer.valueOf((String) cat.get("quantity")), Integer.valueOf((String) cat.get("productTypes"))));

            case 3:
                return new ShoppingBagBuyRuleSL(new ShoppingBagPredicateSL(Integer.valueOf((String) cat.get("quantity")), Integer.valueOf((String) cat.get("productTypes")),Integer.valueOf((String) cat.get("totalPrice"))));
        }
        throw new IllegalArgumentException();
    }
    private BuyRuleSL BuyRuleParseUser(Object user){
        Map<String,Object> cat = (Map<String, Object>) user;
        if(cat.keySet().size()==3){
            return new UserBuyRuleSL(new UserPredicateSL((String) cat.get("userEmail")));
        }
        throw new IllegalArgumentException();
    }
        private BuyRuleSL BuyRuleParseSimple(Map<String,Object> buyRuleMap){
        if(buyRuleMap.containsKey("CategoryRule")){
            return BuyRuleParseCategory(buyRuleMap.get("CategoryRule"));
        }
            if(buyRuleMap.containsKey("productRule")){
                return BuyRuleParseProduct(buyRuleMap.get("productRule"));
            }
            if(buyRuleMap.containsKey("ShoppingRule")){
                return BuyRuleParseShoppingCart(buyRuleMap.get("ShoppingRule"));
            } if(buyRuleMap.containsKey("UserRule")){
                return BuyRuleParseUser(buyRuleMap.get("UserRule"));
            }

            throw new IllegalArgumentException();

    }

}
