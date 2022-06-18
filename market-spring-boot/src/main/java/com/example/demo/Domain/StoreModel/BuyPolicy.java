package com.example.demo.Domain.StoreModel;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Service.ServiceObj.ServiceBuyPolicy;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BuyPolicy {

    private ConcurrentHashMap<Integer,BuyRule> rules;
    private AtomicInteger idCounter = new AtomicInteger(1);


    static Logger logger=Logger.getLogger(BuyPolicy.class);

    public BuyPolicy(ServiceBuyPolicy buyPolicy) {
        this.rules = new ConcurrentHashMap<>();
    }

    public BuyPolicy() {
        this.rules = new ConcurrentHashMap<>();
    }


    public DResponseObj<Boolean> addNewBuyRule(BuyRule buyRule){
        int id = idCounter.getAndIncrement();
        rules.put(id,buyRule);
        buyRule.setID(id);
        logger.info("added new buyRule - id: "+id);
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> removeBuyRule(int buyRuleID){
        BuyRule removed = rules.remove(buyRuleID);
        if (removed == null)
            return new DResponseObj<>(false, ErrorCode.BUY_RULE_NOT_EXIST);
        else{
            logger.info("buy rule #"+buyRuleID+" was removed");
            return new DResponseObj<>(true);
        }
    }
    public DResponseObj<Boolean> checkBuyPolicyShoppingBag(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        for(BuyRule buyRule : rules.values()){
            DResponseObj<Boolean> passRule = buyRule.passRule(user,age,shoppingBag);
            if(!passRule.getValue()) return passRule;
        }
        return new DResponseObj<>(true);
    }

    public int rulesSize(){
        return rules.size();
    }

    public ConcurrentHashMap<Integer, BuyRule> getRules() {
        return rules;
    }


}
