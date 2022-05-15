package com.example.demo.api;

import com.example.demo.Service.ServiceObj.*;
import com.example.demo.api.apiObjects.apiProductType;
import com.example.demo.api.apiObjects.apiUser;
import com.example.demo.api.apiObjects.bridge.proxy;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api")
public class api implements Iapi {
    proxy iMarket = new proxy();

    @Override
    @GetMapping("initMarket")
    public SLResponseOBJ<String> initMarket(@RequestBody apiUser user) {
        return iMarket.initMarket(user.email, user.Password, user.phoneNumber);
    }

    @Override
    @GetMapping("guestVisit")
    public SLResponseOBJ<String> guestVisit() {
        return iMarket.guestVisit();
    }

    @Override
    @GetMapping("guestLeve/{uuid}")
    public SLResponseOBJ<Boolean> guestLeave(@PathVariable String uuid) {
        return iMarket.guestLeave(uuid);
    }

    @Override
    @PostMapping("addNewMember/{uuid}")
    public SLResponseOBJ<Boolean> addNewMember(@PathVariable("uuid") String uuid, @RequestBody apiUser user) {
        return iMarket.addNewMember(uuid, user.email, user.Password, user.phoneNumber);
    }

    @Override
    @PostMapping("login/{uuid}")
    public SLResponseOBJ<String> login(@PathVariable("uuid") String uuid, @RequestBody apiUser user) {
        return iMarket.login(uuid, user.email, user.Password);
    }

    @Override
    @GetMapping("storeid/{sid}")
    public SLResponseOBJ<ServiceStore> getStore(@PathVariable int sid) {
        return iMarket.getStore(sid);
    }

    @Override
    @GetMapping("getInfoProductInStore/{StoreID}/{productID}")
    public SLResponseOBJ<ServiceProductStore> getInfoProductInStore(@PathVariable("StoreID") int storeID, @PathVariable("productID") int productID) {
        return iMarket.getInfoProductInStore(storeID , productID);
    }

    @Override
    @GetMapping("searchProductByName/{productName}")
    public SLResponseOBJ<List<Integer>> searchProductByName(@PathVariable("productName") String productName) {
        return iMarket.searchProductByName(productName);
    }

    @Override
    @GetMapping("searchProductByName/List/{productName}")
    public SLResponseOBJ<List<Integer>> searchProductByName(@RequestBody Map<String, Object> o, @PathVariable("productName") String productName) {
        return iMarket.searchProductByName((List<Integer>)(o.get("lst")),productName );
    }

    @Override
    @GetMapping("filterByName/{productName}")
    public SLResponseOBJ<List<Integer>> filterByName(@RequestBody Map<String, Object> o, @PathVariable("productName") String productName) {
        return iMarket.searchProductByName(((List<Integer>)(o.get("lst"))),productName );
    }

    @Override
    @GetMapping("searchProductByDesc/{desc}")
    public SLResponseOBJ<List<Integer>> searchProductByDesc(@PathVariable("desc") String desc) {
        return iMarket.searchProductByDesc(desc);
    }

    @Override
    @GetMapping("searchProductByDesc/list/{desc}")
    public SLResponseOBJ<List<Integer>> searchProductByDesc(@RequestBody Map<String, Object> o, @PathVariable("desc") String desc) {
        return iMarket.searchProductByDesc((List<Integer>)(o.get("lst")),desc);
    }

    @Override
    @GetMapping("filterByDesc/{desc}")
    public SLResponseOBJ<List<Integer>> filterByDesc(@RequestBody Map<String, Object> o, @PathVariable("desc") String desc) {
        return iMarket.filterByDesc((List<Integer>)(o.get("lst")),desc);
    }

    @Override
    @GetMapping("searchProductByRate/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByRate(@PathVariable("rate") int rate) {
        return iMarket.searchProductByRate(rate);
    }

    @Override
    @GetMapping("searchProductByRate/list/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByRate(@RequestBody Map<String, Object> o, @PathVariable("rate") int rate) {
        return iMarket.searchProductByRate((List<Integer>)(o.get("lst")),rate);
    }

    @Override
    @GetMapping("filterByRate/{minRate}")
    public SLResponseOBJ<List<Integer>> filterByRate(@RequestBody Map<String, Object> o, @PathVariable("minRate") int minRate) {
        return iMarket.filterByRate((List<Integer>)(o.get("lst")),minRate);
    }

