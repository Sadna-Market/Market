package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.StoreModel.Store;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    ShoppingCart shoppingCart;
    @BeforeEach
    void beg(){
        shoppingCart = new ShoppingCart();
    }

    @Test
    void addNewProductToShoppingBag() {
        Store s= new Store();
        shoppingCart.addNewProductToShoppingBag(1,s,20); //the cart not exist
        Assertions.assertTrue(shoppingCart.isCartExist(s.getStoreId())); //
        Assertions.assertTrue(shoppingCart.addNewProductToShoppingBag(2,s,20)); // cart exsist
        Assertions.assertFalse(shoppingCart.addNewProductToShoppingBag(2,s,40)); //add exsisting product to bag

    }

    @Test
    void setProductQuantity() {
        Store s = new Store();
        Assertions.assertFalse(shoppingCart.setProductQuantity(s.getStoreId(),1,20)); //shoppingcartNotExists
        shoppingCart.addNewProductToShoppingBag(1,s,20);
        Assertions.assertFalse(shoppingCart.setProductQuantity(s.getStoreId(),2,10)); //productNotExistsInShoppincart
        Assertions.assertTrue(shoppingCart.setProductQuantity(s.getStoreId(),1,10)); //productNotExistsInShoppincart
    }

    @Test
    void removeProductFromShoppingBag() {
        Store s= new Store();
        shoppingCart.addNewProductToShoppingBag(1,s,20); //the cart not exist
        Assertions.assertTrue(shoppingCart.getHashShoppingCart().get(s.getStoreId()).isContainProduct(1));
        shoppingCart.removeProductFromShoppingBag(s.getStoreId(),1);
        Assertions.assertFalse(shoppingCart.getHashShoppingCart().get(s.getStoreId()).isContainProduct(1));
        Assertions.assertFalse(shoppingCart.removeProductFromShoppingBag(s.getStoreId(),2));
    }

    @Test
    void removeShoppingCart(){
        Store s= new Store();
        shoppingCart.addNewProductToShoppingBag(1,s,20); //the cart not exist
        Assertions.assertTrue(shoppingCart.getHashShoppingCart().containsKey(s.getStoreId()));
        shoppingCart.removeShoppingCart(s.getStoreId());
        Assertions.assertFalse(shoppingCart.getHashShoppingCart().containsKey(s.getStoreId()));
        Assertions.assertFalse(shoppingCart.removeShoppingCart(3));



    }
}