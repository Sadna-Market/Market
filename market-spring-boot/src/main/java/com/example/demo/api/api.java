package com.example.demo.api;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Service.IMarket;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.api.apiObjects.apiProductType;
import com.example.demo.api.apiObjects.apiRole;
import com.example.demo.api.apiObjects.apiStore;
import com.example.demo.api.apiObjects.apiUser;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api")
public class api implements Iapi {
    private IMarket iMarket;
    static Logger logger = Logger.getLogger(api.class);

    @Autowired
    public api(@Qualifier("proxy") IMarket iMarket) {
        this.iMarket = iMarket;
    }


    @Override
    @PostMapping("initMarket")
    public SLResponseOBJ<Boolean> initMarket(@RequestBody apiUser user) {
        return iMarket.initMarket(user.email, user.Password, user.phoneNumber, user.datofbirth);

    }
    @Override
    @PostMapping("removeStoreMenager/{uuid}/{storeid}/{menagerEmail}")
    public SLResponseOBJ<Boolean> removeStoreMenager(@PathVariable("uuid")String userId,@PathVariable("storeid") int storeId,@PathVariable("menagerEmail") String menagerEmail) {
        return iMarket.removeStoreMenager(userId, storeId, menagerEmail);
    }

    @Override
    @PostMapping("getRate/{uuid}/{productTypeID}")
    public SLResponseOBJ<Integer> getRate(@PathVariable("uuid")String uuid,@PathVariable("productTypeID") int productTypeID) {
        System.out.println(uuid);
        System.out.println(productTypeID);
        return iMarket.getRate(uuid, productTypeID);
    }

    @Override
    @PostMapping("setRate/{uuid}/{productTypeID}/{rate}")
    public SLResponseOBJ<Boolean> setRate(@PathVariable("uuid")String uuid,@PathVariable("productTypeID") int productTypeID,@PathVariable("rate") int rate) {
        System.out.println(uuid);
        System.out.println(productTypeID);
        System.out.println(rate);
        return iMarket.setRate(uuid, productTypeID, rate);
    }

    @Override
    @PostMapping("getProductTypeInfo/{productTypeID}")
    public SLResponseOBJ<ServiceProductType> getProductTypeInfo(@PathVariable("productTypeID")Integer productTypeId) {
        System.out.println(productTypeId);
        return iMarket.getProductTypeInfo(productTypeId);

    }

    @Override
    @PostMapping("guestVisit")
    public SLResponseOBJ<String> guestVisit() { //ok
        return iMarket.guestVisit();
    }

    @Override
    @PostMapping("guestLeave/{uuid}")
    public SLResponseOBJ<Boolean> guestLeave(@PathVariable("uuid") String uuid) //ok
    {
        System.out.println(uuid + " aaaaaaaaaaaaaaaaa");
        return iMarket.guestLeave(uuid);
    }

    @Override
    @PostMapping("addNewMember/{uuid}")
    public SLResponseOBJ<Boolean> addNewMember(@PathVariable("uuid") String uuid, @RequestBody apiUser user) {
        return iMarket.addNewMember(uuid, user.email, user.Password, user.phoneNumber, user.datofbirth);

    }


        @Override
    @PostMapping("login/{uuid}")
    public SLResponseOBJ<String> login(@PathVariable("uuid") String uuid, @RequestBody apiUser user) { //ok

        System.out.println(uuid + " " + user.email + "  " + user.Password);

        return iMarket.login(uuid, user.email, user.Password);
    }

    @Override
    @GetMapping("getStore/{sid}")
    public SLResponseOBJ<ServiceStore> getStore(@PathVariable("sid") int sid) { //ok
        System.out.println(sid);
        return iMarket.getStore(sid);
    }

    @Override
    @GetMapping("getInfoProductInStore/{StoreID}/{productID}")
    public SLResponseOBJ<ServiceProductStore> getInfoProductInStore(@PathVariable("StoreID") int storeID, @PathVariable("productID") int productID) {//ok
        System.out.println(storeID + " " + productID);

        return iMarket.getInfoProductInStore(storeID, productID);
    }

    @Override
    @GetMapping("searchProductByName/{productName}")
    public SLResponseOBJ<List<Integer>> searchProductByName(@PathVariable("productName") String productName) { //ok
        System.out.println(productName);
        return iMarket.searchProductByName(productName);
    }

