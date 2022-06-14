package com.example.Unit.ExternalServices;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.ExternalService.PaymentService;
import com.example.demo.ExternalService.SupplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SupplyServiceTest {

    SupplyService test= SupplyService.getInstance();


    @DisplayName("Supply Name  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"Yosofon g","Aldad d","Miri Masika"})
    public void supplyAPI(String s){
        DResponseObj<Integer> output= test.supplyByAPI(s,"Qiryat Gat","Hatmarim", 4);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
        assertTrue(output.getValue()<=100000);
        assertTrue(output.getValue()>=10000);
    }

    @DisplayName("Supply Name  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {""})
    public void supplyAPI2(String s){
        DResponseObj<Integer> output= test.supplyByAPI(s,"Qiryat Gat","Hatmarim", 4);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

    @DisplayName("Supply City  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"Qiryat Gat","Tel Aviv","Holon"})
    public void supplyAPI3(String s){
        DResponseObj<Integer> output= test.supplyByAPI("YAYA YAYA",s,"Hatmarim", 4);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
        assertTrue(output.getValue()<=100000);
        assertTrue(output.getValue()>=10000);
    }

    @DisplayName("Supply City  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {""})
    public void supplyAPI4(String s){
        DResponseObj<Integer> output= test.supplyByAPI("YAYA YAYA",s,"Hatmarim", 4);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

    @DisplayName("Supply Street  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"Rager","Rabin","Hatena"})
    public void supplyAPI5(String s){
        DResponseObj<Integer> output= test.supplyByAPI("YAYA YAYA","Tel Aviv",s, 4);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
        assertTrue(output.getValue()<=100000);
        assertTrue(output.getValue()>=10000);
    }

    @DisplayName("Supply Street  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {""})
    public void supplyAPI6(String s){
        DResponseObj<Integer> output= test.supplyByAPI("YAYA YAYA","Holon",s, 4);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

    @DisplayName("Supply Number  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {145,3,8,56})
    public void supplyAPI7(int s){
        DResponseObj<Integer> output= test.supplyByAPI("YAYA YAYA","Tel Aviv","Rager", s);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
        assertTrue(output.getValue()<=100000);
        assertTrue(output.getValue()>=10000);
    }

    @DisplayName("Supply Number  -  fail")
    @ParameterizedTest
    @ValueSource(ints = {-3,0,-45})
    public void supplyAPI8(int s){
        DResponseObj<Integer> output= test.supplyByAPI("YAYA YAYA","Holon","Rager", s);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

    @DisplayName(" cancel Supply  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"25915","20543","15527"})
    public void cancelPayAPI(String s){
        DResponseObj<Boolean> output= test.cancelSupply(s);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
    }

    @DisplayName("PayAPI Supply  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {"-22332d","a3s3","1574",""})
    public void cancelPayAPI2(String s){
        DResponseObj<Boolean> output= test.cancelSupply(s);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }
}