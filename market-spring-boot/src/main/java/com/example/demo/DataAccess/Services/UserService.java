package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.DataAccess.Repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html - docu for api
@Service
@Transactional(rollbackFor = {Exception.class}, timeout = 10)
public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;



    public boolean insertUser(DataUser user) {
        try {
            DataUser u = userRepository.saveAndFlush(user);
            logger.info(String.format("inserted %s successfully to db", user.getUsername()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to insert %s into db, ERROR: %s", user.getUsername(), e.getMessage()));
            return false;
        }
    }

    public DataUser getUser(String username){
        try {
            DataUser u = userRepository.getById(username);
            logger.info(String.format("fetched %s successfully from db", u.getUsername()));
            return u;
        } catch (Exception e) {
            logger.error(String.format("failed to fetch %s from db, ERROR: %s", username, e.getMessage()));
            return null;
        }
    }

    public List<DataUser> getAllUsers(){
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
