package main.Service;

import main.ErrorCode;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Market.PermissionManager;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.Response.DResponseObj;

import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.UserManager;

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
    public SLResponsOBJ<String> initMarket(String email, String Password, String phoneNumber) {
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
        SLResponsOBJ<String> guestUUID = guestVisit();
        if (guestUUID.errorOccurred()) return new SLResponsOBJ<>(guestUUID.value,guestUUID.errorMsg);

        SLResponsOBJ<Boolean> addedSysManager = addNewMember(guestUUID.value, email, Password, phoneNumber);
        if (addedSysManager.errorOccurred()) return new SLResponsOBJ<>(null,addedSysManager.errorMsg);
        PermissionManager permissionManager = PermissionManager.getInstance();

        DResponseObj<Boolean> setSysManagerPermission = permissionManager.setSystemManager(email);
        if (setSysManagerPermission.errorOccurred()) return new SLResponsOBJ<>(null,setSysManagerPermission.errorMsg);

        DResponseObj<Boolean> initiateExternalServices = market.init();
        if(initiateExternalServices.errorOccurred()) return new SLResponsOBJ<>(null,initiateExternalServices.errorMsg);

        return new SLResponsOBJ<>(guestUUID.value,-1);
    }




    //---------------------------------------1 .פעולות כלליות של מבקר-אורח:-------------------------------------------
    @Override
    public SLResponsOBJ<String> guestVisit() {
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
        return new SLResponsOBJ<>(res.value.toString(),-1);
    }

    @Override
    public SLResponsOBJ<Boolean> guestLeave(String guestId) {

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
        if (guestId == null || guestId.equals("")) return new SLResponsOBJ<>(false, ErrorCode.NOTSTRING);
        return new SLResponsOBJ<>(userManager.GuestLeave(UUID.fromString(guestId)));
    }

    // return new DResponseObj<>(false, "grantor can be null only in case that open new store");
    @Override
    public SLResponsOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber) {
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
        if (uuid == null || uuid.equals(""))
            return new SLResponsOBJ<>(false, ErrorCode.NOTSTRING);

        if (email == null || email.equals(""))
            return new SLResponsOBJ<>(false, ErrorCode.NOTSTRING);

        if (phoneNumber == null || phoneNumber.equals(""))
            return new SLResponsOBJ<>(false, ErrorCode.NOTSTRING);
        DResponseObj<Boolean> res =userManager.AddNewMember(UUID.fromString(uuid), email, Password, phoneNumber);
        return new SLResponsOBJ<>(res.value,res.errorMsg);
    }

    @Override
    public SLResponsOBJ<String> login(String userId, String email, String password) {

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
            return new SLResponsOBJ<>(null, ErrorCode.NOTMEMBER);

        if (email == null || email.equals(""))
            return new SLResponsOBJ<>(null, ErrorCode.NOTSTRING);

        if (password == null || password.equals(""))
            return new SLResponsOBJ<>(null, ErrorCode.NOTSTRING);
        DResponseObj<UUID> res = userManager.Login(UUID.fromString(userId), email, password);
                return res.errorOccurred()? new SLResponsOBJ<>(res.errorMsg): new SLResponsOBJ<>(res.value.toString(),-1);
    }
//-----------------------------------2 .פעולות קנייה של מבקר-אורח-----------------------------------------------

    @Override
    public SLResponsOBJ<String> getInfoProductInStore(int storeId, int productID) {

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
            return new SLResponsOBJ<>(ErrorCode.NEGATIVENUMBER);
        if (productID < 0)
            return new SLResponsOBJ<>(ErrorCode.NEGATIVENUMBER);
        return new SLResponsOBJ<>(market.getInfoProductInStore(storeId, productID));
    }

    @Override
    public SLResponsOBJ<ServiceStore> getStore(int storeId) {
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
        SLResponsOBJ<Store> storeR = new SLResponsOBJ<>(market.getStore(storeId));
        if (storeR.errorOccurred()) return new SLResponsOBJ<>(storeR.errorMsg);

        return new SLResponsOBJ<>(new ServiceStore(storeR.value),-1);
    }

