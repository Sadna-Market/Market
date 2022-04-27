package main.System.Server.Domain.Market;

import Stabs.StoreStab;
import Stabs.UserManagerStab;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;
import main.System.Server.Domain.UserModel.Response.StoreResponse;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.UserManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class Market {
    static Logger logger=Logger.getLogger(Market.class);
    Purchase purchase;
    UserManager userManager;
    ConcurrentHashMap<Integer, Store> stores = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, Store> closeStores = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ProductType> productTypes = new ConcurrentHashMap<>();
    int productCounter=1,storeCounter=1;

    /*
    if you want to use with stores - acquire the lock - lock_stores
    if you want to use with ProductType, productCounter - acquire - lock_TP
     */
    private StampedLock lock_stores= new StampedLock(), lock_TP = new StampedLock();
    private Store buyPolicy;


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

    public ProductStore getInfoProductInStore(int storeID, int productID){
        ProductType p=getProductType(productID);
        if (p==null){
            logger.warn("productID is invalid in the system.");
            return null;
        }
        Store s=getStore(storeID);
        return s.getProductInStoreInfo(productID);
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
        logger.debug("catch the ReadLock.");
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
            logger.debug("released the ReadLock.");
        }
    }

    public List<Integer> searchProductByRangePrices(int productID,int min,int max) {
        if (min>max){
            logger.warn("min bigger then max - invalid");
            return null;
        }
        List<Integer> output=new ArrayList<>();
        ProductType pt= getProductType(productID);
        if (pt==null){
            logger.warn("the productId is not exist in the system");
            return null;
        }
        List<Integer> pt_stores =pt.getStores();
        for (Integer i: pt_stores){
            Store s= getStore(i);
            Double price=s.getProductPrice(productID);
            if (price!=null &&(price<= (double) max & price>=(double) min))
                output.add(i);
        }
        return output;
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
            logger.info("new productType add to the market.");
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
    public boolean AddProductToShoppingBag(UUID userId, int StoreId, int ProductId, int quantity) {
        Store s=getStore(StoreId);
        if (s==null){
            logger.warn("the storeID is not exist in the market");
            return false;
        }
        if (s.isProductExistInStock(ProductId, quantity)) {
            if (!userManager.isOnline(userId)) {
                logger.warn("the userID is not exist in the system.");
                return false;
            }
            return userManager.getUserShoppingCart(userId).value.addNewProductToShoppingBag(ProductId, s, quantity);
        }
        return false;
    }

    public boolean order(UUID userId){
        try {
            ShoppingCart shoppingCart = userManager.getUserShoppingCart(userId).value;
            return purchase.order(shoppingCart);
        }
        catch (Exception e){
            return false;
        }

    }

    public boolean OpenNewStore(UUID userId,String name,String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        if (!userManager.isLogged(userId)){
            logger.warn("the userID does not connect");
            return false;
        }
        Store store = new Store(name,discountPolicy, buyPolicy,founder);
        long stamp = lock_stores.writeLock();
        logger.debug("OpenNewStore catch the WriteLock");
        try {
            stores.put(storeCounter++, store);
            userManager.addFounder(userId, store);
            logger.info("new Store join to the Market");
            return true;
        }
        finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("OpenNewStore released the WriteLock");
        }
    }

    public UserManager getUserManager(){
        return userManager;
    }


    public boolean addNewProductToStore(UUID userId, int storeId, int productId, double price, int quantity) {
        if (checkValid(userId,storeId,productId)){
            ProductType p= getProductType(productId);
            Store s=getStore(storeId);
            return s.addNewProduct(p,quantity,price);
        }
        return false;
    }

    public boolean deleteProductFromStore(UUID userId, int storeId, int productId) {
        if (checkValid(userId,storeId,productId)){
            ProductType p= getProductType(productId);
            Store s=getStore(storeId);
            return s.removeProduct(p.getProductID());
        }
        return false;
    }

    private boolean checkValid(UUID userid,int storeId,int productId){
        Store s=getStore(storeId);

        if (s==null){
            logger.warn("the StoreID is invalid.");
            return false;
        }
        if(userManager.isOwner(userid , s)){
            ProductType p= getProductType(productId);
            if (p==null){
                logger.warn("the storeID is invalid.");
                return false;
            }

            return true;
        }
        else{
            logger.warn("userID is not owner of the Store.");
            return false;
        }
    }

    public boolean setProductPriceInStore(UUID userId, int storeId, int productId, double price) {
        if (checkValid(userId,storeId,productId)){
            Store s=getStore(storeId);
            return s.setProductPrice(productId,price);
        }
        return false;
    }

    public boolean setProductQuantityInStore(UUID userId, int storeId, int productId, int quantity) {
        if (checkValid(userId,storeId,productId)){
            Store s=getStore(storeId);
            return s.setProductQuantity(productId, quantity);
        }
        return false;
    }



    public ATResponseObj<Boolean> addNewStoreOwner(UUID userId, int storeId, String newOnerEmail) {
        Store store = getStore(storeId);
        return userManager.addNewStoreOwner(userId,store,newOnerEmail);
    }

    public ATResponseObj<Boolean> addNewStoreManager(UUID userId, int storeId, String newMangermail) {
        Store store = getStore(storeId);
        return userManager.addNewStoreManager(userId,store,newMangermail);
    }

    public ATResponseObj<Boolean> setManagerPermissions(UUID userId, int storeId, String mangerMail, permissionType.permissionEnum perm) {
        Store store = getStore(storeId);
        return userManager.setManagerPermissions(userId,store,mangerMail,perm);
    }

    public boolean closeStore(UUID userId, int storeId) {
        Store s= getStore(storeId);
        if (s==null){
            logger.warn("this store not open.");
            return false;
        }
        if(userManager.isOwner(userId ,s)){

            if (s.closeStore()){
                long stamp = lock_stores.writeLock();
                logger.debug("catch WriteLock");
                try {
                    stores.remove(s);
                    closeStores.put(s.getStoreId(),s);
                }
                finally {
                    lock_stores.unlockWrite(stamp);
                    logger.debug("released WriteLock");
                }
                logger.info("market update that Store #"+storeId+" close");
                return true;
            }
            logger.warn("market didnt close the store,closeStore() returned false");
            return false;
        }
        logger.warn("market didnt close the store, the user is not owner.");
        return false;
    }

    public boolean getStoreRoles(int userId, int storeId) {
        Store store = getStore(storeId);
        return userManager.getRolesInStore(userId,store);
    }

    public List<History> getStoreOrderHistory(UUID userId, int storeId) {
        Store store = getStore(storeId);
        if (store==null){
            logger.warn("the storeId is invalid");
            return null;
        }

        if(userManager.isOwner(userId ,store)){
            return store.getStoreOrderHistory();
        }
        return null;
    }

    public List<History> getUserHistoryInStore(String userID,int storeID){
        Store store=getStore(storeID);
        if (store==null){
            logger.warn("this storeID not exist in the system.");
            return null;
        }
        return  stores.get(storeID).getUserHistory(userID);


    /* forbidden to use with this function except Test*/
    public void setForTesting(){
        userManager = new UserManagerStab();
        for (int i=0; i<10; i++){
            ProductType p=new ProductType(productCounter++,"product"+i,"hello");
            p.setRate(i);
            p.setCategory(i%3);
            productTypes.put(i,p);
        }
        for (int i=0; i<10; i++){
            Store s= new StoreStab();
            s.addNewProduct(getProductType(1),100,0.5);
            s.newStoreRate(i);
            stores.put(i,s);}
    }
}