    @Override
    @PostMapping("searchProductByName/List/{productName}")
    public SLResponseOBJ<List<Integer>> searchProductByName(@RequestBody Map<String, Object> o, @PathVariable("productName") String productName) { //ok
        System.out.println(o);
        System.out.println(productName);
        return iMarket.searchProductByName((List<Integer>) (o.get("lst")), productName);
    }

    @Override
    @PostMapping("filterByName/{productName}")
    public SLResponseOBJ<List<Integer>> filterByName(@RequestBody Map<String, Object> o, @PathVariable("productName") String productName) { //ok
        System.out.println(o);
        System.out.println(productName);
        return iMarket.searchProductByName(((List<Integer>) (o.get("lst"))), productName);
    }

    @Override
    @GetMapping("searchProductByDesc/{desc}")
    public SLResponseOBJ<List<Integer>> searchProductByDesc(@PathVariable("desc") String desc) { //ok
        System.out.println(desc);
        return iMarket.searchProductByDesc(desc);
    }

    @Override
    @PostMapping("searchProductByDesc/list/{desc}")
    public SLResponseOBJ<List<Integer>> searchProductByDesc(@RequestBody Map<String, Object> o, @PathVariable("desc") String desc) { //ok
        System.out.println(o);
        System.out.println(desc);
        return iMarket.searchProductByDesc((List<Integer>) (o.get("lst")), desc);
    }

    @Override
    @PostMapping("filterByDesc/{desc}")
    public SLResponseOBJ<List<Integer>> filterByDesc(@RequestBody Map<String, Object> o, @PathVariable("desc") String desc) {//ok
        System.out.println(o);
        System.out.println(desc);
        return iMarket.filterByDesc((List<Integer>) (o.get("lst")), desc);
    }

    @Override
    @GetMapping("searchProductByRate/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByRate(@PathVariable("rate") int rate) {//ok
        System.out.println(rate);
        return iMarket.searchProductByRate(rate);
    }

    @Override
    @PostMapping("searchProductByRate/list/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByRate(@RequestBody Map<String, Object> o, @PathVariable("rate") int rate) {//ok
        System.out.println(o);
        System.out.println(rate);
        return iMarket.searchProductByRate((List<Integer>) (o.get("lst")), rate);
    }

    @Override
    @PostMapping("filterByRate/{minRate}")
    public SLResponseOBJ<List<Integer>> filterByRate(@RequestBody Map<String, Object> o, @PathVariable("minRate") int minRate) {//ok
        System.out.println(o);
        System.out.println(minRate);
        return iMarket.filterByRate((List<Integer>) (o.get("lst")), minRate);
    }

    @Override
    @GetMapping("searchProductByCategory/{category}")
    public SLResponseOBJ<List<Integer>> searchProductByCategory(@PathVariable("category") int category) {//ok
        System.out.println(category);
        return iMarket.searchProductByCategory(category);
    }

    @Override
    @PostMapping("searchProductByCategory/list/{category}")
    public SLResponseOBJ<List<Integer>> searchProductByCategory(@RequestBody Map<String, Object> o, @PathVariable("category") int category) {//ok
        System.out.println(o);
        System.out.println(category);
        return iMarket.searchProductByCategory((List<Integer>) (o.get("lst")), category);
    }

    @Override
    @PostMapping("filterByCategory/{category}")
    public SLResponseOBJ<List<Integer>> filterByCategory(@RequestBody Map<String, Object> o, @PathVariable("category") int category) {//ok
        System.out.println(o);
        System.out.println(category);
        return iMarket.searchProductByCategory((List<Integer>) (o.get("lst")), category);
    }

    @Override
    @GetMapping("searchProductByStoreRate/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(@PathVariable("rate") int rate) {//ok
        System.out.println(rate);
        return iMarket.searchProductByStoreRate(rate);
    }

    @Override
    @PostMapping("searchProductByStoreRate/list/{rate}")
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(@RequestBody Map<String, Object> o, @PathVariable("rate") int rate) {//ok
        System.out.println(o);
        System.out.println(rate);
        return iMarket.searchProductByStoreRate((List<Integer>) (o.get("lst")), rate);
    }

