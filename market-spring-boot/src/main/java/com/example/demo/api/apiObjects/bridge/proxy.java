package com.example.demo.api.apiObjects.bridge;

import com.example.demo.Service.*;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
@Component
public class proxy implements IMarket {
    private IMarket REAL;
    @Autowired
    public proxy(@Qualifier("facade") IMarket REAL) {
        this.REAL = REAL;
    }

    @Override
    public SLResponseOBJ<Boolean> initMarket(String email, String Password, String phoneNumber,String dateOfBirth) {

        if(REAL==null){
            return new SLResponseOBJ<>(false,-1);
        }
        return REAL.initMarket(email,Password,phoneNumber,dateOfBirth);
    }

    @Override
    public SLResponseOBJ<Boolean> removeMember(String userId, String email) {
        if(REAL==null){
            return new SLResponseOBJ<>(false,-1);
        }
        return REAL.removeMember(userId, email);
    }

    @Override
    public SLResponseOBJ<String> guestVisit() {
        if(REAL==null){
            return new SLResponseOBJ<>(UUID.randomUUID().toString(),-1);
        }
        return REAL.guestVisit();
    }

    @Override
    public SLResponseOBJ<Boolean> guestLeave(String guestId) {
        if(REAL==null){
            System.out.println(guestId);
            return new SLResponseOBJ<>(true,-1);
        }
        return REAL.guestLeave(guestId);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber,String dateOfBirth) {
        if(REAL==null){
            System.out.println(uuid+' '+email+" "+Password+" "+phoneNumber);
            return new SLResponseOBJ<>(true,-1);
        }
        return REAL.addNewMember(uuid,email,Password,phoneNumber,dateOfBirth);
    }

    @Override
    public SLResponseOBJ<String> login(String userid, String email, String password) {
        if(REAL==null){
            return new SLResponseOBJ<>(userid+ " "+email+" "+password);
        }
        return REAL.login(userid,email,password);
    }

