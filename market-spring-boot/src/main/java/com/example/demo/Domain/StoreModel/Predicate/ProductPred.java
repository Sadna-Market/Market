package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductPred implements Predicate{
    private final int productID;
    private final int minQuantity;
    private final int maxQuantity;
    private final boolean canBuy;

    public ProductPred(int productID,int minQuantity,int maxQuantity, boolean canBuy){
        this.productID = productID;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.canBuy = canBuy;
    }

    //for discount rules
    public ProductPred(int productID,int minQuantity,int maxQuantity){
        this.productID = productID;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.canBuy = true;
    }


    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            ProductStore product = e.getKey();
            if(product.getProductType().getProductID().getValue() == productID){
                if(!canBuy) return new DResponseObj<>(false, ErrorCode.PRODUCT_NOT_FOR_SELL_NOW);
                else if(e.getValue() < minQuantity)
                    return new DResponseObj<>(false, ErrorCode.NOT_PASS_THE_MIN_QUANTITY_TO_BUY);
                else if(e.getValue() > maxQuantity)
                    return new DResponseObj<>(false, ErrorCode.TRY_TO_BUY_MORE_THEN_MAX_QUANTITY_TO_BUY);
            }
            return new DResponseObj<>(true);
        }
        return new DResponseObj<>(true);
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

    public int getMinQuantity() {
        return minQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public boolean isCanBuy() {
        return canBuy;
    }
}
