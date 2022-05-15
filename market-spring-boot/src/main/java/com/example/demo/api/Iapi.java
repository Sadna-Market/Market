package com.example.demo.api;

import com.example.demo.Service.ServiceObj.*;
import com.example.demo.api.apiObjects.apiProductType;
import com.example.demo.api.apiObjects.apiUser;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Iapi {

    //1.1
    public SLResponseOBJ<String> initMarket(apiUser user) ;




    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public SLResponseOBJ<String> guestVisit();


    //2.1.2
    public SLResponseOBJ<Boolean> guestLeave(String guestId);

    //2.1.3
    public SLResponseOBJ<Boolean> addNewMember(String uuid, apiUser user);

    //2.1.4
    public SLResponseOBJ<String> login(String uuid, apiUser user);

    //2.2.1
    public SLResponseOBJ<ServiceStore> getStore(int StoreID);

    public SLResponseOBJ<ServiceProductStore> getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public SLResponseOBJ<List<Integer>> searchProductByName(String productName);
    public SLResponseOBJ<List<Integer>> searchProductByName(Map<String, Object> o, String productName);
    public SLResponseOBJ<List<Integer>> filterByName(Map<String, Object> o, String name);

    public SLResponseOBJ<List<Integer>> searchProductByDesc(String desc);
    public SLResponseOBJ<List<Integer>> searchProductByDesc(Map<String, Object> o, String desc);
    public SLResponseOBJ<List<Integer>> filterByDesc(Map<String, Object> o, String desc);

    public SLResponseOBJ<List<Integer>> searchProductByRate(int rate);
    public SLResponseOBJ<List<Integer>> searchProductByRate(Map<String, Object> o, int rate);
    public SLResponseOBJ<List<Integer>> filterByRate(Map<String, Object> o, int minRate);


    public SLResponseOBJ<List<Integer>> searchProductByCategory(int category);
    public SLResponseOBJ<List<Integer>> searchProductByCategory(Map<String, Object> o, int category);
    public SLResponseOBJ<List<Integer>> filterByCategory(Map<String, Object> o, int category);

    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(int rate);
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(Map<String, Object> o, int rate);
    public SLResponseOBJ<List<Integer>> filterByStoreRate(Map<String, Object> o, int minRate);

    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max);
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(Map<String, Object> o, int productId, int min, int max);
    public SLResponseOBJ<List<Integer>> filterByRangePrices(Map<String, Object> o, int min, int max);

    public SLResponseOBJ<Integer> addNewProductType(String uuid, apiProductType apt);

    //2.2.3
    public SLResponseOBJ<Boolean> addProductToShoppingBag(String userId, int storeId, int productId, int quantity);

    //2.2.4.1
    public SLResponseOBJ<ServiceShoppingCart> getShoppingCart(String userId);

    //2.2.4.2
    public SLResponseOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId);    //2.2.4.3

    public SLResponseOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity);

    //2.2.5
    public SLResponseOBJ<String> orderShoppingCart(String userId, String city, String adress, int apartment , ServiceCreditCard creditCard) ;

    //2.3.1
    public SLResponseOBJ<String> logout(String userId);

    public SLResponseOBJ<Boolean> changePassword(String uuid, String email , String password , String newPassword) ;


    //2.3.2
    public SLResponseOBJ<Integer> openNewStore(String userId, String name, String founder, ServiceDiscountPolicy discountPolicy, ServiceBuyPolicy buyPolicy, ServiceBuyStrategy buyStrategy);


    //2.4.1.1
    public SLResponseOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity);


    //2.4.1.2
    public SLResponseOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId);

    //2.4.1.3
    public SLResponseOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price);

    public SLResponseOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity);


    //2.4.4
    public SLResponseOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail);


    //2.4.6
    public SLResponseOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil);

    //2.4.7
    public SLResponseOBJ<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per , boolean onof);
    //2.4.9
    public SLResponseOBJ<Boolean> closeStore(String UserId, int StoreId);


    //2.4.11
    public SLResponseOBJ<HashMap<String,List<String>>> getStoreRoles(String UserId, int StoreId);



    //2.6.5 && //2.4.13
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId);

    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, apiUser user);

}
