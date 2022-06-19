package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataHistory;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.DataAccess.Repository.HistoryRepository;
import com.example.demo.DataAccess.Repository.StoreRepository;
import com.example.demo.DataAccess.Repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {
    private static final Logger logger = Logger.getLogger(HistoryService.class);

    //Repos
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository, UserRepository userService, StoreRepository storeService) {
        this.historyRepository = historyRepository;
        this.userRepository = userService;
        this.storeRepository = storeService;
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertHistory(DataHistory history,int storeId, String username) {
        try {
            Optional<DataStore> dataStore = storeRepository.findById(storeId);
            Optional<DataUser> dataUser = userRepository.findById(username);
            if(dataStore.isEmpty() || dataUser.isEmpty()){
                logger.warn(String.format("User[%s] or store[%d] not present in db",username,storeId));
                return false;
            }
            history.setDataStore(dataStore.get());
            history.setUser(dataUser.get());
            DataHistory dataHistory = historyRepository.saveAndFlush(history);
            //set the id's that were generated by db and store them to the data object
            logger.info(String.format("inserted history of user %s successfully to db", dataHistory.getUser().getUsername()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to insert history of user %s into db, ERROR: %s", history.getUser().getUsername(), e.getMessage()));
            return false;
        }
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public List<DataHistory> getAllHistoryByUsername(String username) {
        try {
            DataUser user = userRepository.findByUsername(username);
            if(user == null){
                logger.warn(String.format("failed to get %s - not present in db",username));
                return null;
            }
            List<DataHistory> histories = historyRepository.findAllByUser(user);
            logger.info(String.format("fetched all history of %s successfully from db",user.getUsername()));
            return histories;
        } catch (Exception e) {
            logger.error(String.format("failed to fetch all histories of %s from db, ERROR: %s", username,e.getMessage()));
            return null;
        }
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public List<DataHistory> getAllHistoryByStoreId(int storeId) {
        try {
            Optional<DataStore> dataStore = storeRepository.findById(storeId);
            if(dataStore.isEmpty()){
                logger.warn(String.format("failed to get store %d - not present in db",storeId));
                return null;
            }
            List<DataHistory> histories = historyRepository.findAllByStore(dataStore.get());
            logger.info(String.format("fetched all history of %s store successfully from db",dataStore.get().getName()));
            return histories;
        } catch (Exception e) {
            logger.error(String.format("failed to fetch all histories of store %d from db, ERROR: %s", storeId,e.getMessage()));
            return null;
        }
    }
}
