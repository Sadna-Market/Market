package com.example.demo.Service;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.Market;
import com.example.demo.Domain.Market.PermissionManager;
import com.example.demo.Domain.Market.permissionType;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.*;
import com.example.demo.Domain.UserModel.ShoppingCart;
import com.example.demo.Domain.UserModel.UserManager;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Facade implements IMarket {
    UserManager userManager;
    Market market;
    public Facade() {
        this.userManager = new UserManager();
        this.market = new Market(this.userManager);
    }

    @Override
    public SLResponseOBJ<String> initMarket(String email, String Password, String phoneNumber) {
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
        SLResponseOBJ<String> guestUUID = guestVisit();
        if (guestUUID.errorOccurred()) return new SLResponseOBJ<>(guestUUID.value, guestUUID.errorMsg);

        SLResponseOBJ<Boolean> addedSysManager = addNewMember(guestUUID.value, email, Password, phoneNumber);
        if (addedSysManager.errorOccurred()) return new SLResponseOBJ<>(null, addedSysManager.errorMsg);
        PermissionManager permissionManager = PermissionManager.getInstance();

        DResponseObj<Boolean> setSysManagerPermission = permissionManager.setSystemManager(email);
        if (setSysManagerPermission.errorOccurred()) return new SLResponseOBJ<>(null, setSysManagerPermission.errorMsg);

        DResponseObj<Boolean> initiateExternalServices = market.init();
        if (initiateExternalServices.errorOccurred() && initiateExternalServices.errorMsg != 50)
            return new SLResponseOBJ<>(null, initiateExternalServices.errorMsg); //nivvvvvvvvvvvvvvvvvvvvvvvvv !!!!!
        /// the problem was from hear do with it watever you want
        //when you try to connect again to the conection service its throw you with message error
        //hara ahusharmuta
        //&&initiateExternalServices.errorMsg!=50 this is that i add

        return new SLResponseOBJ<>(guestUUID.value, -1);
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
    public SLResponseOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber) {
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
        DResponseObj<Boolean> res = userManager.AddNewMember(UUID.fromString(uuid), email, Password, phoneNumber);
        return new SLResponseOBJ<>(res.value, res.errorMsg);
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
        if (lst == null || lst.size()==0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByName(lst, productName);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByName(List<Integer> list, String name) {
        if(name == null || list == null)
            return new SLResponseOBJ<>(null,ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByName(list,name));
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
        if (lst == null || lst.size()==0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByDesc(lst, desc);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByDesc(List<Integer> list, String desc) {
        if(desc == null || list == null)
            return new SLResponseOBJ<>(null,ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByDesc(list,desc));
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
        if (lst == null || lst.size()==0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByRate(lst, rate);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByRate(List<Integer> list, int minRate) {
        if(minRate < 0 || list == null)
            return new SLResponseOBJ<>(null,ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByRate(list,minRate));
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
        if (lst == null || lst.size()==0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByCategory(lst, category);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByCategory(List<Integer> list, int category) {
        if(category < 0 || list == null)
            return new SLResponseOBJ<>(null,ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByCategory(list,category));
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
        if (lst == null || lst.size()==0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByRangePrices(lst, productId, min, max);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByRangePrices(List<Integer> list, int min, int max) {
        if(min < 0 || max < 0 ||  list == null)
            return new SLResponseOBJ<>(null,ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByRangePrices(list,min,max));
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
        DResponseObj<Boolean> res= RShppingCart.value.removeProductFromShoppingBag(storeId, productId);
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

        DResponseObj<Boolean> res= RShppingCart.value.setProductQuantity(storeId, productId, quantity);
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
        UUID uid = userManager.Logout(UUID.fromString(userId)).value;
        return new SLResponseOBJ<>(uid.toString(), -1);
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
                new DiscountPolicy(discountPolicy), new BuyPolicy(buyPolicy), new BuyStrategy(buyStrategy));
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

    // TODO  II. 4.2  שינוי סוגי וכללי )מדיניות( קניה והנחה של חנות


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
        return new SLResponseOBJ<>(market.addNewStoreOwner(UUID.fromString(userId), storeId, ownerEmail));
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
        try{
            perm = permissionType.permissionEnum.valueOf(per);
        }catch (Exception e){
            return new SLResponseOBJ<>(false,ErrorCode.INVALID_PERMISSION_TYPE);
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

    public SLResponseOBJ<Integer> addNewProductType(String uuid, String name, String description, int category) {
        if (name == null || name.equals("") || description == null || description.equals("") || category < 0) {
            return new SLResponseOBJ<>(-1, ErrorCode.NOTVALIDINPUT);
        }
        DResponseObj<Integer> res = market.addNewProductType(UUID.fromString(uuid), name, description, category);
        return new SLResponseOBJ<>(res.value, -1);
    }

    @Override //TODO way search product ? if you return stores ??
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
        if (lst == null || lst.size()==0)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        DResponseObj<List<Integer>> res = market.searchProductByStoreRate(lst, rate);
        return new SLResponseOBJ<>(res);
    }

    @Override
    public SLResponseOBJ<List<Integer>> filterByStoreRate(List<Integer> list, int minRate) {
        if(minRate < 0 || list == null)
            return new SLResponseOBJ<>(null, ErrorCode.NOTVALIDINPUT);
        return new SLResponseOBJ<>(market.filterByStoreRate(list,minRate));
    }
}