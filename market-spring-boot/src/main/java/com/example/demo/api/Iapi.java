package com.example.demo.api;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.api.apiObjects.apiProductType;
import com.example.demo.api.apiObjects.apiStore;
import com.example.demo.api.apiObjects.apiUser;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Iapi {
    public SLResponseOBJ<Boolean> removeStoreMenager(String userId, int storeId, String menagerEmail) ;
    public SLResponseOBJ<Integer> getRate(String uuid,int productTypeID);
    public SLResponseOBJ<Boolean> setRate(String uuid,int productTypeID,int rate);
    public SLResponseOBJ<Boolean> cancelMembership(String uuid, String cancelMemberUsername) ;
    public SLResponseOBJ<Integer> getStoreRate(@PathVariable("uuid")String uuid,@PathVariable("Store") int Store) ;
    public SLResponseOBJ<Boolean> newStoreRate(@PathVariable("uuid")String uuid,@PathVariable("Store")int Store,@PathVariable("rate") int rate) ;
    public SLResponseOBJ<List<HashMap<String, Object>>> getAllusers() ;

    public SLResponseOBJ<ServiceProductType> getProductTypeInfo(Integer productTypeId);

    //1.1
    public SLResponseOBJ<Boolean> initMarket(  apiUser user) ;

    public SLResponseOBJ<Boolean> removeBuyRule(@PathVariable("uuid")String uuid,@PathVariable("storeId") int storeId,@PathVariable("buyRuleID") int buyRuleID) ;

    public SLResponseOBJ<Boolean> addNewBuyRule(@PathVariable("uuid")String uuid,@PathVariable("storeId") int storeId,@RequestBody Map<String,Object> map) ;
    public SLResponseOBJ<Boolean> addNewDiscountRule(@PathVariable("uuid")String uuid,@PathVariable("storeId") int storeId,@RequestBody Map<String,Object> map) ;


    public SLResponseOBJ<Boolean> removeNewDiscountRule(@PathVariable("uuid")String uuid,@PathVariable("storeId") int storeId,@PathVariable("buyRuleID") int buyRuleID) ;


        // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public SLResponseOBJ<String> guestVisit();


    //2.1.2
    public SLResponseOBJ<Boolean> guestLeave(String guestId);

    //2.1.3
    public SLResponseOBJ<Boolean> addNewMember(String uuid, apiUser user);

    //2.1.4
    public SLResponseOBJ<String> login( String uuid, apiUser user);

    //2.2.1
    public SLResponseOBJ<ServiceStore> getStore(@PathVariable("sid") int sid) ;

    public SLResponseOBJ<ServiceProductStore> getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public SLResponseOBJ<List<Integer>> searchProductByName(String productName);
    public SLResponseOBJ<List<Integer>> searchProductByName( Map<String, Object> o, String productName);
    public SLResponseOBJ<List<Integer>> filterByName( Map<String, Object> o, String name);

    public SLResponseOBJ<List<Integer>> searchProductByDesc(String desc);
    public SLResponseOBJ<List<Integer>> searchProductByDesc(Map<String, Object> o, String desc);
    public SLResponseOBJ<List<Integer>> filterByDesc(Map<String, Object> o,String desc);

    public SLResponseOBJ<List<Integer>> searchProductByRate(int rate);
    public SLResponseOBJ<List<Integer>> searchProductByRate(Map<String, Object> o,int rate);
    public SLResponseOBJ<List<Integer>> filterByRate(Map<String, Object> o,int minRate);


    public SLResponseOBJ<List<Integer>> searchProductByCategory(int category);
    public SLResponseOBJ<List<Integer>> searchProductByCategory(Map<String, Object> o, int category);
    public SLResponseOBJ<List<Integer>> filterByCategory(Map<String, Object> o,int category);

    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(int rate);
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(Map<String, Object> o,int rate);
    public SLResponseOBJ<List<Integer>> filterByStoreRate(Map<String, Object> o,int minRate);

    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max);
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(Map<String, Object> o,int productId, int min, int max);
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
    public SLResponseOBJ<String> orderShoppingCart(String uuid,@RequestBody apiUser apiUser) ;

    //2.3.1
    public SLResponseOBJ<String> logout(String userId);

    public SLResponseOBJ<Boolean> changePassword(@PathVariable("uuid") String uuid ,@RequestBody Map<String,Object> OBJ);


    //2.3.2
    public SLResponseOBJ<Integer> openNewStore(@PathVariable("uuid")String userId,@RequestBody apiStore apiStore) ;


    //2.4.1.1
    public SLResponseOBJ<Boolean> addNewProductToStore(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId, @PathVariable("productId") int productId,@RequestBody Map<String,Object> obj) ;


    //2.4.1.2
    public SLResponseOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId);

    //2.4.1.3
    public SLResponseOBJ<Boolean> setProductPriceInStore(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId,@PathVariable("productId") int productId,@RequestBody Map<String,Object> obj) ;

    public SLResponseOBJ<Boolean> setProductQuantityInStore(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId,@PathVariable("productId") int productId,@RequestBody Map<String,Object> obj) ;


    //2.4.4
    public SLResponseOBJ<Boolean> addNewStoreOwner(@PathVariable("uuid") String uuid, @PathVariable("uuid") int StoreId, @RequestBody Map<String,Object> obj) ;


    //2.4.6
    public SLResponseOBJ<Boolean> addNewStoreManger( String uuid, int StoreId, Map<String,Object> obj);

    public SLResponseOBJ<Boolean> removeMember(String userId,String email) ;

    //2.4.7
    public SLResponseOBJ<Boolean> setManagerPermissions( String uuid, int storeId, Map<String,Object> obj) ;

    //2.4.9
    public SLResponseOBJ<Boolean> closeStore(@PathVariable("uuid") String uuid,@PathVariable("StoreId") int StoreId) ;


    //2.4.11
    public SLResponseOBJ<HashMap<String,List<String>>> getStoreRoles(String UserId, int StoreId);

    public SLResponseOBJ<List<String>> getAllMembers(String userId);

    public SLResponseOBJ<Boolean> createBID(String uuid,  int storeID,int productID, int quantity, int totalPrice);
    public SLResponseOBJ<Boolean> removeBID(String uuid, int storeID, int productID);
    public SLResponseOBJ<Boolean> approveBID(String uuid, String userEmail, int storeID, int productID);
    public SLResponseOBJ<Boolean> rejectBID(String uuid, String userEmail, int storeID, int productID);
    public SLResponseOBJ<Boolean> counterBID(String uuid, String userEmail, int storeID, int productID, int newTotalPrice);
    public SLResponseOBJ<Boolean> responseCounterBID(String uuid, int storeID, int productID , boolean approve);
    public SLResponseOBJ<Boolean> BuyBID(@PathVariable("userId")String userId,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID,@PathVariable("city") String city, @PathVariable("adress")String adress,@PathVariable("apartment") int apartment,@RequestBody Map<String,Object> map) ;