    @Override
    @PostMapping("filterByStoreRate/{minRate}")
    public SLResponseOBJ<List<Integer>> filterByStoreRate(@RequestBody Map<String, Object> o, @PathVariable("minRate") int minRate) {//ok
        System.out.println(o);
        System.out.println(minRate);
        return iMarket.filterByStoreRate((List<Integer>) (o.get("lst")), minRate);
    }

    @Override
    @GetMapping("searchProductByRangePrices/{productId}/{min}/{max}")
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(@PathVariable("productId") int productId, @PathVariable("min") int min, @PathVariable("max") int max) {
        System.out.println(productId + "cccc");
        System.out.println(max);
        System.out.println(min);
        return iMarket.searchProductByRangePrices(productId, min, max);
    }

    @Override
    @PostMapping("searchProductByRangePrices/list/{productId}/{min}/{max}")
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(@RequestBody Map<String, Object> o, @PathVariable("productId") int productId, @PathVariable("min") int min, @PathVariable("max") int max) {
        System.out.println(o);
        System.out.println(max);
        System.out.println(min);
        System.out.println(productId);
        return iMarket.searchProductByRangePrices((List<Integer>) (o.get("lst")), productId, min, max);
    }

    @Override
    @PostMapping("filterByRangePrices/{min}/{max}")
    public SLResponseOBJ<List<Integer>> filterByRangePrices(@RequestBody Map<String, Object> o, @PathVariable("min") int min, @PathVariable("max") int max) {
        System.out.println(o);
        System.out.println(min);
        System.out.println(max);
        return iMarket.filterByRangePrices((List<Integer>) (o.get("lst")), min, max);
    }

    @PostMapping("filterByRangePrices2/{min}/{max}")
    public SLResponseOBJ<List<Integer>> filterByRangePrices2(@RequestBody Map<String, Object> o,@PathVariable("min") int min, @PathVariable("max") int max) {
        System.out.println(min);
        System.out.println(max);
        return new SLResponseOBJ<>(-1);
    }

    @Override
    @PostMapping("addNewProductType/{uuid}")
    public SLResponseOBJ<Integer> addNewProductType(@PathVariable("uuid") String uuid, @RequestBody apiProductType apt) { //ok
        System.out.println(uuid);
        System.out.println(apt.name);
        System.out.println(apt.description);
        System.out.println(apt.category);
        return iMarket.addNewProductType(uuid, apt.name, apt.description, apt.category);
    }

    @Override
    @PostMapping("addProductToShoppingBag/{uuid}/{storeId}/{productId}/{quantity}")
    public SLResponseOBJ<Boolean> addProductToShoppingBag(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("productId") int productId, @PathVariable("quantity") int quantity) {//ok
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(productId);
        System.out.println(quantity);

        return iMarket.addProductToShoppingBag(uuid, storeId, productId, quantity);
    }

    @Override
    @GetMapping("getShoppingCart/{uuid}")
    public SLResponseOBJ<ServiceShoppingCart> getShoppingCart(@PathVariable("uuid") String uuid) {//ok

        System.out.println(uuid);

        logger.debug("161616161616");
        System.out.println(iMarket.getShoppingCart(uuid).value.shoppingBagHash);
        return iMarket.getShoppingCart(uuid);
    }

    @Override
    @DeleteMapping("removeProductFromShoppingBag/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> removeProductFromShoppingBag(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("productId") int productId) {//ok
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(productId);
        return iMarket.removeProductFromShoppingBag(uuid, storeId, productId);
    }

    @Override
    @PostMapping("setProductQuantityShoppingBag/{uuid}/{productId}/{storeId}/{quantity}")
    public SLResponseOBJ<Boolean> setProductQuantityShoppingBag(@PathVariable("uuid") String uuid, @PathVariable("productId") int productId, @PathVariable("storeId") int storeId, @PathVariable("quantity") int quantity) {//ok
        System.out.println(uuid);
        System.out.println(productId);
        System.out.println(storeId);
        System.out.println(quantity);

        return iMarket.setProductQuantityShoppingBag(uuid, productId, storeId, quantity);
    }

