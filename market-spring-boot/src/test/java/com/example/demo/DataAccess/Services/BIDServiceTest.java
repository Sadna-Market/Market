package com.example.demo.DataAccess.Services;

import com.example.demo.Domain.StoreModel.BID;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class BIDServiceTest {

    @Autowired
    private BIDService bidService;


    @Test
    void insertBID(){
      //  vad bid = new BID("niv",1,"banana",3,2,)
    }
}