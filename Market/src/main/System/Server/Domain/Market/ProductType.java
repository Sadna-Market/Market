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
    String productName;
    String description;
    int category;
    List<Integer> stores=new ArrayList<>();

    private StampedLock lock_stores= new StampedLock();

    public ProductType(int productID, String productName, String description) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        logger.debug("the productID: "+productID+" received successfully");
    }

    public boolean storeExist(int storeID){
        long stamp= lock_stores.readLock();
        logger.debug("storeExist() catch the ReadLock.");
        try{
            return stores.contains(storeID);
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("storeExist() released the ReadLock.");
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



}