//    public SLResponseOBJ<HashMap<String,Boolean>> getApprovesList(String uuid, String userEmail, int storeID, int productID);
public SLResponseOBJ<String> getBIDStatus(String uuid, String userEmail, int storeID, int productID);



    //2.6.5 && //2.4.13
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId);

    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, apiUser user);

    public SLResponseOBJ<List<ServiceProductStore>> getAllProductsInStore(int storeID) ;

    public SLResponseOBJ<List<ServiceProductType>> getAllProducts() ;

    public SLResponseOBJ<List<ServiceStore>> getAllStores() ;

    public SLResponseOBJ<List<ServiceUser>> getloggedOutMembers(String uuid) ;

    public SLResponseOBJ<List<ServiceUser>> getloggedInMembers(String uuid) ;
    public SLResponseOBJ<Boolean> isOwnerUUID(String uuid , int storeId);
    public SLResponseOBJ<Boolean> isManagerUUID(String uuid , int storeId);
    public SLResponseOBJ<Boolean> isSystemManagerUUID(String uuid);

    public SLResponseOBJ<Boolean> removeStoreOwner(String UserId, int StoreId, String OwnerEmail);


    public SLResponseOBJ<List<BuyRuleSL>> getBuyPolicy(@PathVariable("uuid")String uuid, @PathVariable("storeId") int storeId) ;

    public SLResponseOBJ<List<DiscountRuleSL>> getDiscountPolicy(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId) ;

    }