//TODO GET INFO ABOUT STORES IN THE MARKET



    @Override
    public SLResponsOBJ<List<Integer>> searchProductByName(String productName) {

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
            return new SLResponsOBJ<>(null,ErrorCode.NOTSTRING);
        return new SLResponsOBJ<>(market.searchProductByName(productName));
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByDesc(String desc) {

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
            return new SLResponsOBJ<>(null,ErrorCode.NOTSTRING);
        return new SLResponsOBJ<>(market.searchProductByDesc(desc));
    }

    @Override
    public SLResponsOBJ<List<Integer>> searchProductByRate(int rate) {

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
            return new SLResponsOBJ<>(null,ErrorCode.NEGATIVENUMBER);

        return new SLResponsOBJ<>(market.searchProductByRate(rate));
    }




    @Override
    public SLResponsOBJ<List<Integer>> searchProductByCategory(int category) {

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
            return new SLResponsOBJ<>(null,ErrorCode.NEGATIVENUMBER);

        return new SLResponsOBJ<>(market.searchProductByCategory(category));
    }


    @Override
    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max) {

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
            return new SLResponsOBJ<>(null,ErrorCode.NEGATIVENUMBER);
        if (min < 0)
            return new SLResponsOBJ<>(null,ErrorCode.NEGATIVENUMBER);
        if (max < 0)
            return new SLResponsOBJ<>(null,ErrorCode.NEGATIVENUMBER);

        return new SLResponsOBJ<>(market.searchProductByRangePrices(productId, min, max));

    }


    @Override
    public SLResponsOBJ<Boolean> addProductToShoppingBag(String userId, int storeId, int productId,
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
            return new SLResponsOBJ<>(false, ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);

        return new SLResponsOBJ<>(market.AddProductToShoppingBag(UUID.fromString(userId), storeId, productId, quantity));
    }

    @Override
    public SLResponsOBJ<ServiceShoppingCard> getShoppingCart(String userId) {

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
            return new SLResponsOBJ<>(null,ErrorCode.NOTSTRING);

        DResponseObj<ShoppingCart> RShoppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShoppingCart.errorOccurred()) return new SLResponsOBJ<>(null,RShoppingCart.errorMsg);

        return new SLResponsOBJ<>(new ServiceShoppingCard(RShoppingCart.value),-1);
    }

    @Override
    public SLResponsOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {

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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);

        DResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new SLResponsOBJ<>(false, RShppingCart.getErrorMsg());

        if (!RShppingCart.value.removeProductFromShoppingBag(storeId, productId).value)
            return new SLResponsOBJ<>(false,-1);

        return new SLResponsOBJ<>(true,-1);
    }

    @Override
    public SLResponsOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId,
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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (productId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);

        DResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new SLResponsOBJ<>(false, RShppingCart.getErrorMsg());

        if (!RShppingCart.value.setProductQuantity(storeId, productId, quantity).value)
            return new SLResponsOBJ<>(false,-1);

        return new SLResponsOBJ<>(true,-1);
    }

    @Override
    public SLResponsOBJ<String> orderShoppingCart(String userId, String city, String adress,int apartment ,ServiceCreditCard creditCard) {

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
        if (userId == null || userId.equals("") || creditCard.CreditCard == null || creditCard.CreditCard.equals("")||creditCard.CreditDate==null||creditCard.CreditDate.equals("")||creditCard.pin==null||creditCard.pin.equals(""))
            return new SLResponsOBJ<>(ErrorCode.NOTSTRING);
        DResponseObj<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> res = market.order(UUID.fromString(userId),city,adress,apartment , creditCard.CreditCard,creditCard.CreditDate,creditCard.pin);
        if(res.errorOccurred()) return new SLResponsOBJ<>(null,res.errorMsg);
        //TODO: make res.val to string of certificates external servieces.
        return new SLResponsOBJ<>("is ok",-1);
    }

    @Override
    public SLResponsOBJ<String> logout(String userId) {

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
            return new SLResponsOBJ<>(null,ErrorCode.NOTSTRING);
        UUID uid = userManager.Logout(UUID.fromString(userId)).value;
        return new SLResponsOBJ<>(uid.toString(),-1);
    }

    @Override
    public SLResponsOBJ<BitSet> changePassword(String email) {
        return null;
    }


    @Override
    public SLResponsOBJ<Integer> openNewStore(String userId, String name, String founder, DiscountPolicy
            discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {

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
            return new SLResponsOBJ<>(-1,ErrorCode.NOTSTRING);

        if (name == null || name.equals(""))
            return new SLResponsOBJ<>(-1,ErrorCode.NOTSTRING);

        if (founder == null || founder.equals(""))
            return new SLResponsOBJ<>(-1,ErrorCode.NOTSTRING);
        DResponseObj<Integer> res = market.OpenNewStore(UUID.fromString(userId), name, founder, discountPolicy, buyPolicy, buyStrategy);
        return new SLResponsOBJ<>(res.value,res.errorMsg);
    }

    @Override
    public SLResponsOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId,
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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (price < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        DResponseObj<Boolean> res = market.addNewProductToStore(UUID.fromString(userId), storeId, productId, price, quantity);
        return new SLResponsOBJ<>(res);
    }

    @Override
    public SLResponsOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {

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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);

        return new SLResponsOBJ<>(market.deleteProductFromStore(UUID.fromString(userId), storeId, productId));
    }

    @Override
    public SLResponsOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId,
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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (price < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);


        return new SLResponsOBJ<>(market.setProductPriceInStore(UUID.fromString(userId), storeId, productId, price));
    }

    @Override
    public SLResponsOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId,
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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (productId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        if (quantity < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        return new SLResponsOBJ<>(market.setProductQuantityInStore(UUID.fromString(userId), storeId, productId, quantity));
    }

    // TODO  II. 4.2  שינוי סוגי וכללי )מדיניות( קניה והנחה של חנות


    @Override
    public SLResponsOBJ<Boolean> addNewStoreOwner(String userId, int storeId, String ownerEmail) {

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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (ownerEmail == null || ownerEmail.equals(""))
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        return new SLResponsOBJ<>(market.addNewStoreOwner(UUID.fromString(userId), storeId, ownerEmail));
    }

    @Override
    public SLResponsOBJ<Boolean> addNewStoreManger(String userId, int storeId, String mangerEmil) {

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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (mangerEmil == null || mangerEmil.equals(""))
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);
        return new SLResponsOBJ<>(market.addNewStoreManager(UUID.fromString(userId), storeId, mangerEmil));
    }

    @Override
    public SLResponsOBJ<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per ,boolean onof) {

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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (mangerEmil == null || mangerEmil.equals(""))
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);

        return new SLResponsOBJ<>(market.setManagerPermissions(UUID.fromString(userId), storeId, mangerEmil, permissionType.permissionEnum.valueOf(per),onof));
    }

    @Override
    public SLResponsOBJ<Boolean> closeStore(String userId, int storeId) {

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
            return new SLResponsOBJ<>(false,ErrorCode.NOTSTRING);
        if (storeId < 0)
            return new SLResponsOBJ<>(false,ErrorCode.NEGATIVENUMBER);

        return new SLResponsOBJ<>(market.closeStore(UUID.fromString(userId), storeId));
    }

    @Override
    public SLResponsOBJ<HashMap<String,List<String>>> getStoreRoles(String userId, int storeId) {
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
        return new SLResponsOBJ<>(market.getStoreRoles(UUID.fromString(userId), storeId));

    }

    @Override
    public SLResponsOBJ<List<ServiceHistory>> getStoreOrderHistory(String userId, int storeId) {

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
        SLResponsOBJ<List<History>> RHistory =new SLResponsOBJ<>(market.getStoreOrderHistory(UUID.fromString(userId), storeId));
        if (RHistory.errorOccurred()) return new SLResponsOBJ<>(null,RHistory.errorMsg);
        List<ServiceHistory> ServiceHistoryList = new ArrayList<>();
        List<History> HistoryList = RHistory.getValue();

        for (History history : HistoryList) {
            ServiceHistoryList.add(new ServiceHistory(history));
        }
        return new SLResponsOBJ<>(ServiceHistoryList,-1);
    }

    @Override
    public SLResponsOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, String email){
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

        DResponseObj<List<List<History>>> h =  market.getUserInfo(userID,email);
        if (h.errorOccurred()) return new SLResponsOBJ<>(null,h.errorMsg);
        List<List<ServiceHistory>> ServiceHistoryList = new ArrayList<>();
        List<List<History>> HistoryList = h.getValue();

        for (List<History> historyList : HistoryList) {
            List<ServiceHistory> s = new LinkedList<>();
            for (History history : historyList ){
                s.add(new ServiceHistory(history));
            }
            ServiceHistoryList.add(s);
        }
        return new SLResponsOBJ<>(ServiceHistoryList,-1);
    }

    public SLResponsOBJ<Integer> addNewProductType(String uuid, String name , String description, int category)
    {
        if(name==null||name.equals("")||description==null||description.equals("")||category<0){
            return new SLResponsOBJ<>(-1,ErrorCode.NOTVALIDINPUT);
        }
        DResponseObj<Integer> res = market.addNewProductType(UUID.fromString(uuid),name,description,category);
        return new SLResponsOBJ<>( res.value,-1);
    }

    @Override //TODO way search product ? if you return stores ??
    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(int rate) {
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
        SLResponsOBJ<List<Integer>> RProductByStoreRate =new SLResponsOBJ<>( market.searchProductByStoreRate(rate));
        if (RProductByStoreRate.errorOccurred()) return new SLResponsOBJ<>(null,RProductByStoreRate.errorMsg);

        return new SLResponsOBJ<>(RProductByStoreRate.value,-1);
    }

}
