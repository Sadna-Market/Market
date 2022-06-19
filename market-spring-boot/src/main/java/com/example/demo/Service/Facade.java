package com.example.demo.Service;

import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.DataAccess.Services.ProductTypeService;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.Market;
import com.example.demo.Domain.Market.PermissionManager;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Market.permissionType;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.*;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.UserModel.ShoppingCart;
import com.example.demo.Domain.UserModel.User;
import com.example.demo.Domain.UserModel.UserManager;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import com.example.demo.configuration.JsonUser;
import com.example.demo.configuration.StateInit.Initilizer;
import com.example.demo.configuration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Qualifier("facade")
public class Facade implements IMarket {
    UserManager userManager;
    Market market;

    @Autowired
    public Facade(UserManager userManager, DataServices dataServices) {
        this.userManager = userManager;
        this.market = new Market(this.userManager, dataServices);
        JsonUser a = config.get_instance().getJsonInit().admin;
        System.out.println(a.email + " " + a.password + " " + a.phoneNumber + " " + a.dateOfBirth);
        System.out.println(config.isMakeState);
        initMarket(a.email, a.password, a.phoneNumber, a.dateOfBirth);
    }

    public Facade() {
        this.userManager = new UserManager();
        this.market = new Market(this.userManager, new DataServices());
        JsonUser a = config.get_instance().getJsonInit().admin;
        System.out.println(a.email + " " + a.password + " " + a.phoneNumber + " " + a.dateOfBirth);
        System.out.println(config.isMakeState);
        initMarket(a.email, a.password, a.phoneNumber, a.dateOfBirth);
    }

    public SLResponseOBJ<Boolean> removeMember(String userId, String email) {
        if (email == null || email.equals("")) return new SLResponseOBJ<>(false, ErrorCode.NOT_VALID_EMILE);
        email = email.toLowerCase();
        return new SLResponseOBJ<>(userManager.removeMember(UUID.fromString(userId), email));
    }


    @Override
    public SLResponseOBJ<Boolean> initMarket(String email, String Password, String phoneNumber, String dateOfBirth) {
        /**
         * @requirement II. 1
         *
         * @param email: String
         * @param Password: String
         * @param phoneNumber: String
         * @param CreditCared: String
         * @param CreditDate: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean - Successfully completed
         *        }
         *
         * @documentation:
         * This function will be called only once when someone open a new market, with the information about the system manager.
         * It will create system manager user and start connections with services (Payment and delivery).
         */
        email = email.toLowerCase();
        SLResponseOBJ<String> guestUUID = guestVisit();
        if (guestUUID.errorOccurred()) return new SLResponseOBJ<>(false, guestUUID.errorMsg);
        SLResponseOBJ<Boolean> addedSysManager = addNewMember(guestUUID.value, email, Password, phoneNumber, dateOfBirth);
        if (addedSysManager.errorOccurred()) return new SLResponseOBJ<>(false, addedSysManager.errorMsg);
        PermissionManager permissionManager = PermissionManager.getInstance();

        DResponseObj<Boolean> setSysManagerPermission = permissionManager.setSystemManager(email);
        if (setSysManagerPermission.errorOccurred())
            return new SLResponseOBJ<>(false, setSysManagerPermission.errorMsg);
        SLResponseOBJ<Boolean> leav = guestLeave(guestUUID.value);
        if (leav.errorOccurred()) return new SLResponseOBJ<>(false, addedSysManager.errorMsg);

        DResponseObj<Boolean> initiateExternalServices = market.init();
        if (initiateExternalServices.errorOccurred() && initiateExternalServices.errorMsg != 50)
            return new SLResponseOBJ<>(false, initiateExternalServices.errorMsg); //nivvvvvvvvvvvvvvvvvvvvvvvvv !!!!!
        /// the problem was from hear do with it watever you want
        //when you try to connect again to the conection service its throw you with message error
        //hara ahusharmuta
        //&&initiateExternalServices.errorMsg!=50 this is that i add
        if (config.get_instance().getJsonInit().initState) {
            System.out.println("make system state");
            Initilizer I = new Initilizer(this);
            try {
                I.initialization(email, Password);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        return new SLResponseOBJ<>(true, -1);
    }


    //---------------------------------------1 .פעולות כלליות של מבקר-אורח:-------------------------------------------
    @Override
    public SLResponseOBJ<String> guestVisit() {
        /**
         * @requirement: II. 1 . 1
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": String - unique string that identifies the Guest
         *        }
         *
         * @documentation:
         *
         * user visit(entering) the market, As a guest-visitor (or in short, a guest). Upon entering, the guest
         * receives a unique string that identifies, a shopping cart, and can function As a buyer.
         * */
        DResponseObj<UUID> res = userManager.GuestVisit();
        return new SLResponseOBJ<>(res.value.toString(), -1);
    }

    @Override
    public SLResponseOBJ<Boolean> guestLeave(String guestId) {

        /**
         * @requirement: II. 1 . 2
         * @param guestId: String- unique string that identifies the Guest
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean - Successfully completed
         *        }
         *
         * @documentation:
         * Guest leave the market. Upon his departure, he loses his shopping cart and does not
         * Defined as a visitor.
         */
        if (guestId == null || guestId.equals("")) return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        return new SLResponseOBJ<>(userManager.GuestLeave(UUID.fromString(guestId)));
    }

    // return new DResponseObj<>(false, "grantor can be null only in case that open new store");
    @Override
    public SLResponseOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber, String dateOfBirth) {
        /**
         * @requirement II. 1 . 3
         *
         * @param uuid: String - unique string that identifies the Guest
         * @param email: String - unique email addresses string
         * @param Password: String
         * @param phoneNumber: String
         * @param CreditCared: String
         * @param CreditDate: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean - Successfully completed
         *        }
         *
         * @documentation:
         * Registration for the trading system: A guest can register by providing unique identifying information. At the end
         * Successful registration process The guest is registered in the system as a member
         * (in order to receive member status, he must log)
         */
        if (Password == null || Password.equals("")) {
            return new SLResponseOBJ<>(false, ErrorCode.NOT_VALID_PASSWORD);
        }
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);

        if (email == null || email.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);

        if (phoneNumber == null || phoneNumber.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        LocalDate date = checkDateOfBirth(dateOfBirth);
        if (date == null) return new SLResponseOBJ<>(false, ErrorCode.NOTLEGALDATE);
        email = email.toLowerCase();
        DResponseObj<Boolean> res = userManager.AddNewMember(UUID.fromString(uuid), email, Password, phoneNumber, date);
        return new SLResponseOBJ<>(res.value, res.errorMsg);
    }

    private LocalDate checkDateOfBirth(String dateOfBirth) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        LocalDate date;
        try {
            date = (format.parse(dateOfBirth)).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            ;
        } catch (ParseException e) {
            return null;
        }
        if (date.isAfter(LocalDate.now())) return null;
        return date;
    }

