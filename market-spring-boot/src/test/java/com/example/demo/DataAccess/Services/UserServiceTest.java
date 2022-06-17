package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataShoppingCart;
import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.Domain.UserModel.User;
import org.junit.jupiter.api.BeforeEach;
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

    User user;
    @BeforeEach
    public void setUp(){
        user = new User("exmaple@gmail.com","1qaz2wsx#EDC","0505555555", LocalDate.of(1993,8,18));
    }


    @Test
    @Transactional
    void insertUser() {
        //pre
        DataUser u = user.getDataObject();
        DataShoppingCart dataShoppingCart = new DataShoppingCart();
        dataShoppingCart.setShoppingBags( new HashSet<>());
        u.setDataShoppingCart(dataShoppingCart);

        //action
        assertTrue(userService.insertUser(u));

        //check
        assertEquals(u.getUsername(),"exmaple@gmail.com");
        assertEquals(u.getPassword(),"1qaz2wsx#EDC");
        assertEquals(u.getDateOfBirth(),LocalDate.of(1993,8,18));
        assertEquals(u.getPhoneNumber(),"0505555555");
        assertNotEquals(0,u.getDataShoppingCart().getShoppingCartId());

    }

    @Test
    @Transactional
    void deleteUser() {
        //pre
        DataUser u = user.getDataObject();
        DataShoppingCart dataShoppingCart = new DataShoppingCart();
        dataShoppingCart.setShoppingBags( new HashSet<>());
        u.setDataShoppingCart(dataShoppingCart);
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
        DataShoppingCart dataShoppingCart = new DataShoppingCart();
        dataShoppingCart.setShoppingBags( new HashSet<>());
        u.setDataShoppingCart(dataShoppingCart);
        assertTrue(userService.insertUser(u));
        //action
        u.setPhoneNumber(newPhone);
        assertTrue(userService.updateUser(u));

        //check
        List<DataUser> afterUser = userService.getAllUsers();
        assertEquals(1, afterUser.size());
        assertEquals(afterUser.get(0).getPhoneNumber(), newPhone);
    }

    @Test
    @Transactional
    void getUserByUsername() {
        DataUser u = user.getDataObject();
        DataShoppingCart dataShoppingCart = new DataShoppingCart();
        dataShoppingCart.setShoppingBags( new HashSet<>());
        u.setDataShoppingCart(dataShoppingCart);
        assertTrue(userService.insertUser(u));
        //action
        DataUser afterUser = userService.getUserByUsername(u.getUsername());
        assertEquals(u.getUsername(),afterUser.getUsername());
        assertEquals(u.getPassword(),afterUser.getPassword());
        assertEquals(u.getDateOfBirth(),afterUser.getDateOfBirth());
        assertEquals(u.getPhoneNumber(),afterUser.getPhoneNumber());
    }

    @Test
    @Transactional
    void getAllUsers() {
        User user1 = new User("exmaple1@gmail.com","1qaz2wsx#EDC","1111111111", LocalDate.of(1993,8,18));
        User user2 = new User("exmaple2@gmail.com","1qaz2wsx#EDC","0000000000", LocalDate.of(1993,8,18));
        User user3 = new User("exmaple3@gmail.com","1qaz2wsx#EDC","2222222222", LocalDate.of(1993,8,18));
        User user4 = new User("exmaple4@gmail.com","1qaz2wsx#EDC","3333333333", LocalDate.of(1993,8,18));
        User user5 = new User("exmaple5@gmail.com","1qaz2wsx#EDC","4444444444", LocalDate.of(1993,8,18));
        DataUser u1 = user1.getDataObject();
        DataUser u2 = user2.getDataObject();
        DataUser u3 = user3.getDataObject();
        DataUser u4 = user4.getDataObject();
        DataUser u5 = user5.getDataObject();
        DataShoppingCart dataShoppingCart1 = new DataShoppingCart();
        dataShoppingCart1.setShoppingBags( new HashSet<>());
        u1.setDataShoppingCart(dataShoppingCart1);
        DataShoppingCart dataShoppingCart2 = new DataShoppingCart();
        dataShoppingCart2.setShoppingBags( new HashSet<>());
        u2.setDataShoppingCart(dataShoppingCart2);
        DataShoppingCart dataShoppingCart3 = new DataShoppingCart();
        dataShoppingCart3.setShoppingBags( new HashSet<>());
        u3.setDataShoppingCart(dataShoppingCart3);
        DataShoppingCart dataShoppingCart4 = new DataShoppingCart();
        dataShoppingCart4.setShoppingBags( new HashSet<>());
        u4.setDataShoppingCart(dataShoppingCart4);
        DataShoppingCart dataShoppingCart5 = new DataShoppingCart();
        dataShoppingCart5.setShoppingBags( new HashSet<>());
        u5.setDataShoppingCart(dataShoppingCart5);
        assertTrue(userService.insertUser(u1));
        assertTrue(userService.insertUser(u2));
        assertTrue(userService.insertUser(u3));
        assertTrue(userService.insertUser(u4));
        assertTrue(userService.insertUser(u5));
        //action
        List<DataUser> users = userService.getAllUsers();
        assertEquals(5,users.size());
    }
}