package main.Service;


import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.ServiceHistory;
import main.System.Server.Domain.ServiceShoppingCard;
import main.System.Server.Domain.ServiceStore;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;
import main.System.Server.Domain.UserModel.Response.ProductResponse;
import main.System.Server.Domain.UserModel.Response.ShoppingCartResponse;
import main.System.Server.Domain.UserModel.Response.StoreResponse;

import java.util.List;

//Api of all
public interface IMarket {
    //1.1
    public ATResponseObj<Boolean> initMarket(String email, String Password, String phoneNumber, String CreditCared, String CreditDate);

    //todo i just declare all the funcs, in the futer we will change the passing args and the return value acording to the drishot.

    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public ATResponseObj<String> guestVisit();

    //2.1.2
    public ATResponseObj<Boolean> guestLeave(String guestId);

    //2.1.3
    public ATResponseObj<Boolean> addNewMember(String uuid,String email, String Password,String phoneNumber,String CreditCared,String CreditDate);

    //2.1.4
    public ATResponseObj<Boolean> login(String userid, String email, String password);

    //2.2.1
    public ATResponseObj<ServiceStore> getStore(int StoreID);
    public ATResponseObj<String> getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public ATResponseObj<List<Integer>> searchProductByName(String productName);
    public ATResponseObj<List<Integer>> searchProductByDesc(String desc);
    public ATResponseObj<List<Integer>> searchProductByRate(int rate);
    public ATResponseObj<List<Integer>> searchProductByCategory(int category);
    public ATResponseObj<List<Integer>> searchProductByStoreRate(int rate);
    public ATResponseObj<List<Integer>> searchProductByRangePrices(int productId,int min,int max);


    //2.2.3
    public ATResponseObj<Boolean> addProductToShoppingBag(String userId,int storeId,int productId , int quantity);
    //todo in the use case we write that the func does not takes any args. but i think it mast have user id/email to identife the correct user.
    //2.2.4.1
    public ATResponseObj<ServiceShoppingCard> getShoppingCart(String userId);
    //2.2.4.2
    //todo update the id of use case in the pdf its not correct, and change the description its shopping bag !not cart!
    public ATResponseObj<Boolean> removeProductFromShoppingBag(String userId,int storeId, int productId);    //2.2.4.3
    //todo soppingbag/sopping cart??
    public ATResponseObj<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId,int quantity);
    //2.2.5
    public ATResponseObj<Boolean> orderShoppingCart(String userId);

    //2.3.1
    public ATResponseObj<Boolean> logout(String userId);


    //2.3.2
    public ATResponseObj<Boolean> openNewStore(String userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy);


    //2.4.1.1
    public ATResponseObj<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity) ;


    //2.4.1.2
    public ATResponseObj<Boolean> deleteProductFromStore(String userId, int storeId, int productId);

    //2.4.1.3
    public ATResponseObj<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price) ;
    public ATResponseObj<Boolean> setProductQuantityInStore(String userId, int storeId, int productId,  int quantity);


    //2.4.4
    public ATResponseObj<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail) ;


    //2.4.6
    public ATResponseObj<Boolean> addNewStoreManger(String UserId, int StoreId,  String mangerEmil) ;

    //2.4.7
    public ATResponseObj<Boolean> setManagerPermissions(String UserId, int StoreId, String mangerEmil, String per);

    //2.4.9
    public ATResponseObj<Boolean> closeStore(String UserId, int StoreId);


    //2.4.11
    public ATResponseObj<Boolean> getStoreRoles(String UserId, int StoreId);


    //2.6.5 && //2.4.13
    public ATResponseObj<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId) ;
    public ATResponseObj<List<ServiceHistory>> getUserHistoryInStore(String userID,int storeID);


    //todo 2.5 use case







}
