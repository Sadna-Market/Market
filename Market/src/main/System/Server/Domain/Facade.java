package main.System.Server.Domain;

import main.Service.IMarket;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Market.PermissionManager;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;

import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Facade implements IMarket {
    UserManager userManager;
    Market market;

    @Override
    public ATResponseObj<Boolean> initMarket(String uuid, String email, String Password, String phoneNumber, String CreditCared, String CreditDate) {
//TODO external sevices
        /**
         * @requirement II. 1
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        ATResponseObj<Boolean> RsystemManager = addNewMember(uuid, email, Password, phoneNumber, CreditCared, CreditDate);
        if (RsystemManager.errorOccurred()) return RsystemManager;
        PermissionManager permissionManager = PermissionManager.getInstance();

        ATResponseObj<Boolean> Respons = permissionManager.setSystemManager(email);
        if (Respons.errorOccurred()) return Respons;


        //supply and payment//TODO YAKI ??

        return new ATResponseObj<>(true);
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
    public ATResponseObj<String> guestVisit() {
        /**
         * @requirement: II. 1 . 1
         *
         * @params: -
         *
         * @return: UUID - user id
         *
         * @documentation:
         */

        return new ATResponseObj<>(userManager.GuestVisit().toString());
    }

    @Override
    public ATResponseObj<Boolean> guestLeave(String guestId) {

        /**
         * @requirement: II. 1 . 2
         *
         * @params:UUID guest user id
         *
         * @return: true / false
         *
         * @documentation:
         */
        if (guestId == null || guestId.equals("")) return new ATResponseObj<>(false, "Illegal guest Id");
        return userManager.GuestLeave(UUID.fromString(guestId));
    }

    // return new ATResponseObj<>(false, "grantor can be null only in case that open new store");
    @Override
    public ATResponseObj<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber, String CreditCared, String CreditDate) {
        /**
         * @requirement II. 1 . 3
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (uuid == null || uuid.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");

        if (email == null || email.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");

        if (phoneNumber == null || phoneNumber.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");

        if (CreditCared == null || CreditCared.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");

        if (CreditDate == null || CreditDate.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");

        return userManager.AddNewMember(UUID.fromString(uuid), email, Password, phoneNumber, CreditCared, CreditDate);
    }

    @Override
    public ATResponseObj<Boolean> login(String userId, String email, String password) {

        /**
         * @requirement II. 1 . 4
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>(false, "NOTMEMBER");

        if (email == null || email.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");

        if (password == null || password.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");

        return userManager.Login(UUID.fromString(userId), email, password);
    }
//-----------------------------------2 .פעולות קנייה של מבקר-אורח-----------------------------------------------

    @Override
    public ATResponseObj<String> getInfoProductInStore(int storeId, int productID) {

        /**
         * @requirement II. 2 . 1
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (productID < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        market.getInfoProductInStore(storeId, productID);
        return null;
    }

    @Override
    public ATResponseObj<ServiceStore> getStore(int storeId) {
        ATResponseObj<Store> storeR = market.getStore(storeId);
        if (storeR.errorOccurred()) new ATResponseObj<>(storeR.errorMsg);

        return new ATResponseObj<>(new ServiceStore(storeR.value));
    }

//TODO GET INFO ABOUT STORES IN THE MARKET


    @Override
    public ATResponseObj<List<Integer>> searchProductByName(String productName) {

        /**
         * @requirement II. 2 . 2
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (productName == null || productName.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        return market.searchProductByName(productName);
    }

    @Override
    public ATResponseObj<List<Integer>> searchProductByDesc(String desc) {

        /**
         * @requirement II. 2 . 2
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (desc == null || desc.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        return market.searchProductByDesc(desc);
    }

    @Override
    public ATResponseObj<List<Integer>> searchProductByRate(int rate) {

        /**
         * @requirement II. 2 . 2
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */

        if (rate < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        return market.searchProductByRate(rate);
    }

    @Override
    public ATResponseObj<List<Integer>> searchProductByCategory(int category) {

        /**
         * @requirement II. 2 . 2
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (category < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        return market.searchProductByCategory(category);
    }


    @Override
    public ATResponseObj<List<Integer>> searchProductByRangePrices(int productId, int min, int max) {

        /**
         * @requirement II. 2 . 2
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (min < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (max < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        return market.searchProductByRangePrices(productId, min, max);

    }


    @Override
    public ATResponseObj<Boolean> addProductToShoppingBag(String userId, int storeId, int productId,
                                                          int quantity) {

        /**
         * @requirement II. 2 .3
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>(false, "NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        return market.AddProductToShoppingBag(UUID.fromString(userId), storeId, productId, quantity);
    }

    //TODO מה הלוז בכאלה
    @Override
    public ATResponseObj<ServiceShoppingCard> getShoppingCart(String userId) {

        /**
         * @requirement II. 2 .4
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");

        ATResponseObj<ShoppingCart> RShoppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShoppingCart.errorOccurred()) return new ATResponseObj<>(RShoppingCart.errorMsg);

        return new ATResponseObj<>(new ServiceShoppingCard(RShoppingCart.getValue()));
    }

    @Override
    public ATResponseObj<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId) {

        /**
         * @requirement II. 2 .4
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        ATResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new ATResponseObj<>(false, RShppingCart.getErrorMsg());

        if (!RShppingCart.value.removeProductFromShoppingBag(storeId, productId))
            return new ATResponseObj<>(false, "TODO");

        return new ATResponseObj<>(true);
    }

    @Override
    public ATResponseObj<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId,
                                                                int quantity) {

        /**
         * @requirement II. 2 .4
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        ATResponseObj<ShoppingCart> RShppingCart = userManager.getUserShoppingCart(UUID.fromString(userId));
        if (RShppingCart.errorOccurred()) return new ATResponseObj<>(false, RShppingCart.getErrorMsg());

        if (!RShppingCart.value.setProductQuantity(storeId, productId, quantity))
            return new ATResponseObj<>(false, "TODO");

        return new ATResponseObj<>(true);
    }

    @Override
    public ATResponseObj<Boolean> orderShoppingCart(String userId) {

        /**
         * @requirement II. 2 .5
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");

        return market.order(UUID.fromString(userId));
    }

    @Override
    public ATResponseObj<Boolean> logout(String userId) {

        /**
         * @requirement II. 3.1
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");

        return userManager.Logout(UUID.fromString(userId));
    }

    //TODO
    @Override
    public ATResponseObj<Boolean> openNewStore(String userId, String name, String founder, DiscountPolicy
            discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {

        /**
         * @requirement II. 3.2
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");

        if (name == null || name.equals(""))
            return new ATResponseObj<>("NOTSTRING");

        if (founder == null || founder.equals(""))
            return new ATResponseObj<>("NOTSTRING");

        return market.OpenNewStore(UUID.fromString(userId), name, founder, discountPolicy, buyPolicy, buyStrategy);
    }

    @Override
    public ATResponseObj<Boolean> addNewProductToStore(String userId, int storeId, int productId,
                                                       double price, int quantity) {

        /**
         * @requirement II. 4.1
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */

        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (price < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        return market.addNewProductToStore(UUID.fromString(userId), storeId, productId, price, quantity);
    }

    @Override
    public ATResponseObj<Boolean> deleteProductFromStore(String userId, int storeId, int productId) {

        /**
         * @requirement II. 4.1
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */

        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        return market.deleteProductFromStore(UUID.fromString(userId), storeId, productId);
    }

    @Override
    public ATResponseObj<Boolean> setProductPriceInStore(String userId, int storeId, int productId,
                                                         double price) {

        /**
         * @requirement II. 4.1
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (price < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");


        return market.setProductPriceInStore(UUID.fromString(userId), storeId, productId, price);
    }

    @Override
    public ATResponseObj<Boolean> setProductQuantityInStore(String userId, int storeId, int productId,
                                                            int quantity) {

        /**
         * @requirement II. 4.1
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (productId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        if (quantity < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        return market.setProductQuantityInStore(UUID.fromString(userId), storeId, productId, quantity);
    }

    // TODO  II. 4.2  שינוי סוגי וכללי )מדיניות( קניה והנחה של חנות


    @Override
    public ATResponseObj<Boolean> addNewStoreOwner(String userId, int storeId, String ownerEmail) {

        /**
         * @requirement II. 4.4
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (ownerEmail == null || ownerEmail.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        return market.addNewStoreOwner(UUID.fromString(userId), storeId, ownerEmail);
    }

    @Override
    public ATResponseObj<Boolean> addNewStoreManger(String userId, int storeId, String mangerEmil) {

        /**
         * @requirement II. 4.6
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (mangerEmil == null || mangerEmil.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        return market.addNewStoreManager(UUID.fromString(userId), storeId, mangerEmil);
    }

    @Override
    public ATResponseObj<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per) {

        /**
         * @requirement II. 4.7
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (mangerEmil == null || mangerEmil.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        return market.setManagerPermissions(UUID.fromString(userId), storeId, mangerEmil, permissionType.permissionEnum.valueOf(per));
    }

    @Override
    public ATResponseObj<Boolean> closeStore(String userId, int storeId) {

        /**
         * @requirement II. 4.9
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");

        return market.closeStore(UUID.fromString(userId), storeId);
    }

    @Override
    public ATResponseObj<Boolean> getStoreRoles(String userId, int storeId) {

        /**
         * @requirement II. 4.11
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        if (userId == null || userId.equals(""))
            return new ATResponseObj<>("NOTSTRING");
        if (storeId < 0)
            return new ATResponseObj<>("NEGATIVENUMBER");
        //need to chang -1 to UUID.fromString(userId)  after yaki fix his code
        return market.getStoreRoles(-1, storeId);
        //TODO yaki need to chang from int to UUID
    }

    @Override
    public ATResponseObj<List<ServiceHistory>> getStoreOrderHistory(String userId, int storeId) {

        /**
         * @requirement II. 4.13
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        ATResponseObj<List<History>> RHistory = market.getStoreOrderHistory(UUID.fromString(userId), storeId);
        if (RHistory.errorOccurred()) return new ATResponseObj<>(RHistory.errorMsg);
        List<ServiceHistory> ServiceHistoryList = new ArrayList<>();
        List<History> HistoryList = RHistory.getValue();

        for (History history : HistoryList) {
            ServiceHistoryList.add(new ServiceHistory(history));
        }
        return new ATResponseObj<>(ServiceHistoryList);
    }

    @Override
    public ATResponseObj<List<ServiceHistory>> getUserHistoryInStore(String userId, int storeId) {
        /**
         * @requirement
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */

        market.getUserHistoryInStore(userId, storeId);
        ATResponseObj<List<History>> RHistory = market.getUserHistoryInStore(userId, storeId);
        if (RHistory.errorOccurred()) return new ATResponseObj<>(RHistory.errorMsg);
        List<ServiceHistory> ServiceHistoryList = new ArrayList<>();
        List<History> HistoryList = RHistory.getValue();

        for (History history : HistoryList) {
            ServiceHistoryList.add(new ServiceHistory(history));
        }
        return new ATResponseObj<>(ServiceHistoryList);
    }

    @Override //TODO way search product ? if you return stores ??
    public ATResponseObj<List<Integer>> searchProductByStoreRate(int rate) {
        /**
         * @requirement
         *
         * @params
         *
         * @return
         *
         * @documentation:
         */
        ATResponseObj<List<Integer>> RProductByStoreRate = market.searchProductByStoreRate(rate);
        if (RProductByStoreRate.errorOccurred()) return new ATResponseObj<>(RProductByStoreRate.errorMsg);

        return new ATResponseObj<>(RProductByStoreRate.value);
    }
}
