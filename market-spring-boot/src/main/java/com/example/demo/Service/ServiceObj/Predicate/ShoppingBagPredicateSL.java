package com.example.demo.Service.ServiceObj.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ShoppingBagPredicateSL implements PredicateSL {
    public int minProductQuantity;
    public int minProductTypes;
    public double totalPrice;

    //for buy rule
    public ShoppingBagPredicateSL(int minProductQuantity, int minProductTypes){
        this.minProductQuantity = minProductQuantity;
        this.minProductTypes = minProductTypes;
        totalPrice = -1; //sign not to check it
    }

    //for discount rule
    public ShoppingBagPredicateSL(int minProductQuantity, int minProductTypes, double totalPrice){
        this.minProductQuantity = minProductQuantity;
        this.minProductTypes = minProductTypes;
        this.totalPrice = totalPrice;
    }

    public ShoppingBagPredicateSL(ShoppingBagPred pred){
        this.minProductQuantity = pred.getMinProductQuantity();
        this.minProductTypes = pred.getMinProductTypes();
        this.totalPrice = pred.getTotalPrice();
    }

    @Override
    public String getPredicateBuyRule() {
        return "The minimum quantity of products to buy is "+minProductQuantity + " and the minimum products types is "+minProductTypes;
    }

    @Override
    public String getPredicateDiscountRule(){
        return "If the shopping bag contains at least "+minProductTypes+" types of products and at least "+minProductQuantity +" quantity of products and the total price is more than "+totalPrice;
    }

    @Override
    public SLResponseOBJ<Predicate> convertToPredicateDL() {
        if(minProductQuantity<0 || minProductTypes<0 || totalPrice < -1)
            return new SLResponseOBJ<>(null, ErrorCode.INVALID_ARGS_FOR_RULE);
        return new SLResponseOBJ<>(new ShoppingBagPred(minProductQuantity,minProductTypes,totalPrice));
    }

}
