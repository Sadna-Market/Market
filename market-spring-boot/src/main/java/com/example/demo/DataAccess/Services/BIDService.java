package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.CompositeKeys.BIDID;
import com.example.demo.DataAccess.Entity.DataBID;
import com.example.demo.DataAccess.Repository.BIDRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BIDService {
    private static Logger logger = Logger.getLogger(BIDService.class);

    @Autowired
    private BIDRepository bidRepository;

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

    //@Transactional(rollbackFor = {Exception.class}, timeout = 10)
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
    }

}
