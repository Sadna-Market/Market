package com.example.demo.api.apiObjects.bridge;

import com.example.demo.service.*;
import com.example.demo.service.ServiceResponse.SLResponsOBJ;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.boot.autoconfigure.session.NonUniqueSessionRepositoryException;

import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class proxy implements IMarket {
    IMarket REAL=null;
    @Override
    public SLResponsOBJ<String> initMarket(String email, String Password, String phoneNumber) {
        if(REAL==null){
            return new SLResponsOBJ<>(email+" "+Password+" "+phoneNumber,-1);
        }
        return REAL.initMarket(email,Password,phoneNumber);
    }

    @Override
    public SLResponsOBJ<String> guestVisit() {
        if(REAL==null){
            return new SLResponsOBJ<>(UUID.randomUUID().toString(),-1);
        }
        return REAL.guestVisit();
    }

    @Override
    public SLResponsOBJ<Boolean> guestLeave(String guestId) {
        if(REAL==null){
            System.out.println(guestId);
            return new SLResponsOBJ<>(true,-1);
        }
        return REAL.guestLeave(guestId);
    }

    @Override
    public SLResponsOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber) {
        if(REAL==null){
            System.out.println(uuid+' '+email+" "+Password+" "+phoneNumber);
            return new SLResponsOBJ<>(true,-1);
        }
        return REAL.addNewMember(uuid,email,Password,phoneNumber);
    }

    @Override
    public SLResponsOBJ<String> login(String userid, String email, String password) {
        if(REAL==null){
            return new SLResponsOBJ<>(userid+ " "+email+" "+password);
        }
        return REAL.login(userid,email,password);
    }

    @Override
    public SLResponsOBJ<ServiceStore> getStore(int StoreID) {
        if(REAL==null){
            System.out.println(StoreID);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.getStore(StoreID);
    }

    @Override
    public SLResponsOBJ<ServiceProductStore> getInfoProductInStore(int storeID, int productID) {
        if(REAL==null){
            System.out.println(storeID);
            System.out.println(productID);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.getInfoProductInStore(storeID,productID);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByName(String productName) {
        if(REAL==null){
            System.out.println(productName);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByName(productName);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByName(List<Integer> lst, String productName) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(productName);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByName(lst,productName);
    }

    @Override
    public SLResponsOBJ<List<Integer>> filterByName(List<Integer> list, String name) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(name);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.filterByName(list,name);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByDesc(String desc) {
        if(REAL==null){
            System.out.println(desc);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByDesc(desc);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByDesc(List<Integer> lst, String desc) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(desc);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByDesc(lst,desc);
    }

    @Override
    public SLResponsOBJ<List<Integer>> filterByDesc(List<Integer> list, String desc) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(desc);

            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.filterByDesc(list,desc);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByRate(int rate) {
        if(REAL==null){
            System.out.println(rate);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByRate(rate);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByRate(List<Integer> lst, int rate) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(rate);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByRate(lst,rate);
    }

    @Override
    public SLResponsOBJ<List<Integer>> filterByRate(List<Integer> list, int minRate) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(minRate);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.filterByRate(list,minRate);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByCategory(int category) {
        if(REAL==null){
            System.out.println(category);

            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByCategory(category);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByCategory(List<Integer> lst, int category) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(category);

            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByCategory(lst,category);
    }

    @Override
    public SLResponsOBJ<List<Integer>> filterByCategory(List<Integer> list, int category) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(category);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.filterByCategory(list,category);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(int rate) {
        if(REAL==null){
            System.out.println(rate);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByStoreRate(rate);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(List<Integer> lst, int rate) {
        if(REAL==null){
            System.out.println(lst);
            System.out.println(rate);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByStoreRate(lst,rate);
    }

    @Override
    public SLResponsOBJ<List<Integer>> filterByStoreRate(List<Integer> list, int minRate) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(minRate);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.filterByStoreRate(list,minRate);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max) {
        if(REAL==null){
            System.out.println(productId);
            System.out.println(min);
            System.out.println(max);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByRangePrices(productId,min,max);
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(List<Integer> lst, int productId, int min, int max) {
        if(REAL==null) {
            System.out.println(lst);
            System.out.println(productId);
            System.out.println(max);
            System.out.println(min);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.searchProductByRangePrices(lst,productId,min,max);

    }

    @Override
    public SLResponsOBJ<List<Integer>> filterByRangePrices(List<Integer> list, int min, int max) {
        if(REAL==null){
            System.out.println(list);
            System.out.println(min);
            System.out.println(max);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.filterByRangePrices(list,min,max);
    }

    @Override
    public SLResponsOBJ<Integer> addNewProductType(String uuid, String name, String description, int category) {
        if(REAL==null){
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.addNewProductType(uuid,name,description,category);
    }

    @Override
    public SLResponsOBJ<Boolean> addProductToShoppingBag(String userId, int storeId, int productId, int quantity) {
        if(REAL==null){
            return new SLResponsOBJ<>(true,-1);
        }
        return REAL.addProductToShoppingBag(userId,storeId,productId,quantity);
    }

    @Override
    public SLResponsOBJ<ServiceShoppingCart> getShoppingCart(String userId) {
        if(REAL==null){
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.getShoppingCart(userId);
    }

    @Override
    public SLResponsOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {
        if(REAL==null){
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.removeProductFromShoppingBag(userId,storeId,productId);
    }

    @Override
    public SLResponsOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity) {
        if(REAL==null){
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.setProductQuantityShoppingBag(userId,productId,storeId,quantity);
    }

    @Override
    public SLResponsOBJ<String> orderShoppingCart(String userId, String city, String adress, int apartment, ServiceCreditCard creditCard) {
        if(REAL==null){
            return new SLResponsOBJ<>(userId+city+adress+" "+apartment,-1);
        }
        return REAL.orderShoppingCart(userId,city,adress,apartment,creditCard);
    }

    @Override
    public SLResponsOBJ<String> logout(String userId) {
        if(REAL==null){
            return new SLResponsOBJ<>(userId,-1);
        }
        return REAL.logout(userId);
    }

    @Override
    public SLResponsOBJ<Boolean> changePassword(String uuid, String email, String password, String newPassword) {
        if(REAL==null){
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.changePassword(uuid,email,password,newPassword);
    }

    @Override
    public SLResponsOBJ<Integer> openNewStore(String userId, String name, String founder, ServiceDiscountPolicy discountPolicy, ServiceBuyPolicy buyPolicy, ServiceBuyStrategy buyStrategy) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(name);
            System.out.println(founder);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.openNewStore(userId,name,founder,discountPolicy,buyPolicy ,buyStrategy);
    }

    @Override
    public SLResponsOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity) {
         if(REAL==null){
             System.out.println(userId);
             System.out.println(storeId);
             System.out.println(productId);
             System.out.println(price);
             System.out.println(price);
             return new SLResponsOBJ<>(null,-1);
         }
         return REAL.addNewProductToStore(userId,storeId,productId,price,quantity);
    }

    @Override
    public SLResponsOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(productId);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.deleteProductFromStore(userId,storeId,productId);
    }

    @Override
    public SLResponsOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(productId);
            System.out.println(price);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.setProductPriceInStore(userId,storeId,productId,price);
    }

    @Override
    public SLResponsOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(productId);
            System.out.println(storeId);
            System.out.println(quantity);
            return new SLResponsOBJ<>(null,-1);
        }
        return setProductQuantityInStore(userId,storeId,productId,quantity);
    }

    @Override
    public SLResponsOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            System.out.println(OwnerEmail);
            return new SLResponsOBJ<>(null,-1);
        }
        return addNewStoreOwner(UserId,StoreId,OwnerEmail);
    }

    @Override
    public SLResponsOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil) {
         if(REAL==null){
             System.out.println(UserId);
             System.out.println(StoreId);
             System.out.println(mangerEmil);
             return new SLResponsOBJ<>(null,-1);
         }
         return REAL.addNewStoreManger(UserId,StoreId,mangerEmil);
    }

    @Override
    public SLResponsOBJ<Boolean> setManagerPermissions(String userId, int storeId, String mangerEmil, String per, boolean onof) {
        if(REAL==null){
            System.out.println(userId);
            System.out.println(storeId);
            System.out.println(mangerEmil);
            System.out.println(per);
            System.out.println(onof);
            return new SLResponsOBJ<>(null,-1);
            }
        return REAL.setManagerPermissions(userId,storeId,mangerEmil,per,onof);

    }

    @Override
    public SLResponsOBJ<Boolean> closeStore(String UserId, int StoreId) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.closeStore(UserId,StoreId);
    }

    @Override
    public SLResponsOBJ<HashMap<String, List<String>>> getStoreRoles(String UserId, int StoreId) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.getStoreRoles(UserId,StoreId);
    }

    @Override
    public SLResponsOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.getStoreOrderHistory(UserId,StoreId);
    }

    @Override
    public SLResponsOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, String email) {
        if(REAL==null){
            System.out.println(userID);
            System.out.println(email);
            return new SLResponsOBJ<>(null,-1);
        }
        return REAL.getUserInfo(userID,email);
    }
}
