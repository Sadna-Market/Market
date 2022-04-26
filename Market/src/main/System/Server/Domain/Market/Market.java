package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.BuyStrategy;
import main.System.Server.Domain.StoreModel.DiscountPolicy;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.Response.StoreResponse;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.UserManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Market {

    Purchase purchase;
    UserManager userManager;
    ConcurrentHashMap<Integer, Store> StoreHashMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ProductType> ProductTypeHashMap = new ConcurrentHashMap<>();


    //search the store
    public StoreResponse GetStoreInfo(int StoreID) {
        StoreHashMap.get(StoreID).GetStoreInfo();
        return null;
    }

    public List<ProductType> ProductSearch(String productName, String category) {
        return null;
    }


    //todo maybe need to change the position of the func ?
    public boolean AddProductToShoppingBag(UUID userId, int StoreId, int ProductId, int quantity) {
        //get store
        Store s = StoreHashMap.get(StoreId);
        //check if the store has the quantity of the product in the stock
        if (s.isProductExistInStock(ProductId, quantity)) {
            Store store = StoreHashMap.get(StoreId);
            return userManager.getUserShoppingCart(userId).addNewProductToShoppingBag(ProductId, store, quantity);
        }
        return false;
    }

    public boolean order(UUID userId){
        ShoppingCart shoppingCart = userManager.getUserShoppingCart(userId);
        return purchase.order(shoppingCart);
    }

    public boolean OpenNewStore(int userId, DiscountPolicy discountPolicy, Store.BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        Store store = new Store();
        return false;
    }


    public boolean addNewProductToStore(UUID userId, int storeId, int productId, String productName, String categori, double price, int quantity, String description) {

        return false;
    }

    public boolean deleteProductFromStore(int userId, int storeId, int productId) {

        return false;
    }

    public boolean setProductInStore(int userId, int storeId, int productId, String productName, String category, int price, int quantity, String description) {

        return false;
    }

    public boolean addNewStoreOwner(int userId, int storeId, int newOwnerId) {
        return false;
    }

    public boolean addNewStoreManager(int userId, int storeId, int newMangerId) {
        return false;
    }

    public boolean setManagerPermissions(int userId, int storeId, int managerId,permissionType.permissionEnum per) {
        Store store = StoreHashMap.get(storeId);
        return false;
    }

    public boolean deleteStore(int userId, int storeId) {
            Store store = StoreHashMap.remove(storeId);
            return true;

    }

    public boolean getStoreRoles(int userId, int storeId) {
        Store store = StoreHashMap.get(storeId);
        return userManager.getRolesInStore(userId,store);
    }

    public boolean getStoreOrderHistory(int userId, int storeId) {
            Store store = StoreHashMap.get(storeId);
            return store.getStoreOrderHistory();
        }
    }

