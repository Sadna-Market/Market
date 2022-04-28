package main.System.Server.Domain.Market;

import Stabs.StoreStab;
import Stabs.UserManagerStab;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.UserManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class Market {
    static Logger logger = Logger.getLogger(Market.class);
    Purchase purchase;
    UserManager userManager;
    ConcurrentHashMap<Integer, Store> stores = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, Store> closeStores = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ProductType> productTypes = new ConcurrentHashMap<>();
    int productCounter = 1, storeCounter = 1;

    /*
    if you want to use with stores - acquire the lock - lock_stores
    if you want to use with ProductType, productCounter - acquire - lock_TP
     */
    private StampedLock lock_stores = new StampedLock(), lock_TP = new StampedLock();


    public Market(UserManager userManager) {
        this.userManager = userManager;
    }


    public ATResponseObj<String> getInfoProductInStore(int storeID, int productID) {
        ATResponseObj<ProductType> p = getProductType(productID);

        if (p.errorOccurred()){
            ATResponseObj<String> output=new ATResponseObj<>();
            output.setErrorMsg(p.getErrorMsg());
            return output;
        }

        ATResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()){
            ATResponseObj<String> output=new ATResponseObj<>();
            output.setErrorMsg(s.getErrorMsg());
            return output;
        }
        return s.getValue().getProductInStoreInfo(productID);
    }

    public ATResponseObj<List<Integer>> searchProductByName(String name) {
        if (name==null){
            String warning="name arrived null";
            logger.warn(warning);
            ATResponseObj<List<Integer>> output=new ATResponseObj<>();
            output.setErrorMsg(warning);
            return output;
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                if (p.containName(name))
                    output.add(p.getProductID());
            }
            return new ATResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    public ATResponseObj<List<Integer>> searchProductByDesc(String desc) {
        if (desc==null){
            String warning="description arrived null";
            logger.warn(warning);
            ATResponseObj<List<Integer>> output=new ATResponseObj<>();
            output.setErrorMsg(warning);
            return output;
        }
        long stamp = lock_TP.readLock();
        logger.debug("ProductSearchByDesc() catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                if (p.containDesc(desc))
                    output.add(p.getProductID());
            }
            return new ATResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("ProductSearchByDesc() released the ReadLock.");
        }
    }

    public ATResponseObj<List<Integer>> searchProductByRate(int minRate) {
        if (minRate < 0 || minRate > 10) {
            String warning="args invalid";
            logger.warn(warning);
            return new ATResponseObj<>(warning);
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                if (p.getRate() >= minRate)
                    output.add(p.getProductID());
            }
            return new ATResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    public ATResponseObj<List<Integer>> searchProductByStoreRate(int rate) {
        if (rate < 0 || rate > 10) {
            String warning="args invalid";
            logger.warn(warning);
            return new ATResponseObj<>(warning);
        }
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (Store store : stores.values()) {
                if (store.getRate() >= rate)
                    output.add(store.getStoreId());
            }
            return new ATResponseObj<>(output);
        } finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    public ATResponseObj<List<Integer>> searchProductByRangePrices(int productID, int min, int max) {
        if (min > max) {
            String warning="min bigger then max - invalid";
            logger.warn(warning);
            return new ATResponseObj<>(warning);
        }

        ATResponseObj<ProductType> pt = getProductType(productID);
        if (pt.errorOccurred()) {
            return new ATResponseObj<>(pt.getErrorMsg());
        }
        List<Integer> output = new ArrayList<>();
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock");
        try {
            for (Store s: stores.values()){
                Double price = s.getProductPrice(productID);
                if (price != null && (price <= (double) max & price >= (double) min))
                    output.add(s.getStoreId());
            }
            return new ATResponseObj<>(output);
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("release the ReadLock");
        }
    }

    //post-cond: the return value is empty List and not null!
    public ATResponseObj<List<Integer>> searchProductByCategory(int category) {
        if (category<0){
            String warning = "categoryID illegal";
            logger.warn(warning);
            return new ATResponseObj<>(warning);
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                if (p.getCategory() == category)
                    output.add(p.getProductID());
            }
            return new ATResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }


    private ATResponseObj<ProductType> getProductType(int productID) {
        if (productID<0){
            String warning= "productID is illegal";
            logger.warn(warning);
            return new ATResponseObj<>(warning);
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            return new ATResponseObj<>(productTypes.get(productID));
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    //todo maybe need to change the position of the func ?
    public ATResponseObj<Boolean> AddProductToShoppingBag(UUID userId, int StoreId, int ProductId, int quantity) {
        if (!userManager.isOnline(userId)){
            String warning= "the UID is illegal";
            logger.warn(warning);
            return new ATResponseObj<>(warning);
        }
        ATResponseObj<Store> s = getStore(StoreId);
        if (s.errorOccurred()) return new ATResponseObj<>(s.getErrorMsg());

        if (s.value.isProductExistInStock(ProductId, quantity)) {
            return new ATResponseObj<>(userManager.getUserShoppingCart(userId).value.addNewProductToShoppingBag(ProductId, s.value, quantity));
        }
        return new ATResponseObj<>(false);
    }

    public ATResponseObj<Boolean> order(UUID userId) {
        if (!userManager.isOnline(userId)){
            String warning= "The UID does not connect online";
            logger.warn(warning);
            return new ATResponseObj<>(warning);
        }
        ATResponseObj<ShoppingCart> shoppingCart = userManager.getUserShoppingCart(userId);
        if (shoppingCart.errorOccurred()) return new ATResponseObj<>(shoppingCart.getErrorMsg());
        return new ATResponseObj<>(purchase.order(shoppingCart.value));


    }

    public ATResponseObj<Boolean> OpenNewStore(UUID userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        if (!userManager.isLogged(userId)) {
            logger.warn("the userID does not connect");
            return false;
        }
        Store store = new Store(name, discountPolicy, buyPolicy, founder);
        long stamp = lock_stores.writeLock();
        logger.debug("OpenNewStore catch the WriteLock");
        try {
            stores.put(storeCounter++, store);
            userManager.addFounder(userId, store);
            logger.info("new Store join to the Market");
            return true;
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("OpenNewStore released the WriteLock");
        }
    }

    public UserManager getUserManager() {
        return userManager;
    }


    public ATResponseObj<Boolean> addNewProductToStore(UUID userId, int storeId, int productId, double price, int quantity) {
        if (checkValid(userId, storeId, productId)) {
            ProductType p = getProductType(productId);
            Store s = getStore(storeId);
            return s.addNewProduct(p, quantity, price);
        }
        return false;
    }

    public ATResponseObj<Boolean> deleteProductFromStore(UUID userId, int storeId, int productId) {
        if (checkValid(userId, storeId, productId)) {
            ProductType p = getProductType(productId);
            Store s = getStore(storeId);
            return s.removeProduct(p.getProductID());
        }
        return false;
    }

    private boolean checkValid(UUID userid, int storeId, int productId) {
        Store s = getStore(storeId);

        if (s == null) {
            logger.warn("the StoreID is invalid.");
            return false;
        }
        if (userManager.isOwner(userid, s)) {
            ProductType p = getProductType(productId);
            if (p == null) {
                logger.warn("the storeID is invalid.");
                return false;
            }

            return true;
        } else {
            logger.warn("userID is not owner of the Store.");
            return false;
        }
    }

    public ATResponseObj<Boolean> setProductPriceInStore(UUID userId, int storeId, int productId, double price) {
        if (checkValid(userId, storeId, productId)) {
            Store s = getStore(storeId);
            return s.setProductPrice(productId, price);
        }
        return false;
    }

    public ATResponseObj<Boolean> setProductQuantityInStore(UUID userId, int storeId, int productId, int quantity) {
        if (checkValid(userId, storeId, productId)) {
            Store s = getStore(storeId);
            return s.setProductQuantity(productId, quantity);
        }
        return false;
    }


    public ATResponseObj<Boolean> addNewStoreOwner(UUID userId, int storeId, String newOnerEmail) {
        Store store = getStore(storeId);
        return userManager.addNewStoreOwner(userId, store, newOnerEmail);
    }

    public ATResponseObj<Boolean> addNewStoreManager(UUID userId, int storeId, String newMangermail) {
        Store store = getStore(storeId);
        return userManager.addNewStoreManager(userId, store, newMangermail);
    }

    public ATResponseObj<Boolean> setManagerPermissions(UUID userId, int storeId, String mangerMail, permissionType.permissionEnum perm) {
        Store store = getStore(storeId);
        return userManager.setManagerPermissions(userId, store, mangerMail, perm);
    }

    public ATResponseObj<Boolean> closeStore(UUID userId, int storeId) {
        Store s = getStore(storeId);
        if (s == null) {
            logger.warn("this store not open.");
            return false;
        }
        if (userManager.isOwner(userId, s)) {

            if (s.closeStore()) {
                long stamp = lock_stores.writeLock();
                logger.debug("catch WriteLock");
                try {
                    stores.remove(s);
                    closeStores.put(s.getStoreId(), s);
                } finally {
                    lock_stores.unlockWrite(stamp);
                    logger.debug("released WriteLock");
                }
                logger.info("market update that Store #" + storeId + " close");
                return true;
            }
            logger.warn("market didnt close the store,closeStore() returned false");
            return false;
        }
        logger.warn("market didnt close the store, the user is not owner.");
        return false;
    }

    public ATResponseObj<Boolean> getStoreRoles(int userId, int storeId) {
        Store store = getStore(storeId);
        return userManager.getRolesInStore(userId, store);
    }

    public ATResponseObj<List<History>> getStoreOrderHistory(UUID userId, int storeId) {
        Store store = getStore(storeId);
        if (store == null) {
            logger.warn("the storeId is invalid");
            return null;
        }

        if (userManager.isOwner(userId, store)) {
            return store.getStoreOrderHistory();
        }
        return null;
    }

    public ATResponseObj<List<History>> getUserHistoryInStore(String userID, int storeID) {
        Store store = getStore(storeID);
        if (store == null) {
            logger.warn("this storeID not exist in the system.");
            return null;
        }
        return stores.get(storeID).getUserHistory(userID);


        /* forbidden to use with this function except Test*/
        public void setForTesting () {
            userManager = new UserManagerStab();
            for (int i = 0; i < 10; i++) {
                ProductType p = new ProductType(productCounter++, "product" + i, "hello");
                p.setRate(i);
                p.setCategory(i % 3);
                productTypes.put(i, p);
            }
            for (int i = 0; i < 10; i++) {
                Store s = new StoreStab();
                s.addNewProduct(getProductType(1), 100, 0.5);
                s.newStoreRate(i);
                stores.put(i, s);
            }
        }
    }

    public ATResponseObj<Store> getStore(int storeID){
        long stamp= lock_stores.readLock();
        logger.debug("getStore() catch the ReadLock.");
        try{
            return stores.get(storeID);
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("getStore() released the ReadLock.");
        }
    }
}