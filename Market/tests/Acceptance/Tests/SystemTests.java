package Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.util.List;

@DisplayName("System Tests - AT")
public class SystemTests extends MarketTests{
    @BeforeEach
    public void setUp() {
        market.initSystem();
    }

    @AfterEach
    public void tearDown() {
        market.exitSystem();
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
    @DisplayName("req: #1.1 - fail test [no system manager]")
    void InitSystem_Fail1() {
        market.deleteSystemManagers();
        market.exitSystem();
        assertFalse(market.initSystem());
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
        CreditCard creditCard = new CreditCard("1111222233334444", "1110", "111"); //invalid exp date

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


}
