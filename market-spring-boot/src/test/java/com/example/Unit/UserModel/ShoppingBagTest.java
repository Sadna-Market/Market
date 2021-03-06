package com.example.Unit.UserModel;

import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.ShoppingBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingBagTest {
    ShoppingBag shoppingBag;
    Store s;

    @BeforeEach
    void start(){
        shoppingBag= new ShoppingBag(s,"");
        s = new Store(1,null,null,null,null);

    }

    @Test
    void addProduct() {
        shoppingBag.addProduct(2,30);
        Assertions.assertTrue(shoppingBag.isContainProduct(2).value); //add product ok
    }

    @Test
    void setProductQuantity() {
        shoppingBag.addProduct(1,20);
        shoppingBag.setProductQuantity(1,30);
        Assertions.assertEquals(shoppingBag.getProductQuantity(1).value,30);
        Assertions.assertFalse(shoppingBag.setProductQuantity(2,20).value);
    }

    @Test
    void removeProductFromShoppingBag() {
        Assertions.assertFalse(shoppingBag.removeProductFromShoppingBag(4).value); //remove not exisisting product
        shoppingBag.removeProductFromShoppingBag(1);
        Assertions.assertFalse(shoppingBag.isContainProduct(1).value);
    }
}