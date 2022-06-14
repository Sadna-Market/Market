package com.example.demo.api;

import com.example.demo.configuration.StateInit.JsonReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class apiDiscountPparserTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test1() {
        try {
            apiBuyPparser apiBuyPparser = new apiBuyPparser();
            File file = new File("test1.json");
            ObjectMapper objectMapper = new ObjectMapper();
            String absolutePath = file.getAbsolutePath();
            jsonReaderTest jsonReader = objectMapper.readValue(new File(absolutePath), jsonReaderTest.class);
            for(Map<String,Object> d : jsonReader.js){
                System.out.println(d);
            apiBuyPparser.BuyRuleParse(d);}
        } catch (Exception e) {
            fail("Should not have thrown any exception"+e.getMessage());
        }
    }
}