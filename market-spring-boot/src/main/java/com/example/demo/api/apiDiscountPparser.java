package com.example.demo.api;

import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Service.ServiceObj.BuyRules.AndBuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.OrBuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.*;
import com.example.demo.Service.ServiceObj.Predicate.CategoryPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class apiDiscountPparser {
    private DiscountRuleSL parseConditionOnCategoryDiscount(Object condCateg) {
        Map<String, Object> conditionOnCategoryDiscount = (Map<String, Object>) condCateg;
        switch (conditionOnCategoryDiscount.size() - 2) {
            case 5: {
                CategoryPredicateSL c = new CategoryPredicateSL(Integer.valueOf((String) conditionOnCategoryDiscount.get("categoryID")),
                        Integer.valueOf((String) conditionOnCategoryDiscount.get("minimumAge")),
                        Integer.valueOf((String) conditionOnCategoryDiscount.get("minimumHour")),
                        Integer.valueOf((String) conditionOnCategoryDiscount.get("maximumHour")));
                return new ConditionCategoryDiscountRuleSL(c, Double.valueOf((String) conditionOnCategoryDiscount.get("discount")));
            }
            case 3: {
                CategoryPredicateSL c = new CategoryPredicateSL(Integer.valueOf((String) conditionOnCategoryDiscount.get("categoryID")),
                        Integer.valueOf((String) conditionOnCategoryDiscount.get("minimumAge")));
                return new ConditionCategoryDiscountRuleSL(c, Double.valueOf((String) conditionOnCategoryDiscount.get("discount")));
            }
            case 2: {
                CategoryPredicateSL c = new CategoryPredicateSL(Integer.valueOf((String) conditionOnCategoryDiscount.get("categoryID")));
                return new ConditionCategoryDiscountRuleSL(c, Double.valueOf((String) conditionOnCategoryDiscount.get("discount")));
            }
        }
        throw new IllegalArgumentException();
    }


    private DiscountRuleSL parseConditionOnProductDiscount(Object condCateg) {
        Map<String, Object> conditionOnProductDiscount = (Map<String, Object>) condCateg;
        switch (conditionOnProductDiscount.size() - 2) {
            case 5: {
                ProductPredicateSL c = new ProductPredicateSL(Integer.valueOf((String) conditionOnProductDiscount.get("productID")),
                        Integer.valueOf((String) conditionOnProductDiscount.get("minQuantity")),
                        Integer.valueOf((String) conditionOnProductDiscount.get("maxQuantity")),
                        Boolean.valueOf((String) conditionOnProductDiscount.get("canBuy")));
                return new ConditionProductDiscountRuleSL(c, Double.valueOf((String) conditionOnProductDiscount.get("discount")));
            }
            case 4: {
                ProductPredicateSL c = new ProductPredicateSL(Integer.valueOf((String) conditionOnProductDiscount.get("productID")),
                        Integer.valueOf((String) conditionOnProductDiscount.get("minQuantity")),
                        Integer.valueOf((String) conditionOnProductDiscount.get("maxQuantity")));
                return new ConditionProductDiscountRuleSL(c, Double.valueOf((String) conditionOnProductDiscount.get("discount")));
            }
        }
        throw new IllegalArgumentException();
    }

    private DiscountRuleSL parseConditionOnStoreDiscount(Object cond) {
        Map<String, Object> ConditionOnStoreDiscount = (Map<String, Object>) cond;
        switch (ConditionOnStoreDiscount.size() - 2) {
            case 3: {
                ShoppingBagPredicateSL a = new ShoppingBagPredicateSL(Integer.valueOf((String) ConditionOnStoreDiscount.get("discount")),
                        Integer.valueOf((String) ConditionOnStoreDiscount.get("minProductTypes")));
                return new ConditionStoreDiscountRuleSL(a, Integer.valueOf((String) ConditionOnStoreDiscount.get("discount")));
            }
            case 4: {
                ShoppingBagPredicateSL a = new ShoppingBagPredicateSL(Integer.valueOf((String) ConditionOnStoreDiscount.get("discount")),
                        Integer.valueOf((String) ConditionOnStoreDiscount.get("minProductTypes")),
                        Integer.valueOf((String) ConditionOnStoreDiscount.get("totalPrice")));
                return new ConditionStoreDiscountRuleSL(a, Integer.valueOf((String) ConditionOnStoreDiscount.get("discount")));
            }
        }
        throw new IllegalArgumentException();

    }



    private DiscountRuleSL parsecategoryDiscount(Object cond) {
        Map<String, Object> parsecategoryDiscount = (Map<String, Object>) cond;
        switch (parsecategoryDiscount.size() - 2) {
            case 2: {
                CategoryPredicateSL c = new CategoryPredicateSL(
                        Integer.valueOf((String) parsecategoryDiscount.get("categoryID"))
                );
                return new ConditionCategoryDiscountRuleSL(c, Integer.valueOf((String) parsecategoryDiscount.get("discount")));
            }
            case 3: {
                CategoryPredicateSL c = new CategoryPredicateSL(
                        Integer.valueOf((String) parsecategoryDiscount.get("categoryID")),
                        Integer.valueOf((String) parsecategoryDiscount.get("minAge"))
                );
                return new ConditionCategoryDiscountRuleSL(c, Integer.valueOf((String) parsecategoryDiscount.get("discount")));
            }
            case 4: {
                CategoryPredicateSL c = new CategoryPredicateSL(
                        Integer.valueOf((String) parsecategoryDiscount.get("categoryID")),
                        Integer.valueOf((String) parsecategoryDiscount.get("minAge")),
                        Integer.valueOf((String) parsecategoryDiscount.get("minHour")),
                        Integer.valueOf((String) parsecategoryDiscount.get("maxHour"))
                );
                return new ConditionCategoryDiscountRuleSL(c, Integer.valueOf((String) parsecategoryDiscount.get("discount")));

            }


        }
        throw new IllegalArgumentException();

    }


        private DiscountRuleSL parseProductDiscount  (Object cond)
        {        Map<String, Object> ProductDiscount = (Map<String, Object>) cond;

            switch (ProductDiscount.size() - 2) {
                case 2:
                    return new SimpleProductDiscountRuleSL(Double.valueOf((String) ProductDiscount.get("discount")),Integer.valueOf((String) ProductDiscount.get("productID")));

            }
            throw new IllegalArgumentException();
        }


    private DiscountRuleSL parseFullDiscount(Object cond) {
        Map<String, Object> FullDiscount = (Map<String, Object>) cond;
        switch (FullDiscount.size() - 2) {
            case 1:
                return new SimpleStoreDiscountRuleSL(Double.valueOf((String) FullDiscount.get("discount")));

        }
        throw new IllegalArgumentException();
    }

    public List<Integer> combineAnd(Map<String,Object> map){
        return (List<Integer>) map.get("combineAnd");
    }

    public List<Integer> combineOr(Map<String,Object> map){
        return (List<Integer>) map.get("combineOr");
    }

    public List<Integer> combineXor(Map<String,Object> map){
        return (List<Integer>) map.get("combineXor");
    }

    public DiscountRuleSL DiscountParse(Map<String,Object> map){
        if(map.containsKey("or"))
        {
            return DiscountOr((Map<String, Object>) map.get("or"));
        }
        if(map.containsKey("and"))
        {
            return DiscountAnd((Map<String, Object>) map.get("and"));
        }
        if(map.containsKey("xor")){
            return DiscountXor((Map<String, Object>) map.get("xor"));
        }
        if (map.containsKey("add")){
            return DiscountAdd((Map<String, Object>) map.get("add"));
        }

        return simpleDiscountParse(map);
    }

    private DiscountRuleSL DiscountAdd(Map<String,Object> or){
        List<Map<String,Object>> cl= (List<Map<String, Object>>) or.get("list");
        List<DiscountRuleSL> simpleDisRules = new LinkedList<>();
        for(Map<String,Object> m : cl) {
            DiscountRuleSL b=simpleDiscountParse(m);
            simpleDisRules.add(b);
        }
        return new AddDiscountRuleSL(simpleDisRules);
    }

    private DiscountRuleSL DiscountOr(Map<String,Object> or){
        List<Map<String,Object>> cl= (List<Map<String, Object>>) or.get("list");
        List<DiscountRuleSL> simpleDisRules = new LinkedList<>();
        for(Map<String,Object> m : cl) {
            DiscountRuleSL b=simpleDiscountParse(m);
            simpleDisRules.add(b);
        }
        return new OrDiscountRuleSL(simpleDisRules,Integer.valueOf((String) or.get("categoryID")),Double.valueOf((String) or.get("discount")));
    }

    private DiscountRuleSL DiscountAnd(Map<String,Object> and){
        List<Map<String,Object>> cl= (List<Map<String, Object>>) and.get("list");
        List<DiscountRuleSL> simpleDisRules = new LinkedList<>();
        for(Map<String,Object> m : cl) {

            DiscountRuleSL b=simpleDiscountParse(m);
            simpleDisRules.add(b);
        }
        return new AndDiscountRuleSL(simpleDisRules,Integer.valueOf((String) and.get("categoryID")),Double.valueOf((String) and.get("discount")));
    }

    private DiscountRuleSL DiscountXor(Map<String,Object> Xor){
        List<Map<String,Object>> cl= (List<Map<String, Object>>) Xor.get("list");
        List<DiscountRuleSL> simpleDisRules = new LinkedList<>();
        for(Map<String,Object> m : cl) {
            DiscountRuleSL b=simpleDiscountParse(m);
            simpleDisRules.add(b);
        }
        System.out.println((String) Xor.get("decision"));
        return new XorDiscountRuleSL(simpleDisRules,(String) Xor.get("decision"));
    }


    private DiscountRuleSL simpleDiscountParse(Map<String,Object> m){
        if(m.containsKey("conditionOnCategoryDiscount")){
            return parseConditionOnCategoryDiscount(m.get("conditionOnCategoryDiscount"));

        }
        if(m.containsKey("conditionOnProductDiscount")){
            return parseConditionOnProductDiscount(m.get("conditionOnProductDiscount"));
        }

        if(m.containsKey("conditionOnStoreDiscount")){
            return parseConditionOnStoreDiscount(m.get("conditionOnStoreDiscount"));
        }

        if(m.containsKey("categoryDiscount")){
            return parsecategoryDiscount(m.get("categoryDiscount"));
        }

        if(m.containsKey("ProductDiscount")){
            return parseProductDiscount(m.get("ProductDiscount"));
        }

        if(m.containsKey("FullDiscount")){
            return parseFullDiscount(m.get("FullDiscount"));

        }
        throw new IllegalArgumentException();
    }

}
