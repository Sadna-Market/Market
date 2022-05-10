package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.StoreModel.Store;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    ShoppingCart shoppingCart;
    @BeforeEach
    void beg(){
        shoppingCart = new ShoppingCart();
    }

    @Test
    void addNewProductToShoppingBag() {
        Store s= new Store(1,null,null ,null,null);
        shoppingCart.addNewProductToShoppingBag(1,s,20); //the cart not exist
        Assertions.assertTrue(shoppingCart.isCartExist(s.getStoreId().value).value); //
        Assertions.assertTrue(shoppingCart.addNewProductToShoppingBag(2,s,20).value); // cart exsist
        Assertions.assertFalse(shoppingCart.addNewProductToShoppingBag(2,s,40).errorOccurred()); //add exsisting product to bag

    }

    @Test
    void setProductQuantity() {
        Store s= new Store(1,null,null ,null,null);
        Assertions.assertFalse(shoppingCart.setProductQuantity(s.getStoreId().value,1,20).value); //shoppingcartNotExists
        shoppingCart.addNewProductToShoppingBag(1,s,20);
        Assertions.assertFalse(shoppingCart.setProductQuantity(s.getStoreId().value,2,10).value); //productNotExistsInShoppincart
    }

    @Test
    void removeProductFromShoppingBag() {
        Store s= new Store(1,null,null ,null,null);
        shoppingCart.addNewProductToShoppingBag(1,s,20); //the cart not exist
        Assertions.assertTrue(shoppingCart.getHashShoppingCart().value.get(s.getStoreId().value).isContainProduct(1).value);
        shoppingCart.removeProductFromShoppingBag(s.getStoreId().value,1);
        ConcurrentHashMap<Integer,ShoppingBag> A =shoppingCart.getHashShoppingCart().value;
        Assertions.assertFalse(shoppingCart.getHashShoppingCart().value.containsKey(s.getStoreId().value));
    }

    @Test
    void removeShoppingCart(){
        Store s= new Store(1,null,null ,null,null);
        shoppingCart.addNewProductToShoppingBag(1,s,20); //the cart not exist
        Assertions.assertTrue(shoppingCart.getHashShoppingCart().value.containsKey(s.getStoreId().value));
        shoppingCart.removeShoppingCart(s.getStoreId().value);
        Assertions.assertFalse(shoppingCart.getHashShoppingCart().value.containsKey(s.getStoreId()));
        Assertions.assertFalse(shoppingCart.removeShoppingCart(3).errorOccurred());



    }
}