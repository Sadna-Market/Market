package Acceptance.Tests;
import Acceptance.Obj.PasswordGenerator;
import Acceptance.Obj.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Guest General Tests  - AT")
public class GuestGeneralTests extends MarketTests{
    @BeforeEach
    public void setUp() {
        market.initSystem();
    }

    @AfterEach
    public void tearDown() {
        market.exitSystem();
    }

    /**
     * Requirement: enter guest to system- #2.1.1
     */
    @Test
    @DisplayName("req: #2.1.1 - success test")
    void entrance_Success(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
    }

    /**
     * Requirement: exit guest from system - #2.1.2
     */
    @Test
    @DisplayName("req: #2.1.2 - success test")
    void exit_Success(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        market.exitSystem();
        assertFalse(market.cartExists());
        assertFalse(market.guestOnline());
    }


    /**
     * Requirement: registration system  - #2.1.3
     */
    @Test
    @DisplayName("req: #2.1.3 - success test")
    void registration_Success(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        User newUser = generateUser();
        assertTrue(market.register(newUser.username,newUser.password));
        assertTrue(market.isMember(newUser));
    }
    @Test
    @DisplayName("req: #2.1.3 - fail test [invalid password]")
    void registration_Fail1(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        User newUser = generateUser();
        assertFalse(market.register(newUser.username,""));
        assertFalse(market.isMember(newUser));
    }

    @Test
    @DisplayName("req: #2.1.3 - fail test [user already exists]")
    void registration_Fail2(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        User newUser = generateUser();
        assertTrue(market.register(newUser.username,newUser.password));
        assertTrue(market.isMember(newUser));
        assertFalse(market.register(newUser.username,newUser.password));
    }
    @Test
    @DisplayName("req: #2.1.3 - fail test [invalid inputs]")
    void registration_Fail3(){
        User newUser = generateUser();
        assertFalse(market.register(null,newUser.password));
        assertFalse(market.register(newUser.username,null));
    }


}
