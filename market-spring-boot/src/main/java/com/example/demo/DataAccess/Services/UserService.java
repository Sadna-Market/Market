package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.DataAccess.Repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html - docu for api
@Service
public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class);


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This insertion creates a User row in db
     * Note: ShoppingCartID is generated! the id is in the user object that called this function.
     * @param user
     * @return true if success, else false
     */
    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean insertUser(DataUser user) {
        try {
            DataUser u = userRepository.saveAndFlush(user);
//            user.getDataShoppingCart().setShoppingCartId(u.getDataShoppingCart().getShoppingCartId());
            logger.info(String.format("inserted %s successfully to db", user.getUsername()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to insert %s into db, ERROR: %s", user.getUsername(), e.getMessage()));
            return false;
        }
    }
    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean deleteUser(String username){
        try{
            userRepository.deleteById(username);
            logger.info(String.format("deleted %s successfully from db", username));
            return true;
        }catch (Exception e){
            logger.error(String.format("failed to delete %s from db, ERROR: %s", username, e.getMessage()));
            return false;
        }
    }
    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean updateUser(DataUser user){
        try{
            DataUser u = userRepository.findByUsername(user.getUsername());
            u.update(user);
            userRepository.save(u);
            logger.info(String.format("updated %s successfully in db", user.getUsername()));
            return true;
        }catch (Exception e){
            logger.error(String.format("failed to updated %s in db, ERROR: %s", user.getUsername(), e.getMessage()));
            return false;
        }
    }
    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public DataUser getUserByUsername(String username) {
        try {
            DataUser u = userRepository.findByUsername(username);
            logger.info(String.format("fetched %s successfully from db", u.getUsername()));
            return u;
        } catch (Exception e) {
            logger.error(String.format("failed to fetch %s from db, ERROR: %s", username, e.getMessage()));
            return null;
        }
    }
    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public List<DataUser> getAllUsers() {
        try {
            List<DataUser> users = userRepository.findAll();
            logger.info("fetched all users successfully from db");
            return users;
        } catch (Exception e) {
            logger.error(String.format("failed to fetch all users from db, ERROR: %s", e.getMessage()));
            return null;
        }
    }
}
