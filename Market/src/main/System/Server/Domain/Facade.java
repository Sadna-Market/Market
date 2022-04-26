package main.System.Server.Domain;

import main.Service.IMarket;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.BuyStrategy;
import main.System.Server.Domain.StoreModel.DiscountPolicy;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.Response.ProductResponse;
import main.System.Server.Domain.UserModel.Response.ShoppingCartResponse;
import main.System.Server.Domain.UserModel.Response.StoreResponse;
import main.System.Server.Domain.UserModel.UserManager;

import java.util.List;
import java.util.UUID;

public class Facade implements IMarket {
    UserManager userManager;
    Market market;

    @Override
    public boolean InitMarket() {
        return false;
    }


    @Override
    public UUID GuestVisit() {
        return userManager.GuestVisit();
    }

    @Override
    public boolean GuestLeave(UUID guestId) {
        return userManager.GuestLeave(guestId);
    }

    @Override
    public boolean AddNewMember(UUID uuid,String email, String Password,String phoneNumber,String CreditCared,String CreditDate) {
        return userManager.AddNewMember(uuid,email,Password,phoneNumber,CreditCared,CreditDate);
    }

    @Override
    public boolean Login(UUID userid, String email, String password) {
        return userManager.Login(userid,email,password);
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
    public boolean AddProductToShoppingBag(UUID userId,int storeId,int productId , int quantity) {
        return market.AddProductToShoppingBag(userId,storeId,productId,quantity);
    }

    @Override
    public ShoppingCartResponse GetShoppingCart(UUID userId) {
        userManager.getUserShoppingCart(userId);
        return null;
    }

    @Override
    public boolean RemoveProductFromShoppingBag(UUID userId,int storeId, int productId) {
        return userManager.getUserShoppingCart(userId).removeProductFromShoppingBag(storeId,productId);
    }

    @Override
    public boolean setProductQuantityShoppingBag(UUID userId, int productId, int storeId,int quantity) {
        return userManager.getUserShoppingCart(userId).setProductQuantity(storeId,productId,quantity);
    }

    @Override
    public boolean orderShoppingCart(UUID userId) {
        return market.order(userId);
    }

    @Override
    public boolean Logout(UUID userId) {
        return userManager.Logout(userId);
    }


    @Override
    public boolean OpenNewStore(int userId, DiscountPolicy discountPolicy, Store.BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        return market.OpenNewStore(userId,discountPolicy,buyPolicy,buyStrategy);
    }

    @Override
    public boolean AddNewProductToStore(int userId, int storeId, int productId, String productName, String categori, double price, int quantity, String description) {
      return false;
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
    public boolean SetMangerPermissions(int UserId, int StoreId, int ManagerId, permissionType.permissionEnum per) {
        return market.setManagerPermissions(UserId, StoreId, ManagerId,per);
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
