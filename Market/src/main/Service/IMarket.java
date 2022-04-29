package main.Service;


import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.Response.DResponseObj;

import java.util.List;

//Api of all
public interface IMarket {
    //1.1
    public DResponseObj<Boolean> initMarket(String email, String Password, String phoneNumber, String CreditCared, String CreditDate);

    //todo i just declare all the funcs, in the futer we will change the passing args and the return value acording to the drishot.

    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public DResponseObj<String> guestVisit();

    //2.1.2
    public DResponseObj<Boolean> guestLeave(String guestId);

    //2.1.3
    public DResponseObj<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber, String CreditCared, String CreditDate);

    //2.1.4
    public DResponseObj<Boolean> login(String userid, String email, String password);

    //2.2.1
    public DResponseObj<ServiceStore> getStore(int StoreID);
    public DResponseObj<String> getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public DResponseObj<List<Integer>> searchProductByName(String productName);
    public DResponseObj<List<Integer>> searchProductByDesc(String desc);
    public DResponseObj<List<Integer>> searchProductByRate(int rate);
    public DResponseObj<List<Integer>> searchProductByCategory(int category);
    public DResponseObj<List<Integer>> searchProductByStoreRate(int rate);
    public DResponseObj<List<Integer>> searchProductByRangePrices(int productId, int min, int max);


    //2.2.3
    public DResponseObj<Boolean> addProductToShoppingBag(String userId, int storeId, int productId , int quantity);
    //todo in the use case we write that the func does not takes any args. but i think it mast have user id/email to identife the correct user.
    //2.2.4.1
    public DResponseObj<ServiceShoppingCard> getShoppingCart(String userId);
    //2.2.4.2
    //todo update the id of use case in the pdf its not correct, and change the description its shopping bag !not cart!
    public DResponseObj<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId);    //2.2.4.3
    //todo soppingbag/sopping cart??
    public DResponseObj<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity);
    //2.2.5
    public DResponseObj<Boolean> orderShoppingCart(String userId);

    //2.3.1
    public DResponseObj<Boolean> logout(String userId);


    //2.3.2
    public DResponseObj<Boolean> openNewStore(String userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy);


    //2.4.1.1
    public DResponseObj<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity) ;


    //2.4.1.2
    public DResponseObj<Boolean> deleteProductFromStore(String userId, int storeId, int productId);

    //2.4.1.3
    public DResponseObj<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price) ;
    public DResponseObj<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity);


    //2.4.4
    public DResponseObj<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail) ;


    //2.4.6
    public DResponseObj<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil) ;

    //2.4.7
    public DResponseObj<Boolean> setManagerPermissions(String UserId, int StoreId, String mangerEmil, String per);

    //2.4.9
    public DResponseObj<Boolean> closeStore(String UserId, int StoreId);


    //2.4.11
    public DResponseObj<Boolean> getStoreRoles(String UserId, int StoreId);


    //2.6.5 && //2.4.13
    public DResponseObj<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId) ;
    public DResponseObj<List<ServiceHistory>> getUserHistoryInStore(String userID, int storeID);


    //todo 2.5 use case







}
