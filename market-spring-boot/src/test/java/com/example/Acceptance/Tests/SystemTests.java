package com.example.Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.Acceptance.Obj.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.util.List;

@DisplayName("System Tests - AT")
public class SystemTests extends MarketTests{
    String uuid;
    @BeforeEach
    public void setUp() {
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
        uuid = market.guestVisit();
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory();
        market = null; //for garbage collector
    }

    /**
     * Requirement: init system - #1.1
     */
    @Test
    @DisplayName("req: #1.1 - success test")
    void InitSystem_Success() {
        //init in setup
        assertTrue(market.hasSystemManager());
        assertTrue(market.hasPaymentService());
        assertTrue(market.hasSupplierService());
    }

    @Test
    @DisplayName("req: #1.1 - fail test [no connection to services]")
    void InitSystem_Fail2() {
        market.disconnectExternalService("Payment");
        assertFalse(market.serviceIsAlive("Payment"));
        ATResponseObj<String> res = market.pay(new CreditCard("1111222233334444","123","111"),100);
        assertTrue(res.errorOccurred());
    }

    /**
     * Requirement: Add/Edit/Change ExternalService - #1.2
     * //TODO: req #1.2 (next version)
     */
    @Test
    @DisplayName("req: #1.2.1 - success test")
    void Add_Connection_With_ExternalService_Success() {
        //TODO: next version
    }

    @Test
    @DisplayName("req: #1.2.1 - fail test [illegal service]")
    void Add_Connection_With_ExternalService_Fail1() {
        //TODO: next version
    }

    /**
     * Requirement: payment with external service - #1.3
     * Assumptions: credit card number is 16 digits long,
     * card num : 1111222233334444 - valid    cvv: 111 - valid
     */
    @Test
    @DisplayName("req: #1.3 - success test")
    void PaymentService_Success() {
        CreditCard creditCard = new CreditCard("1111222233334444", "1122", "111");

        ATResponseObj<String> response = market.pay(creditCard, 10);

        assertFalse(response.errorOccurred());
        assertNotNull(response.value);
        assertNotEquals(response.value, "");

    }

    @Test
    @DisplayName("req: #1.3 - fail test [invalid payment details]")
    void PaymentService_Fail1() {
        CreditCard creditCard = new CreditCard("1111222233334444", "110", "111"); //invalid exp date

        ATResponseObj<String> response = market.pay(creditCard, 10);

        assertTrue(response.errorOccurred());
    }

    @Test
    @DisplayName("req: #1.3 - fail test [invalid price]")
    void PaymentService_Fail2() {
        CreditCard creditCard = new CreditCard("1111222233334444", "1123", "111"); //invalid exp date

        ATResponseObj<String> response = market.pay(creditCard, -1);
        assertTrue(response.errorOccurred());
        response = market.pay(creditCard, 0);
        assertTrue(response.errorOccurred());
    }

    @Test
    @DisplayName("req: #1.3 - fail test [invalid input]")
    void PaymentService_Fail3() {
        ATResponseObj<String> response = market.pay(null, 10);
        assertTrue(response.errorOccurred());
    }

    /**
     * Requirement: supply items with external service - #1.4
     */
    @Test
    @DisplayName("req: #1.4 - success test")
    void SupplyService_Success() {
        User user = generateUser();
        ItemDetail item = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item.itemID = IPHONE_5_ID;
        List<ItemDetail> deliver = List.of(item);
        ATResponseObj<String> response = market.supply(deliver, user);

        assertFalse(response.errorOccurred());
        assertNotSame("", response.value);
    }


    @Test
    @DisplayName("req: #1.4 - fail test [item doesnt exist in market]")
    void SupplyService_Fail1() {
        User user = generateUser();
        ItemDetail item = new ItemDetail("iphoneXZ", 1, 10, List.of("phone"), "phone");
        item.itemID = 8887;
        List<ItemDetail> deliver = List.of(item);
        ATResponseObj<String> response = market.supply(deliver, user);
        assertTrue(response.errorOccurred());
    }

    @Test
    @DisplayName("req: #1.4 - fail test [invalid input]")
    void SupplyService_Fail2() {
        ATResponseObj<String> response = market.supply(null, member);
        assertTrue(response.errorOccurred());
    }


    /**
     * Requirement: alert realtime (connected user) - #1.5
     */
    @Test
    @DisplayName("req: #1.5 - success test")
    void RealAlert_Success() {
        //TODO: req: #1.5 - success test (next version)
    }

    @Test
    @DisplayName("req: #1.5 - fail test [...]")
    void RealAlert_Fail1() {
        //TODO: req: #1.5 - fail test (next version)
    }

    @Test
    @DisplayName("req: #1.5 - fail test [invalid input]")
    void RealAlert_Fail2() {
        //TODO: req: #1.5 - fail test (next version)
    }


    /**
     * Requirement: alert - #1.6
     */
    @Test
    @DisplayName("req: #1.6 - success test")
    void Alert_Success() {
        //TODO: req: #1.6 - success test (next version)
    }

    @Test
    @DisplayName("req: #1.6 - fail test [...]")
    void Alert_Fail1() {
        //TODO: req: #1.6 - fail test (next version)
    }

    @Test
    @DisplayName("req: #1.6 - fail test [invalid input]")
    void Alert_Fail2() {
        //TODO: req: #1.6 - fail test (next version)
    }



}
