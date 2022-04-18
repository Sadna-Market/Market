package main.System.Server.Domain;

import main.System.Server.Domain.Market.*;
import main.System.Server.Domain.UserComponent.Response.ProductResponse;
import main.System.Server.Domain.UserComponent.Response.ShoppingCartResponse;
import main.System.Server.Domain.UserComponent.Response.StoreResponse;
import main.System.Server.Domain.UserComponent.User;
import main.System.Server.Domain.UserComponent.UserManager;

import java.util.List;

public class Facade implements IMarket {
    UserManager userManager;
    Market market;

    @Override
    public boolean InitMarket() {
        return false;
    }


    @Override
    public boolean GuestVisit() {
        return userManager.GuestVisit();
    }

    @Override
    public boolean GuestLeave(int guestId) {
        return userManager.GuestLeave(guestId);
    }

    @Override
    public boolean AddNewMember(String email, int Password) {
        return userManager.AddNewMember(email,Password);
    }

    @Override
    public boolean Login(String email, int password) {
        return userManager.Login(email,password);
    }

    @Override
    public StoreResponse GetStoreInfo(int StoreID) {
        return market.GetStoreInfo(StoreID);
    }

    @Override
    public List<ProductResponse> ProductSearch(String productName, String category) {
        market.ProductSearch(productName,category);
        return null;
    }

    @Override
    public boolean AddProductToShoppingBag(int userId,int storeId,int productId , int quantity) {
        return market.AddProductToShoppingBag(userId,storeId,productId,quantity);
    }

    @Override
    public ShoppingCartResponse GetShoppingCart(int userId) {
        userManager.getUser(userId).getShoppingCart();
        return null;
    }

    @Override
    public boolean RemoveProductFromShoppingBag(int userId,int storeId, int productId) {
        return userManager.getUser(userId).getShoppingCart().removeProductFromShoppingBag(storeId,productId);
    }

    @Override
    public boolean setProductQuantityShoppingBag(int userId, int productId, int storeId,int quantity) {
        return userManager.getUser(userId).getShoppingCart().setProductQuantity(storeId,productId,quantity);
    }

    @Override
    public boolean orderShoppingCart(int userId) {
        return market.order(userId);
    }

    @Override
    public boolean Logout(int userId) {
        return userManager.Logout(userId);
    }


    @Override
    public boolean OpenNewStore(int userId, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        return market.OpenNewStore(userId,discountPolicy,buyPolicy,buyStrategy);
    }

    @Override
    public boolean AddNewProductToStore(int userId, int storeId, int productId, String productName, String categori, double price, int quantity, String description) {
      return market.addNewProductToStore(userId,storeId, productId, productName,categori,price, quantity, description);
    }

    @Override
    public boolean DeleteProductFromStore(int userId, int storeId, int productId) {
        return market.deleteProductFromStore(userId,storeId,productId);
    }

    @Override
    public boolean SetProductInStore(int userId, int storeId, int productId, String productName, String category, double price, int quantity, String description) {
        return market.setProductInStore(userId,storeId,productId,productName,category,productId,quantity,description);
    }

    @Override
    public boolean AddNewStoreOwner(int UserId, int StoreId, int newOwnerId) {
        return market.addNewStoreOwner(UserId, StoreId, newOwnerId);
    }

    @Override
    public boolean AddNewStoreManger(int UserId, int StoreId, int newMangerId) {
        return market.addNewStoreManager(UserId, StoreId, newMangerId);
    }

    @Override
    public boolean SetMangerPermissions(int UserId, int StoreId, int ManagerId) {
        return market.setManagerPermissions(UserId, StoreId, ManagerId);
    }


    @Override
    public boolean DeleteStore(int UserId, int StoreId) {
        return market.deleteStore(UserId, StoreId);
    }

    @Override
    public boolean getStoreRoles(int UserId, int StoreId) {
        return market.getStoreRoles(UserId,StoreId);
    }

    @Override
    public boolean getStoreOrderHistory(int UserId, int StoreId) {
        return market.getStoreOrderHistory(UserId,StoreId);
    }
}