    @Override
    @PostMapping("orderShoppingCart/{uuid}")
    public SLResponseOBJ<String> orderShoppingCart(@PathVariable("uuid") String uuid, @RequestBody apiUser apiUser) {
        System.out.println(uuid);
        System.out.println(apiUser.city);
        System.out.println(apiUser.adress);
        System.out.println(apiUser.apartment);
        System.out.println(apiUser.pin);
        System.out.println(apiUser.apartment);
        return iMarket.orderShoppingCart(uuid, apiUser.city, apiUser.adress, apiUser.apartment, new ServiceCreditCard(apiUser.CreditCard, apiUser.CreditDate, apiUser.pin));
    }

    @Override
    @PostMapping("logout/{uuid}")
    public SLResponseOBJ<String> logout(@PathVariable("uuid") String uuid) {
        System.out.println(uuid);
        return iMarket.logout(uuid);
    }

    @Override
    @PostMapping("changePassword/{uuid}")
    public SLResponseOBJ<Boolean> changePassword(@PathVariable("uuid") String uuid, @RequestBody Map<String, Object> OBJ) {
        System.out.println(uuid);
        System.out.println((String) OBJ.get("email"));
        System.out.println((String) OBJ.get("Password"));
        System.out.println((String) OBJ.get("newPassword"));
        return iMarket.changePassword(uuid, (String) OBJ.get("email"), (String) OBJ.get("Password"), (String) OBJ.get("newPassword"));
    }

    @Override
    @PostMapping("openNewStore/{uuid}")
    public SLResponseOBJ<Integer> openNewStore(@PathVariable("uuid") String userId, @RequestBody apiStore apiStore) {
        System.out.println(userId);
        System.out.println(apiStore.name);
        System.out.println(apiStore.founder);
        return iMarket.openNewStore(userId, apiStore.name, apiStore.founder, new ServiceDiscountPolicy(), new ServiceBuyPolicy(), new ServiceBuyStrategy());
    }

    @Override
    @PostMapping("addNewProductToStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> addNewProductToStore(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("productId") int productId, @RequestBody Map<String, Object> obj) {
        System.out.println(obj);
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(productId);
        System.out.println(Double.valueOf((String) obj.get("price")));
        System.out.println(Integer.valueOf((String)  obj.get("quantity")));

        return iMarket.addNewProductToStore(uuid, storeId, productId, Double.valueOf((String) obj.get("price")), Integer.valueOf((String)  obj.get("quantity")));
    }

    @Override
    @PostMapping("deleteProductFromStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> deleteProductFromStore(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("productId") int productId) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(productId);
        return iMarket.deleteProductFromStore(uuid, storeId, productId);
    }

    @Override
    @PostMapping("setProductPriceInStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> setProductPriceInStore(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("productId") int productId, @RequestBody Map<String, Object> obj) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(productId);
        System.out.println(Double.valueOf((String) obj.get("price")));
        return iMarket.setProductPriceInStore(uuid, storeId, productId, Double.valueOf((String) obj.get("price")));
    }

