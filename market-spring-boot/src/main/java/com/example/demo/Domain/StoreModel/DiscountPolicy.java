package com.example.demo.Domain.StoreModel;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.DataAccess.Entity.DataDiscountRule;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.AndDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.OrDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.XorDiscountRule;
import com.example.demo.Service.ServiceObj.ServiceDiscountPolicy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscountPolicy {

    private static DataServices dataServices;

    public static void setDataServices(DataServices dataService) { DiscountPolicy.dataServices = dataService;}


    private ConcurrentHashMap<Integer, DiscountRule> rules;
    private AtomicInteger idCounter = new AtomicInteger(1);

    static Logger logger=Logger.getLogger(DiscountPolicy.class);


    public DiscountPolicy(ServiceDiscountPolicy discountPolicy) {
        this.rules = new ConcurrentHashMap<>();
    }

    //for upload store from db
    public DiscountPolicy(ConcurrentHashMap<Integer, DiscountRule> discountRules){
        this.rules = discountRules;
        int maxID = 1;
        for(Integer id : discountRules.keySet())
            if(id>maxID) maxID = id;
        idCounter = new AtomicInteger(maxID+1);
    }

    public DiscountPolicy() {
        this.rules = new ConcurrentHashMap<>();
    }


    public DResponseObj<Boolean> addNewDiscountRule(DiscountRule discountRule, int storeID){
        if(discountRule.getPercentDiscount() < 0 || discountRule.getPercentDiscount() > 100) return new DResponseObj<>(false,ErrorCode.INVALID_PERECNT_DISCOUNT);
        //TODO remove id counter and setID without db
        int id = idCounter.getAndIncrement();
        rules.put(id,discountRule);
        discountRule.setID(id);
        logger.info("added new discountRule - id: "+id);
        //db
        if (dataServices != null && dataServices.getRuleService() != null) { //because no autowire in AT
            DataDiscountRule dataDiscountRule = discountRule.getDataObject();
            if(dataDiscountRule == null) return new DResponseObj<>(false,ErrorCode.DB_ERROR);
            int discountRuleID = dataServices.getRuleService().insertDiscountRule(dataDiscountRule, storeID);
            if (discountRuleID == -1) {
                logger.error(String.format("didnt save discountRule:\n %s", dataDiscountRule.getRule()));
                return new DResponseObj<>(false, ErrorCode.DB_ERROR);
            }
            discountRule.setID(discountRuleID);
        }
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> removeDiscountRule(int discountRuleID){
        DiscountRule removed = rules.remove(discountRuleID);
        if (removed == null)
            return new DResponseObj<>(false, ErrorCode.DISCOUNT_RULE_NOT_EXIST);
        else{
            logger.info("discount rule #" + discountRuleID +" was removed");
            if (dataServices != null && dataServices.getRuleService() != null) {
                if (!dataServices.getRuleService().deleteDiscountRule(discountRuleID)) {
                    logger.error(String.format("failed to remove discountRule %d", discountRuleID));
                    return new DResponseObj<>(false, ErrorCode.DB_ERROR);
                }
            }
            return new DResponseObj<>(true);
        }
    }
    public DResponseObj<Double> checkDiscountPolicyShoppingBag(String username, int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        Double totalDiscount = 0.0;
        for(DiscountRule discountRule : rules.values()){
            DResponseObj<Double> discount = discountRule.howMuchDiscount(username,age,shoppingBag);
            if(discount.errorOccurred()) return discount;
            totalDiscount += discount.getValue();
        }
        return new DResponseObj<>(totalDiscount);
    }

    public int rulesSize(){
        return rules.size();
    }

    public ConcurrentHashMap<Integer,DiscountRule> getRules(){
        return rules;
    }

    public DResponseObj<Boolean> combineANDORDiscountRules(String operator, List<Integer> toCombineRules, int category, int discount,int storeID) {
        List<DiscountRule> rulesForCombine = new ArrayList<>();
        for(Integer id : toCombineRules) {
            DiscountRule rule = rules.get(id);
            if (rule == null) return new DResponseObj<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
            rulesForCombine.add(rule);
        }
        DiscountRule combine;
        switch(operator) {
            case "and":
                combine = new AndDiscountRule(rulesForCombine, category,discount);
                break;
            case "or":
                combine = new OrDiscountRule(rulesForCombine, category, discount);
                break;
            default:
                return new DResponseObj<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
        }
        DResponseObj<Boolean> addCombine = addNewDiscountRule(combine,storeID);  // add the combine rule
        if(addCombine.errorOccurred()) return addCombine;
        toCombineRules.forEach(this::removeDiscountRule);  // remove all the rules that combine
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> combineXORDiscountRules(List<Integer> toCombineRules, String decision,int storeID) {
        List<DiscountRule> rulesForCombine = new ArrayList<>();
        for(Integer id : toCombineRules) {
            DiscountRule rule = rules.get(id);
            if (rule == null) return new DResponseObj<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
            rulesForCombine.add(rule);
        }
        DiscountRule xor = new XorDiscountRule(rulesForCombine,decision);
        DResponseObj<Boolean> addCombine = addNewDiscountRule(xor,storeID);  // add the combine rule
        if(addCombine.errorOccurred()) return addCombine;
        toCombineRules.forEach(this::removeDiscountRule);  // remove all the rules that combine
        return new DResponseObj<>(true);
    }

    public DataDiscountRule getDataObject() {
        return new DataDiscountRule();
    }

    public DResponseObj<DiscountRule> getDiscountRuleByID(int discountRuleID) {
        DiscountRule discountRule = rules.get(discountRuleID);
        return discountRule == null ? new DResponseObj<>(null,ErrorCode.DISCOUNT_RULE_NOT_EXIST) : new DResponseObj<>(discountRule);
    }
}
