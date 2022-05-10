package com.example.demo.api;

import com.example.demo.api.apiObjects.apiProductType;
import com.example.demo.api.apiObjects.apiUser;
import com.example.demo.api.apiObjects.bridge.proxy;
import com.example.demo.service.*;
import com.example.demo.service.ServiceResponse.SLResponsOBJ;
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
    public SLResponsOBJ<String> initMarket(@RequestBody apiUser user) {
        return iMarket.initMarket(user.email, user.Password, user.phoneNumber);
    }

    @Override
    @GetMapping("guestVisit")
    public SLResponsOBJ<String> guestVisit() {
        return iMarket.guestVisit();
    }

    @Override
    @GetMapping("guestLeve/{uuid}")
    public SLResponsOBJ<Boolean> guestLeave(@PathVariable String uuid) {
        return iMarket.guestLeave(uuid);
    }

    @Override
    @PostMapping("addNewMember/{uuid}")
    public SLResponsOBJ<Boolean> addNewMember(@PathVariable("uuid") String uuid, @RequestBody apiUser user) {
        return iMarket.addNewMember(uuid, user.email, user.Password, user.phoneNumber);
    }

    @Override
    @PostMapping("login/{uuid}")
    public SLResponsOBJ<String> login(@PathVariable("uuid") String uuid, @RequestBody apiUser user) {
        return iMarket.login(uuid, user.email, user.Password);
    }

    @Override
    @GetMapping("storeid/{sid}")
    public SLResponsOBJ<ServiceStore> getStore(@PathVariable int sid) {
        return iMarket.getStore(sid);
    }

    @Override
    @GetMapping("getInfoProductInStore/{StoreID}/{productID}")
    public SLResponsOBJ<ServiceProductStore> getInfoProductInStore(@PathVariable("StoreID") int storeID, @PathVariable("productID") int productID) {
        return iMarket.getInfoProductInStore(storeID , productID);
    }

    @Override
    @GetMapping("searchProductByName/{productName}")
    public SLResponsOBJ<List<Integer>> searchProductByName(@PathVariable("productName") String productName) {
        return iMarket.searchProductByName(productName);
    }

    @Override
    @GetMapping("searchProductByName/List/{productName}")
    public SLResponsOBJ<List<Integer>> searchProductByName(@RequestBody Map<String, Object> o, @PathVariable("productName") String productName) {
        return iMarket.searchProductByName((List<Integer>)(o.get("lst")),productName );
    }

    @Override
    @GetMapping("filterByName/{productName}")
    public SLResponsOBJ<List<Integer>> filterByName(@RequestBody Map<String, Object> o,@PathVariable("productName") String productName) {
        return iMarket.searchProductByName(((List<Integer>)(o.get("lst"))),productName );
    }

    @Override
    @GetMapping("searchProductByDesc/{desc}")
    public SLResponsOBJ<List<Integer>> searchProductByDesc(@PathVariable("desc") String desc) {
        return iMarket.searchProductByDesc(desc);
    }

    @Override
    @GetMapping("searchProductByDesc/list/{desc}")
    public SLResponsOBJ<List<Integer>> searchProductByDesc(@RequestBody Map<String, Object> o, @PathVariable("desc") String desc) {
        return iMarket.searchProductByDesc((List<Integer>)(o.get("lst")),desc);
    }

    @Override
    @GetMapping("filterByDesc/{desc}")
    public SLResponsOBJ<List<Integer>> filterByDesc(@RequestBody Map<String, Object> o, @PathVariable("desc") String desc) {
        return iMarket.filterByDesc((List<Integer>)(o.get("lst")),desc);
    }

    @Override
    @GetMapping("searchProductByRate/{rate}")
    public SLResponsOBJ<List<Integer>> searchProductByRate( @PathVariable("rate") int rate) {
        return iMarket.searchProductByRate(rate);
    }

    @Override
    @GetMapping("searchProductByRate/list/{rate}")
    public SLResponsOBJ<List<Integer>> searchProductByRate(@RequestBody Map<String, Object> o,@PathVariable("rate") int rate) {
        return iMarket.searchProductByRate((List<Integer>)(o.get("lst")),rate);
    }

    @Override
    @GetMapping("filterByRate/{minRate}")
    public SLResponsOBJ<List<Integer>> filterByRate(@RequestBody Map<String, Object> o,@PathVariable("minRate") int minRate) {
        return iMarket.filterByRate((List<Integer>)(o.get("lst")),minRate);
    }

    @Override
    @GetMapping("searchProductByCategory/{category}")
    public SLResponsOBJ<List<Integer>> searchProductByCategory(@PathVariable("category") int category) {
        return iMarket.searchProductByCategory(category);
    }

    @Override
    @GetMapping("searchProductByCategory/list/{category}")
    public SLResponsOBJ<List<Integer>> searchProductByCategory(@RequestBody Map<String, Object> o,@PathVariable("category") int category) {
        return iMarket.searchProductByCategory((List<Integer>)(o.get("lst")),category);
    }

    @Override
    @GetMapping("filterByCategory/{category}")
    public SLResponsOBJ<List<Integer>> filterByCategory(@RequestBody Map<String, Object> o, @PathVariable("category") int category) {
        return  iMarket.searchProductByCategory((List<Integer>)(o.get("lst")),category);
    }

    @Override
    @GetMapping("searchProductByStoreRate/{rate}")
    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(@PathVariable("rate") int rate) {
        return iMarket.searchProductByStoreRate(rate);
    }

    @Override
    @GetMapping("searchProductByStoreRate/list/{rate}")
    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(@RequestBody Map<String, Object> o,@PathVariable("rate") int rate) {
        return iMarket.searchProductByStoreRate((List<Integer>)(o.get("lst")),rate);
    }

    @Override
    @GetMapping("filterByStoreRate/{minRate}")
    public SLResponsOBJ<List<Integer>> filterByStoreRate(@RequestBody Map<String, Object> o,@PathVariable("minRate") int minRate) {
        return iMarket.filterByStoreRate((List<Integer>)(o.get("lst")),minRate);
    }

    @Override
    @GetMapping("searchProductByRangePrices/{productId}/{min}/{max}")
    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(@PathVariable("productId") int productId,@PathVariable("min") int min,@PathVariable("max") int max) {
        return iMarket.searchProductByRangePrices(productId,min,max);
    }

    @Override
    @GetMapping("searchProductByRangePrices/list/{productId}/{min}/{max}")
    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(@RequestBody Map<String, Object> o,@PathVariable("productId") int productId,@PathVariable("min") int min,@PathVariable("max") int max) {
        return iMarket.searchProductByRangePrices((List<Integer>)(o.get("lst")),productId,min,max);
    }

    @Override
    @GetMapping("filterByRangePrices/{min}/{max}")
    public SLResponsOBJ<List<Integer>> filterByRangePrices(@RequestBody Map<String, Object> o, @PathVariable("min")int min,@PathVariable("max") int max) {
        return iMarket.filterByRangePrices((List<Integer>)(o.get("lst")),min,max);
    }

    @Override
    @GetMapping("addNewProductType/{uuid}")
    public SLResponsOBJ<Integer> addNewProductType(@PathVariable("uuid") String uuid,@RequestBody apiProductType apt) {
        return iMarket.addNewProductType(uuid,apt.name,apt.description,apt.category);
    }

    @Override
    @GetMapping("addProductToShoppingBag/{uuid}")
    public SLResponsOBJ<Boolean> addProductToShoppingBag(String uuid, int storeId, int productId, int quantity) {
        return null;
    }

    @Override
    public SLResponsOBJ<ServiceShoppingCart> getShoppingCart(String userId) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity) {
        return null;
    }

    @Override
    public SLResponsOBJ<String> orderShoppingCart(String userId, String city, String adress, int apartment, ServiceCreditCard creditCard) {
        return null;
    }

    @Override
    public SLResponsOBJ<String> logout(String userId) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> changePassword(String uuid, String email, String password, String newPassword) {
        return null;
    }

    @Override
    public SLResponsOBJ<Integer> openNewStore(String userId, String name, String founder, ServiceDiscountPolicy discountPolicy, ServiceBuyPolicy buyPolicy, ServiceBuyStrategy buyStrategy) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> setManagerPermissions(String userId, int storeId, String mangerEmil, String per, boolean onof) {
        return null;
    }

    @Override
    public SLResponsOBJ<Boolean> closeStore(String UserId, int StoreId) {
        return null;
    }

    @Override
    public SLResponsOBJ<HashMap<String, List<String>>> getStoreRoles(String UserId, int StoreId) {
        return null;
    }

    @Override
    public SLResponsOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId) {
        return null;
    }

    @Override
    @GetMapping("getUserInfo/{userID}")
    public SLResponsOBJ<List<List<ServiceHistory>>> getUserInfo(@PathVariable("userID") String userID,@RequestBody apiUser user) {
        return iMarket.getUserInfo(userID,user.email);
    }


}