package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.BuyStrategy;
import main.System.Server.Domain.StoreModel.DiscountPolicy;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.Response.StoreResponse;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.UserManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class Market {
    static Logger logger=Logger.getLogger(Market.class);
    Purchase purchase;
    UserManager userManager;
    ConcurrentHashMap<Integer, Store> stores = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ProductType> productTypes = new ConcurrentHashMap<>();
    int productCounter=1;

    /*
    if you want to use with stores - acquire the lock - lock_stores
    if you want to use with ProductType, productCounter - acquire - lock_TP
     */
    private StampedLock lock_stores= new StampedLock(), lock_TP = new StampedLock();




    public Market(UserManager userManager) {
        this.userManager = userManager;
    }


    public Store getStore(int storeID){
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

    public String getInfoProductInStore(int storeID, int productID){
        long stamp = lock_stores.readLock();
        logger.debug("getInfoProductInStore released the ReadLock");
        try {
            return stores.get(storeID).getProductInStoreInfo(productID);
        }
        catch (Exception e){
            return null;
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("getInfoProductInStore released the ReadLock");
        }
    }

    public List<Integer> searchProductByName(String name){
        long stamp = lock_TP.readLock();
        logger.debug("searchProductByName() catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            for (ProductType p : productTypes.values()){
                if (p.containName(name))
                    output.add(p.getProductID());
            }
            return output;
        }
        finally {
            lock_TP.unlockRead(stamp);
            logger.debug("searchProductByName() released the ReadLock.");
        }
    }

    public List<Integer> searchProductByDesc(String desc) {
        long stamp = lock_TP.readLock();
        logger.debug("ProductSearchByDesc() catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            for (ProductType p : productTypes.values()){
                if (p.containDesc(desc))
                    output.add(p.getProductID());
            }
            return output;
        }
        finally {
            lock_TP.unlockRead(stamp);
            logger.debug("ProductSearchByDesc() released the ReadLock.");
        }
    }

    public List<Integer> searchProductByRate(int minRate) {
        if (minRate<0 || minRate>10){
            logger.warn("args invalid");
            return null;
        }
        long stamp = lock_TP.readLock();
        logger.debug("ProductSearchByRate() catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            for (ProductType p : productTypes.values()){
                if (p.getRate()>=minRate)
                    output.add(p.getProductID());
            }
            return output;
        }
        finally {
            lock_TP.unlockRead(stamp);
            logger.debug("ProductSearchByRate() released the ReadLock.");
        }
    }

    public List<Integer> searchProductByStoreRate(int rate) {
        if (rate<0 || rate>10){
            logger.warn("args is invalid");
            return null;
        }
        long stamp = lock_stores.readLock();
        logger.debug("searchProductByStoreRate() catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            for (Store store : stores.values()){
                if (store.getRate()>=rate)
                    output.add(store.getStoreId());
            }
            return output;
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("searchProductByStoreRate() released the ReadLock.");
        }
    }

    public List<Integer> searchProductByRangePrices(int productID,int min,int max) {
        if (min>max){
            logger.warn("min bigger then max - invalid");
            return null;
        }
        long stamp = lock_stores.readLock();
        logger.debug("searchProductByRangePrices() catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            ProductType pt= productTypes.get(productID);
            if (pt==null){
                logger.warn("the productId is not exist in the system");
                return null;
            }
            else{
                List<Integer> pt_stores =pt.getStores();
                for (Integer i: pt_stores){
                    Store s= getStore(i);
                    int price=s.getProductPrice(productID);
                    if (price<=max & price>=min)
                        output.add(i);
                }
                return output;
            }
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("searchProductByRangePrices() released the ReadLock.");
        }
    }

    //post-cond: the return value is empty List and not null!
    public List<Integer> searchProductByCategory(int category) {
        long stamp = lock_TP.readLock();
        logger.debug("ProductSearchByCategory() catch the ReadLock.");
        try{
            List<Integer> output=new ArrayList<>();
            for (ProductType p : productTypes.values()){
                if (p.getCategory()==category)
                    output.add(p.getProductID());
            }
            return output;
        }
        finally {
            lock_TP.unlockRead(stamp);
            logger.debug("ProductSearchByRate() released the ReadLock.");
        }
    }

    public boolean addProductType(String name, String desc){
        if (name==null || desc==null){
            logger.warn("the args is null");
            return false;
        }
        if (name==""){
            logger.warn("the name is empty");
            return false;
        }
        long stamp = lock_TP.writeLock();
        logger.debug("addProductType() catch the WriteLock.");
        try{
            int value= productCounter++;
            productTypes.put(value,new ProductType(value,name,desc));
            return true;
        }
        finally {
            lock_TP.unlockWrite(stamp);
            logger.debug("addProductType() release the WriteLock.");
        }
    }


    public ProductType getProductType(int productID){
        long stamp= lock_TP.readLock();
        logger.debug("getProductType() catch the ReadLock.");
        try{
            return productTypes.get(productID);
        }
        finally {
            lock_TP.unlockRead(stamp);
            logger.debug("getProductType() released the ReadLock.");
        }
    }

    //todo maybe need to change the position of the func ?
    public boolean AddProductToShoppingBag(int userId, int StoreId, int ProductId, int quantity) {
        Store s = stores.get(StoreId);
        if (s==null){
            logger.warn("the storeID is not exist in the market");
            return false;
        }
        if (s.isProductExistInStock(ProductId, quantity)) {
            User user = userManager.getUser(userId);
            if (user==null) {
                logger.warn("the userID is not exist in the system.");
                return false;
            }
            return user.getShoppingCart().addNewProductToShoppingBag(ProductId, s, quantity);
        }
        return false;
    }

    public boolean order(int userId){
        ShoppingCart shoppingCart = userManager.getUser(userId).getShoppingCart();
        return purchase.order(shoppingCart);
    }

    public boolean OpenNewStore(int userId, DiscountPolicy discountPolicy, Store.BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        Store store = new Store();
        stores.put(store.getStoreId(),store);
        userManager.addFounder(userId,store);
        return false;
    }


    public boolean addNewProductToStore(int userId, int storeId, int productId, String productName, String categori, double price, int quantity, String description) {
        if(userManager.isOwner(userId , storeId)){
            return stores.get(storeId).addNewProduct(productId,productName,categori,price, quantity,description);
        }
        return false;
    }

    public boolean deleteProductFromStore(int userId, int storeId, int productId) {
        if(userManager.isOwner(userId ,storeId)){
            return stores.get(storeId).removeProduct(productId);
        }
        return false;
    }

    public boolean setProductInStore(int userId, int storeId, int productId, String productName, String category, int price, int quantity, String description) {
        if(userManager.isOwner(userId ,storeId)){
            return stores.get(storeId).setProduct(productId,productName,category,price, quantity,description);
        }
        return false;
    }

    public boolean addNewStoreOwner(int userId, int storeId, int newOwnerId) {
        Store store = stores.get(storeId);
        return userManager.addNewStoreOwner(userId,store,newOwnerId);
    }

    public boolean addNewStoreManager(int userId, int storeId, int newMangerId) {
        Store store = stores.get(storeId);
        return userManager.addNewStoreManager(userId,store,newMangerId);
    }

    public boolean setManagerPermissions(int userId, int storeId, int managerId) {
        Store store = stores.get(storeId);
        return userManager.setManagerPermissions(userId,store,managerId);
    }

    public boolean deleteStore(int userId, int storeId) {
        if(userManager.isOwner(userId ,storeId)){
            Store store = stores.remove(storeId);
            return true;
        }
        return false;
    }

    public boolean getStoreRoles(int userId, int storeId) {
        Store store = stores.get(storeId);
        return userManager.getRolesInStore(userId,store);
    }

    public boolean getStoreOrderHistory(int userId, int storeId) {
        if(userManager.isOwner(userId ,storeId)){
            Store store = stores.get(storeId);
            return store.getStoreOrderHistory();
        }
        return false;
    }
    /* forbidden to use with this function except Test*/
    public void setForTesting(){
        for (int i=0; i<10; i++){
            Store s= new Store();
            s.setRate(i);
            stores.put(i,s);}
        for (int i=0; i<10; i++){
            ProductType p=new ProductType(productCounter++,"product"+i,"hello");
            p.setRate(i);
            p.setCategory(i%3);
            productTypes.put(i,p);
        }
    }
}
