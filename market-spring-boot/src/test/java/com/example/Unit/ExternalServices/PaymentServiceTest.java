package com.example.Unit.ExternalServices;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.ExternalService.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {
    PaymentService test= PaymentService.getInstance();


    @DisplayName("PayAPI Card  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"4580575714149865","4580575714143465","4580575711755865"})
    public void PayAPI(String s){
        DResponseObj<Integer> output= test.payByAPI(s,"12/26","154",12.4);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
        assertTrue(output.getValue()<=100000);
        assertTrue(output.getValue()>=10000);
    }

    @DisplayName("PayAPI Card  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {""})
    public void PayAPI2(String s){
        DResponseObj<Integer> output= test.payByAPI(s,"12/26","154",12.4);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

    @DisplayName("PayAPI Date  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"05/27","12/25","06/24"})
    public void PayAPI3(String s){
        DResponseObj<Integer> output= test.payByAPI("4580575714143465",s,"154",12.4);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
        assertTrue(output.getValue()<=100000);
        assertTrue(output.getValue()>=10000);
    }

    @DisplayName("PayAPI Date  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {"-22332d","as3","","12/12/12"})
    public void PayAPI4(String s){
        DResponseObj<Integer> output= test.payByAPI("4580575714143465",s,"154",12.4);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

    @DisplayName("PayAPI Pin  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"259","203","157"})
    public void PayAPI5(String s){
        DResponseObj<Integer> output= test.payByAPI("4580575714143465","12/24",s,12.4);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
        assertTrue(output.getValue()<=100000);
        assertTrue(output.getValue()>=10000);
    }

    @DisplayName("PayAPI Date  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {"-22332d","a3s3","1574",""})
    public void PayAPI6(String s){
        DResponseObj<Integer> output= test.payByAPI("4580575714143465","12/26",s,12.4);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

    @DisplayName(" cancel PayAPI  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"25915","20543","15527"})
    public void cancelPayAPI(String s){
        DResponseObj<Boolean> output= test.cancelPay(s);
        //System.out.println(output.getValue());
        assertFalse(output.errorOccurred());
    }

    @DisplayName("PayAPI Date  -  fail")
    @ParameterizedTest
    @ValueSource(strings = {"-22332d","a3s3","1574",""})
    public void cancelPayAPI2(String s){
        DResponseObj<Boolean> output= test.cancelPay(s);
        //System.out.println(output.getValue());
        assertTrue(output.errorOccurred());
    }

}