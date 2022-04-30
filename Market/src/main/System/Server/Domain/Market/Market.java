package main.System.Server.Domain.Market;

import Stabs.StoreStab;
import Stabs.UserManagerStab;
import main.ErrorCode;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.Response.DResponseObj;

import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.UserManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class Market {
    /*************************************************fields************************************************************/
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

    /*************************************************constructors******************************************************/
    public Market(UserManager userManager) {
        this.userManager = userManager;
    }
    /*************************************************Functions*********************************************************/
    //2.2.1
    //pre: -
    //post: get info from valid store and product.
    public DResponseObj<String> getInfoProductInStore(int storeID, int productID) {
        DResponseObj<ProductType> p = getProductType(productID);

        if (p.errorOccurred()){
            DResponseObj<String> output=new DResponseObj<>();
            output.setErrorMsg(p.getErrorMsg());
            return output;
        }

        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()){
            DResponseObj<String> output=new DResponseObj<>();
            output.setErrorMsg(s.getErrorMsg());
            return output;
        }
        DResponseObj<String> str =s.getValue().getProductInStoreInfo(productID);
        return str;
    }

    //2.2.2
    //pre: -
    //post: get all the open stores that the arg is apart of their names
    public DResponseObj<List<Integer>> searchProductByName(String name) {
        if (name==null){
            logger.warn("the name is null");
            DResponseObj<List<Integer>> output=new DResponseObj<>();
            output.setErrorMsg(ErrorCode.NOTVALIDINPUT);
            return output;
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                DResponseObj<Boolean> checkIfExist=p.containName(name);
                if (!checkIfExist.errorOccurred() && checkIfExist.value) {
                    DResponseObj<Integer> pID= p.getProductID();
                    if (!pID.errorOccurred())  output.add(p.getProductID().value);
                }
            }
            return new DResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }
    //2.2.2
    //pre: -
    //post: get all the open stores that the arg is apart of their description
    public DResponseObj<List<Integer>> searchProductByDesc(String desc) {
        if (desc==null){
            logger.warn("description arrived null");
            DResponseObj<List<Integer>> output=new DResponseObj<>();
            output.setErrorMsg(ErrorCode.NOTVALIDINPUT);
            return output;
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                DResponseObj<Boolean> existInP=p.containDesc(desc);
                if (!existInP.errorOccurred() && existInP.value) {
                    DResponseObj<Integer> val= p.getProductID();
                    if (!val.errorOccurred()) output.add(val.getValue());
                }
            }
            return new DResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    //2.2.2
    //pre: -
    //post: get all the products that them rate higher or equal to the arg(arg>0)
    public DResponseObj<List<Integer>> searchProductByRate(int minRate) {
        if (minRate < 0 || minRate > 10) {
            logger.warn("rate is invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                DResponseObj<Integer> checkRate=p.getRate();
                if (!checkRate.errorOccurred() && checkRate.getValue() >= minRate) {
                    DResponseObj<Integer> getID=p.getProductID();
                    if (!getID.errorOccurred()) output.add(getID.getValue());
                }
            }
            return new DResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    //2.2.2
    //pre: -
    //post: get all the open stores that their rate higher or equal to the arg(arg>0)
    public DResponseObj<List<Integer>> searchProductByStoreRate(int rate) {
        if (rate < 0 || rate > 10) {
            logger.warn("rate is invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (Store store : stores.values()) {
                DResponseObj<Integer> getRate=store.getRate();
                if (!getRate.errorOccurred() && getRate.getValue() >= rate){
                    DResponseObj<Integer> getStoreID=store.getStoreId();
                    if (!getStoreID.errorOccurred()) output.add(getStoreID.getValue());
                }
            }
            return new DResponseObj<>(output);
        } finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    //2.2.2
    //pre: -
    //post: get all the products that them price is between min and max
    public DResponseObj<List<Integer>> searchProductByRangePrices(int productID, int min, int max) {
        if (min > max) {
            logger.warn("min bigger then max - invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }

        DResponseObj<ProductType> pt = getProductType(productID);
        if (pt.errorOccurred()) {
            return new DResponseObj<>(pt.getErrorMsg());
        }
        List<Integer> output = new ArrayList<>();
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock");
        try {
            for (Store store: stores.values()){
                DResponseObj<Double> getPrice = store.getProductPrice(productID);
                if (!getPrice.errorOccurred()) {
                    Double price=getPrice.getValue();
                    if (price != null && (price <= (double) max & price >= (double) min)) {
                        DResponseObj<Integer> getStoreID = store.getStoreId();
                        if (getStoreID.errorOccurred()) output.add(getStoreID.getValue());
                    }
                }
            }
            return new DResponseObj<>(output);
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("release the ReadLock");
        }
    }

    //2.2.2
    //pre: -
    //post: get all the products that them price is between min and max
    public DResponseObj<List<Integer>> searchProductByCategory(int category) {
        if (category<0){
            logger.warn("new categoryID is illegal");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            List<Integer> output = new ArrayList<>();
            for (ProductType p : productTypes.values()) {
                DResponseObj<Integer> cat = p.getCategory();
                DResponseObj<Integer> pID = p.getProductID();
                if (!cat.errorOccurred() && !pID.errorOccurred() && category==cat.getValue()) output.add(pID.getValue());
            }
            return new DResponseObj<>(output);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }


    //2.2.3
    //pre: user is online
    //post: add <quantity> times this product from this store
    public DResponseObj<Boolean> AddProductToShoppingBag(UUID userId, int StoreId, int ProductId, int quantity) {
        DResponseObj<Boolean> isOnline = isOnline(userId);
        if (isOnline.errorOccurred() || !isOnline.getValue()) return isOnline;
        DResponseObj<Store> s = getStore(StoreId);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        return s.getValue().isProductExistInStock(ProductId, quantity);

    }



    //2.2.5
    //pre: user is online
    //post: start process of sealing with the User
    public DResponseObj<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> order(UUID userId) {
        //////////////////////////////////////
        DResponseObj<Boolean> online=userManager.isOnline(userId);
        if (online.errorOccurred()) return new DResponseObj<>(online.getErrorMsg());
        DResponseObj<ShoppingCart> shoppingCart = userManager.getUserShoppingCart(userId);
        if (shoppingCart.errorOccurred()) return new DResponseObj<>(shoppingCart.getErrorMsg());
        DResponseObj<User> user=userManager.getOnlineUser(userId);
        if (user.errorOccurred()) return new DResponseObj<>(user.getErrorMsg());
        return new DResponseObj(purchase.order(user.getValue()));

    }

    //2.3.2
    //pre: user is Member
    //post: new Store add to the market
    public DResponseObj<Boolean> OpenNewStore(UUID userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        DResponseObj<Boolean> checkUM=userManager.isLogged(userId);
        if (checkUM.errorOccurred() || !checkUM.getValue()) return checkUM;

        Store store = new Store(name, discountPolicy, buyPolicy, founder);
        long stamp = lock_stores.writeLock();
        logger.debug("catch the WriteLock");
        try {
            stores.put(storeCounter++, store);
            userManager.addFounder(userId, store);
            logger.info("new Store join to the Market");
            return new DResponseObj<>(true);
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released the WriteLock");
        }
    }

    //2.4.1.1
    //pre: user is Owner
    //post: product that his ProductType exist in the market, exist in this store.
    public DResponseObj<Boolean> addNewProductToStore(UUID userId, int storeId, int productId, double price, int quantity) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.addNewProductToStore,productId);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;
        ProductType productType= result.getValue().item2;

        return store.addNewProduct(productType, quantity, price);
    }


    //2.4.1.2
    //pre: user is Owner
    //post: product that his ProductType  exist in the market, not exist anymore in this store.
    public DResponseObj<Boolean> deleteProductFromStore(UUID userId, int storeId, int productId) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.deleteProductFromStore,productId);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;
        ProductType productType= result.getValue().item2;

        DResponseObj<Integer> getPID = productType.getProductID();
        if (getPID.errorOccurred()) return new DResponseObj<>(getPID.getErrorMsg());
        return store.removeProduct(getPID.getValue());
    }


    //2.4.1.3
    //pre: user is Owner of the store
    //post: the price of this product in this store changed.
    public DResponseObj<Boolean> setProductPriceInStore(UUID userId, int storeId, int productId, double price) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.setProductPriceInStore,productId);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        return store.setProductPrice(productId, price);
    }

    //2.4.1.3
    //pre: user is Owner of the store
    //post: the quantity of this product in this store changed.
    public DResponseObj<Boolean> setProductQuantityInStore(UUID userId, int storeId, int productId, int quantity) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.setProductPriceInStore,productId);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        return store.setProductQuantity(productId, quantity);
    }

    //2.4.4
    //pre: the store exist in the system.
    //post: other user became to be owner on this store.
    public DResponseObj<Boolean> addNewStoreOwner(UUID userId, int storeId, String newOnerEmail) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.AddNewStoreOwner,null);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        return userManager.addNewStoreOwner(userId, store, newOnerEmail);
    }

    //2.4.6
    //pre: the store exist in the system.
    //post: other user became to be manager on this store.
    public DResponseObj<Boolean> addNewStoreManager(UUID userId, int storeId, String newMangermail) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.AddNewStoreManger,null);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        return userManager.addNewStoreManager(userId, store, newMangermail);
    }

    //2.4.7
    //pre: the store exist in the system.
    //post: other user that already manager became to be manager on this store with other permissions.
    public DResponseObj<Boolean> setManagerPermissions(UUID userId, int storeId, String mangerMail, permissionType.permissionEnum perm) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.setManagerPermissions,null);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        return userManager.setManagerPermissions(userId, store, mangerMail, perm);
    }

    //2.4.9
    //pre: the store exist in the system, the user is owner of this store.
    //post: the market move this store to the closeStores, users can not see this store again(until she will be open).
    public DResponseObj<Boolean> closeStore(UUID userId, int storeId) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.closeStore,null);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        DResponseObj<Boolean> checkCloseStore=store.closeStore();
        if (checkCloseStore.errorOccurred()) return checkCloseStore;
        if (!checkCloseStore.getValue()){
            logger.warn("Store return that can not close this store");
            return new DResponseObj<>(ErrorCode.CANNOTCLOSESTORE);
        }
        DResponseObj<Integer> getStoreID = store.getStoreId();
        if (getStoreID.errorOccurred()) return new DResponseObj<>(getStoreID.getErrorMsg());
        long stamp = lock_stores.writeLock();
        logger.debug("catch WriteLock");
        try {
            stores.remove(store);
            closeStores.put(getStoreID.getValue(), store);
            logger.info("market update that Store #" + storeId + " close");
            return new DResponseObj<>(true);
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released WriteLock");
        }
    }

    //2.4.11
    //pre: the store exist in the system.
    //post: market ask UserManager about this user with this store.
    public DResponseObj<HashMap<String,List<String>>> getStoreRoles(UUID userId, int storeId) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.getStoreRoles,null);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        return store.getStoreRoles();
    }

    //2.4.13
    //pre: the store exist in the system. the user is owner of this store.
    //post: market ask the store about that.
    public DResponseObj<List<History>> getStoreOrderHistory(UUID userId, int storeId) {
        DResponseObj<Tuple<Store,ProductType>> result = checkValid(userId,storeId,permissionType.permissionEnum.getStoreRoles,null);
        if (result.errorOccurred())  return new DResponseObj<>(result.getErrorMsg());
        Store store=result.getValue().item1;

        return store.getStoreOrderHistory();
    }


    //2.4.13 & 2.6.4 (only system manager)
    //pre: this store exist in the system.
    //post: market ask the store about that with USerEmail.
    public DResponseObj<List<History>> getUserHistoryInStore(String userID, int storeID) {
        DResponseObj<Boolean> isOnline = userManager.isOnline(UUID.fromString(userID));
        if (isOnline.errorOccurred()) return new DResponseObj<>(isOnline.getErrorMsg());
        DResponseObj<Store> store = getStore(storeID);
        if (store.errorOccurred()) return new DResponseObj<>(store.getErrorMsg());

        return store.getValue().getUserHistory(userID);
    }


    //2.2.1
    //pre: the store exist in the system.
    //post: market receive this store to the user.
    public DResponseObj<Store> getStore(int storeID){
        if (storeID<0 | storeID>=productCounter){
            logger.warn("the StoreID is illegal");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp= lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try{
            return new DResponseObj<>(stores.get(storeID));
        }
        finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    /*************************************************private methods*****************************************************/

    private DResponseObj<Tuple<Store, ProductType>> checkValid(UUID userId, int storeId, permissionType.permissionEnum permissionEnum, Integer productId) {
        DResponseObj<User> logIN=userManager.getOnlineUser(userId);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeId);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        PermissionManager permissionManager= PermissionManager.getInstance();
        DResponseObj<Boolean> hasPer = permissionManager.hasPermission(permissionEnum,logIN.getValue(),s.getValue());
        if (hasPer.errorOccurred()) return new DResponseObj<>(hasPer.getErrorMsg());
        if (!hasPer.getValue()) {
            logger.warn("this user does not have permission to "+permissionEnum.name()+"in the store #"+storeId);
            return new DResponseObj<>(ErrorCode.NOPERMISSION);
        }
        if (productId==null) return new DResponseObj<>(new Tuple(s.getValue(),null));

        DResponseObj<ProductType> p = getProductType(productId);
        if (p.errorOccurred()) return new DResponseObj<>(p.getErrorMsg());

        return new DResponseObj<>(new Tuple(s.getValue(),p.getValue()));
    }



    private DResponseObj<ProductType> getProductType(int productID) {
        if (productID<0){
            logger.warn("productID is illegal");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            ProductType p=productTypes.get(productID);
            if (p==null){
                logger.warn("the productID not exist in the system");
                return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
            }
            return new DResponseObj<>(p);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    private DResponseObj<Boolean> isOnline(UUID userId){
        DResponseObj<Boolean> online=userManager.isOnline(userId);
        if (online.errorOccurred()) return online;
        if (!online.getValue()){
            logger.warn("the user not online");
            return new DResponseObj<>(ErrorCode.NOTONLINE);
        }
        return new DResponseObj<>(true);
    }

    class Tuple<E,T>{
        E item1;
        T item2;

        public Tuple(E item1, T item2) {
            this.item1 = item1;
            this.item2 = item2;
        }
    }

    /*************************************************for testing*********************************************************/

    /* forbidden to use with this function except Test*/
    public void setForTesting(){
        userManager = new UserManagerStab();
        for (int i = 0; i < 10; i++) {
            ProductType p = new ProductType(productCounter++, "product" + i, "hello");
            p.setRate(i);
            p.setCategory(i % 3);
            productTypes.put(i, p);
        }
        for (int i = 0; i < 10; i++) {
            Store s = new StoreStab();
            s.addNewProduct(getProductType(1).getValue(), 100, 0.5);
            s.newStoreRate(i);
            stores.put(i, s);
        }
    }


}
