package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;
import com.example.demo.DataAccess.Entity.DataShoppingBag;
import com.example.demo.DataAccess.Repository.ShoppingBagRepository;
import com.example.demo.DataAccess.Repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingBagService {
    private static final Logger logger = Logger.getLogger(ShoppingBagService.class);


    private final ShoppingBagRepository shoppingBagRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShoppingBagService(ShoppingBagRepository shoppingBagRepository, UserRepository userRepository) {
        this.shoppingBagRepository = shoppingBagRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean insertShoppingBag(DataShoppingBag shoppingBag) {
        try {
            var dataUser = userRepository.findByUsername(shoppingBag.getShoppingBagId().getUsername());
            shoppingBag.setUser(dataUser);
            DataShoppingBag dataShoppingBag = shoppingBagRepository.saveAndFlush(shoppingBag);
            //set the id's that were generated by db and store them to the data object
            logger.info(String.format("inserted shopping bag of %s successfully to db",
                    dataShoppingBag.getShoppingBagId().getUsername()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to insert shopping bag of %s into db, ERROR: %s",
                    shoppingBag.getShoppingBagId().getUsername(), e.getMessage()));
            return false;
        }
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean deleteShoppingBag(ShoppingBagId shoppingBagId) {
        try {
            shoppingBagRepository.deleteById(shoppingBagId);
            logger.info(String.format("deleted shopping bag of %s and store %d successfully from db",
                    shoppingBagId.getUsername(), shoppingBagId.getStoreId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to deleted shopping bag of %s and store %d from db, ERROR: %s",
                    shoppingBagId.getUsername(),
                    shoppingBagId.getStoreId(),
                    e.getMessage()));
            return false;
        }
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean deleteShoppingBags(List<DataShoppingBag> shoppingBags, String username) {
        try {
            shoppingBagRepository.deleteAll(shoppingBags);
            logger.info(String.format("deleted shopping bags of %s successfully from db",
                    username));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to deleted shopping bags of %s from db, ERROR: %s",
                    username,
                    e.getMessage()));
            return false;
        }
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean updateShoppingBag(DataShoppingBag shoppingBag) {
        var shoppingBagId = shoppingBag.getShoppingBagId();
        try {
            var optionalShoppingBag = shoppingBagRepository.findById(shoppingBagId);
            if (optionalShoppingBag.isEmpty()) {
                logger.warn(String.format("shopping bag (user,storeId) ~ (%s,%d) not found in db",
                        shoppingBagId.getUsername(),
                        shoppingBagId.getStoreId()));
                return false;
            }
            optionalShoppingBag.get().update(shoppingBag);
            shoppingBagRepository.saveAndFlush(optionalShoppingBag.get());
            logger.info(String.format("updated shopping bag (user,storeId) ~ (%s,%d) successfully from db",
                    shoppingBagId.getUsername(),
                    shoppingBagId.getStoreId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to update shopping bag (user,storeId) ~ (%s,%d) from db, ERROR: %s",
                    shoppingBagId.getUsername(),
                    shoppingBagId.getStoreId(),
                    e.getMessage()));
            return false;
        }
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public List<DataShoppingBag> getUserShoppingBags(String username) {
        try {
            List<DataShoppingBag> shoppingBags = shoppingBagRepository.findAllByShoppingBagId_Username(username);
            logger.info(String.format("fetched all shopping bags of %s successfully from db", username));
            return shoppingBags;
        } catch (Exception e) {
            logger.error(String.format("failed to fetch all shopping bags of %s from db, ERROR: %s", username, e.getMessage()));
            return null;
        }
    }
}
