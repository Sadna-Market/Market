package com.example.demo.Service;


import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.HashMap;
import java.util.List;

//Api of all
public interface IMarket {
    //1.1
    public SLResponseOBJ<String> initMarket(String email, String Password, String phoneNumber, String dateOfBirth);




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

    public SLResponseOBJ<List<BuyRuleSL>> getBuyPolicy(String userId, int storeId);
    public SLResponseOBJ<List<DiscountRuleSL>> getDiscountPolicy(String userId, int storeId);


    //2.4.4
    public SLResponseOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail);

    //2.4.5
    public SLResponseOBJ<Boolean> removeStoreOwner(String UserId, int StoreId, String OwnerEmail);

    //2.4.6
    public SLResponseOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil);

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
}
