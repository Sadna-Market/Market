package com.example.demo.Service;


import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//Api of all
public interface IMarket {
    //1.1
    public SLResponseOBJ<Boolean> initMarket(String email, String Password, String phoneNumber, String dateOfBirth) ;


    public SLResponseOBJ<Boolean> removeMember(String userId,String email) ;


    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public SLResponseOBJ<String> guestVisit();


    //2.1.2
    public SLResponseOBJ<Boolean> guestLeave(String guestId);

    //2.1.3
    public SLResponseOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber,String dateOfBirth) ;

    //2.1.4
    public SLResponseOBJ<String> login(String userid, String email, String password);

    //2.2.1
    public SLResponseOBJ<ServiceStore> getStore(int StoreID);

    public SLResponseOBJ<ServiceProductStore> getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public SLResponseOBJ<List<Integer>> searchProductByName(String productName);
    public SLResponseOBJ<List<Integer>> searchProductByName(List<Integer> lst, String productName);
    public SLResponseOBJ<List<Integer>> filterByName(List<Integer> list, String name);

    public SLResponseOBJ<List<Integer>> searchProductByDesc(String desc);
    public SLResponseOBJ<List<Integer>> searchProductByDesc(List<Integer> lst, String desc);
    public SLResponseOBJ<List<Integer>> filterByDesc(List<Integer> list, String desc);

    public SLResponseOBJ<List<Integer>> searchProductByRate(int rate);
    public SLResponseOBJ<List<Integer>> searchProductByRate(List<Integer> lst, int rate);
    public SLResponseOBJ<List<Integer>> filterByRate(List<Integer> list, int minRate);


    public SLResponseOBJ<List<Integer>> searchProductByCategory(int category);
    public SLResponseOBJ<List<Integer>> searchProductByCategory(List<Integer> lst, int category);
    public SLResponseOBJ<List<Integer>> filterByCategory(List<Integer> list, int category);

    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(int rate);
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(List<Integer> lst, int rate);
    public SLResponseOBJ<List<Integer>> filterByStoreRate(List<Integer> list, int minRate);

    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max);
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(List<Integer> lst, int productId, int min, int max);
    public SLResponseOBJ<List<Integer>> filterByRangePrices(List<Integer> list, int min, int max);

    public SLResponseOBJ<Integer> addNewProductType(String uuid, String name , String description, int category);

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

    //2.4.2

    public SLResponseOBJ<Boolean> addNewBuyRule(String userId, int storeId, BuyRuleSL buyRule);
    public SLResponseOBJ<Boolean> removeBuyRule(String userId, int storeId, int buyRuleID);
    public SLResponseOBJ<Boolean> addNewDiscountRule(String userId, int storeId, DiscountRuleSL discountRule);
    public SLResponseOBJ<Boolean> removeDiscountRule(String userId, int storeId, int discountRuleID);
    public SLResponseOBJ<Boolean> combineANDORDiscountRules(String userId , int storeId,  String operator, List<Integer> rules, int category, int discount);
    public SLResponseOBJ<Boolean> combineXORDiscountRules(String userId , int storeId, String decision, List<Integer> rules);
    public SLResponseOBJ<List<BuyRuleSL>> getBuyPolicy(String userId, int storeId);
    public SLResponseOBJ<List<DiscountRuleSL>> getDiscountPolicy(String userId, int storeId);


    //2.4.4
    public SLResponseOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail);

    //2.4.5
    public SLResponseOBJ<Boolean> removeStoreOwner(String UserId, int StoreId, String OwnerEmail);
    public SLResponseOBJ<Boolean> removeStoreMenager(String userId, int storeId, String menagerEmail) ;

    //2.4.6
    public SLResponseOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil);
    public SLResponseOBJ<Integer> getRate(String uuid,int productTypeID);
    public SLResponseOBJ<Boolean> setRate(String uuid,int productTypeID,int rate);
    public SLResponseOBJ<ServiceProductType> getProductTypeInfo(Integer productTypeId);
    public SLResponseOBJ<List<HashMap<String,Object>>> getAllusers();

    public SLResponseOBJ<Integer> getStoreRate(String UUID,int Store);
    public SLResponseOBJ<Boolean> newStoreRate(String UUID,int Store,int rate);

        //2.4.7
    public SLResponseOBJ<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per , boolean onof);
    //2.4.9
    public SLResponseOBJ<Boolean> closeStore(String UserId, int StoreId);


    //2.4.11
    public SLResponseOBJ<HashMap<String,List<String>>> getStoreRoles(String UserId, int StoreId);

    //2.6.2
    SLResponseOBJ<Boolean> cancelMembership(String uuid, String cancelMemberUsername);

    //2.6.5 && //2.4.13
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId);

    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, String email);

    //2.6.6
    public SLResponseOBJ<List<ServiceUser>> getloggedInMembers(String uuid);
    public SLResponseOBJ<List<ServiceUser>> getloggedOutMembers(String uuid);

    public SLResponseOBJ<List<ServiceStore>> getAllStores();
    public SLResponseOBJ<List<ServiceProductType>> getAllProducts();
    public SLResponseOBJ<List<ServiceProductStore>> getAllProductsInStore(int storeID);
    public SLResponseOBJ<Boolean> isOwnerUUID(String uuid , int storeId);
    public SLResponseOBJ<Boolean> isManagerUUID(String uuid , int storeId);
    public SLResponseOBJ<Boolean> isSystemManagerUUID(String uuid);


    public SLResponseOBJ<Boolean> createBID(String uuid,  int storeID,int productID, int quantity, int totalPrice);
    public SLResponseOBJ<Boolean> removeBID(String uuid, int storeID, int productID);
    public SLResponseOBJ<Boolean> approveBID(String uuid, String userEmail, int storeID, int productID);
    public SLResponseOBJ<Boolean> rejectBID(String uuid, String userEmail, int storeID, int productID);
    public SLResponseOBJ<Boolean> counterBID(String uuid, String userEmail, int storeID, int productID, int newTotalPrice);
    public SLResponseOBJ<Boolean> responseCounterBID(String uuid, int storeID, int productID , boolean approve);
    public SLResponseOBJ<Boolean> BuyBID(String userId,int storeID, int productID, String city, String adress, int apartment, ServiceCreditCard creditCard);
    public SLResponseOBJ<String> getBIDStatus(String uuid, String userEmail, int storeID, int productID);
    
    public SLResponseOBJ<List<String>> getAllMembers(String userId);




 }