    @Override
    public SLResponseOBJ<String> login(String userId, String email, String password) {

        /**
         * @requirement II. 1 . 4
         *
         * @param userId: String - unique string that identifies the user
         * @param email: String - unique email addresses string
         * @param Password: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean - Successfully completed
         *        }
         *
         * @documentation:
         *  login using the identifying details. At the end of the login process
         * Successful user is identified as a member
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTMEMBER);

        if (email == null || email.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);

        if (password == null || password.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        email = email.toLowerCase();
        DResponseObj<UUID> res = userManager.Login(UUID.fromString(userId), email, password);
        return res.errorOccurred() ? new SLResponseOBJ<>(res.errorMsg) : new SLResponseOBJ<>(res.value.toString(), -1);
    }
//-----------------------------------2 .פעולות קנייה של מבקר-אורח-----------------------------------------------

    @Override
    public SLResponseOBJ<ServiceProductStore> getInfoProductInStore(int storeId, int productID) {

        /**
         * @requirement II. 2 . 1
         *
         * @param storeId: int
         * @param productID: int
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": String
         *        }
         *
         * @documentation:
         * Receiving information about products in stores.
         */
        if (storeId < 0)
            return new SLResponseOBJ<>(ErrorCode.NEGATIVENUMBER);
        if (productID < 0)
            return new SLResponseOBJ<>(ErrorCode.NEGATIVENUMBER);
        DResponseObj<ProductStore> productStore = market.getInfoProductInStore(storeId, productID);
        if (productStore.errorOccurred()) return new SLResponseOBJ<>(null, productStore.getErrorMsg());
        return new SLResponseOBJ<>(new ServiceProductStore(productStore.getValue()));
    }

    @Override
    public SLResponseOBJ<ServiceStore> getStore(int storeId) {
        /**
         * @requirement II. 2 . 1
         *
         * @param storeId: int
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": ServiceStore
         *        }
         *
         * @documentation:
         * Receiving information about stores in the market.
         */
        SLResponseOBJ<Store> storeR = new SLResponseOBJ<>(market.getStore(storeId));
        if (storeR.errorOccurred()) return new SLResponseOBJ<>(storeR.errorMsg);

        return new SLResponseOBJ<>(new ServiceStore(storeR.value), -1);
    }

//TODO GET INFO ABOUT STORES IN THE MARKET


