package com.example.demo.api.apiObjects.bridge;

import com.example.demo.Service.*;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class proxy implements IMarket {
    IMarket REAL=new Facade();
    @Override
    public SLResponseOBJ<String> initMarket(String email, String Password, String phoneNumber) {
        if(REAL==null){
            return new SLResponseOBJ<>(email+" "+Password+" "+phoneNumber,-1);
        }
        return REAL.initMarket(email,Password,phoneNumber);
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
    public SLResponseOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber) {
        if(REAL==null){
            System.out.println(uuid+' '+email+" "+Password+" "+phoneNumber);
            return new SLResponseOBJ<>(true,-1);
        }
        return REAL.addNewMember(uuid,email,Password,phoneNumber);
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
        return setProductQuantityInStore(userId,storeId,productId,quantity);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail) {
        if(REAL==null){
            System.out.println(UserId);
            System.out.println(StoreId);
            System.out.println(OwnerEmail);
            return new SLResponseOBJ<>(null,-1);
        }
        return addNewStoreOwner(UserId,StoreId,OwnerEmail);
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
}