    @Override
    @PostMapping("setProductQuantityInStore/{uuid}/{storeId}/{productId}")
    public SLResponseOBJ<Boolean> setProductQuantityInStore(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("productId") int productId, @RequestBody Map<String, Object> obj) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(productId);
        System.out.println(Integer.valueOf((String)  obj.get("quantity")));
        System.out.println(obj);
        System.out.println(obj);
        return iMarket.setProductQuantityInStore(uuid, storeId, productId,Integer.valueOf((String)  obj.get("quantity")));
    }

    @Override
    @PostMapping("addNewStoreOwner/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> addNewStoreOwner(@PathVariable("uuid") String uuid, @PathVariable("storeId") int StoreId, @RequestBody Map<String, Object> obj) {
        System.out.println(uuid);
        System.out.println(StoreId);
        System.out.println((String) obj.get("OwnerEmail"));
        return iMarket.addNewStoreOwner(uuid, StoreId, (String) obj.get("OwnerEmail"));
    }

    @Override
    @PostMapping("addNewStoreManger/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> addNewStoreManger(@PathVariable("uuid") String uuid, @PathVariable("storeId") int StoreId, @RequestBody Map<String, Object> obj) {
        System.out.println(uuid);
        System.out.println(StoreId);
        System.out.println((String) obj.get("mangerEmail"));
        System.out.println(uuid);
        System.out.println(uuid);
        return iMarket.addNewStoreManger(uuid, StoreId, (String) obj.get("mangerEmail"));
    }

    @Override
    @GetMapping("removeMember/{uuid}/{email}")
    public SLResponseOBJ<Boolean> removeMember(@PathVariable("uuid") String userId,@PathVariable("email") String email) {
        System.out.println(userId+"       ddddddddddddddddddddddddddd");
        return iMarket.removeMember(userId, email);
    }

    @Override
    @PostMapping("setManagerPermissions/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> setManagerPermissions(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @RequestBody Map<String, Object> obj) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println((String) obj.get("mangerEmail"));
        System.out.println((String) obj.get("per"));
        System.out.println(Boolean.getBoolean((String) obj.get("onof")));
        return iMarket.setManagerPermissions(uuid, storeId, (String) obj.get("mangerEmail"), (String) obj.get("per"), Boolean.getBoolean((String) obj.get("onof")));
    }

    @Override
    @PostMapping("closeStore/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> closeStore(@PathVariable("uuid") String uuid, @PathVariable("storeId") int StoreId) {
        System.out.println(uuid);
        System.out.println(StoreId);
        return iMarket.closeStore(uuid, StoreId);
    }

    @Override
    @GetMapping("getStoreRoles/{uuid}/{storeId}")
    public SLResponseOBJ<HashMap<String, List<String>>> getStoreRoles(@PathVariable("uuid") String uuid, @PathVariable("storeId") int StoreId) {
        System.out.println(uuid);
        System.out.println(StoreId);
        return iMarket.getStoreRoles(uuid, StoreId);
    }

    @Override
    @GetMapping("getAllMembers/{uuid}")
    public SLResponseOBJ<List<String>> getAllMembers(@PathVariable("uuid") String userId) {
        System.out.println(userId);
        return iMarket.getAllMembers(userId);
    }

    @Override
    @PostMapping("createBID/{uuid}/{storeID}/{productID}/{quantity}/{totalPrice}")
    public SLResponseOBJ<Boolean> createBID(@PathVariable("uuid")String uuid,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID,@PathVariable("quantity") int quantity,@PathVariable("totalPrice") int totalPrice) {
        System.out.println(uuid);
        System.out.println(storeID);
        System.out.println(productID);
        System.out.println(quantity);
        System.out.println(totalPrice);

        return iMarket.createBID(uuid, storeID, productID, quantity, totalPrice);
    }

    @Override
    @PostMapping("removeBID/{uuid}/{storeID}/{productID}")
    public SLResponseOBJ<Boolean> removeBID(@PathVariable("uuid")String uuid,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID) {
        System.out.println(uuid);
        System.out.println(storeID);
        System.out.println(productID);
        return iMarket.removeBID(uuid, storeID, productID);
    }

    @Override
    @PostMapping("approveBID/{uuid}/{userEmail}/{storeID}/{productID}")
    public SLResponseOBJ<Boolean> approveBID(@PathVariable("uuid")String uuid,@PathVariable("userEmail") String userEmail,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID) {
        System.out.println(uuid);
        System.out.println(userEmail);
        System.out.println(storeID);
        System.out.println(productID);
        return iMarket.approveBID(uuid, userEmail, storeID, productID);
    }

    @Override
    @PostMapping("rejectBID/{uuid}/{userEmail}/{storeID}/{productID}")
    public SLResponseOBJ<Boolean> rejectBID(@PathVariable("uuid")String uuid, @PathVariable("userEmail")String userEmail,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID) {
        System.out.println(uuid);
        System.out.println(userEmail);
        System.out.println(storeID);
        System.out.println(productID);
        return iMarket.rejectBID(uuid, userEmail, storeID, productID);
    }

    @Override
    @PostMapping("counterBID/{uuid}/{userEmail}/{storeID}/{productID}/{newTotalPrice}")
    public SLResponseOBJ<Boolean> counterBID(@PathVariable("uuid")String uuid,@PathVariable("userEmail") String userEmail,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID,@PathVariable("newTotalPrice") int newTotalPrice) {
        System.out.println(uuid);
        System.out.println(userEmail);
        System.out.println(storeID);
        System.out.println(productID);
        System.out.println(newTotalPrice);
        return iMarket.counterBID(uuid, userEmail, storeID, productID, newTotalPrice);
    }

    @Override
    @PostMapping("responseCounterBID/{uuid}/{storeID}/{productID}/{approve}")
    public SLResponseOBJ<Boolean> responseCounterBID(@PathVariable("uuid")String uuid,@PathVariable("storeID") int storeID, @PathVariable("productID")int productID,@PathVariable("approve") boolean approve) {
        System.out.println(uuid);
        System.out.println(productID);
        System.out.println(storeID);
        System.out.println(approve);
        return iMarket.responseCounterBID(uuid, storeID, productID, approve);
    }

    @Override
    @PostMapping("BuyBID/{userId}/{storeID}/{productID}/{city}/{adress}/{apartment}")
    public SLResponseOBJ<Boolean> BuyBID(@PathVariable("userId")String userId,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID,@PathVariable("city") String city, @PathVariable("adress")String adress,@PathVariable("apartment") int apartment,@RequestBody Map<String,Object> map) {
        System.out.println(userId);
        System.out.println(productID);
        System.out.println(storeID);
        System.out.println(city);
//        System.out.println(creditCard.creditDate);
//        System.out.println(creditCard.creditCard);
//        System.out.println(creditCard.pin);
        return iMarket.BuyBID(userId, storeID, productID, city, adress, apartment, new ServiceCreditCard((String) map.get("creditCard"),(String) map.get("creditDate"),(String)map.get("pin")));
    }

    @Override
    @PostMapping("getBIDStatus/{uuid}/{userEmail}/{storeID}/{productID}")
    public SLResponseOBJ<String> getBIDStatus(@PathVariable("uuid")String uuid,@PathVariable("userEmail") String userEmail,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID) {
        System.out.println(uuid);
        System.out.println(userEmail);
        System.out.println(storeID);
        System.out.println(productID);
        return iMarket.getBIDStatus(uuid, userEmail, storeID, productID);
        }


