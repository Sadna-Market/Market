package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.CompositeKeys.BIDID;
import com.example.demo.DataAccess.CompositeKeys.PermissionId;
import com.example.demo.DataAccess.Entity.DataBID;
import com.example.demo.DataAccess.Entity.DataPermission;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Repository.BIDRepository;
import com.example.demo.DataAccess.Repository.StoreRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BIDService {
    private static Logger logger = Logger.getLogger(BIDService.class);


    private final BIDRepository bidRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public BIDService(BIDRepository bidRepository,StoreRepository storeRepository){
        this.bidRepository = bidRepository;
        this.storeRepository = storeRepository;
    }


    //@Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean insertBID(DataBID dataBID) {
        try {
            bidRepository.saveAndFlush(dataBID);
            logger.info(String.format("inserted bid (%d,%s,%d)~(store,username,productId) to db",
                    dataBID.getId().getStoreId(),
                    dataBID.getId().getUsername(),
                    dataBID.getId().getProductTypeId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to insert bid (%d,%s,%d)~(store,username,productId) to db, EEROR: %s",
                    dataBID.getId().getStoreId(),
                    dataBID.getId().getUsername(),
                    dataBID.getId().getProductTypeId(),
                    e.getMessage()));
            return false;
        }
    }

    //@Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean removeBID(BIDID bidid) {
        try {
            bidRepository.deleteById(bidid);
            logger.info(String.format("removed bid (%d,%s,%d)~(store,username,productId) from db",
                    bidid.getStoreId(),
                    bidid.getUsername(),
                    bidid.getProductTypeId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to delete bid (%d,%s,%d)~(store,username,productId) from db, EEROR: %s",
                    bidid.getStoreId(),
                    bidid.getUsername(),
                    bidid.getProductTypeId(),
                    e.getMessage()));
            return false;
        }
    }

/*    //@Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean updateBID(DataBID dataBID) {
        try {
            var databid = bidRepository.findById(dataBID.getId());
            if(databid.isEmpty()){
                logger.error(String.format("bid (%d,%s,%d)~(store,username,productId) not present in db",
                        dataBID.getId().getStoreId(),
                        dataBID.getId().getUsername(),
                        dataBID.getId().getProductTypeId()));
                return false;
            }
            databid.get().update(dataBID);
            bidRepository.saveAndFlush(databid.get());
            logger.info(String.format("updated bid (%d,%s,%d)~(store,username,productId) to db",
                    dataBID.getId().getStoreId(),
                    dataBID.getId().getUsername(),
                    dataBID.getId().getProductTypeId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to update bid (%d,%s,%d)~(store,username,productId) to db, EEROR: %s",
                    dataBID.getId().getStoreId(),
                    dataBID.getId().getUsername(),
                    dataBID.getId().getProductTypeId(),
                    e.getMessage()));
            return false;
        }
    }*/

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public DataBID getBIDById(BIDID BIDId) {
        try {
            Optional<DataBID> dataBID = bidRepository.findById(BIDId);
            if (dataBID.isEmpty()) {
                logger.warn(String.format("BID (storeId,productId,usernameId) ~ (%d,%d,%s) is not present in db",
                        BIDId.getStoreId(),
                        BIDId.getProductTypeId(),
                        BIDId.getUsername()));
                return null;
            }
            logger.info(String.format("fetched BID (storeId,productId,usernameId) ~ (%d,%d,%s) successfully from db",
                    dataBID.get().getId().getStoreId(),
                    dataBID.get().getId().getProductTypeId(),
                    dataBID.get().getId().getUsername()));
            return dataBID.get();
        } catch (Exception e) {
            logger.error(String.format("failed to fetch BID (storeId,productId,usernameId) ~ (%d,%d,%s) from db, ERROR: %s",
                    BIDId.getStoreId(),
                    BIDId.getProductTypeId(),
                    BIDId.getUsername(),
                    e.getMessage()));
            return null;
        }
    }

}
