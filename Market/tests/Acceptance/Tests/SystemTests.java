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

}