//    @Override
//    @PostMapping("getApprovesList/{uuid}/{userEmail}/{storeID}/{productID}")
//    public SLResponseOBJ<HashMap<String, Boolean>> getApprovesList(@PathVariable("uuid")String uuid,@PathVariable("userEmail") String userEmail,@PathVariable("storeID") int storeID,@PathVariable("productID") int productID) {
//        System.out.println(uuid);
//        System.out.println(userEmail);
//        System.out.println(storeID);
//        System.out.println(productID);
//        return iMarket.getApprovesList(uuid, userEmail, storeID, productID);
//    }



    @Override
    @GetMapping("getStoreOrderHistory/{uuid}/{storeId}")
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(@PathVariable("uuid") String uuid, @PathVariable("storeId") int StoreId) {
        System.out.println(uuid);
        System.out.println(StoreId);
        return iMarket.getStoreOrderHistory(uuid, StoreId);
    }

    @Override
    @PostMapping("getUserInfo/{uuid}")
    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(@PathVariable("uuid") String userID, @RequestBody apiUser user) {

        System.out.println(userID);
        System.out.println(user.email);
        return iMarket.getUserInfo(userID, user.email);
    }

    @Override
    @GetMapping("getAllProductsInStore/{storeID}")
    public SLResponseOBJ<List<ServiceProductStore>> getAllProductsInStore(@PathVariable("storeID") int storeID) {
        return iMarket.getAllProductsInStore(storeID);
    }

    @Override
    @GetMapping("getAllProducts")
    public SLResponseOBJ<List<ServiceProductType>> getAllProducts() {
        return iMarket.getAllProducts();
    }

    @Override
    @GetMapping("getAllStores")
    public SLResponseOBJ<List<ServiceStore>> getAllStores() {
        return iMarket.getAllStores();
    }

    @Override
    @GetMapping("getloggedOutMembers/{uuid}")
    public SLResponseOBJ<List<ServiceUser>> getloggedOutMembers(@PathVariable("uuid") String uuid) {
        return iMarket.getloggedInMembers(uuid);
    }

    @Override
    @GetMapping("getloggedInMembers/{uuid}")
    public SLResponseOBJ<List<ServiceUser>> getloggedInMembers(@PathVariable("uuid") String uuid) {
        return iMarket.getloggedInMembers(uuid);
    }

    @Override
    @GetMapping("isOwnerUUID/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> isOwnerUUID(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId) {
        return iMarket.isOwnerUUID(uuid, storeId);
    }

    @Override
    @GetMapping("isManagerUUID/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> isManagerUUID(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId) {
        return iMarket.isManagerUUID(uuid, storeId);
    }

    @Override
    @GetMapping("isSystemManagerUUID/{uuid}")
    public SLResponseOBJ<Boolean> isSystemManagerUUID(@PathVariable("uuid") String uuid) {
        return iMarket.isSystemManagerUUID(uuid);
    }

    @Override
    @GetMapping("removeStoreOwner/{UserId}/{StoreId}/{OwnerEmail}")
    public SLResponseOBJ<Boolean> removeStoreOwner(@PathVariable("UserId") String UserId,@PathVariable("StoreId") int StoreId,@PathVariable("OwnerEmail") String OwnerEmail) {
        return iMarket.removeStoreOwner(UserId,StoreId,OwnerEmail);
    }

    @Override
    @PostMapping("addNewBuyRule/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> addNewBuyRule(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @RequestBody Map<String, Object> map) {
        apiBuyPparser p = new apiBuyPparser();
        System.out.println(uuid);
        System.out.println(map);
        return iMarket.addNewBuyRule(uuid, storeId, p.BuyRuleParse(map));
    }

    @Override
    @GetMapping("removeBuyRule/{uuid}/{storeId}/{buyRuleID}")
    public SLResponseOBJ<Boolean> removeBuyRule(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("buyRuleID") int buyRuleID) {
        apiBuyPparser p = new apiBuyPparser();
        return iMarket.removeBuyRule(uuid, storeId, buyRuleID);
    }


    @PostMapping("combineANDDiscountRules/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> combineANDDiscountRules(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @RequestBody Map<String, Object> map) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(map);
        System.out.println(uuid);
        System.out.println(uuid);
        return iMarket.combineANDORDiscountRules(uuid,storeId,"and", (List<Integer>) map.get("combineAnd"),Integer.valueOf((String) map.get("category")),Integer.valueOf((String)map.get("discount")));
    }
    @PostMapping("combineORDiscountRules/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> combineORDiscountRules(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @RequestBody Map<String, Object> map) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(map);
        System.out.println(uuid);
        System.out.println(uuid);
        return iMarket.combineANDORDiscountRules(uuid,storeId,"or", (List<Integer>) map.get("combineOr"),Integer.valueOf((String) map.get("category")),Integer.valueOf((String)map.get("discount")));
    }

    @PostMapping("combineXorDiscountRules/{uuid}/{storeId}/{desicion}")
    public SLResponseOBJ<Boolean> combineXorDiscountRules(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId,@PathVariable("desicion")String desicion, @RequestBody Map<String, Object> map) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(map);
        System.out.println(desicion);
        System.out.println(uuid);
        return iMarket.combineXORDiscountRules(uuid,storeId,desicion , (List<Integer>) map.get("combineXor"));
    }

    @Override
    @PostMapping("addNewDiscountRule/{uuid}/{storeId}")
    public SLResponseOBJ<Boolean> addNewDiscountRule(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @RequestBody Map<String, Object> map ) {
        System.out.println(uuid);
        System.out.println(storeId);
        System.out.println(map);
        System.out.println(uuid);
        System.out.println(uuid);
        apiDiscountPparser p = new apiDiscountPparser();

        return iMarket.addNewDiscountRule(uuid, storeId, p.DiscountParse(map));
    }



    @Override
    @GetMapping("removeNewDiscountRule/{uuid}/{storeId}/{DiscountRuleID}")
    public SLResponseOBJ<Boolean> removeNewDiscountRule(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId, @PathVariable("DiscountRuleID") int DiscountRuleID) {
        return iMarket.removeDiscountRule(uuid, storeId, DiscountRuleID);
    }

    @Override
    @GetMapping("getBuyPolicy/{uuid}/{storeId}")
    public SLResponseOBJ<List<BuyRuleSL>> getBuyPolicy(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId) {
        return iMarket.getBuyPolicy(uuid, storeId);
    }

    @Override
    @GetMapping("getDiscountPolicy/{uuid}/{storeId}")
    public SLResponseOBJ<List<DiscountRuleSL>> getDiscountPolicy(@PathVariable("uuid") String uuid, @PathVariable("storeId") int storeId) {
        return iMarket.getDiscountPolicy(uuid, storeId);
    }

}

