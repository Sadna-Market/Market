package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.Market;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property ="BuyRule")
@JsonSubTypes({
        @JsonSubTypes.Type(value=LeafBuyRule.class, name="LeafBuyRule"),
        @JsonSubTypes.Type(value=CompositionBuyRule.class, name="CompositionBuyRule")
})
public interface BuyRule {
    static Logger logger = Logger.getLogger(BuyRule.class);


    DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag);
    /*DResponseObj<String> getBuyRule();*/
    void setID(int id);
    int getId();
    DResponseObj<BuyRuleSL> convertToBuyRuleSL();

    @JsonIgnore
    default DataBuyRule getDataObject() {
        try{
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String ruleJson = ow.writeValueAsString(this);
            DataBuyRule dbr = new DataBuyRule();
            dbr.setRule(ruleJson);
            return dbr;
        }catch (Exception e) {
            logger.error("can't get data object - buy rule");
            return null;
        }
    }
}