    @Override
    @GetMapping("searchProductByCategory/{category}")
    public SLResponseOBJ<List<Integer>> searchProductByCategory(@PathVariable("category") int category) {
        return iMarket.searchProductByCategory(category);
    }

    @Override
    @GetMapping("searchProductByCategory/list/{category}")
    public SLResponseOBJ<List<Integer>> searchProductByCategory(@RequestBody Map<String, Object> o, @PathVariable("category") int category) {
        return iMarket.searchProductByCategory((List<Integer>)(o.get("lst")),category);
    }

    @Override
    @GetMapping("filterByCategory/{category}")
    public SLResponseOBJ<List<Integer>> filterByCategory(@RequestBody Map<String, Object> o, @PathVariable("category") int category) {
        return  iMarket.searchProductByCategory((List<Integer>)(o.get("lst")),category);
    }

    @Override
    @GetMapping("searchProductByStoreRate/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(@PathVariable("rate") int rate) {
        return iMarket.searchProductByStoreRate(rate);
    }

    @Override
    @GetMapping("searchProductByStoreRate/list/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(@RequestBody Map<String, Object> o, @PathVariable("rate") int rate) {
        return iMarket.searchProductByStoreRate((List<Integer>)(o.get("lst")),rate);
    }

    @Override
    @GetMapping("filterByStoreRate/{minRate}")
    public SLResponseOBJ<List<Integer>> filterByStoreRate(@RequestBody Map<String, Object> o, @PathVariable("minRate") int minRate) {
        return iMarket.filterByStoreRate((List<Integer>)(o.get("lst")),minRate);
    }

    @Override
    @GetMapping("searchProductByRangePrices/{productId}/{min}/{max}")
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(@PathVariable("productId") int productId, @PathVariable("min") int min, @PathVariable("max") int max) {
        return iMarket.searchProductByRangePrices(productId,min,max);
    }

    @Override
    @GetMapping("searchProductByRangePrices/list/{productId}/{min}/{max}")
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(@RequestBody Map<String, Object> o, @PathVariable("productId") int productId, @PathVariable("min") int min, @PathVariable("max") int max) {
        return iMarket.searchProductByRangePrices((List<Integer>)(o.get("lst")),productId,min,max);
    }

    @Override
    @GetMapping("filterByRangePrices/{min}/{max}")
    public SLResponseOBJ<List<Integer>> filterByRangePrices(@RequestBody Map<String, Object> o, @PathVariable("min")int min, @PathVariable("max") int max) {
        return iMarket.filterByRangePrices((List<Integer>)(o.get("lst")),min,max);
    }

    @Override
    @GetMapping("addNewProductType/{uuid}")
    public SLResponseOBJ<Integer> addNewProductType(@PathVariable("uuid") String uuid, @RequestBody apiProductType apt) {
        return iMarket.addNewProductType(uuid,apt.name,apt.description,apt.category);
    }

    @Override
    @GetMapping("addProductToShoppingBag/{uuid}")
    public SLResponseOBJ<Boolean> addProductToShoppingBag(String uuid, int storeId, int productId, int quantity) {
        return null;
    }

    @Override
    public SLResponseOBJ<ServiceShoppingCart> getShoppingCart(String userId) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity) {
        return null;
    }

    @Override
    public SLResponseOBJ<String> orderShoppingCart(String userId, String city, String adress, int apartment, ServiceCreditCard creditCard) {
        return null;
    }

    @Override
    public SLResponseOBJ<String> logout(String userId) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> changePassword(String uuid, String email, String password, String newPassword) {
        return null;
    }

    @Override
    public SLResponseOBJ<Integer> openNewStore(String userId, String name, String founder, ServiceDiscountPolicy discountPolicy, ServiceBuyPolicy buyPolicy, ServiceBuyStrategy buyStrategy) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> setManagerPermissions(String userId, int storeId, String mangerEmil, String per, boolean onof) {
        return null;
    }

    @Override
    public SLResponseOBJ<Boolean> closeStore(String UserId, int StoreId) {
        return null;
    }

    @Override
    public SLResponseOBJ<HashMap<String, List<String>>> getStoreRoles(String UserId, int StoreId) {
        return null;
    }

    @Override
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId) {
        return null;
    }

    @Override
    @GetMapping("getUserInfo/{userID}")
    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(@PathVariable("userID") String userID, @RequestBody apiUser user) {
        return iMarket.getUserInfo(userID,user.email);
    }


}