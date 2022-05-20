package com.example.demo.api.apiObjects;

import com.example.demo.Service.ServiceObj.ServiceBuyStrategy;
import com.example.demo.Service.ServiceObj.ServiceDiscountPolicy;

public class apiStore {

    public String name;
   public String founder;
  public   apiDiscountPolicy discountPolicy;

    public apiStore(String name, String founder, apiDiscountPolicy discountPolicy, apiBuyPolicy buyPolicy, apiBuyStrategy buyStrategy) {
        this.name = name;
        this.founder = founder;
        this.discountPolicy = discountPolicy;
        this.buyPolicy = buyPolicy;
        this.buyStrategy = buyStrategy;
    }

   public apiBuyPolicy buyPolicy;
 public    apiBuyStrategy buyStrategy;
}
