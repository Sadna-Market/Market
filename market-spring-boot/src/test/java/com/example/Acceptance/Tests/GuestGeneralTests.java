package com.example.Acceptance.Tests;

import com.example.Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Guest General Tests  - AT")
public class GuestGeneralTests extends MarketTests {
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
     * Requirement: enter guest to system- #2.1.1
     */
    @Test
    @DisplayName("req: #2.1.1 - success test")
    void entrance_Success() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
    }

    /**
     * Requirement: exit guest from system - #2.1.2
     */
    @Test
    @DisplayName("req: #2.1.2 - success test")
    void exit_Success() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        market.exitSystem(uuid);
        assertFalse(market.cartExists(uuid));
        assertFalse(market.guestOnline(uuid));
    }


    /**
     * Requirement: registration system  - #2.1.3
     */
    @Test
    @DisplayName("req: #2.1.3 - success test")
    void registration_Success() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        User newUser = generateUser();
        assertTrue(market.register(uuid, newUser.username, newUser.password,newUser.dateOfBirth));
        assertTrue(market.isMember(newUser));
    }

    @Test
    @DisplayName("req: #2.1.3 - fail test [invalid password]")
    void registration_Fail1() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        User newUser = generateUser();
        assertFalse(market.register(uuid, newUser.username, "",newUser.dateOfBirth));
        assertFalse(market.isMember(newUser));
    }

    @Test
    @DisplayName("req: #2.1.3 - fail test [user already exists]")
    void registration_Fail2() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        User newUser = generateUser();
        assertTrue(market.register(uuid, newUser.username, newUser.password,newUser.dateOfBirth));
        assertTrue(market.isMember(newUser));
        assertFalse(market.register(uuid, newUser.username, newUser.password,newUser.dateOfBirth));
    }

    @Test
    @DisplayName("req: #2.1.3 - fail test [invalid inputs]")
    void registration_Fail3() {
        User newUser = generateUser();
        assertFalse(market.register(uuid, null, newUser.password,newUser.dateOfBirth));
        assertFalse(market.register(uuid, newUser.username, null,newUser.dateOfBirth));
    }

    /**
     * Requirement: login system  - #2.1.4.1
     */
    @Test
    @DisplayName("req: #2.1.4.1 - success test")
    void login_Success() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isLoggedIn(uuid));
    }

    @Test
    @DisplayName("req: #2.1.4.1 - fail test [invalid username]")
    void login_Fail1() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ATResponseObj<String> memberID = market.login(uuid, generateUser());
        assertTrue(memberID.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.1.4.1 - fail test [invalid password]")
    void login_Fail2() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        String oldpass = member.password;
        member.password = "xxxxx";
        ATResponseObj<String> memberID = market.login(uuid, member);
        assertTrue(memberID.errorOccurred());
        assertFalse(market.isLoggedIn(uuid));
        member.password = oldpass;
    }

    @Test
    @DisplayName("req: #2.1.4.1 - fail test [invalid inputs]")
    void login_Fail3() {
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        String oldpass = member.password;
        member.password = null;
        ATResponseObj<String> memberID = market.login(uuid, member);
        assertTrue(memberID.errorOccurred());
        member.password = oldpass;
        String username = member.username;
        member.username = null;
        memberID = market.login(uuid, member);
        assertTrue(memberID.errorOccurred());
        uuid = memberID.value;
        member.username = username;
    }

    /**
     * Requirement: change password - #2.1.4.2
     */
    @Test
    @DisplayName("req: #2.1.4.2 - success test")
    void changePass_Success() {
        String newPass = PasswordGenerator.generateStrongPassword();
        assertTrue(market.changePassword(uuid, member, newPass));
        member.password = newPass;
        ATResponseObj<String> res = market.login(uuid, member);
        assertFalse(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.1.4.2 - fail test [user is not a member]")
    void changePass_Fail1() {
        User user = generateUser();
        String newPass = PasswordGenerator.generateStrongPassword();
        assertFalse(market.changePassword(uuid, user, newPass));
    }

    @Test
    @DisplayName("req: #2.1.4.2 - fail test [invalid  new password]")
    void changePass_Fail2() {
        String newPass = "123";
        assertFalse(market.changePassword(uuid, member, newPass));
    }

    @Test
    @DisplayName("req: #2.1.4.2 - fail test [invalid inputs]")
    void changePass_Fail3() {
        assertFalse(market.changePassword(uuid, member, null));
    }


}
