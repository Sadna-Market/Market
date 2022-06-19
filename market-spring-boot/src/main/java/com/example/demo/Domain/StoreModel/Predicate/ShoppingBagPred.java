package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingBagPred implements Predicate{
    private final int minProductQuantity;
    private final int minProductTypes;
    private final double totalPrice;

    //for buy rule
    public ShoppingBagPred(int minProductQuantity, int minProductTypes){
        this.minProductQuantity = minProductQuantity;
        this.minProductTypes = minProductTypes;
        totalPrice = -1; //sign not to check it
    }

    //for discount rule
    public ShoppingBagPred(int minProductQuantity, int minProductTypes, double totalPrice){
        this.minProductQuantity = minProductQuantity;
        this.minProductTypes = minProductTypes;
        this.totalPrice = totalPrice;
    }
    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        int productQuantity = 0;
        int numOfTypes = 0;
        if(totalPrice == -1) {
            for (Map.Entry<ProductStore, Integer> e : shoppingBag.entrySet()) {
                productQuantity += e.getValue();
                numOfTypes++;
            }
        }else{
            double price = 0.0;
            for (Map.Entry<ProductStore, Integer> e : shoppingBag.entrySet()) {
                price += e.getKey().getPrice().value * e.getValue();
                productQuantity += e.getValue();
                numOfTypes++;
            }
            if(price < totalPrice) return new DResponseObj<>(false,ErrorCode.NOT_MOVED_PRICE_FOR_DISCOUNT);
        }
        return productQuantity >= minProductQuantity && numOfTypes >= minProductTypes ?
                new DResponseObj<>(true) : new DResponseObj<>(false, ErrorCode.NOT_PASS_THE_MIN_QUANTITY_TO_BUY);
    }

    @Override
    public String getPredicateBuyRule() {
        return "The minimum quantity of products to buy is "+minProductQuantity + " and the minimum products types is "+minProductTypes;
    }

    @Override
    public String getPredicateDiscountRule(){
        return "If the shopping bag contains at least "+minProductTypes+" types of products and at least "+minProductQuantity +" quantity of products and the total price is more than "+totalPrice;
    }


    public int getMinProductQuantity() {
        return minProductQuantity;
    }

    public int getMinProductTypes() {
        return minProductTypes;
    }

    public double getTotalPrice() {
        return totalPrice;
    }


}
