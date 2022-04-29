package Acceptance.Tests;

import Acceptance.Obj.ATResponseObj;
import Acceptance.Obj.ItemDetail;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Guest Buy Tests  - AT")
public class GuestBuyTests extends MarketTests{
    @BeforeEach
    public void setUp() {
        market.initSystem();
    }

    @AfterEach
    public void tearDown() {
        market.exitSystem();
    }


    /**
     * Requirement: get information of a store and its products  - #2.2.1
     */
    @Test
    @DisplayName("req: #2.2.1 - success test")
    void GetInfoStore_Success() {
        ATResponseObj<String> info = market.getStoreInfo(existing_storeID);
        assertFalse(info.errorOccurred());
        assertNotNull(info.value);
        assertNotSame("", info.value);
    }

    @Test
    @DisplayName("req: #2.2.1 - fail test [storeID not exist]")
    void GetInfoStore_Fail1() {
        ATResponseObj<String> info = market.getStoreInfo(existing_storeID + 5000);
        assertTrue(info.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.2.1 - fail test [invalid input]")
    void GetInfoStore_Fail2() {
        ATResponseObj<String> info = market.getStoreInfo(-1);
        assertTrue(info.errorOccurred());
    }

    /**
     * Requirement: search for item  - #2.2.2
     */
    @Test
    @DisplayName("req: #2.2.2 - success test")
    void SearchItem_Success() {
        String itemName = "iphone";
        String category = "mobile phone";
        List<String> keyWords = List.of("telephone,apple");
        ATResponseObj<List<ItemDetail>> response = market.searchItems(itemName, category, keyWords);
        assertFalse(response.errorOccurred());
        assertFalse(response.value.isEmpty());
    }

    @Test
    @DisplayName("req: #2.2.2 - fail test [item doesnt exist]")
    void SearchItem_Fail1() {
        String itemName = "PC";
        String category = "computer";
        List<String> keyWords = List.of("microsoft,dell");
        ATResponseObj<List<ItemDetail>> response = market.searchItems(itemName, category, keyWords);
        assertTrue(response.value.isEmpty());
    }

    @Test
    @DisplayName("req: #2.2.2 - fail test [invalid input]")
    void SearchItem_Fail2() {
        ATResponseObj<List<ItemDetail>> response = market.searchItems(null, "null", new LinkedList<>());
        assertTrue(response.errorOccurred());
        response = market.searchItems("null", null, new LinkedList<>());
        assertTrue(response.errorOccurred());
        response = market.searchItems("null", "null", null);
        assertTrue(response.errorOccurred());
    }

    /**
     * Requirement: filter items after search  - #2.2.2
     */
    @Test
    @DisplayName("req: #2.2.2 - success test")
    void Filter_Success() {
        String itemName = "iphone";
        String category = "mobile phone";
        List<String> keyWords = List.of("telephone,apple");
        ATResponseObj<List<ItemDetail>> response = market.searchItems(itemName, category, keyWords);
        assertFalse(response.errorOccurred());
        assertFalse(response.value.isEmpty());

        int productRank = 2;
        String priceRange = "0-10";
        int storeRank = 5;
        ATResponseObj<List<ItemDetail>> filteredResponse = market.filterSearchResults(response.value, productRank, priceRange, category, storeRank);
        assertFalse(filteredResponse.errorOccurred());
        assertFalse(filteredResponse.value.isEmpty());
    }

    @Test
    @DisplayName("req: #2.2.2 - fail test [filter list to be empty]")
    void Filter_Fail1() {
        String itemName = "iphone";
        String category = "mobile phone";
        List<String> keyWords = List.of("telephone,apple");
        ATResponseObj<List<ItemDetail>> response = market.searchItems(itemName, category, keyWords);
        assertFalse(response.errorOccurred());
        assertFalse(response.value.isEmpty());

        int productRank = 2;
        String priceRange = "100-200";
        int storeRank = 5;
        ATResponseObj<List<ItemDetail>> filteredResponse = market.filterSearchResults(response.value, productRank, priceRange, category, storeRank);
        assertTrue(filteredResponse.errorOccurred());
        assertTrue(filteredResponse.value.isEmpty());
    }

    @Test
    @DisplayName("req: #2.2.2 - fail test [invalid input]")
    void Filter_Fail2() {
        ATResponseObj<List<ItemDetail>> filteredResponse = market.filterSearchResults(null, 2, "priceRange", "category", 5);
        assertTrue(filteredResponse.errorOccurred());
    }

    /**
     * Requirement: add product to shopping bag  - #2.2.3
     */
    @Test
    @DisplayName("req: #2.2.3 - success1 test")
    void addProductToShoppingBag_Success1() {
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        assertTrue(market.addToCart(existing_storeID, item1));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertEquals(1, cart.size());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(1, bag.size());
        assertEquals("iphone5", bag.get(0).name);
        int amountInStock = market.getAmountOfProductInStore(existing_storeID, item1);
        assertEquals(1, amountInStock);
    }

    @Test
    @DisplayName("req: #2.2.3 - success2 test")
    void addProductToShoppingBag_Success2() {
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3232, 1, 10, List.of("TV"), "screen");
        assertTrue(market.addToCart(existing_storeID, item3));
        assertTrue(market.addToCart(existing_storeID, item3));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertEquals(1, cart.size());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(1, bag.size());
        assertEquals("screenFULLHD", bag.get(0).name);
        assertEquals(2, bag.get(0).quantity);
        int amountInStock = market.getAmountOfProductInStore(existing_storeID, item3);
        assertEquals(3, amountInStock);
    }

    @Test
    @DisplayName("req: #2.2.3 - fail test [want more then there is in stock]")
    void addProductToShoppingBag_Fail1() {
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 2, 10, List.of("phone"), "phone");
        assertFalse(market.addToCart(existing_storeID, item1));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertEquals(0, cart.size());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(0, bag.size());
        int amountInStock = market.getAmountOfProductInStore(existing_storeID, item1);
        assertEquals(1, amountInStock);
    }

