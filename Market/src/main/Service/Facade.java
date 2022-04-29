package main.Service;

import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Market.PermissionManager;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.Response.DResponseObj;

import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Facade implements IMarket {
    UserManager userManager;
    Market market;

    @Override
    public DResponseObj<Boolean> initMarket(String email, String Password, String phoneNumber, String CreditCared, String CreditDate) {
//TODO external sevices
        /**
         * @requirement II. 1
         *
         * @param email: String
         * @param Password: String
         * @param phoneNumber: String
         * @param CreditCared: String
         * @param CreditDate: String
         *
         * @return ATResponseObj
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
        DResponseObj<String> RguestVisit = guestVisit();
        if (RguestVisit.errorOccurred()) new DResponseObj<>(false, RguestVisit.getErrorMsg());

        DResponseObj<Boolean> RsystemManager = addNewMember(RguestVisit.value, email, Password, phoneNumber, CreditCared, CreditDate);
        if (RsystemManager.errorOccurred()) return RsystemManager;
        PermissionManager permissionManager = PermissionManager.getInstance();

        DResponseObj<Boolean> Respons = permissionManager.setSystemManager(email);
        if (Respons.errorOccurred()) return Respons;

        //supply and payment connections//TODO YAKI ??

        return new DResponseObj<>(true);
    }

//supply TODO YAKI
    /**
     * @requirement II. 3
     *
     * @params
     *
     * @return
     *
     * @documentation:
     */

//payment TODO YAKI

    /**
     * @return
     * @requirement II. 4
     * @params
     * @documentation:
     */
//---------------------------------------1 .פעולות כלליות של מבקר-אורח:-------------------------------------------
    @Override
    public DResponseObj<String> guestVisit() {
        /**
         * @requirement: II. 1 . 1
         *
         *
         * @return ATResponseObj
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

        return new DResponseObj<>(userManager.GuestVisit().toString());
    }

    @Override
    public DResponseObj<Boolean> guestLeave(String guestId) {

        /**
         * @requirement: II. 1 . 2
         * @param guestId: String- unique string that identifies the Guest
         *
         * @return ATResponseObj
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
        if (guestId == null || guestId.equals("")) return new DResponseObj<>(false, "NOTSTRING");
        return userManager.GuestLeave(UUID.fromString(guestId));
    }

    // return new ATResponseObj<>(false, "grantor can be null only in case that open new store");
    @Override
    public DResponseObj<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber, String CreditCared, String CreditDate) {
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
         * @return ATResponseObj
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
            return new DResponseObj<>(false, "NOTSTRING");

        if (email == null || email.equals(""))
            return new DResponseObj<>(false, "NOTSTRING");

        if (phoneNumber == null || phoneNumber.equals(""))
            return new DResponseObj<>(false, "NOTSTRING");

        if (CreditCared == null || CreditCared.equals(""))
            return new DResponseObj<>(false, "NOTSTRING");

        if (CreditDate == null || CreditDate.equals(""))
            return new DResponseObj<>(false, "NOTSTRING");

        return userManager.AddNewMember(UUID.fromString(uuid), email, Password, phoneNumber, CreditCared, CreditDate);
    }

    @Override
    public DResponseObj<Boolean> login(String userId, String email, String password) {

        /**
         * @requirement II. 1 . 4
         *
         * @param userId: String - unique string that identifies the user
         * @param email: String - unique email addresses string
         * @param Password: String
         *
         * @return ATResponseObj
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
            return new DResponseObj<>(false, "NOTMEMBER");

        if (email == null || email.equals(""))
            return new DResponseObj<>(false, "NOTSTRING");

        if (password == null || password.equals(""))
            return new DResponseObj<>(false, "NOTSTRING");

        return userManager.Login(UUID.fromString(userId), email, password);
    }
//-----------------------------------2 .פעולות קנייה של מבקר-אורח-----------------------------------------------

    @Override
    public DResponseObj<String> getInfoProductInStore(int storeId, int productID) {

        /**
         * @requirement II. 2 . 1
         *
         * @param storeId: int
         * @param productID: int
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NEGATIVENUMBER");
        if (productID < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        return market.getInfoProductInStore(storeId, productID);
    }

    @Override
    public DResponseObj<ServiceStore> getStore(int storeId) {
        /**
         * @requirement II. 2 . 1
         *
         * @param storeId: int
         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": ServiceStore
         *        }
         *
         * @documentation:
         * Receiving information about stores in the market.
         */
        DResponseObj<Store> storeR = market.getStore(storeId);
        if (storeR.errorOccurred()) new DResponseObj<>(storeR.errorMsg);

        return new DResponseObj<>(new ServiceStore(storeR.value));
    }

//TODO GET INFO ABOUT STORES IN THE MARKET


    @Override
    public DResponseObj<List<Integer>> searchProductByName(String productName) {

        /**
         * @requirement II. 2 . 2
         * @param productName: String
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        return market.searchProductByName(productName);
    }

    @Override
    public DResponseObj<List<Integer>> searchProductByDesc(String desc) {

        /**
         * @requirement II. 2 . 2
         * @param desc: String
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        return market.searchProductByDesc(desc);
    }

    @Override
    public DResponseObj<List<Integer>> searchProductByRate(int rate) {

        /**
         * @requirement II. 2 . 2
         * @param rate: int
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NEGATIVENUMBER");

        return market.searchProductByRate(rate);
    }

    @Override
    public DResponseObj<List<Integer>> searchProductByCategory(int category) {

        /**
         * @requirement II. 2 . 2
         * @param category: int
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NEGATIVENUMBER");

        return market.searchProductByCategory(category);
    }


    @Override
    public DResponseObj<List<Integer>> searchProductByRangePrices(int productId, int min, int max) {

        /**
         * @requirement II. 2 . 2
         * @param productId: int
         * @param min: int
         * @param max: int

         * @return ATResponseObj
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
            return new DResponseObj<>("NEGATIVENUMBER");
        if (min < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (max < 0)
            return new DResponseObj<>("NEGATIVENUMBER");

        return market.searchProductByRangePrices(productId, min, max);

    }


    @Override
    public DResponseObj<Boolean> addProductToShoppingBag(String userId, int storeId, int productId,
                                                         int quantity) {

        /**
         * @requirement II. 2 .3
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @param quantity: int
         *
         * @return ATResponseObj
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
            return new DResponseObj<>(false, "NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new DResponseObj<>("NEGATIVENUMBER");

        return market.AddProductToShoppingBag(UUID.fromString(userId), storeId, productId, quantity);
    }

    @Override
    public DResponseObj<ServiceShoppingCard> getShoppingCart(String userId) {

        /**
         * @requirement II. 2 .4

         * @param userId: String

         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");

        DResponseObj<ShoppingCart> RShoppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShoppingCart.errorOccurred()) return new DResponseObj<>(RShoppingCart.errorMsg);

        return new DResponseObj<>(new ServiceShoppingCard(RShoppingCart.getValue()));
    }

    @Override
    public DResponseObj<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {

        /**
         * @requirement II. 2 .4
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");

        DResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new DResponseObj<>(false, RShppingCart.getErrorMsg());

        if (!RShppingCart.value.removeProductFromShoppingBag(storeId, productId))
            return new DResponseObj<>(false, "TODO");

        return new DResponseObj<>(true);
    }

    @Override
    public DResponseObj<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId,
                                                               int quantity) {

        /**
         * @requirement II. 2 .4
         *
         * @param userId: String
         * @param productId: int
         * @param storeId: int
         * @param quantity: int
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        if (productId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new DResponseObj<>("NEGATIVENUMBER");

        DResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new DResponseObj<>(false, RShppingCart.getErrorMsg());

        if (!RShppingCart.value.setProductQuantity(storeId, productId, quantity))
            return new DResponseObj<>(false, "TODO");

        return new DResponseObj<>(true);
    }

    @Override
    public DResponseObj<Boolean> orderShoppingCart(String userId) {

        /**
         * @requirement II. 2 .5
         *
         * @param userId: String

         *
         * @return ATResponseObj
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
        if (userId == null || userId.equals(""))
            return new DResponseObj<>("NOTSTRING");

        return market.order(UUID.fromString(userId));
    }

    @Override
    public DResponseObj<Boolean> logout(String userId) {

        /**
         * @requirement II. 3.1
         *
         * @param userId: String

         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");

        return userManager.Logout(UUID.fromString(userId));
    }


    @Override
    public DResponseObj<Boolean> openNewStore(String userId, String name, String founder, DiscountPolicy
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
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");

        if (name == null || name.equals(""))
            return new DResponseObj<>("NOTSTRING");

        if (founder == null || founder.equals(""))
            return new DResponseObj<>("NOTSTRING");

        return market.OpenNewStore(UUID.fromString(userId), name, founder, discountPolicy, buyPolicy, buyStrategy);
    }

    @Override
    public DResponseObj<Boolean> addNewProductToStore(String userId, int storeId, int productId,
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
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: Adding products
         */
        if (userId == null || userId.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (price < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        return market.addNewProductToStore(UUID.fromString(userId), storeId, productId, price, quantity);
    }

    @Override
    public DResponseObj<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {

        /**
         * @requirement II. 4.1
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int


         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: removing products
         */

        if (userId == null || userId.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");

        return market.deleteProductFromStore(UUID.fromString(userId), storeId, productId);
    }

    @Override
    public DResponseObj<Boolean> setProductPriceInStore(String userId, int storeId, int productId,
                                                        double price) {

        /**
         * @requirement II. 4.1
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @param price: double
         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: changing products Price
         */
        if (userId == null || userId.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (price < 0)
            return new DResponseObj<>("NEGATIVENUMBER");


        return market.setProductPriceInStore(UUID.fromString(userId), storeId, productId, price);
    }

    @Override
    public DResponseObj<Boolean> setProductQuantityInStore(String userId, int storeId, int productId,
                                                           int quantity) {

        /**
         * @requirement II. 4.1
         *
         * @param userId: String
         * @param storeId: int
         * @param productId: int
         * @param quantity: int
         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: A store owner can manage the product inventory of a store he owns: changing products quantity
         */
        if (userId == null || userId.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        return market.setProductQuantityInStore(UUID.fromString(userId), storeId, productId, quantity);
    }

    // TODO  II. 4.2  שינוי סוגי וכללי )מדיניות( קניה והנחה של חנות


    @Override
    public DResponseObj<Boolean> addNewStoreOwner(String userId, int storeId, String ownerEmail) {

        /**
         * @requirement II. 4.4
         *
         * @param userId: String
         * @param storeId: int
         * @param mangerEmil: String
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        if (ownerEmail == null || ownerEmail.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        return market.addNewStoreOwner(UUID.fromString(userId), storeId, ownerEmail);
    }

    @Override
    public DResponseObj<Boolean> addNewStoreManger(String userId, int storeId, String mangerEmil) {

        /**
         * @requirement II. 4.6
         *
         * @param userId: String
         * @param storeId: int
         * @param mangerEmil: String
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        if (mangerEmil == null || mangerEmil.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        return market.addNewStoreManager(UUID.fromString(userId), storeId, mangerEmil);
    }

    @Override
    public DResponseObj<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per) {

        /**
         * @requirement II. 4.7
         *
         * @param userId: String
         * @param storeId: int
         * @param mangerEmil: String
         * @param per: String
         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        if (mangerEmil == null || mangerEmil.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");

        return market.setManagerPermissions(UUID.fromString(userId), storeId, mangerEmil, permissionType.permissionEnum.valueOf(per));
    }

    @Override
    public DResponseObj<Boolean> closeStore(String userId, int storeId) {

        /**
         * @requirement II. 4.9
         *
         * @param userId: String
         * @param storeId: int

         *
         * @return ATResponseObj
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
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");

        return market.closeStore(UUID.fromString(userId), storeId);
    }

    @Override
    public DResponseObj<Boolean> getStoreRoles(String userId, int storeId) {

        /**
         * @requirement II. 4.11
         *
         * @param userId: String
         * @param storeId: int

         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":Boolean- Successfully completed
         *        }
         *
         * @documentation: Request for information about positions in the store: A store owner can request and receive
         * information about the function holders In the store he owns and about permissions managers .
         */
        if (userId == null || userId.equals(""))
            return new DResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new DResponseObj<>("NEGATIVENUMBER");
        //need to chang -1 to UUID.fromString(userId)  after yaki fix his code
        return market.getStoreRoles(-1, storeId);
        //TODO yaki need to chang from int to UUID
    }

    @Override
    public DResponseObj<List<ServiceHistory>> getStoreOrderHistory(String userId, int storeId) {

        /**
         * @requirement II. 4.13
         *
         * @param userId: String
         * @param storeId: int

         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":List<ServiceHistory>
         *        }
         *
         * @documentation: Receiving information on in-store purchase history
         */
        DResponseObj<List<History>> RHistory = market.getStoreOrderHistory(UUID.fromString(userId), storeId);
        if (RHistory.errorOccurred()) return new DResponseObj<>(RHistory.errorMsg);
        List<ServiceHistory> ServiceHistoryList = new ArrayList<>();
        List<History> HistoryList = RHistory.getValue();

        for (History history : HistoryList) {
            ServiceHistoryList.add(new ServiceHistory(history));
        }
        return new DResponseObj<>(ServiceHistoryList);
    }

    @Override
    public DResponseObj<List<ServiceHistory>> getUserHistoryInStore(String userId, int storeId) {
        /**
         * @requirement: ?????
         * @param userId: String
         * @param storeId: int

         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":List<ServiceHistory>
         *        }
         *
         * @documentation:
         */

        market.getUserHistoryInStore(userId, storeId);
        DResponseObj<List<History>> RHistory = market.getUserHistoryInStore(userId, storeId);
        if (RHistory.errorOccurred()) return new DResponseObj<>(RHistory.errorMsg);
        List<ServiceHistory> ServiceHistoryList = new ArrayList<>();
        List<History> HistoryList = RHistory.getValue();

        for (History history : HistoryList) {
            ServiceHistoryList.add(new ServiceHistory(history));
        }
        return new DResponseObj<>(ServiceHistoryList);
    }

    @Override //TODO way search product ? if you return stores ??
    public DResponseObj<List<Integer>> searchProductByStoreRate(int rate) {
        /**
         * @requirement: ?????
         * @param userId: String
         * @param storeId: int

         *
         * @return ATResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value":List<ServiceHistory>
         *        }
         *
         * @documentation:
         */
        DResponseObj<List<Integer>> RProductByStoreRate = market.searchProductByStoreRate(rate);
        if (RProductByStoreRate.errorOccurred()) return new DResponseObj<>(RProductByStoreRate.errorMsg);

        return new DResponseObj<>(RProductByStoreRate.value);
    }
}
