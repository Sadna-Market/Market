package main.System.Server.Domain.Market;

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

    int productID;
    int rate=0, counter_rates=0;

    String productName;
    String description;
    int category;
    List<Integer> stores=new ArrayList<>();

    private StampedLock lock_stores= new StampedLock(),rateLock=new StampedLock();

    public ProductType(int productID, String productName, String description) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        logger.debug("the productID: "+productID+" received successfully");
    }

    public int getRate() {
        long stamp = rateLock.readLock();
        logger.debug("getRate catch the ReadLock");
        try{
            return rate;
        }
        finally {
            rateLock.unlockRead(stamp);
            logger.debug("getRate released the ReadLock");
        }
    }

    public boolean setRate(int r) {
        if (r<0 || r>10)
            return false;
        long stamp = rateLock.writeLock();
        logger.debug("getRate catch the WriteLock");
        try{
            rate = ((rate*counter_rates)+r)/(counter_rates+1);
            counter_rates++;
            return true;
        }
        finally {
            rateLock.unlockWrite(stamp);
            logger.debug("getRate released the ReadLock");
        }

    }

    public boolean storeExist(int storeID){
        long stamp= lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try{
            return stores.contains(storeID);
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    public boolean removeStore(int storeID){
        if (!storeExist(storeID)){
            logger.warn("the store in not in the list of this product.");
            return false;
        }
        long stamp= lock_stores.writeLock();
        logger.debug("catch the WriteLock.");
        try{
            return stores.remove(Integer.valueOf(storeID));
        }
        finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released the WriteLock.");
        }
    }

    public List<Integer> getStores() {
        long stamp = lock_stores.readLock();
        logger.debug("getStores() catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            for (Integer storeID : stores) output.add(storeID);
            return output;
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("getStores() released the ReadLock.");
        }

    }

    public boolean addStore(int storeID){
        long stamp= lock_stores.writeLock();
        logger.debug("addStore() catch the WriteLock.");
        try{
            if (stores.contains(storeID)){
                logger.debug("this storeID: "+storeID+" exists in this ProductType");
                return false;
            }
            else{
                logger.info("this storeID: "+storeID+" add this ProductType to the stores.");
                stores.add(storeID);
                return true;
            }
        }
        finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("addStore() released the WriteLock.");
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public boolean containName(String name){
        return productName.contains(name);
    }
    public boolean containDesc(String desc){
        return description.contains(desc);
    }

}