    @Override
    public SLResponseOBJ<ServiceStore> getStore(int StoreID) {
        if(REAL==null){
            System.out.println(StoreID);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getStore(StoreID);
    }

    @Override
    public SLResponseOBJ<ServiceProductStore> getInfoProductInStore(int storeID, int productID) {
        if(REAL==null){
            System.out.println(storeID);
            System.out.println(productID);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getInfoProductInStore(storeID,productID);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByName(String productName) {
        if(REAL==null){
            System.out.println(productName);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByName(productName);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByName(List<Integer> lst, String productName) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(productName);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByName(lst,productName);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByName(List<Integer> list, String name) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(name);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.filterByName(list,name);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByDesc(String desc) {
        if(REAL==null){
            System.out.println(desc);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByDesc(desc);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByDesc(List<Integer> lst, String desc) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(desc);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByDesc(lst,desc);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByDesc(List<Integer> list, String desc) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(desc);

            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.filterByDesc(list,desc);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRate(int rate) {
        if(REAL==null){
            System.out.println(rate);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByRate(rate);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRate(List<Integer> lst, int rate) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(rate);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByRate(lst,rate);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByRate(List<Integer> list, int minRate) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(minRate);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.filterByRate(list,minRate);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByCategory(int category) {
        if(REAL==null){
            System.out.println(category);

            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByCategory(category);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByCategory(List<Integer> lst, int category) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(category);

            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByCategory(lst,category);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByCategory(List<Integer> list, int category) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(category);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.filterByCategory(list,category);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(int rate) {
        if(REAL==null){
            System.out.println(rate);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByStoreRate(rate);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(List<Integer> lst, int rate) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(rate);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByStoreRate(lst,rate);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByStoreRate(List<Integer> list, int minRate) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(minRate);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.filterByStoreRate(list,minRate);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max) {
        if(REAL==null){
            System.out.println(productId);
            System.out.println(min);
            System.out.println(max);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByRangePrices(productId,min,max);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(List<Integer> lst, int productId, int min, int max) {
        if(REAL==null) {
            System.out.println(lst);
            System.out.println(productId);
            System.out.println(max);
            System.out.println(min);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.searchProductByRangePrices(lst,productId,min,max);

    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByRangePrices(List<Integer> list, int min, int max) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(min);
            System.out.println(max);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.filterByRangePrices(list,min,max);
    }

    @Override
    public SLResponseOBJ<Integer> addNewProductType(String uuid, String name, String description, int category) {
        if(REAL==null){
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.addNewProductType(uuid,name,description,category);
    }

    @Override
    public SLResponseOBJ<Boolean> addProductToShoppingBag(String userId, int storeId, int productId, int quantity) {
        if(REAL==null){
            return new SLResponseOBJ<>(true,-1);
        }
        return REAL.addProductToShoppingBag(userId,storeId,productId,quantity);
    }

    @Override
    public SLResponseOBJ<ServiceShoppingCart> getShoppingCart(String userId) {
        if(REAL==null){
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getShoppingCart(userId);
    }

    @Override
    public SLResponseOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {
        if(REAL==null){
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.removeProductFromShoppingBag(userId,storeId,productId);
    }

    @Override
    public SLResponseOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity) {
        if(REAL==null){
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.setProductQuantityShoppingBag(userId,productId,storeId,quantity);
    }

    @Override
    public SLResponseOBJ<String> orderShoppingCart(String userId, String city, String adress, int apartment, ServiceCreditCard creditCard) {
        if(REAL==null){
            return new SLResponseOBJ<>(userId+city+adress+" "+apartment,-1);
        }
        return REAL.orderShoppingCart(userId,city,adress,apartment,creditCard);
    }

    @Override
    public SLResponseOBJ<String> logout(String userId) {
        if(REAL==null){
            return new SLResponseOBJ<>(userId,-1);
        }
        return REAL.logout(userId);
    }

    @Override
    public SLResponseOBJ<Boolean> changePassword(String uuid, String email, String password, String newPassword) {
        if(REAL==null){
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.changePassword(uuid,email,password,newPassword);
    }

    @Override
    public SLResponseOBJ<Integer> openNewStore(String userId, String name, String founder, ServiceDiscountPolicy discountPolicy, ServiceBuyPolicy buyPolicy, ServiceBuyStrategy buyStrategy) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(name);
            System.out.println(founder);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.openNewStore(userId,name,founder,discountPolicy,buyPolicy ,buyStrategy);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity) {
         if(REAL==null){
             System.out.println(userId);
             System.out.println(storeId);
             System.out.println(productId);
             System.out.println(price);
             System.out.println(price);
             return new SLResponseOBJ<>(null,-1);
         }
         return REAL.addNewProductToStore(userId,storeId,productId,price,quantity);
    }

    @Override
    public SLResponseOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(productId);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.deleteProductFromStore(userId,storeId,productId);
    }

    @Override
    public SLResponseOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(productId);
            System.out.println(price);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.setProductPriceInStore(userId,storeId,productId,price);
    }

    @Override
    public SLResponseOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(productId);
            System.out.println(storeId);
            System.out.println(quantity);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.setProductQuantityInStore(userId,storeId,productId,quantity);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewBuyRule(String userId, int storeId, BuyRuleSL buyRule) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(buyRule.toString());
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.addNewBuyRule(userId,storeId,buyRule);
    }

    @Override
    public SLResponseOBJ<Boolean> removeBuyRule(String userId, int storeId, int buyRuleID) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(buyRuleID);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.removeBuyRule(userId,storeId,buyRuleID);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewDiscountRule(String userId, int storeId, DiscountRuleSL discountRule) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(discountRule.toString());
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.addNewDiscountRule(userId,storeId,discountRule);
    }

    @Override
    public SLResponseOBJ<Boolean> removeDiscountRule(String userId, int storeId, int discountRuleID) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(discountRuleID);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.removeDiscountRule(userId,storeId,discountRuleID);
    }

    @Override
    public SLResponseOBJ<Boolean> combineANDORDiscountRules(String userId, int storeId, String operator, List<Integer> rules, int category, int discount) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.combineANDORDiscountRules(userId, storeId, operator, rules, category, discount);    }

    @Override
    public SLResponseOBJ<Boolean> combineXORDiscountRules(String userId, int storeId, String decision, List<Integer> rules) {
        if (REAL == null) {
            System.out.println(userId);
            System.out.println(storeId);
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.combineXORDiscountRules(userId, storeId, decision, rules);
    }
    @Override
    public SLResponseOBJ<List<BuyRuleSL>> getBuyPolicy(String userId, int storeId) {
        if(REAL==null){

            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getBuyPolicy(userId,storeId);    }

    @Override
    public SLResponseOBJ<List<DiscountRuleSL>> getDiscountPolicy(String userId, int storeId) {

        if(REAL==null){

            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getDiscountPolicy(userId,storeId);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            System.out.println(OwnerEmail);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.addNewStoreOwner(UserId,StoreId,OwnerEmail);
    }

    @Override
    public SLResponseOBJ<Boolean> removeStoreOwner(String UserId, int StoreId, String OwnerEmail) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            System.out.println(OwnerEmail);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.removeStoreOwner(UserId,StoreId,OwnerEmail);
    }

    @Override
    public SLResponseOBJ<Boolean> removeStoreMenager(String userId, int storeId, String menagerEmail) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(menagerEmail);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.removeStoreOwner(userId,storeId,menagerEmail);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil) {
         if(REAL==null){
             System.out.println(UserId);
             System.out.println(StoreId);
             System.out.println(mangerEmil);
             return new SLResponseOBJ<>(null,-1);
         }
         return REAL.addNewStoreManger(UserId,StoreId,mangerEmil);
    }

    @Override
    public SLResponseOBJ<Integer> getRate(String uuid, int productTypeID) {
        if(REAL==null){
            System.out.println(uuid);
            System.out.println(productTypeID);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getRate(uuid, productTypeID);    }

    @Override
    public SLResponseOBJ<Boolean> setRate(String uuid, int productTypeID, int rate) {
        if(REAL==null){
            System.out.println(uuid);
            System.out.println(productTypeID);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.setRate(uuid, productTypeID,rate);
    }

    @Override
    public SLResponseOBJ<ServiceProductType> getProductTypeInfo(Integer productTypeId) {
        if(REAL==null){

            System.out.println(productTypeId);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getProductTypeInfo(productTypeId);
    }


    @Override
    public SLResponseOBJ<Boolean> setManagerPermissions(String userId, int storeId, String mangerEmil, String per, boolean onof) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(mangerEmil);
            System.out.println(per);
            System.out.println(onof);
            return new SLResponseOBJ<>(null,-1);
            }
        return REAL.setManagerPermissions(userId,storeId,mangerEmil,per,onof);

    }

    @Override
    public SLResponseOBJ<Boolean> closeStore(String UserId, int StoreId) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.closeStore(UserId,StoreId);
    }

    @Override
    public SLResponseOBJ<HashMap<String, List<String>>> getStoreRoles(String UserId, int StoreId) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getStoreRoles(UserId,StoreId);
    }

    @Override
    public SLResponseOBJ<Boolean> cancelMembership(String uuid, String cancelMemberUsername) {
        return null;
    }

    @Override
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getStoreOrderHistory(UserId,StoreId);
    }

    @Override
    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, String email) {
        if(REAL==null){
            System.out.println(userID);
            System.out.println(email);
            return new SLResponseOBJ<>(null,-1);
        }
        return REAL.getUserInfo(userID,email);
    }

    //TODO: daniel
    @Override
    public SLResponseOBJ<List<ServiceUser>> getloggedInMembers(String uuid) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.getloggedInMembers(uuid);
    }

    @Override
    public SLResponseOBJ<List<ServiceUser>> getloggedOutMembers(String uuid) {

        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.getloggedOutMembers(uuid);
    }

    @Override
    public SLResponseOBJ<List<ServiceStore>> getAllStores() {

        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.getAllStores();   }

    @Override
    public SLResponseOBJ<List<ServiceProductType>> getAllProducts() {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.getAllProducts();
    }

    @Override
    public SLResponseOBJ<List<ServiceProductStore>> getAllProductsInStore(int storeID) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.getAllProductsInStore(storeID);
    }

    @Override
    public SLResponseOBJ<Boolean> isOwnerUUID(String uuid, int storeId) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.isOwnerUUID(uuid,storeId);
    }

    @Override
    public SLResponseOBJ<Boolean> isManagerUUID(String uuid, int storeId) {

        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.isManagerUUID(uuid,storeId);
    }

    @Override
    public SLResponseOBJ<Boolean> isSystemManagerUUID(String uuid) {

        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.isSystemManagerUUID(uuid);
    }

    @Override
    public SLResponseOBJ<Boolean> createBID(String uuid, int storeID, int productID, int quantity, int totalPrice) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.createBID(uuid, storeID, productID, quantity, totalPrice);    }

    @Override
    public SLResponseOBJ<Boolean> removeBID(String uuid, int storeID, int productID) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.removeBID(uuid, storeID, productID);    }

    @Override
    public SLResponseOBJ<Boolean> approveBID(String uuid, String userEmail, int storeID, int productID) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.approveBID(uuid, userEmail, storeID, productID);
    }

    @Override
    public SLResponseOBJ<Boolean> rejectBID(String uuid, String userEmail, int storeID, int productID) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.rejectBID(uuid, userEmail, storeID, productID);    }

    @Override
    public SLResponseOBJ<Boolean> counterBID(String uuid, String userEmail, int storeID, int productID, int newTotalPrice) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.counterBID(uuid, userEmail, storeID, productID, newTotalPrice);     }

    @Override
    public SLResponseOBJ<Boolean> responseCounterBID(String uuid, int storeID, int productID, boolean approve) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.responseCounterBID(uuid, storeID, productID, approve);     }

    @Override
    public SLResponseOBJ<Boolean> BuyBID(String userId, int storeID, int productID, String city, String adress, int apartment, ServiceCreditCard creditCard) {
        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.BuyBID(userId, storeID, productID, city, adress, apartment, creditCard);
    }

//    @Override
//    public SLResponseOBJ<HashMap<String, Boolean>> getApprovesList(String uuid, String userEmail, int storeID, int productID) {
//        if(REAL==null){
//            return new SLResponseOBJ<>(null, -1);
//        }
//        return REAL.getApprovesList(uuid, userEmail, storeID, productID);
//    }
@Override
public SLResponseOBJ<String> getBIDStatus(String uuid, String userEmail, int storeID, int productID){
    if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.getBIDStatus(uuid, userEmail, storeID, productID);
}

    @Override
    public SLResponseOBJ<HashMap<Integer, List<ServiceBID>>> getAllOffersBIDS(String uuid, int storeID) {
        return null;
    }

    @Override
    public SLResponseOBJ<List<ServiceBID>> getMyBIDs(String uuid, int storeID) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> reopenStore(String uuid, int storeID) {
        return null;
    }


    public SLResponseOBJ<List<String>> getAllMembers(String userId) {

        if(REAL==null){
            return new SLResponseOBJ<>(null, -1);
        }
        return REAL.getAllMembers(userId);

    }
}
