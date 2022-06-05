package com.example.demo.Service.ServiceObj.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class ProductPredicateSL implements PredicateSL {

    public int productID;
    public int minQuantity;
    public int maxQuantity;
    public boolean canBuy;

    //for buy rule
    public ProductPredicateSL(int productID, int minQuantity, int maxQuantity, boolean canBuy){
        this.productID = productID;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.canBuy = canBuy;
    }

    //for discount rules
    public ProductPredicateSL(int productID, int minQuantity, int maxQuantity){
        this.productID = productID;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.canBuy = true;
    }



    @Override
    public String getPredicateBuyRule() {
        String s = "productID " + productID +" : the minimum quantity to buy is "+ minQuantity + " ,the maximum quantity is "+maxQuantity;
        if(!canBuy) s+= " and no one can buy this product";
        return s;
    }

    @Override
    public String getPredicateDiscountRule() {
        return "If you buy from product "+productID+" at least "+minQuantity+" and at most "+maxQuantity;
    }

    public int getProductID(){
        return productID;
    }

    @Override
    public SLResponseOBJ<Predicate> convertToPredicateDL() {
        if(productID < 0 || minQuantity<0 || maxQuantity < 0 || maxQuantity < minQuantity)
            return new SLResponseOBJ<>(null, ErrorCode.INVALID_ARGS_FOR_RULE);
        return new SLResponseOBJ<>(new ProductPred(productID,minQuantity,maxQuantity,canBuy));
    }
}