    @Override
    public SLResponseOBJ<List<Integer>> searchProductByName(String productName) {

        /**
         * @requirement II. 2 . 2
         * @param productName: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": List<Integer>- list of a products id
         *        }
         *
         * @documentation:
         * Search for products without focusing on a specific store, by product name.
         */
        if (productName == null || productName.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        return new SLResponseOBJ<>(market.searchProductByName(productName));
    }


    @Override
    public SLResponseOBJ<List<Integer>> searchProductByName(List<Integer> lst, String productName) {
        if (productName == null || productName.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        if (lst == null || lst.size() == 0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByName(lst, productName);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByName(List<Integer> list, String name) {
        if (name == null || list == null)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByName(list, name));
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByDesc(String desc) {

        /**
         * @requirement II. 2 . 2
         * @param desc: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": List<Integer>- list of a products id
         *        }
         *
         * @documentation:
         * Search for products without focusing on a specific store, by product description.
         */
        if (desc == null || desc.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        return new SLResponseOBJ<>(market.searchProductByDesc(desc));
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByDesc(List<Integer> lst, String desc) {
        if (desc == null || desc.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        if (lst == null || lst.size() == 0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByDesc(lst, desc);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByDesc(List<Integer> list, String desc) {
        if (desc == null || list == null)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByDesc(list, desc));
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRate(int rate) {

        /**
         * @requirement II. 2 . 2
         * @param rate: int
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": List<Integer> - list of a products id
         *        }
         *
         * @documentation:
         * Search for products without focusing on a specific store, by product rate.
         */

        if (rate < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);

        return new SLResponseOBJ<>(market.searchProductByRate(rate));
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRate(List<Integer> lst, int rate) {
        if (rate < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (lst == null || lst.size() == 0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByRate(lst, rate);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByRate(List<Integer> list, int minRate) {
        if (minRate < 0 || list == null)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByRate(list, minRate));
    }


    @Override
    public SLResponseOBJ<List<Integer>> searchProductByCategory(int category) {

        /**
         * @requirement II. 2 . 2
         * @param category: int
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": List<Integer> - list of a products id
         *        }
         *
         * @documentation:
         * Search for products without focusing on a specific store, by product category.
         */

        if (category < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);

        return new SLResponseOBJ<>(market.searchProductByCategory(category));
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByCategory(List<Integer> lst, int category) {
        if (category < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (lst == null || lst.size() == 0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByCategory(lst, category);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByCategory(List<Integer> list, int category) {
        if (category < 0 || list == null)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByCategory(list, category));
    }


    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max) {

        /**
         * @requirement II. 2 . 2
         * @param productId: int
         * @param min: int
         * @param max: int

         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": List<Integer> - list of a products id
         *        }
         *
         * @documentation:
         * Search for products without focusing on a specific store, by product Range Prices.
         */
        if (productId < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (min < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (max < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);

        return new SLResponseOBJ<>(market.searchProductByRangePrices(productId, min, max));

    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByRangePrices(List<Integer> lst, int productId, int min, int max) {
        if (productId < 0 || min < 0 || max < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (lst == null || lst.size() == 0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByRangePrices(lst, productId, min, max);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByRangePrices(List<Integer> list, int min, int max) {
        if (min < 0 || max < 0 || list == null)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByRangePrices(list, min, max));
    }


    @Override
    public SLResponseOBJ<Boolean> addProductToShoppingBag(String userId, int storeId, int productId,
                                                          int quantity) {

        /**
         * @requirement II. 2 .3
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @param quantity: int
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean - Successfully completed
         *        }
         *
         * @documentation:
         * Save products in the buyer's shopping cart, for any store.
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);

        return new SLResponseOBJ<>(market.AddProductToShoppingBag(UUID.fromString(userId), storeId, productId, quantity));
    }

    public SLResponseOBJ<Integer> getStoreRate(String uuid, int Store) {
        if (Store < 0) return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (uuid == null || uuid.equals("")) return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);

        return new SLResponseOBJ<>(market.getStoreRate(UUID.fromString(uuid), Store));
    }

    public SLResponseOBJ<Boolean> newStoreRate(String uuid, int Store, int rate) {
        if (uuid == null || uuid.equals("")) return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);

        if (Store < 0) return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (rate < 0) return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);

        return new SLResponseOBJ<>(market.newStoreRate(UUID.fromString(uuid), Store, rate));
    }

    @Override
    public SLResponseOBJ<ServiceStore> getStoreInfo(int storeId) {
        if (storeId < 0) return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        DResponseObj<Store> s = market.getStore(storeId);
        if (s.errorOccurred()) return new SLResponseOBJ<>(null, s.getErrorMsg());
        return new SLResponseOBJ<>(new ServiceStore(market.getStore(storeId).value));
    }

    @Override
    public SLResponseOBJ<ServiceShoppingCart> getShoppingCart(String userId) {

        /**
         * @requirement II. 2 .4

         * @param userId: String

         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":ServiceShoppingCard
         *        }
         *
         * @documentation:
         * Checking the contents of the shopping cart .
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);

        DResponseObj<ShoppingCart> RShoppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShoppingCart.errorOccurred()) return new SLResponseOBJ<>(null, RShoppingCart.errorMsg);

        return new SLResponseOBJ<>(new ServiceShoppingCart(RShoppingCart.value), -1);
    }

    @Override
    public SLResponseOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {

        /**
         * @requirement II. 2 .4
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":ServiceShoppingCard
         *        }
         *
         * @documentation:
         * Delete product from the shopping cart
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);

        DResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new SLResponseOBJ<>(false, RShppingCart.getErrorMsg());
        DResponseObj<Boolean> res = RShppingCart.value.removeProductFromShoppingBag(storeId, productId);
        if (res.errorOccurred())
            return new SLResponseOBJ<>(res);

        return new SLResponseOBJ<>(true, -1);
    }

    @Override
    public SLResponseOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId,
                                                                int quantity) {

        /**
         * @requirement II. 2 .4
         *
         * @param userId: String
         * @param productId: int
         * @param storeId: int
         * @param quantity: int
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation:
         * Change product quantity from the shopping cart.
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (productId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);

        DResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new SLResponseOBJ<>(false, RShppingCart.getErrorMsg());

        DResponseObj<Boolean> res = RShppingCart.value.setProductQuantity(storeId, productId, quantity);
        if (res.errorOccurred())
            return new SLResponseOBJ<>(res);

        return new SLResponseOBJ<>(true, -1);
    }

    @Override
    public SLResponseOBJ<String> orderShoppingCart(String userId, String city, String adress, int apartment, ServiceCreditCard creditCard) {

        /**
         * @requirement II. 2 .5
         *
         * @param userId: String

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation:
         * Purchase of the shopping cart.
         * //next version : in accordance with the possible types of purchase and discount for guests, according to policy
         */
        if (userId == null ||
                userId.equals("") ||
                creditCard.creditCard == null ||
                creditCard.creditCard.equals("") ||
                creditCard.creditDate == null ||
                creditCard.creditDate.equals("") ||
                creditCard.pin == null ||
                creditCard.pin.equals(""))
            return new SLResponseOBJ<>(ErrorCode.NOTSTRING);
        DResponseObj<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> res =
                market.order(UUID.fromString(userId), city, adress, apartment, creditCard.creditCard, creditCard.creditDate, creditCard.pin);
        if (res.errorOccurred()) return new SLResponseOBJ<>(null, res.errorMsg);
        //TODO: make res.val to string of certificates external servieces.
        return new SLResponseOBJ<>("is ok", -1);
    }


    @Override
    public SLResponseOBJ<String> logout(String userId) {

        /**
         * @requirement II. 3.1
         *
         * @param userId: String

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation:
         * Cancellation of identification (logout:) Upon cancellation of identification of the buyer)
         * Subscriber-visitor (returns to be a guest.
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        DResponseObj<UUID> a = userManager.Logout(UUID.fromString(userId));
        return a.errorOccurred() ? new SLResponseOBJ<>(a.errorMsg) : new SLResponseOBJ<>(a.value.toString(), -1);
    }

    @Override
    public SLResponseOBJ<Boolean> changePassword(String uuid, String email, String password, String newPassword) {
        if (uuid == null) {
            return new SLResponseOBJ<>(false, ErrorCode.NOTVALIDINPUT);
        }
        if (email == null || email.equals("")) {
            return new SLResponseOBJ<>(false, ErrorCode.NOT_VALID_EMILE);
        }
        if (password == null || password.equals("")) {
            return new SLResponseOBJ<>(false, ErrorCode.NOT_VALID_PASSWORD);
        }
        if (newPassword == null || newPassword.equals("")) {
            return new SLResponseOBJ<>(false, ErrorCode.NOT_VALID_PASSWORD);
        }
        email = email.toLowerCase();
        DResponseObj<Boolean> res = userManager.changePassword(UUID.fromString(uuid), email, password, newPassword);
        if (res.errorOccurred()) {
            return new SLResponseOBJ<>(false, res.errorMsg);
        }
        return new SLResponseOBJ<>(res);
    }


    @Override
    public SLResponseOBJ<Integer> openNewStore(String userId, String name, String founder, ServiceDiscountPolicy
            discountPolicy, ServiceBuyPolicy buyPolicy, ServiceBuyStrategy buyStrategy) {

        /**
         * @requirement II. 3.2
         *
         * @param userId: String
         * @param name: String
         * @param founder: String
         * @param discountPolicy: DiscountPolicy
         * @param buyPolicy: BuyPolicy
         * @param buyStrategy: BuyStrategy

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: Opening a store: A subscriber of the market can open a store, and be the founder
         *  of the store (the store owner)
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(-1, ErrorCode.NOTSTRING);

        if (name == null || name.equals(""))
            return new SLResponseOBJ<>(-1, ErrorCode.NOTSTRING);

        if (founder == null || founder.equals(""))
            return new SLResponseOBJ<>(-1, ErrorCode.NOTSTRING);
        DResponseObj<Integer> res = market.OpenNewStore(UUID.fromString(userId), name, founder,
                new DiscountPolicy(discountPolicy), new BuyPolicy(buyPolicy));
        return new SLResponseOBJ<>(res.value, res.errorMsg);
    }

    @Override
    public SLResponseOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId,
                                                       double price, int quantity) {

        /**
         * @requirement II. 4.1
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @param price: double
         * @param quantity: int

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: Adding products
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (price < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        DResponseObj<Boolean> res = market.addNewProductToStore(UUID.fromString(userId), storeId, productId, price, quantity);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {

        /**
         * @requirement II. 4.1
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int


         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: removing products
         */

        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);

        return new SLResponseOBJ<>(market.deleteProductFromStore(UUID.fromString(userId), storeId, productId));
    }

    @Override
    public SLResponseOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId,
                                                         double price) {

        /**
         * @requirement II. 4.1
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @param price: double
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: changing products Price
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (price < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);


        return new SLResponseOBJ<>(market.setProductPriceInStore(UUID.fromString(userId), storeId, productId, price));
    }

    @Override
    public SLResponseOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId,
                                                            int quantity) {

        /**
         * @requirement II. 4.1
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @param quantity: int
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: changing products quantity
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.setProductQuantityInStore(UUID.fromString(userId), storeId, productId, quantity));
    }

    @Override
    public SLResponseOBJ<Boolean> addNewBuyRule(String userId, int storeId, BuyRuleSL buyRule) {

        /**
         * @requirement II. 4.2
         *
         * @param userId: String
         * @param storeId: int
         * @param buyRule: BuyRule

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: addNewBuyRule: A store owner may add new buy rule for his store. Users who are not store owners can't use this function
         *
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (buyRule == null)
            return new SLResponseOBJ<>(false, ErrorCode.BUY_RULE_IS_NULL);
        SLResponseOBJ<BuyRule> buyRuleConverted = buyRule.convertToBuyRuleDL();
        if (buyRuleConverted.errorOccurred()) return new SLResponseOBJ<>(buyRuleConverted.getErrorMsg());
        return new SLResponseOBJ<>(market.addNewBuyRule(UUID.fromString(userId), storeId, buyRuleConverted.value));
    }

    @Override
    public SLResponseOBJ<Boolean> removeBuyRule(String userId, int storeId, int buyRuleID) {
        /**
         * @requirement II. 4.2
         *
         * @param userId: String
         * @param storeId: int
         * @param buyRuleID: int

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: removeBuyRule: A store owner may remove buy rule from his store. Users who are not store owners can't use this function
         *
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0 || buyRuleID < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.removeBuyRule(UUID.fromString(userId), storeId, buyRuleID));
    }

    /**
     * @param userId
     * @param storeId
     * @param discountRule
     * @return
     * @requirement II. 4.2
     * @documentation: A store owner may add discount rule from his store. Users who are not store owners can't use this function
     */

    @Override
    public SLResponseOBJ<Boolean> addNewDiscountRule(String userId, int storeId, DiscountRuleSL discountRule) {
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (discountRule == null)
            return new SLResponseOBJ<>(false, ErrorCode.BUY_RULE_IS_NULL);
        SLResponseOBJ<DiscountRule> DiscountRuleConverted = discountRule.convertToDiscountRuleDL();
        if (DiscountRuleConverted.errorOccurred()) return new SLResponseOBJ<>(DiscountRuleConverted.getErrorMsg());
        return new SLResponseOBJ<>(market.addNewDiscountRule(UUID.fromString(userId), storeId, DiscountRuleConverted.value));
    }

    /**
     * @param userId
     * @param storeId
     * @param discountRuleID
     * @return
     * @requirement II. 4.2
     * @documentation: A store owner may remove discount rule from his store. Users who are not store owners can't use this function
     */
    @Override
    public SLResponseOBJ<Boolean> removeDiscountRule(String userId, int storeId, int discountRuleID) {
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0 || discountRuleID < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.removeDiscountRule(UUID.fromString(userId), storeId, discountRuleID));
    }

    /**
     * combine 2 or more rules by the operator and remove the rules
     *
     * @param userId
     * @param storeId
     * @param operator
     * @param rules
     * @param category
     * @param discount
     * @return
     */
    @Override
    public SLResponseOBJ<Boolean> combineANDORDiscountRules(String userId, int storeId, String operator, List<Integer> rules, int category, int discount) {
        if (userId == null || userId.equals("") || operator == null || operator.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (rules == null || rules.size() < 2)
            return new SLResponseOBJ<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
        if (storeId < 0 || category < 0 || discount < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.combineANDORDiscountRules(UUID.fromString(userId), storeId, operator, rules, category, discount));
    }


    /**
     * combine 2 or more rules to Xor and remove the rules
     *
     * @param userId
     * @param storeId
     * @param decision
     * @param rules
     * @return
     */
    public SLResponseOBJ<Boolean> combineXORDiscountRules(String userId, int storeId, String decision, List<Integer> rules) {
        if (userId == null || userId.equals("") || decision == null || decision.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (rules == null || rules.size() < 2 || !decision.equals("xor"))
            return new SLResponseOBJ<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.combineXorDiscountRules(UUID.fromString(userId), storeId, decision, rules));
    }

    /**
     * get buy policy of store (all buy rules)
     *
     * @param userId
     * @param storeId
     * @return list of buy rules or error if error occur
     */
    @Override
    public SLResponseOBJ<List<BuyRuleSL>> getBuyPolicy(String userId, int storeId) {
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(ErrorCode.NEGATIVENUMBER);
        DResponseObj<List<BuyRule>> buyPolicy = market.getBuyPolicy(UUID.fromString(userId), storeId);
        if (buyPolicy.errorOccurred()) return new SLResponseOBJ<>(buyPolicy.getErrorMsg());
        List<BuyRuleSL> converted = new ArrayList<>();
        for (BuyRule br : buyPolicy.value) {
            DResponseObj<BuyRuleSL> brSL = br.convertToBuyRuleSL();
            if (brSL.errorOccurred()) return new SLResponseOBJ<>(brSL.getErrorMsg());
            converted.add(brSL.value);
        }
        return new SLResponseOBJ<>(converted);
    }

    /**
     * get discount policy of store (all discount rules)
     *
     * @param userId
     * @param storeId
     * @return list of buy rules or error if error occur
     */
    @Override
    public SLResponseOBJ<List<DiscountRuleSL>> getDiscountPolicy(String userId, int storeId) {
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(ErrorCode.NEGATIVENUMBER);
        DResponseObj<List<DiscountRule>> discountPolicy = market.getDiscountPolicy(UUID.fromString(userId), storeId);
        if (discountPolicy.errorOccurred()) return new SLResponseOBJ<>(discountPolicy.getErrorMsg());
        List<DiscountRuleSL> converted = new ArrayList<>();
        for (DiscountRule dr : discountPolicy.value) {
            DResponseObj<DiscountRuleSL> drSL = dr.convertToDiscountRuleSL();
            if (drSL.errorOccurred()) return new SLResponseOBJ<>(drSL.getErrorMsg());
            converted.add(drSL.value);
        }
        return new SLResponseOBJ<>(converted);
    }


    @Override
    public SLResponseOBJ<Boolean> addNewStoreOwner(String userId, int storeId, String ownerEmail) {

        /**
         * @requirement II. 4.4
         *
         * @param userId: String
         * @param storeId: int
         * @param mangerEmil: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner may appoint a subscriber of the market (who is not yet the store owner)
         * For another store owner. An eight-member subscriber has a new store owner's policy rights And store management.
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (ownerEmail == null || ownerEmail.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        ownerEmail = ownerEmail.toLowerCase();
        return new SLResponseOBJ<>(market.addNewStoreOwner(UUID.fromString(userId), storeId, ownerEmail));
    }


    /**
     * remove store owner from store
     *
     * @param userId
     * @param storeId
     * @param ownerEmail
     * @return
     */
    @Override
    public SLResponseOBJ<Boolean> removeStoreOwner(String userId, int storeId, String ownerEmail) {
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (ownerEmail == null || ownerEmail.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        ownerEmail = ownerEmail.toLowerCase();
        return new SLResponseOBJ<>(market.removeStoreOwner(UUID.fromString(userId), storeId, ownerEmail));
    }

    @Override
    public SLResponseOBJ<Boolean> removeStoreMenager(String userId, int storeId, String menagerEmail) {
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (menagerEmail == null || menagerEmail.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        menagerEmail = menagerEmail.toLowerCase();
        return new SLResponseOBJ<>(market.removeStoreMenager(UUID.fromString(userId), storeId, menagerEmail));
    }

    public SLResponseOBJ<Integer> getRate(String uuid, int productTypeID) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        if (productTypeID < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.getRate(UUID.fromString(uuid), productTypeID));
    }


    public SLResponseOBJ<ServiceProductType> getProductTypeInfo(Integer productTypeId) {
        if (productTypeId < 0) {
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        }
        return new SLResponseOBJ<>(market.getProductTypeInfo(productTypeId));
    }

    public SLResponseOBJ<Boolean> setRate(String uuid, int productTypeID, int rate) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (productTypeID < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (rate < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.setRate(UUID.fromString(uuid), productTypeID, rate));
    }

    @Override
    public SLResponseOBJ<Boolean> addNewStoreManger(String userId, int storeId, String mangerEmil) {

        /**
         * @requirement II. 4.6
         *
         * @param userId: String
         * @param storeId: int
         * @param mangerEmil: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: Appointment of a store manager: A store owner may appoint a subscriber of the market who
         * is not yet a manager or owner the store
         *
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (mangerEmil == null || mangerEmil.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        return new SLResponseOBJ<>(market.addNewStoreManager(UUID.fromString(userId), storeId, mangerEmil));
    }

    @Override
    public SLResponseOBJ<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per, boolean onof) {

        /**
         * @requirement II. 4.7
         *
         * @param userId: String
         * @param storeId: int
         * @param mangerEmil: String
         * @param per: String
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: Changing the permissions of a store manager: A store owner may determine and change the management options
         * For a manager he has appointed. Each manager can set separate permissions.
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (mangerEmil == null || mangerEmil.equals("") || per == null)
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        permissionType.permissionEnum perm;
        try {
            perm = permissionType.permissionEnum.valueOf(per);
        } catch (Exception e) {
            return new SLResponseOBJ<>(false, ErrorCode.INVALID_PERMISSION_TYPE);
        }
        return new SLResponseOBJ<>(market.setManagerPermissions(UUID.fromString(userId), storeId, mangerEmil, perm, onof));
    }

    @Override
    public SLResponseOBJ<Boolean> closeStore(String userId, int storeId) {

        /**
         * @requirement II. 4.9
         *
         * @param userId: String
         * @param storeId: int

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: Store Closing: A store founder may close a store he has opened. When a store closes it becomes no
         * Active. Users who are not store owners or system manager cannot get information
         * Inactive (closed) store owners and managers of a closed store receive a notification of its closure, but
         * Their appointment was not harmed.
         */
        if (userId == null || userId.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);

        return new SLResponseOBJ<>(market.closeStore(UUID.fromString(userId), storeId));
    }

    @Override
    public SLResponseOBJ<HashMap<String, List<String>>> getStoreRoles(String userId, int storeId) {
        /**
         * @requirement II. 4.11
         *
         * @param userId: String
         * @param storeId: int

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: Request for information about positions in the store: A store owner can request and receive
         * information about the function holders In the store he owns and about permissions managers .
         */
//        if (userId == null || userId.equals(""))
//            return new DResponseObj<>("NOTSTRING");
//        if (storeId < 0)
//            return new DResponseObj<>("NEGATIVENUMBER");
        //need to chang -1 to UUID.fromString(userId)  after yaki fix his code
        return new SLResponseOBJ<>(market.getStoreRoles(UUID.fromString(userId), storeId));

    }

    /**
     * Cancel a membership in the market
     * This can only be done by the System manager
     * Note: if the user to cancel is the founder of a store then store will be removed from the market and Owners/Managers will be informed.
     *
     * @param uuid                 the uuid of the System manager
     * @param cancelMemberUsername the user to cancel
     * @return true if success, else false
     */
    @Override
    public SLResponseOBJ<Boolean> cancelMembership(String uuid, String cancelMemberUsername) {
        DResponseObj<List<Store>> res = userManager.cancelMembership(UUID.fromString(uuid), cancelMemberUsername);
        if (res.errorOccurred()) return new SLResponseOBJ<>(false, res.errorMsg);
        return market.deleteStoresFromMarket(res.value);
    }

    @Override
    public SLResponseOBJ<List<ServiceHistory>> getStoreOrderHistory(String userId, int storeId) {

        /**
         * @requirement II. 4.13
         *
         * @param userId: String
         * @param storeId: int

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":List<ServiceHistory>
         *        }
         *
         * @documentation: Receiving information on in-store purchase history
         */

        DResponseObj<List<History>> hist = market.getStoreOrderHistory(UUID.fromString(userId), storeId);
        SLResponseOBJ<List<History>> RHistory = new SLResponseOBJ<>(hist);
        if (RHistory.errorOccurred()) return new SLResponseOBJ<>(null, RHistory.errorMsg);
        List<ServiceHistory> ServiceHistoryList = new ArrayList<>();
        List<History> HistoryList = RHistory.getValue();

        for (History history : HistoryList) {
            ServiceHistoryList.add(new ServiceHistory(history));
        }
        return new SLResponseOBJ<>(ServiceHistoryList, -1);
    }

    @Override
    public SLResponseOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, String email) {
        /**
         * @requirement: ?????
         * @param userId: String
         * @param storeId: int

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":List<ServiceHistory>
         *        }
         *
         * @documentation:
         */
        email = email.toLowerCase();
        DResponseObj<List<List<History>>> h = market.getUserInfo(userID, email);
        if (h.errorOccurred()) return new SLResponseOBJ<>(null, h.errorMsg);
        List<List<ServiceHistory>> ServiceHistoryList = new ArrayList<>();
        List<List<History>> HistoryList = h.getValue();


        for (List<History> historyList : HistoryList) {
            List<ServiceHistory> s = new LinkedList<>();
            for (History history : historyList) {
                s.add(new ServiceHistory(history));
            }
            ServiceHistoryList.add(s);
        }
        return new SLResponseOBJ<>(ServiceHistoryList, -1);
    }

    /**
     * gets all current logged in members.
     *
     * @param uuid
     * @return
     */
    @Override
    public SLResponseOBJ<List<ServiceUser>> getloggedInMembers(String uuid) {
        if (uuid == null || uuid.equals("")) return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<ConcurrentHashMap<UUID, User>> loggedInMems = userManager.getloggedInMembers(UUID.fromString(uuid));
        if (loggedInMems.errorOccurred()) return new SLResponseOBJ<>(null, loggedInMems.errorMsg);
        List<ServiceUser> users = new ArrayList<>();
        loggedInMems.value.forEach((uid, user) -> {
            users.add(new ServiceUser(user));
        });
        return new SLResponseOBJ<>(users);
    }

    /**
     * gets all current loged out members
     *
     * @param uuid
     * @return
     */
    @Override
    public SLResponseOBJ<List<ServiceUser>> getloggedOutMembers(String uuid) {
        if (uuid == null || uuid.equals("")) return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<ConcurrentHashMap<String, User>> loggedOutMems = userManager.getloggedOutMembers(UUID.fromString(uuid));
        if (loggedOutMems.errorOccurred()) return new SLResponseOBJ<>(null, loggedOutMems.errorMsg);
        List<ServiceUser> users = new ArrayList<>();
        loggedOutMems.value.forEach((uid, user) -> {
            users.add(new ServiceUser(user));
        });
        return new SLResponseOBJ<>(users);
    }

    @Override
    public SLResponseOBJ<List<ServiceStore>> getAllStores() {
        DResponseObj<List<Store>> stores = market.getAllStores();
        if (stores.errorOccurred()) return new SLResponseOBJ<>(null, stores.errorMsg);
        List<ServiceStore> all = new ArrayList<>();
        stores.value.forEach(store -> {
            all.add(new ServiceStore(store));
        });
        return new SLResponseOBJ<>(all, -1);
    }

    @Override
    public SLResponseOBJ<List<ServiceProductType>> getAllProducts() {
        List<ProductType> lst = market.getAllProductTypes().value;
        List<ServiceProductType> out = new ArrayList<>();
        lst.forEach(productType -> {
            out.add(new ServiceProductType(productType));
        });
        return new SLResponseOBJ<>(out, -1);
    }

    @Override
    public SLResponseOBJ<List<ServiceProductStore>> getAllProductsInStore(int storeID) {
        DResponseObj<List<ProductStore>> products = market.getAllProductsInStore(storeID);
        if (products.errorOccurred()) return new SLResponseOBJ<>(null, products.errorMsg);
        List<ServiceProductStore> lst = new ArrayList<>();
        products.value.forEach(productStore -> {

            lst.add(new ServiceProductStore(productStore));
        });
        return new SLResponseOBJ<>(lst, -1);
    }

    public UserManager getUserManager() {
        return userManager;
    }


    public SLResponseOBJ<Integer> addNewProductType(String uuid, String name, String description, int category) {
        if (name == null || name.equals("") || description == null || description.equals("") || category < 0) {
            return new SLResponseOBJ<>(-1, ErrorCode.NOTVALIDINPUT);
        }
        DResponseObj<Integer> res = market.addNewProductType(UUID.fromString(uuid), name, description, category);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(int rate) {
        /**
         * @requirement: ?????
         * @param userId: String
         * @param storeId: int

         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":List<ServiceHistory>
         *        }
         *
         * @documentation:
         */
        if (rate < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        SLResponseOBJ<List<Integer>> RProductByStoreRate = new SLResponseOBJ<>(market.searchProductByStoreRate(rate));
        if (RProductByStoreRate.errorOccurred()) return new SLResponseOBJ<>(null, RProductByStoreRate.errorMsg);

        return new SLResponseOBJ<>(RProductByStoreRate.value, -1);
    }

    @Override
    public SLResponseOBJ<List<Integer>> searchProductByStoreRate(List<Integer> lst, int rate) {
        if (rate < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        if (lst == null || lst.size() == 0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByStoreRate(lst, rate);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByStoreRate(List<Integer> list, int minRate) {
        if (minRate < 0 || list == null)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByStoreRate(list, minRate));
    }


    public SLResponseOBJ<Boolean> isOwner(String email, int storeId) {
        email = email.toLowerCase();
        DResponseObj<Store> s = market.getStore(storeId);
        if (s.errorOccurred()) {
            return new SLResponseOBJ<>(false, s.errorMsg);
        }
        DResponseObj<Boolean> res = userManager.isOwner(email, s.value);
        return new SLResponseOBJ<>(res);
    }

    public SLResponseOBJ<Boolean> isManager(String email, int storeId) {
        email = email.toLowerCase();
        DResponseObj<Store> s = market.getStore(storeId);
        if (s.errorOccurred()) {
            return new SLResponseOBJ<>(false, s.errorMsg);
        }
        DResponseObj<Boolean> res = userManager.isManager(email, s.value);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<Boolean> isOwnerUUID(String uuid, int storeId) {
        DResponseObj<User> res = userManager.getOnlineUser(UUID.fromString(uuid));
        if (res.errorOccurred()) return new SLResponseOBJ<>(false, res.errorMsg);
        User user = res.value;
        return isOwner(user.getEmail().value, storeId);
    }

    @Override
    public SLResponseOBJ<Boolean> isManagerUUID(String uuid, int storeId) {
        DResponseObj<User> res = userManager.getOnlineUser(UUID.fromString(uuid));
        if (res.errorOccurred()) return new SLResponseOBJ<>(false, res.errorMsg);
        User user = res.value;
        return isManager(user.getEmail().value, storeId);
    }

    @Override
    public SLResponseOBJ<Boolean> isSystemManagerUUID(String uuid) {
        DResponseObj<User> res = userManager.getOnlineUser(UUID.fromString(uuid));
        if (res.errorOccurred()) return new SLResponseOBJ<>(false, res.errorMsg);
        User user = res.value;
        return new SLResponseOBJ<>(PermissionManager.getInstance().isSystemManager(user.getEmail().value));
    }


    /**
     * create BID - only for users
     *
     * @param uuid
     * @param storeID
     * @param productID
     * @param quantity
     * @param totalPrice
     * @return succuess or not
     */
    @Override
    public SLResponseOBJ<Boolean> createBID(String uuid, int storeID, int productID, int quantity, int totalPrice) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0 || totalPrice < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        if (quantity < 1)
            return new SLResponseOBJ<>(false, ErrorCode.INVALIDQUANTITY);
        DResponseObj<Boolean> res = market.createBID(UUID.fromString(uuid), storeID, productID, quantity, totalPrice);
        return res.errorOccurred() ? new SLResponseOBJ<>(false, res.errorMsg) : new SLResponseOBJ<>(res.value);
    }

    /**
     * remove BID
     *
     * @param uuid
     * @param storeID
     * @param productID
     * @return true if success else false with error msg
     */
    @Override
    public SLResponseOBJ<Boolean> removeBID(String uuid, int storeID, int productID) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        DResponseObj<Boolean> res = market.removeBID(UUID.fromString(uuid), storeID, productID);
        return res.errorOccurred() ? new SLResponseOBJ<>(false, res.errorMsg) : new SLResponseOBJ<>(res.value);
    }

    /**
     * a owner or manager with permission want to approve BID
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return true if success else false with error msg
     */
    @Override
    public SLResponseOBJ<Boolean> approveBID(String uuid, String userEmail, int storeID, int productID) {
        if (uuid == null || uuid.equals("") || userEmail == null || userEmail.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        userEmail = userEmail.toLowerCase();
        DResponseObj<Boolean> res = market.approveBID(UUID.fromString(uuid), userEmail, storeID, productID);
        return res.errorOccurred() ? new SLResponseOBJ<>(false, res.errorMsg) : new SLResponseOBJ<>(res.value);
    }

    /**
     * a owner or manager with permission want to reject BID
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return true if success else false with error msg
     */
    @Override
    public SLResponseOBJ<Boolean> rejectBID(String uuid, String userEmail, int storeID, int productID) {
        if (uuid == null || uuid.equals("") || userEmail == null || userEmail.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        userEmail = userEmail.toLowerCase();
        DResponseObj<Boolean> res = market.rejectBID(UUID.fromString(uuid), userEmail, storeID, productID);
        return res.errorOccurred() ? new SLResponseOBJ<>(false, res.errorMsg) : new SLResponseOBJ<>(res.value);
    }

    /**
     * a owner or manager with permission want to counter BID
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @param newTotalPrice
     * @return true if success else false with error msg
     */
    @Override
    public SLResponseOBJ<Boolean> counterBID(String uuid, String userEmail, int storeID, int productID, int newTotalPrice) {
        if (uuid == null || uuid.equals("") || userEmail == null || userEmail.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0 || newTotalPrice < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        userEmail = userEmail.toLowerCase();
        DResponseObj<Boolean> res = market.counterBID(UUID.fromString(uuid), userEmail, storeID, productID, newTotalPrice);
        return res.errorOccurred() ? new SLResponseOBJ<>(false, res.errorMsg) : new SLResponseOBJ<>(res.value);
    }

    /**
     * a user want to response his countered BID
     *
     * @param uuid
     * @param storeID
     * @param productID
     * @param approve
     * @return true if success else false with error msg
     */
    @Override
    public SLResponseOBJ<Boolean> responseCounterBID(String uuid, int storeID, int productID, boolean approve) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0)
            return new SLResponseOBJ<>(false, ErrorCode.NEGATIVENUMBER);
        DResponseObj<Boolean> res = market.responseCounterBID(UUID.fromString(uuid), storeID, productID, approve);
        return res.errorOccurred() ? new SLResponseOBJ<>(false, res.errorMsg) : new SLResponseOBJ<>(res.value);
    }

    /**
     * a owner or manager with permission want to get status of BID
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return approves list or error msg
     */
    @Override
    public SLResponseOBJ<String> getBIDStatus(String uuid, String userEmail, int storeID, int productID) {
        if (uuid == null || uuid.equals("") || userEmail == null || userEmail.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        userEmail = userEmail.toLowerCase();
        DResponseObj<String> res = market.getBIDStatus(UUID.fromString(uuid), userEmail, storeID, productID);
        return res.errorOccurred() ? new SLResponseOBJ<>(null, res.errorMsg) : new SLResponseOBJ<>("res.value");
    }

    /**
     * get all bids in the store if has permission
     *
     * @param uuid
     * @param storeID
     * @return list of bids or error msg
     */
    @Override
    public SLResponseOBJ<HashMap<Integer, List<ServiceBID>>> getAllOffersBIDS(String uuid, int storeID) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        if (storeID < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        DResponseObj<HashMap<Integer, List<BID>>> res = market.getAllOffersBIDs(UUID.fromString(uuid), storeID);
        if (res.errorOccurred()) return new SLResponseOBJ<>(null, res.errorMsg);
        HashMap<Integer, List<ServiceBID>> allBIDs = new HashMap<>();
        for (Map.Entry<Integer, List<BID>> entry : res.value.entrySet()) {
            Integer productID = entry.getKey();
            List<BID> dBIDSs = entry.getValue();
            List<ServiceBID> sBIDs = new ArrayList<>();
            dBIDSs.forEach(b -> {
                ServiceBID serviceBID = new ServiceBID(b);
                sBIDs.add(serviceBID);
            });
            allBIDs.put(productID, sBIDs);
        }
        return new SLResponseOBJ<>(allBIDs);
    }

    /**
     * get all bids of user in the store
     *
     * @param uuid
     * @param storeID
     * @return list of bids or error msg
     */
    @Override
    public SLResponseOBJ<List<ServiceBID>> getMyBIDs(String uuid, int storeID) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        if (storeID < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        DResponseObj<List<BID>> res = market.getMyBIDs(UUID.fromString(uuid), storeID);
        if (res.errorOccurred()) return new SLResponseOBJ<>(null, res.errorMsg);
        List<ServiceBID> bids = new ArrayList<>();
        res.value.forEach(b -> {
            ServiceBID serviceBID = new ServiceBID(b);
            bids.add(serviceBID);
        });
        return new SLResponseOBJ<>(bids);
    }

    /**
     * reopen store that close by founder
     *
     * @param uuid
     * @param storeID
     * @return true if success else error msg
     */
    @Override
    public SLResponseOBJ<Boolean> reopenStore(String uuid, int storeID) {
        if (uuid == null || uuid.equals(""))
            return new SLResponseOBJ<>(null, ErrorCode.NOTSTRING);
        if (storeID < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        DResponseObj<Boolean> reopen = market.reopenStore(UUID.fromString(uuid), storeID);
        return reopen.errorOccurred() ? new SLResponseOBJ<>(false, reopen.errorMsg) : new SLResponseOBJ<>(reopen);
    }

    /**
     * * user want to buy BID that approved
     *
     * @param userId
     * @param storeID
     * @param productID
     * @param city
     * @param adress
     * @param apartment
     * @param creditCard
     * @return true if success else error msg
     */
    @Override
    public SLResponseOBJ<Boolean> BuyBID(String userId, int storeID, int productID, String city, String adress, int apartment, ServiceCreditCard creditCard) {
        if (userId == null ||
                userId.equals("") ||
                creditCard.creditCard == null ||
                creditCard.creditCard.equals("") ||
                creditCard.creditDate == null ||
                creditCard.creditDate.equals("") ||
                creditCard.pin == null ||
                creditCard.pin.equals(""))
            return new SLResponseOBJ<>(ErrorCode.NOTSTRING);
        if (storeID < 0 || productID < 0)
            return new SLResponseOBJ<>(null, ErrorCode.NEGATIVENUMBER);
        DResponseObj<Boolean> res =
                market.buyBID(UUID.fromString(userId), storeID, productID, city, adress, apartment, creditCard.creditCard, creditCard.creditDate, creditCard.pin);
        return res.errorOccurred() ? new SLResponseOBJ<>(null, res.errorMsg) : new SLResponseOBJ<>(res.value);
    }

    @Override
    public SLResponseOBJ<List<String>> getAllMembers(String userId) {
        DResponseObj<List<String>> res = userManager.getAllMembers(UUID.fromString(userId));
        if (res.errorOccurred()) return new SLResponseOBJ<>(null, res.errorMsg);
        return new SLResponseOBJ<List<String>>(res);
    }

    @Override
    public SLResponseOBJ<List<HashMap<String, Object>>> getAllusers() {
        return new SLResponseOBJ<>(userManager.getAllusers());
    }
    //db test only
    public void deleteAllMembers() {
        userManager.deleteAllMembers();
    }

    public void loadMembers(){
        userManager.load();
    }

    public boolean isMember2(String user){
        return userManager.isMember(user).value;
    }
}