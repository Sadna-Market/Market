package com.example.Unit.ExternalServices;

import com.example.demo.Domain.Market.Purchase;
import com.example.demo.Domain.UserModel.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseUnitTest {

    Purchase purchase=new Purchase();
    String[] emails = {"yosi@gmail.com","kobi@gmail.com","shalom@gmai.com","aaaa"};
    String[] passwords = {"Yosi123$","Kobi123$","Shalom123$","11111"};
    String[] PhoneNum = {"0538265477","0538265477","0538265477","0538265477"};


    @Test
    public void order(){
        assertTrue(purchase.order(new User(emails[0],passwords[0],PhoneNum[0], LocalDate.of(1998,11,22)),"aa","aa",1,"11212223331","12/26","123").errorOccurred());
    }
}
