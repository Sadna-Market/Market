package com.example.demo.Domain.Market;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;


public class ProductType {

    static Logger logger=Logger.getLogger(ProductType.class);
    /*
    This class is a Data structure that hold all the StoreID of the stores that sell it.
    all the fields can change.
    the lock is read/write lock , this is private lock for keep to stores right.
     */

    private int productID;
    private int rate=0, counter_rates=0;

    private String productName;
    private String description;
    private int category;
    public List<Integer> stores=new ArrayList<>();

    private StampedLock lock_stores= new StampedLock(),rateLock=new StampedLock();

    public ProductType(int productID, String productName, String description,int category) {
        this.category = category;
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        logger.debug("the productID: "+productID+" received successfully");
    }

    public DResponseObj<Integer> getRate() {
        long stamp = rateLock.readLock();
        logger.debug("catch the ReadLock");
        try{
            DResponseObj<Integer> output=new DResponseObj<>();
            output.value=rate;
            return output;
        }
        finally {
            rateLock.unlockRead(stamp);
            logger.debug("released the ReadLock");
        }
    }

    public DResponseObj<Boolean> setRate(int r) {
        if (r<0 || r>10) {
            String warning = "the Rate in not between 1-10";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = rateLock.writeLock();
        logger.debug("getRate catch the WriteLock");
        try{
            rate = ((rate*counter_rates)+r)/(counter_rates+1);
            counter_rates++;
            return new DResponseObj<>(true);
        }
        finally {
            rateLock.unlockWrite(stamp);
            logger.debug("getRate released the ReadLock");
        }

    }

    public DResponseObj<Boolean> storeExist(int storeID){
        if (storeID<0){
            String warning = "storeID #"+storeID+ " is not valid.";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp= lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try{
            if (stores.contains(storeID))
                return new DResponseObj<>(true);
            return new DResponseObj<>(false);
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    public DResponseObj<Boolean> removeStore(int storeID){
        DResponseObj<Boolean> existInStore=storeExist(storeID);
        if(existInStore.errorOccurred()) return existInStore;
        if (!existInStore.getValue()){
            logger.warn("this store number not exist in the product type list");
            return new DResponseObj<>(ErrorCode.STORENOTINTHEPRODUCTTYPE);
        }
        long stamp= lock_stores.writeLock();
        logger.debug("catch the WriteLock.");
        try{
            if (stores.remove(Integer.valueOf(storeID)))
                return new DResponseObj<>(true);
            String warning = "the product typre can not remove this store from his list";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.STORESTAYINTHEPRODUCTTYPE);
        }
        finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released the WriteLock.");
        }
    }

    public DResponseObj<List<Integer>> getStores() {
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            for (Integer storeID : stores) output.add(storeID);
            return new DResponseObj<>(output);
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }

    }

    public DResponseObj<Boolean> addStore(int storeID){
        DResponseObj<Boolean> checkExist=storeExist(storeID);
        if (checkExist.errorOccurred()) return checkExist;
        if (checkExist.getValue()){
            String warning = "storeID #" + storeID + " is exist in the system, you can not use that";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.STORENOTINTHEPRODUCTTYPE);
        }
        long stamp= lock_stores.writeLock();
        logger.debug("catch the WriteLock.");
        try{
            stores.add(storeID);
            logger.info("this storeID: "+storeID+" add this ProductType to the stores.");
            return new DResponseObj<>(true);
        }
        finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released the WriteLock.");
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public DResponseObj<Integer> getProductID() {
        DResponseObj<Integer> output=new DResponseObj<>();
        output.value=productID;
        return output;
    }

    public DResponseObj<String> getProductName() {
        return new DResponseObj<>(productName);
    }

    public DResponseObj<String> getDescription() {
        return new DResponseObj<>(description);
    }

    public DResponseObj<Integer> getCategory() {
        DResponseObj<Integer> output=new DResponseObj<>();
        output.value=category;
        return output;
    }

    public DResponseObj<Boolean> containName(String name){
        return new DResponseObj<>(productName.contains(name));
    }
    public DResponseObj<Boolean> containDesc(String desc){
        return new DResponseObj<>(description.contains(desc));
    }

}
