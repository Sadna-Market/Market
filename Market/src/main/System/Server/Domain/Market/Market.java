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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class Market {
    static Logger logger=Logger.getLogger(Market.class);
    Purchase purchase;
    UserManager userManager;
    ConcurrentHashMap<Integer, Store> StoreHashMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ProductType> productTypes = new ConcurrentHashMap<>();
    private StampedLock lock_stores= new StampedLock(), lock_TP = new StampedLock();

    public Market(UserManager userManager) {
        this.userManager = userManager;
    }

    //search the store
    public StoreResponse GetStoreInfo(int StoreID) {
        //Stores.get(StoreID).GetStoreInfo();
        return null;
    }

    public List<ProductType> searchProductByName(String name){
        long stamp = lock_TP.readLock();
        logger.debug("searchProductByName() catch the ReadLock.");
        try{
            List<ProductType> output=new ArrayList<>();
            for (ProductType p : productTypes.values()){
                if (p.getProductName().contains(name))
                    output.add(p);
            }
            return output;
        }
        finally {
            lock_TP.unlockRead(stamp);
            logger.debug("searchProductByName() released the ReadLock.");
        }
    }

    public List<ProductType> ProductSearch(String productName, String category) {
        return null;
    }


    //todo maybe need to change the position of the func ?
    public boolean AddProductToShoppingBag(int userId, int StoreId, int ProductId, int quantity) {
        //get store
        Store s = StoreHashMap.get(StoreId);
        //check if the store has the quantity of the product in the stock
        if (s.isProductExistInStock(ProductId, quantity)) {
            User user = userManager.getUser(userId);
            Store store = StoreHashMap.get(StoreId);
            return user.getShoppingCart().addNewProductToShoppingBag(ProductId, store, quantity);
        }
        return false;
    }

    public boolean order(int userId){
        ShoppingCart shoppingCart = userManager.getUser(userId).getShoppingCart();
        return purchase.order(shoppingCart);
    }

    public boolean OpenNewStore(int userId, DiscountPolicy discountPolicy, Store.BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        Store store = new Store();
        StoreHashMap.put(store.getStoreId(),store);
        userManager.addFounder(userId,store);
        return false;
    }


    public boolean addNewProductToStore(int userId, int storeId, int productId, String productName, String categori, double price, int quantity, String description) {
        if(userManager.isOwner(userId , storeId)){
            return StoreHashMap.get(storeId).addNewProduct(productId,productName,categori,price, quantity,description);
        }
        return false;
    }

    public boolean deleteProductFromStore(int userId, int storeId, int productId) {
        if(userManager.isOwner(userId ,storeId)){
            return StoreHashMap.get(storeId).removeProduct(productId);
        }
        return false;
    }

    public boolean setProductInStore(int userId, int storeId, int productId, String productName, String category, int price, int quantity, String description) {
        if(userManager.isOwner(userId ,storeId)){
            return StoreHashMap.get(storeId).setProduct(productId,productName,category,price, quantity,description);
        }
        return false;
    }

    public boolean addNewStoreOwner(int userId, int storeId, int newOwnerId) {
        Store store = StoreHashMap.get(storeId);
        return userManager.addNewStoreOwner(userId,store,newOwnerId);
    }

    public boolean addNewStoreManager(int userId, int storeId, int newMangerId) {
        Store store = StoreHashMap.get(storeId);
        return userManager.addNewStoreManager(userId,store,newMangerId);
    }

    public boolean setManagerPermissions(int userId, int storeId, int managerId) {
        Store store = StoreHashMap.get(storeId);
        return userManager.setManagerPermissions(userId,store,managerId);
    }

    public boolean deleteStore(int userId, int storeId) {
        if(userManager.isOwner(userId ,storeId)){
            Store store = StoreHashMap.remove(storeId);
            return true;
        }
        return false;
    }

    public boolean getStoreRoles(int userId, int storeId) {
        Store store = StoreHashMap.get(storeId);
        return userManager.getRolesInStore(userId,store);
    }

    public boolean getStoreOrderHistory(int userId, int storeId) {
        if(userManager.isOwner(userId ,storeId)){
            Store store = StoreHashMap.get(storeId);
            return store.getStoreOrderHistory();
        }
        return false;
    }
}
