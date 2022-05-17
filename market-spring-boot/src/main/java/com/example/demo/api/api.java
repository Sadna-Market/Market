package com.example.demo.api;

import com.example.demo.Service.ServiceObj.*;
import com.example.demo.api.apiObjects.apiProductType;
import com.example.demo.api.apiObjects.apiStore;
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
    @PostMapping("initMarket")
    public SLResponseOBJ<String> initMarket(@RequestBody apiUser user) {;
        return iMarket.initMarket(user.email, user.Password, user.phoneNumber);
    }

    @Override
    @PostMapping("guestVisit")
    public SLResponseOBJ<String> guestVisit() {
        return iMarket.guestVisit();
    }

    @Override
    @PostMapping("guestLeave/{uuid}")
    public SLResponseOBJ<Boolean> guestLeave(@PathVariable("uuid") String uuid) {
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
    @GetMapping("getStore/{sid}")
    public SLResponseOBJ<ServiceStore> getStore(@PathVariable("sid") String sid) {
        return iMarket.getStore(Integer.parseInt(sid));
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
    public SLResponseOBJ<List<Integer>> filterByName(@RequestBody Map<String, Object> o,@PathVariable("productName") String productName) {
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
    public SLResponseOBJ<List<Integer>> searchProductByRate( @PathVariable("rate") int rate) {
        return iMarket.searchProductByRate(rate);
    }

    @Override
    @GetMapping("searchProductByRate/list/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByRate(@RequestBody Map<String, Object> o,@PathVariable("rate") int rate) {
        return iMarket.searchProductByRate((List<Integer>)(o.get("lst")),rate);
    }

    @Override
    @GetMapping("filterByRate/{minRate}")
    public SLResponseOBJ<List<Integer>> filterByRate(@RequestBody Map<String, Object> o,@PathVariable("minRate") int minRate) {
        return iMarket.filterByRate((List<Integer>)(o.get("lst")),minRate);
    }

    @Override
    @GetMapping("searchProductByCategory/{category}")
    public SLResponseOBJ<List<Integer>> searchProductByCategory(@PathVariable("category") int category) {
        return iMarket.searchProductByCategory(category);
    }

    @Override
    @GetMapping("searchProductByCategory/list/{category}")
    public SLResponseOBJ<List<Integer>> searchProductByCategory(@RequestBody Map<String, Object> o,@PathVariable("category") int category) {
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
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(@RequestBody Map<String, Object> o,@PathVariable("rate") int rate) {
        return iMarket.searchProductByStoreRate((List<Integer>)(o.get("lst")),rate);
    }

    @Override
    @GetMapping("filterByStoreRate/{minRate}")
    public SLResponseOBJ<List<Integer>> filterByStoreRate(@RequestBody Map<String, Object> o,@PathVariable("minRate") int minRate) {
        return iMarket.filterByStoreRate((List<Integer>)(o.get("lst")),minRate);
    }

    @Override
    @GetMapping("searchProductByRangePrices/{productId}/{min}/{max}")
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(@PathVariable("productId") int productId,@PathVariable("min") int min,@PathVariable("max") int max) {
        return iMarket.searchProductByRangePrices(productId,min,max);
    }

    @Override
    @GetMapping("searchProductByRangePrices/list/{productId}/{min}/{max}")
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(@RequestBody Map<String, Object> o,@PathVariable("productId") int productId,@PathVariable("min") int min,@PathVariable("max") int max) {
        return iMarket.searchProductByRangePrices((List<Integer>)(o.get("lst")),productId,min,max);
    }

    @Override
    @GetMapping("filterByRangePrices/{min}/{max}")
    public SLResponseOBJ<List<Integer>> filterByRangePrices(@RequestBody Map<String, Object> o, @PathVariable("min")int min,@PathVariable("max") int max) {
        return iMarket.filterByRangePrices((List<Integer>)(o.get("lst")),min,max);
    }

    @Override
    @PostMapping("addNewProductType/{uuid}")
    public SLResponseOBJ<Integer> addNewProductType(@PathVariable("uuid") String uuid,@RequestBody apiProductType apt) {
        return iMarket.addNewProductType(uuid,apt.name,apt.description,apt.category);
    }

    @Override
    @PostMapping("addProductToShoppingBag/{uuid}/{storeId}/{productId}/{quantity}")
    public SLResponseOBJ<Boolean> addProductToShoppingBag(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId,@PathVariable("productId") int productId,@PathVariable("quantity") int quantity) {
        return iMarket.addProductToShoppingBag(uuid, storeId, productId, quantity);
    }

    @Override
    @GetMapping("getShoppingCart/{uuid}")
    public SLResponseOBJ<ServiceShoppingCart> getShoppingCart(@PathVariable("uuid") String uuid) {
        return iMarket.getShoppingCart(uuid);
    }

    @Override
    @DeleteMapping("removeProductFromShoppingBag/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> removeProductFromShoppingBag(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId,@PathVariable("productId") int productId) {
        return iMarket.removeProductFromShoppingBag(uuid,storeId,productId);
    }

    @Override
    @PostMapping("setProductQuantityShoppingBag/{uuid}/{productId}/{storeId}/{quantity}")
    public SLResponseOBJ<Boolean> setProductQuantityShoppingBag(@PathVariable("uuid") String uuid,@PathVariable("productId")  int productId,@PathVariable("storeId")  int storeId,@PathVariable("quantity") int quantity) {
        return iMarket.setProductQuantityShoppingBag(uuid, productId, storeId, quantity);
    }

    @Override
    @PostMapping("orderShoppingCart/{uuid}")
    public SLResponseOBJ<String> orderShoppingCart(@PathVariable("uuid") String uuid,@RequestBody apiUser apiUser) {
        return iMarket.orderShoppingCart(uuid,apiUser.city,apiUser.adress,apiUser.apartment,new ServiceCreditCard(apiUser.CreditCard,apiUser.CreditDate,apiUser.pin));
    }

    @Override
    @PostMapping("logout/{uuid}")
    public SLResponseOBJ<String> logout(@PathVariable("uuid") String uuid) {
        return iMarket.logout(uuid);
    }

    @Override
    @PostMapping("changePassword/{uuid}")
    public SLResponseOBJ<Boolean> changePassword(@PathVariable("uuid") String uuid ,@RequestBody Map<String,Object> OBJ){
        return iMarket.changePassword(uuid,(String)OBJ.get("email"),(String)OBJ.get("Password"),(String)OBJ.get("newPassword"));
    }

    @Override
    @PostMapping("openNewStore/{uuid}")
    public SLResponseOBJ<Integer> openNewStore(@PathVariable("uuid")String userId,@RequestBody apiStore apiStore) {
        return iMarket.openNewStore(userId,apiStore.name,apiStore.founder,new ServiceDiscountPolicy(),new ServiceBuyPolicy(),new ServiceBuyStrategy());
    }

    @Override
    @PostMapping("addNewProductToStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> addNewProductToStore(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId, @PathVariable("productId") int productId,@RequestBody Map<String,Object> obj)
    {
        System.out.println(obj.toString());
        return iMarket.addNewProductToStore(uuid,storeId,productId,(double) obj.get("price"),(int)obj.get("quantity"));
    }

    @Override
    @PostMapping("deleteProductFromStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> deleteProductFromStore(@PathVariable("uuid") String uuid,@PathVariable("storeId")  int storeId,@PathVariable("productId")  int productId) {
        return iMarket.deleteProductFromStore(uuid,storeId,productId);
    }

    @Override
    @PostMapping("setProductPriceInStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> setProductPriceInStore(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId,@PathVariable("productId") int productId,@RequestBody Map<String,Object> obj) {
        return iMarket.setProductPriceInStore(uuid, storeId, productId, (double)obj.get("price"));
    }

    @Override
    @PostMapping("setProductQuantityInStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> setProductQuantityInStore(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId,@PathVariable("productId") int productId,@RequestBody Map<String,Object> obj) {
        System.out.println(obj);
        return iMarket.setProductQuantityInStore(uuid,storeId,productId,(int)obj.get("quantity"));
    }

    @Override
    @PostMapping("addNewStoreOwner/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> addNewStoreOwner(@PathVariable("uuid") String uuid,@PathVariable("storeId") int StoreId,@RequestBody Map<String,Object> obj) {
        return iMarket.addNewStoreOwner(uuid, StoreId,(String) obj.get("OwnerEmail"));
    }

    @Override
    @PostMapping("addNewStoreManger/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> addNewStoreManger(@PathVariable("uuid") String uuid,@PathVariable("storeId") int StoreId,@RequestBody Map<String,Object> obj) {
        return iMarket.addNewStoreManger(uuid, StoreId,(String)obj.get("mangerEmail"));
    }

    @Override
    @PostMapping("setManagerPermissions/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> setManagerPermissions(@PathVariable("uuid") String uuid,@PathVariable("storeId") int storeId,@RequestBody Map<String,Object> obj) {
        return iMarket.setManagerPermissions(uuid, storeId,(String)obj.get("mangerEmail"),(String)obj.get("per"),(boolean)obj.get( "onof"));
    }

    @Override
    @PostMapping("closeStore/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> closeStore(@PathVariable("uuid") String uuid,@PathVariable("storeId") int StoreId) {
        return iMarket.closeStore(uuid, StoreId);
    }

    @Override
    @GetMapping("getStoreRoles/{uuid}/{storeId}")
    public SLResponseOBJ<HashMap<String, List<String>>> getStoreRoles(@PathVariable("uuid") String uuid,@PathVariable("storeId") int StoreId) {
        return iMarket.getStoreRoles(uuid, StoreId);
    }

    @Override
    @GetMapping("getStoreOrderHistory/{uuid}/{storeId}")
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(@PathVariable("uuid") String uuid,@PathVariable("storeId") int StoreId) {

        return iMarket.getStoreOrderHistory(uuid, StoreId);
    }

    @Override
    @GetMapping("getUserInfo/{uuid}")
    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(@PathVariable("uuid") String userID,@RequestBody apiUser user) {
        return iMarket.getUserInfo(userID,user.email);
    }


}