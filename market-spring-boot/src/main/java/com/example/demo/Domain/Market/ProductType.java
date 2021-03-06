package com.example.demo.Domain.Market;

import com.example.demo.DataAccess.Entity.DataProductType;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.StampedLock;


public class ProductType {

    private static DataServices dataServices;
    static Logger logger = Logger.getLogger(ProductType.class);
    /*
    This class is a Data structure that hold all the StoreID of the stores that sell it.
    all the fields can change.
    the lock is read/write lock , this is private lock for keep to stores right.
     */

    private int productID;
    private int rate = 0, counter_rates = 0;

    private String productName;
    private String description;
    private int category;
    public List<Integer> stores = new ArrayList<>();

    public ProductType(int productID, int rate, int counter_rates, String productName, String description, int category, List<Integer> stores) {
        this.productID = productID;
        this.rate = rate;
        this.counter_rates = counter_rates;
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.stores = stores;
    }

    private StampedLock lock_stores= new StampedLock(),rateLock=new StampedLock();


    public ProductType(int productID, String productName, String description,int category) {

        this.category = category;
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        logger.debug("the productID: " + productID + " received successfully");
    }

    public ProductType(String productName, String description, int category) {
        this.category = category;
        this.productName = productName;
        this.description = description;
    }

    public ProductType() {
    }

    public DResponseObj<Integer> getRate() {
        long stamp = rateLock.readLock();
        logger.debug("catch the ReadLock");
        try {
            DResponseObj<Integer> output = new DResponseObj<>();
            output.value = rate;
            return output;
        } finally {
            rateLock.unlockRead(stamp);
            logger.debug("released the ReadLock");
        }
    }

    public DResponseObj<Boolean> setRate(int r) {
        if (r < 0 || r > 10) {
            String warning = "the Rate in not between 1-10";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = rateLock.writeLock();
        logger.debug("getRate catch the WriteLock");
        try {
            rate = ((rate * counter_rates) + r) / (counter_rates + 1);
            counter_rates++;
            //db
            if (dataServices != null && dataServices.getProductTypeService() != null) {
                if (!dataServices.getProductTypeService().updateProductRate(productID, rate, counter_rates)) {
                    logger.error(String.format("failed to updated rate %d of product %s in db", rate, productName));
                }
            }
            return new DResponseObj<>(true);
        } finally {
            rateLock.unlockWrite(stamp);
            logger.debug("getRate released the ReadLock");
        }

    }

    public DResponseObj<Boolean> storeExist(int storeID) {
        if (storeID < 0) {
            String warning = "storeID #" + storeID + " is not valid.";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try {
            if (stores.contains(storeID))
                return new DResponseObj<>(true);
            return new DResponseObj<>(false);
        } finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    public DResponseObj<Boolean> removeStore(int storeID) {
        DResponseObj<Boolean> existInStore = storeExist(storeID);
        if (existInStore.errorOccurred()) return existInStore;
        if (!existInStore.getValue()) {
            logger.warn("this store number not exist in the product type list");
            return new DResponseObj<>(ErrorCode.STORENOTINTHEPRODUCTTYPE);
        }
        long stamp = lock_stores.writeLock();
        logger.debug("catch the WriteLock.");
        try {
            if (stores.remove(Integer.valueOf(storeID))) {
                //db
                if (dataServices != null && dataServices.getProductTypeService() != null) {
                    var data = this.getDataObject();
                    dataServices.getProductTypeService().updateProductType(data);
                }
                return new DResponseObj<>(true);
            }
            String warning = "the product typre can not remove this store from his list";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.STORESTAYINTHEPRODUCTTYPE);
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released the WriteLock.");
        }
    }

    public DResponseObj<List<Integer>> getStores() {
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (Integer storeID : stores) output.add(storeID);
            return new DResponseObj<>(output);
        } finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }

    }

    public DResponseObj<Boolean> addStore(int storeID) {
        DResponseObj<Boolean> checkExist = storeExist(storeID);
        if (checkExist.errorOccurred()) return checkExist;
        if (checkExist.getValue()) {
            String warning = "storeID #" + storeID + " is exist in the system, you can not use that";
            logger.warn(warning);
            return new DResponseObj<>(ErrorCode.STORENOTINTHEPRODUCTTYPE);
        }
        long stamp = lock_stores.writeLock();
        logger.debug("catch the WriteLock.");
        try {
            stores.add(storeID);
            logger.info("this storeID: " + storeID + " add this ProductType to the stores.");
            //db
            if (dataServices != null && dataServices.getProductTypeService() != null) {
                var data = this.getDataObject();
                dataServices.getProductTypeService().updateProductType(data);
            }
            return new DResponseObj<>(true);
        } finally {
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
        DResponseObj<Integer> output = new DResponseObj<>();
        output.value = productID;
        return output;
    }

    public DResponseObj<String> getProductName() {
        return new DResponseObj<>(productName);
    }

    public DResponseObj<String> getDescription() {
        return new DResponseObj<>(description);
    }

    public DResponseObj<Integer> getCategory() {
        DResponseObj<Integer> output = new DResponseObj<>();
        output.value = category;
        return output;
    }

    public DResponseObj<Boolean> containName(String name) {
        return new DResponseObj<>(productName.contains(name));
    }

    public DResponseObj<Boolean> containDesc(String desc) {
        return new DResponseObj<>(description.contains(desc));
    }

    public DataProductType getDataObject() {
        DataProductType dataProductType = new DataProductType();
        dataProductType.setProductTypeId(this.productID);
        dataProductType.setRate(this.rate);
        dataProductType.setCounter_rates(this.counter_rates);
        dataProductType.setProductName(this.productName);
        dataProductType.setDescription(this.description);
        dataProductType.setCategory(this.category);
        Set<Integer> stores = new HashSet<>(this.stores);
        dataProductType.setStores(stores);
        return dataProductType;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public ProductType fromData(DataProductType dataProductType) {
        this.productID = dataProductType.getProductTypeId();
        this.productName = dataProductType.getProductName();
        this.description = dataProductType.getDescription();
        this.category = dataProductType.getCategory();
        return this;
    }

    public static void setDataServices(DataServices dataServices) {
        ProductType.dataServices = dataServices;
    }
}
