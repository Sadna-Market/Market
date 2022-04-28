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
    /**
     * Requirement: login system  - #2.1.4.1
     */
    @Test
    @DisplayName("req: #2.1.4.1 - success test")
    void login_Success(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        assertTrue(market.login(member));
        assertTrue(market.isLoggedIn(member));
    }
    @Test
    @DisplayName("req: #2.1.4.1 - fail test [invalid username]")
    void login_Fail1(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        assertFalse(market.login(generateUser()));
    }

    @Test
    @DisplayName("req: #2.1.4.1 - fail test [invalid password]")
    void login_Fail2(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        String oldpass = member.password;
        member.password = "xxxxx";
        assertFalse(market.login(member));
        assertFalse(market.isLoggedIn(member));
        member.password = oldpass;
    }
    @Test
    @DisplayName("req: #2.1.4.1 - fail test [invalid inputs]")
    void login_Fail3(){
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        String oldpass = member.password;
        member.password = null;
        assertFalse(market.login(member));
        member.password = oldpass;
        String username = member.username;
        member.username = null;
        assertFalse(market.login(member));
        member.username = username;
    }

    /**
     * Requirement: change password - #2.1.4.2
     */
    @Test
    @DisplayName("req: #2.1.4.2 - success test")
    void changePass_Success(){
        String oldpass = member.password;
        String newPass = PasswordGenerator.generateStrongPassword();
        assertTrue(market.changePassword(member,newPass));
        assertEquals(member.password,newPass);
        assertTrue(market.changePassword(member,oldpass));
        assertEquals(member.password,oldpass);
    }
    @Test
    @DisplayName("req: #2.1.4.2 - fail test [user is not a member]")
    void changePass_Fail1(){
        User user = generateUser();
        String newPass = PasswordGenerator.generateStrongPassword();
        assertFalse(market.changePassword(user,newPass));
    }

    @Test
    @DisplayName("req: #2.1.4.2 - fail test [invalid  new password]")
    void changePass_Fail2(){
        String oldpass = member.password;
        String newPass = "123";
        assertFalse(market.changePassword(member,newPass));
        assertNotEquals(member.password,newPass);
        assertEquals(member.password,oldpass);
    }
    @Test
    @DisplayName("req: #2.1.4.2 - fail test [invalid inputs]")
    void changePass_Fail3(){
        String oldpass = member.password;
        assertFalse(market.changePassword(member,null));
        assertNotEquals(member.password,null);
        assertEquals(member.password,oldpass);
    }



}
