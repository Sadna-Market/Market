package main.System.Server.Domain;

import main.Service.IMarket;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.*;
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

        return userManager.GuestVisit().value;
    }

    @Override
    public boolean GuestLeave(UUID guestId) {
        return userManager.GuestLeave(guestId).value;
    }

    @Override
    public boolean AddNewMember(UUID uuid,String email, String Password,String phoneNumber,String CreditCared,String CreditDate) {
        userManager.AddNewMember(uuid,email,Password,phoneNumber,CreditCared,CreditDate);
        return false;
    }

    @Override
    public boolean Login(UUID userid, String email, String password) {
        return userManager.Login(userid,email,password).value;
    }

    @Override
    public Store getStore(int storeID) {
        return market.getStore(storeID);
    }

    @Override
    public String getInfoProductInStore(int storeID, int productID) {
      market.getInfoProductInStore(storeID,productID);
      return null;
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
        return userManager.getUserShoppingCart(userId).value.removeProductFromShoppingBag(storeId,productId);
    }

    @Override
    public boolean setProductQuantityShoppingBag(UUID userId, int productId, int storeId,int quantity) {
        return userManager.getUserShoppingCart(userId).value.setProductQuantity(storeId,productId,quantity);
    }

    @Override
    public boolean orderShoppingCart(UUID userId) {
        return market.order(userId);
    }

    @Override
    public boolean Logout(UUID userId) {
        userManager.Logout(userId);
        return false;
    }


    @Override
    public boolean OpenNewStore(UUID userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        return market.OpenNewStore(userId,name,founder,discountPolicy,buyPolicy,buyStrategy);
    }

    @Override
    public boolean AddNewProductToStore(UUID userId, int storeId, int productId, double price, int quantity) {
      return market.addNewProductToStore(userId,storeId, productId,price, quantity);
    }

    @Override
    public boolean DeleteProductFromStore(UUID userId, int storeId, int productId) {
        return market.deleteProductFromStore(userId,storeId,productId);
    }

    @Override
    public boolean setProductPriceInStore(UUID userId, int storeId, int productId, double price) {
        return market.setProductPriceInStore(userId,storeId,productId,price);
    }

    @Override
    public boolean setProductQuantityInStore(UUID userId, int storeId, int productId,  int quantity) {
        return market.setProductQuantityInStore(userId,storeId,productId,quantity);
    }

    @Override
    public boolean AddNewStoreOwner(UUID UserId, int StoreId, String OwnerEmail) {
        return market.addNewStoreOwner(UserId, StoreId, OwnerEmail).value;
    }

    @Override
    public boolean AddNewStoreManger(UUID UserId, int StoreId,  String mangerEmil) {
        return market.addNewStoreManager(UserId, StoreId, mangerEmil).value;
    }

    @Override
    public boolean SetMangerPermissions(UUID UserId, int StoreId, String mangerEmil, permissionType.permissionEnum per) {
        return market.setManagerPermissions(UserId, StoreId, mangerEmil,per).value;
    }


    @Override
    public boolean closeStore(UUID UserId, int StoreId) {
        return market.closeStore(UserId, StoreId);
    }

    @Override
    public boolean getStoreRoles(int UserId, int StoreId) {
        return market.getStoreRoles(UserId,StoreId);
    }

    @Override
    public List<History> getStoreOrderHistory(UUID UserId, int StoreId) {
        return market.getStoreOrderHistory(UserId,StoreId);
    }
}
