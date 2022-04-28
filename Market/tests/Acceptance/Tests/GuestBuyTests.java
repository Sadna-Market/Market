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
}
