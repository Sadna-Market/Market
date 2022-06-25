package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.DataAccess.Entity.DataDiscountRule;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.CompositionBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.LeafBuyRule;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property ="DiscountRule")
@JsonSubTypes({
        @JsonSubTypes.Type(value= LeafDiscountRule.class, name="LeafDiscountRule"),
        @JsonSubTypes.Type(value= CompositionDiscountRule.class, name="CompositionDiscountRule")
})
public interface DiscountRule {

    static Logger logger = Logger.getLogger(DiscountRule.class);

    DResponseObj<Double> howMuchDiscount(String username, int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag);
    void setID(int id);
    int getId();
    DResponseObj<DiscountRuleSL> convertToDiscountRuleSL();
    double getPercentDiscount();

    @JsonIgnore
    default DataDiscountRule getDataObject() {
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String ruleJson = ow.writeValueAsString(this);
            DataDiscountRule ddr = new DataDiscountRule();
            ddr.setRule(ruleJson);
            return ddr;
        }catch (Exception e) {
            logger.error("can't get data object - discount rule:\n " + e.getMessage());
            return null;
        }
    }

    //DResponseObj<String> getDiscountRule();

}
