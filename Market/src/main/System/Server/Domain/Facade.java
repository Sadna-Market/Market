package main.System.Server.Domain;

import main.Service.IMarket;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.StoreModel.BuyStrategy;
import main.System.Server.Domain.StoreModel.DiscountPolicy;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.Response.ProductResponse;
import main.System.Server.Domain.UserModel.Response.ShoppingCartResponse;
import main.System.Server.Domain.UserModel.Response.StoreResponse;
import main.System.Server.Domain.UserModel.UserManager;

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
    public Store getStore(int storeID) {
        return market.getStore(storeID);
    }

    @Override
    public String getInfoProductInStore(int storeID, int productID) {
        return market.getInfoProductInStore(storeID,productID);
    }


    @Override
    public List<Integer> searchProductByName(String productName) {
        return null;
    }

    @Override
    public List<Integer> searchProductByDesc(String desc) {
        return null;
    }

    @Override
    public List<Integer> searchProductByRate(int rate) {
        return null;
    }

    @Override
    public List<Integer> searchProductByCategory(int category) {
        return null;
    }

    @Override
    public List<Integer> searchProductByStoreRate(int rate) {
        return null;
    }

    @Override
    public List<Integer> searchProductByRangePrices(int productId, int min, int max) {
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
    public boolean OpenNewStore(int userId,String name, String founder, DiscountPolicy discountPolicy, Store.BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        return market.OpenNewStore(userId,name,founder,discountPolicy,buyPolicy,buyStrategy);
    }

    @Override
    public boolean AddNewProductToStore(int userId, int storeId, int productId, double price, int quantity) {
      return market.addNewProductToStore(userId,storeId, productId,price, quantity);
    }

    @Override
    public boolean DeleteProductFromStore(int userId, int storeId, int productId) {
        return market.deleteProductFromStore(userId,storeId,productId);
    }

    @Override
    public boolean setProductPriceInStore(int userId, int storeId, int productId, double price) {
        return market.setProductPriceInStore(userId,storeId,productId,price);
    }

    @Override
    public boolean setProductQuantityInStore(int userId, int storeId, int productId,  int quantity) {
        return market.setProductQuantityInStore(userId,storeId,productId,quantity);
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
