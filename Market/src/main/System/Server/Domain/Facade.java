package main.System.Server.Domain;

import com.sun.mail.imap.protocol.ListInfo;
import main.Service.IMarket;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;
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
        ATResponseObj<Store> store=market.getStore(storeID);
        return store.errorOccurred()? null: store.value;
    }

    @Override
    public String getInfoProductInStore(int storeID, int productID) {
      ATResponseObj<String> s=market.getInfoProductInStore(storeID,productID);
      return s.errorOccurred()? null:s.value;
    }


    @Override
    public List<Integer> searchProductByName(String productName) {
        ATResponseObj<List<Integer>> list=market.searchProductByName(productName);
        return list.errorOccurred()? null: list.value;
    }

    @Override
    public List<Integer> searchProductByDesc(String desc) {
        ATResponseObj<List<Integer>> list=market.searchProductByDesc(desc);
        return list.errorOccurred()? null: list.value;
    }

    @Override
    public List<Integer> searchProductByRate(int rate) {
        ATResponseObj<List<Integer>> list=market.searchProductByRate(rate);
        return list.errorOccurred()? null: list.value;
    }

    @Override
    public List<Integer> searchProductByCategory(int category) {
        ATResponseObj<List<Integer>> list=market.searchProductByCategory(category);
        return list.errorOccurred()? null: list.value;
    }

    @Override
    public List<Integer> searchProductByStoreRate(int rate) {
        ATResponseObj<List<Integer>> list=market.searchProductByStoreRate(rate);
        return list.errorOccurred()? null: list.value;
    }

    @Override
    public List<Integer> searchProductByRangePrices(int productId, int min, int max) {
        ATResponseObj<List<Integer>> list=market.searchProductByRangePrices(productId,min,max);
        return list.errorOccurred()? null: list.value;
    }


    @Override
    public boolean AddProductToShoppingBag(UUID userId,int storeId,int productId , int quantity) {
        ATResponseObj<Boolean> b=market.AddProductToShoppingBag(userId,storeId,productId,quantity);
        return !b.errorOccurred() && b.value;
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
        ATResponseObj<Boolean> b=market.order(userId);
        return !b.errorOccurred() && b.value;
    }

    @Override
    public boolean Logout(UUID userId) {
        userManager.Logout(userId);
        return false;
    }


    @Override
    public boolean OpenNewStore(UUID userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
        ATResponseObj<Boolean> b=market.OpenNewStore(userId,name,founder,discountPolicy,buyPolicy,buyStrategy);
        return !b.errorOccurred() && b.value;
    }

    @Override
    public boolean AddNewProductToStore(UUID userId, int storeId, int productId, double price, int quantity) {
        ATResponseObj<Boolean> b=market.addNewProductToStore(userId,storeId, productId,price, quantity);
        return b.errorOccurred() && b.value;
    }

    @Override
    public boolean DeleteProductFromStore(UUID userId, int storeId, int productId) {
        ATResponseObj<Boolean> b=market.deleteProductFromStore(userId,storeId,productId);
        return b.errorOccurred() && b.value;
    }

    @Override
    public boolean setProductPriceInStore(UUID userId, int storeId, int productId, double price) {
        ATResponseObj<Boolean> b=market.setProductPriceInStore(userId,storeId,productId,price);
        return b.errorOccurred() && b.value;
    }

    @Override
    public boolean setProductQuantityInStore(UUID userId, int storeId, int productId,  int quantity) {
        ATResponseObj<Boolean> b=market.setProductQuantityInStore(userId,storeId,productId,quantity);
        return b.errorOccurred() && b.value;
    }

    @Override
    public boolean AddNewStoreOwner(UUID UserId, int StoreId, String OwnerEmail) {
        ATResponseObj<Boolean> b=market.addNewStoreOwner(UserId, StoreId, OwnerEmail);
        return b.errorOccurred() && b.value;
    }

    @Override
    public boolean AddNewStoreManger(UUID UserId, int StoreId,  String mangerEmil) {
        ATResponseObj<Boolean> b=market.addNewStoreManager(UserId, StoreId, mangerEmil);
        return b.errorOccurred() && b.value;
    }

    @Override
    public boolean SetMangerPermissions(UUID UserId, int StoreId, String mangerEmil, permissionType.permissionEnum per) {
        ATResponseObj<Boolean> b=market.setManagerPermissions(UserId, StoreId, mangerEmil,per);
        return b.errorOccurred() && b.value;
    }


    @Override
    public boolean closeStore(UUID UserId, int StoreId) {
        ATResponseObj<Boolean> b = market.closeStore(UserId, StoreId);
        return b.errorOccurred() && b.value;
    }

    @Override
    public boolean getStoreRoles(int UserId, int StoreId) {
        ATResponseObj<Boolean> b = market.getStoreRoles(UserId,StoreId);
        return b.errorOccurred() && b.value;
    }

    @Override
    public List<History> getStoreOrderHistory(UUID UserId, int StoreId) {
        ATResponseObj<List<History>> list = market.getStoreOrderHistory(UserId,StoreId);;
        return list.errorOccurred()? null: list.value;
    }
    @Override
    public List<History> getUserHistoryInStore(String userID,int storeID){
        ATResponseObj<List<History>> list= market.getUserHistoryInStore(userID,storeID);
        return list.errorOccurred()? null: list.value;

    }
}
