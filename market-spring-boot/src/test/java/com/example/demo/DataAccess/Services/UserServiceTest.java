package com.example.demo.DataAccess.Services;

import com.example.Acceptance.Obj.PasswordGenerator;
import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.Domain.UserModel.User;
import com.example.demo.Domain.UserModel.Validator;
import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private Facade market;

    User user;

    @BeforeEach
    public void setUp() {
        user = new User("exmaple1111@gmail.com", "1qaz2wsx#EDC", "0505555555", LocalDate.of(1993, 8, 18));
    }


    @Test
    @Transactional
    void insertUser() {
        //pre
        DataUser u = user.getDataObject();

        //action
        assertTrue(userService.insertUser(u));

        //check
        assertEquals(u.getUsername(), "exmaple1111@gmail.com");
        assertEquals("1qaz2wsx#EDC", Validator.getInstance().decryptAES(u.getPassword()));
        assertEquals(u.getDateOfBirth(), LocalDate.of(1993, 8, 18));
        assertEquals(u.getPhoneNumber(), "0505555555");
//        assertNotEquals(0, u.getDataShoppingCart().getShoppingCartId());

    }

    @Test
    @Transactional
    void deleteUser() {
        //pre
        DataUser u = user.getDataObject();
        assertTrue(userService.insertUser(u));
        //action
        assertTrue(userService.deleteUser(u.getUsername()));

        //check
        DataUser afterUser = userService.getUserByUsername(u.getUsername());
        assertNull(afterUser);
    }

    @Test
    @Transactional
    void updateUser() {
        //pre
        String newPhone = "0522222222";
        DataUser u = user.getDataObject();
        assertTrue(userService.insertUser(u));
        //action
        u.setPhoneNumber(newPhone);
        assertTrue(userService.updateUser(u));

        //check
        List<DataUser> afterUser = userService.getAllUsers();
        assertEquals(2, afterUser.size());
        assertEquals(afterUser.get(1).getPhoneNumber(), newPhone);
    }

    @Test
    @Transactional
    void getUserByUsername() {
        DataUser u = user.getDataObject();
        assertTrue(userService.insertUser(u));
        //action
        DataUser afterUser = userService.getUserByUsername(u.getUsername());
        assertEquals(u.getUsername(), afterUser.getUsername());
        assertEquals(u.getPassword(), afterUser.getPassword());
        assertEquals(u.getDateOfBirth(), afterUser.getDateOfBirth());
        assertEquals(u.getPhoneNumber(), afterUser.getPhoneNumber());
    }

    @Test
    //@Transactional
    void getAllUsers() {
        User user1 = new User("exmaple1@gmail.com", "1qaz2wsx#EDC", "1111111111", LocalDate.of(1993, 8, 18));
        User user2 = new User("exmaple2@gmail.com", "1qaz2wsx#EDC", "0000000000", LocalDate.of(1993, 8, 18));
        User user3 = new User("exmaple3@gmail.com", "1qaz2wsx#EDC", "2222222222", LocalDate.of(1993, 8, 18));
        User user4 = new User("exmaple4@gmail.com", "1qaz2wsx#EDC", "3333333333", LocalDate.of(1993, 8, 18));
        User user5 = new User("exmaple5@gmail.com", "1qaz2wsx#EDC", "4444444444", LocalDate.of(1993, 8, 18));
        DataUser u1 = user1.getDataObject();
        DataUser u2 = user2.getDataObject();
        DataUser u3 = user3.getDataObject();
        DataUser u4 = user4.getDataObject();
        DataUser u5 = user5.getDataObject();
        assertTrue(userService.insertUser(u1));
        assertTrue(userService.insertUser(u2));
        assertTrue(userService.insertUser(u3));
        assertTrue(userService.insertUser(u4));
        assertTrue(userService.insertUser(u5));
        //action
        List<DataUser> users = userService.getAllUsers();
        assertEquals(6, users.size());
    }

    @Test
    @Transactional
    void registration_Success() {
        String uuid = market.guestVisit().value;
        SLResponseOBJ<Boolean> res = market.addNewMember(uuid, "niv123@gmail.com", "Shalom123$", "0523251252", "16/3/2012");
        assertFalse(res.errorOccurred());
    }

    @Test
    @Transactional
    void changePass_Success() {
        var username = "niv@gmail.com";
        var password = "Shalom123$";
        String uuid = market.guestVisit().value;
        SLResponseOBJ<Boolean> res = market.addNewMember(uuid, username, password, "0523251252", "16/3/2012");
        assertFalse(res.errorOccurred());
        SLResponseOBJ<String> result = market.login(uuid, username, password);
        assertFalse(result.errorOccurred());
        uuid = result.value;
        String newPass = PasswordGenerator.generateStrongPassword();
        res = market.changePassword(uuid, username, password, newPass);
        assertFalse(res.errorOccurred());
    }

    @Test
    @Transactional
    void removeUser() throws InterruptedException {
        String uuid = market.guestVisit().value;
        SLResponseOBJ<Boolean> res = market.addNewMember(uuid, "niv200@gmail.com", "Shalom123$", "0523251252", "16/3/2012");
        assertNotEquals(false,res.value,String.valueOf(res.errorMsg));
        Thread.sleep(5);
        SLResponseOBJ<String> result = market.login(uuid, "sysManager@gmail.com", "Shalom123$");
        assertFalse(result.errorOccurred());
        uuid = result.value;
        res = market.removeMember(uuid, "niv200@gmail.com");
        assertFalse(res.errorOccurred());
    }

//    @Test
//    @Transactional
//    void load() {
//        int numOfUsers = 10;
//        String uuid = market.guestVisit().value;
//        for (int i = 0; i < numOfUsers; i++) {
//            String email = "niv" + i + "@gmail.com";
//            String pass = "Shalom123$";
//            String phone = "052325125" + i;
//            String date = "16/3/181" + i;
//            SLResponseOBJ<Boolean> res = market.addNewMember(uuid,
//                    email,
//                    pass,
//                    phone,
//                    date);
//            assertFalse(res.errorOccurred());
//        }
//        market.deleteAllMembers();
//        market.loadMembers();
//        for (int i = 0; i < numOfUsers; i++) {
//            String email = "niv" + i + "@gmail.com";
//            assertTrue(market.isMember2(email));
//        }
//    }
}