    @Test
    @DisplayName("req: #2.2.3 - fail test [invalid input]")
    void addProductToShoppingBag_Fail2() {
        assertFalse(market.addToCart(existing_storeID, null));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertEquals(0, cart.size());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(0, bag.size());
    }

    /**
     * Requirement: get cart details  - #2.2.4.1
     */
    @Test
    @DisplayName("req: #2.2.4.1 - success test")
    void getCartDetails_Success() {
        assertTrue(market.cartExists());
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        assertTrue(market.addToCart(existing_storeID, item1));

        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertFalse(cart.isEmpty());
        assertFalse(cart.get(0).isEmpty());
        assertEquals("iphone5", cart.get(0).get(0).name);

    }

    @Test
    @DisplayName("req: #2.2.4.1 - fail test [empty cart]")
    void getCartDetails_Fail1() {
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertTrue(cart.isEmpty());
    }

    /**
     * Requirement: remove product from cart - #2.2.4.2
     */
    @Test
    @DisplayName("req: #2.2.4.2 - success test")
    void removeProductFromCart_Success() {
        assertTrue(market.cartExists());
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        assertTrue(market.addToCart(existing_storeID, item1));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertFalse(cart.isEmpty());

        assertTrue(market.removeProductFromCart(item1));
        response = market.getCart();
        assertFalse(response.errorOccurred());
        cart = response.value;
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("req: #2.2.4.2 - fail test [product not exits in cart]")
    void removeProductFromCart_Fail1() {
        ItemDetail item = new ItemDetail("xxx", 1111, 1, 10, List.of("bla"), "bb");
        assertFalse(market.removeProductFromCart(item));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertTrue(cart.isEmpty());

    }

    @Test
    @DisplayName("req: #2.2.4.2 - fail test [invalid input]")
    void removeProductFromCart_Fail2() {
        assertFalse(market.removeProductFromCart(null));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertTrue(cart.isEmpty());
    }

    /**
     * Requirement: update quantity of product in cart  - #2.2.4.3
     */
    @Test
    @DisplayName("req: #2.2.4.3 - success test")
    void updateQuantityInCart_Success() {
        assertTrue(market.cartExists());
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3232, 1, 10, List.of("TV"), "screen");
        assertTrue(market.addToCart(existing_storeID, item3));

        assertTrue(market.updateProductQuantity(item3,2));

        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertFalse(cart.isEmpty());
        assertFalse(cart.get(0).isEmpty());
        assertEquals("screenFULLHD", cart.get(0).get(0).name);
        assertEquals(2,cart.get(0).get(0).quantity);
    }

    @Test
    @DisplayName("req: #2.2.4.3 - fail test [product no available in cart]")
    void updateQuantityInCart_Fail1() {
        assertTrue(market.cartExists());
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3232, 1, 10, List.of("TV"), "screen");
        assertTrue(market.addToCart(existing_storeID, item3));

        ItemDetail item2 = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        assertFalse(market.updateProductQuantity(item2,2));

        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertFalse(cart.isEmpty());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(1,bag.size());
        assertEquals("screenFULLHD", bag.get(0).name);
        assertEquals(1,bag.get(0).quantity);
    }

    @Test
    @DisplayName("req: #2.2.4.3 - fail test [quantity to updated is not available]")
    void updateQuantityInCart_Fail2() {
        assertTrue(market.cartExists());
        ItemDetail item2 = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        assertTrue(market.addToCart(existing_storeID, item2));

        assertFalse(market.updateProductQuantity(item2,2));

        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertFalse(cart.isEmpty());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(1,bag.size());
        assertEquals("iphone6", bag.get(0).name);
        assertEquals(1,bag.get(0).quantity);
    }

    @Test
    @DisplayName("req: #2.2.4.3 - fail test [invalid input]")
    void updateQuantityInCart_Fail3() {
        assertTrue(market.cartExists());
        ItemDetail item2 = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        assertTrue(market.addToCart(existing_storeID, item2));

        assertFalse(market.updateProductQuantity(item2,-1));

        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertFalse(cart.isEmpty());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(1,bag.size());
        assertEquals("iphone6", bag.get(0).name);
        assertEquals(1,bag.get(0).quantity);
    }
}
