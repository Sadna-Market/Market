package main.System.Server.Domain.Market;

import main.System.Server.Domain.UserComponent.Response.StoreResponse;
import main.System.Server.Domain.UserComponent.User;
import main.System.Server.Domain.UserComponent.UserManager;

import java.util.List;
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



}
