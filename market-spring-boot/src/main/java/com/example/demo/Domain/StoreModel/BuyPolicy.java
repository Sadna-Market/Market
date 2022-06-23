package com.example.demo.Domain.StoreModel;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.UserModel.ShoppingBag;
import com.example.demo.Service.ServiceObj.ServiceBuyPolicy;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BuyPolicy {

    private ConcurrentHashMap<Integer,BuyRule> rules;
    private AtomicInteger idCounter = new AtomicInteger(1);


    private static DataServices dataServices;

    static Logger logger=Logger.getLogger(BuyPolicy.class);

    public BuyPolicy(ServiceBuyPolicy buyPolicy) {
        this.rules = new ConcurrentHashMap<>();
    }

    public BuyPolicy() {
        this.rules = new ConcurrentHashMap<>();
    }


    public DResponseObj<Boolean> addNewBuyRule(BuyRule buyRule,int storeID) {
        //TODO remove id counter and setID without db
        int id = idCounter.getAndIncrement();
        rules.put(id,buyRule);
        buyRule.setID(id);
        logger.info("added new buyRule - id: "+id);
        //db
        if (dataServices != null && dataServices.getRuleService() != null) { //because no autowire in AT
            DataBuyRule dataBuyRule = buyRule.getDataObject();
            if(dataBuyRule == null) return new DResponseObj<>(false,ErrorCode.DB_ERROR);
            int buyRuleID = dataServices.getRuleService().insertBuyRule(dataBuyRule, storeID);
            if (buyRuleID == -1) {
                logger.error(String.format("didnt save buyRule:\n %s", dataBuyRule.getRule()));
                return new DResponseObj<>(false, ErrorCode.DB_ERROR);
            }
            buyRule.setID(buyRuleID);
        }
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> removeBuyRule(int buyRuleID){
        BuyRule removed = rules.remove(buyRuleID);
        if (removed == null)
            return new DResponseObj<>(false, ErrorCode.BUY_RULE_NOT_EXIST);
        else{
            logger.info("buy rule #"+buyRuleID+" was removed");
            //db
            if (dataServices != null && dataServices.getRuleService() != null) {
                if (!dataServices.getRuleService().deleteBuyRule(buyRuleID)) {
                    logger.error(String.format("failed to remove buyRule %d", buyRuleID));
                    return new DResponseObj<>(false, ErrorCode.DB_ERROR);
                }
            }
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


    public DataBuyRule getDataObject() {
        return new DataBuyRule();
    }
    public static void setDataServices(DataServices dataServices) {
        BuyPolicy.dataServices = dataServices;
    }

}
