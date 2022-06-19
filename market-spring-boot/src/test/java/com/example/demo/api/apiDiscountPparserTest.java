package com.example.demo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class apiDiscountPparserTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void testParser() {
        try {
            apiDiscountPparser apiDiscountPparser = new apiDiscountPparser();
            File file = new File("testDiscount.json");
            ObjectMapper objectMapper = new ObjectMapper();
            String absolutePath = file.getAbsolutePath();
            jsonReaderTest jsonReader = objectMapper.readValue(new File(absolutePath), jsonReaderTest.class);
            for(Map<String,Object> d : jsonReader.js){
                System.out.println(d);
                apiDiscountPparser.DiscountParse(d);}
        } catch (Exception e) {
            fail("Should not have thrown any exception"+e.getMessage());
        }
    }

}