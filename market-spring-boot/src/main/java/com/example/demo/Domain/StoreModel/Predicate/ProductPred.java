package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductPred implements Predicate{
    int productID;
    int minQuantity;
    int maxQuantity;
    boolean canBuy;

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
}
