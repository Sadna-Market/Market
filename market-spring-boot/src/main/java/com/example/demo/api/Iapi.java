package com.example.demo.api;

import com.example.demo.api.apiObjects.apiProductType;
import com.example.demo.api.apiObjects.apiUser;
import com.example.demo.service.*;
import com.example.demo.service.ServiceResponse.SLResponsOBJ;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Iapi {

    //1.1
    public SLResponsOBJ<String> initMarket(  apiUser user) ;




    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public SLResponsOBJ<String> guestVisit();


    //2.1.2
    public SLResponsOBJ<Boolean> guestLeave(String guestId);

    //2.1.3
    public SLResponsOBJ<Boolean> addNewMember(String uuid, apiUser user);

    //2.1.4
    public SLResponsOBJ<String> login( String uuid, apiUser user);

    //2.2.1
    public SLResponsOBJ<ServiceStore> getStore(int StoreID);

    public SLResponsOBJ<ServiceProductStore> getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public SLResponsOBJ<List<Integer>> searchProductByName(String productName);
    public SLResponsOBJ<List<Integer>> searchProductByName( Map<String, Object> o, String productName);
    public SLResponsOBJ<List<Integer>> filterByName( Map<String, Object> o, String name);

    public SLResponsOBJ<List<Integer>> searchProductByDesc(String desc);
    public SLResponsOBJ<List<Integer>> searchProductByDesc(Map<String, Object> o, String desc);
    public SLResponsOBJ<List<Integer>> filterByDesc(Map<String, Object> o,String desc);

    public SLResponsOBJ<List<Integer>> searchProductByRate(int rate);
    public SLResponsOBJ<List<Integer>> searchProductByRate(Map<String, Object> o,int rate);
    public SLResponsOBJ<List<Integer>> filterByRate(Map<String, Object> o,int minRate);


    public SLResponsOBJ<List<Integer>> searchProductByCategory(int category);
    public SLResponsOBJ<List<Integer>> searchProductByCategory(Map<String, Object> o, int category);
    public SLResponsOBJ<List<Integer>> filterByCategory(Map<String, Object> o,int category);

    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(int rate);
    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(Map<String, Object> o,int rate);
    public SLResponsOBJ<List<Integer>> filterByStoreRate(Map<String, Object> o,int minRate);

    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max);
    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(Map<String, Object> o,int productId, int min, int max);
    public SLResponsOBJ<List<Integer>> filterByRangePrices(Map<String, Object> o, int min, int max);

    public SLResponsOBJ<Integer> addNewProductType(String uuid, apiProductType apt);

    //2.2.3
    public SLResponsOBJ<Boolean> addProductToShoppingBag(String userId, int storeId, int productId, int quantity);

    //2.2.4.1
    public SLResponsOBJ<ServiceShoppingCart> getShoppingCart(String userId);

    //2.2.4.2
    public SLResponsOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId);    //2.2.4.3

    public SLResponsOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity);

    //2.2.5
    public SLResponsOBJ<String> orderShoppingCart(String userId, String city, String adress, int apartment , ServiceCreditCard creditCard) ;

    //2.3.1
    public SLResponsOBJ<String> logout(String userId);

    public SLResponsOBJ<Boolean> changePassword(String uuid, String email , String password ,String newPassword) ;


    //2.3.2
    public SLResponsOBJ<Integer> openNewStore(String userId, String name, String founder, ServiceDiscountPolicy discountPolicy, ServiceBuyPolicy buyPolicy, ServiceBuyStrategy buyStrategy);


    //2.4.1.1
    public SLResponsOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity);


    //2.4.1.2
    public SLResponsOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId);

    //2.4.1.3
    public SLResponsOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price);

    public SLResponsOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity);


    //2.4.4
    public SLResponsOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail);


    //2.4.6
    public SLResponsOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil);

    //2.4.7
    public SLResponsOBJ<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per ,boolean onof);
    //2.4.9
    public SLResponsOBJ<Boolean> closeStore(String UserId, int StoreId);


    //2.4.11
    public SLResponsOBJ<HashMap<String,List<String>>> getStoreRoles(String UserId, int StoreId);



    //2.6.5 && //2.4.13
    public SLResponsOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId);

    public SLResponsOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, apiUser user);

}
