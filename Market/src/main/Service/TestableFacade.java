package main.Service;

import main.ErrorCode;
import main.ExternalService.AbsExternalService;
import main.ExternalService.ExternalService;
import main.ExternalService.PaymentService;
import main.ExternalService.SupplyService;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.StoreModel.Inventory;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.UserManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TestableFacade extends Facade {

//-------------------------------for tests only !---------------------------------------------

    public void reset() {
        /**
         * @requirement
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": void
         *        }
         *
         * @documentation:

         * */
        this.userManager = new UserManager();
        this.market = new Market(this.userManager);
        this.market.init();
    }

    /**
     * Checks if the system has a system manager
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean> hasSystemManager() {
        return new SLResponsOBJ<>(userManager.ishasSystemManager().value);
    }

    /**
     * checks is the system has an External Service Payment connected
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean> hasPaymentService() {
        DResponseObj<Boolean> res = PaymentService.getInstance().isConnect();
        return new SLResponsOBJ<>(res);
    }

    /**
     * checks is the system has an External Service Supply connected
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean> hasSupplierService() {
        DResponseObj<Boolean> res = SupplyService.getInstance().isConnect();
        return new SLResponsOBJ<>(res);
    }


    /**
     * disconnects the service from system
     *
     * @param service the service to disconnect
     */
    public void disconnectExternalService(String service) {
        DResponseObj<AbsExternalService> res = ExternalService.getService(service);
        res.value.disConnect();
    }

    /**
     * checks wether the service is connected to the system
     *
     * @param service the service to check
     * @return true if the system has connection else false
     */
    public SLResponsOBJ<Boolean> serviceIsAlive(String service) {
        return new SLResponsOBJ<>(ExternalService.getService(service).value.isConnect().value);
    }

    public SLResponsOBJ<Integer> supply(ServiceUser user, List<ServiceItem> items) {
        /**
         * @requirement @requirement II. 3
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean -
         *        }
         *
         * @documentation: not for the user!

         * */
        ConcurrentHashMap<Integer, Integer> a = new ConcurrentHashMap<>();
        for (ServiceItem i : items) {
            a.put(i.itemID, i.quantity);
        }
        User u = user.getUser();
        DResponseObj<Integer> sup = SupplyService.getInstance().supply(u, user.city, user.Street, user.apartment, a);
        return sup.errorOccurred() ? new SLResponsOBJ<>(-1,ErrorCode.EXTERNAL_SERVICE_ERROR) : new SLResponsOBJ<>(sup);
    }


    public SLResponsOBJ<String> payment(ServiceCreditCard C, double amount) {
        /**
         * @requirement II. 4
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean
         *        }
         *
         * @documentation: not for the user!

         * */

        if (C == null || amount <= 0 || C.CreditCard.length() != 16 || C.CreditDate.length() != 4 || C.pin.length() != 3) return new SLResponsOBJ<>("error", -2);
        DResponseObj<Integer> res = PaymentService.getInstance().pay(C.CreditCard, C.CreditDate, C.pin, amount);
        return res.errorOccurred() ? new SLResponsOBJ<>(null,res.errorMsg) : new SLResponsOBJ<>(res.value.toString(), -1);
    }

    public SLResponsOBJ<Boolean> connectService(String serviceName) {
        /**
         * @requirement
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean -
         *        }
         *
         * @documentation: not for the user!

         * */
        DResponseObj<AbsExternalService> res = ExternalService.newService(serviceName);
        return new SLResponsOBJ<>(res.value != null, res.errorMsg);
    }

    public SLResponsOBJ<Boolean> disconnectService(String serviceName) {
        /**
         * @requirement
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean -
         *        }
         *
         * @documentation: not for the user!

         * */
        DResponseObj<AbsExternalService> res = ExternalService.getService(serviceName);
        DResponseObj<Boolean> r = res.value.disConnect();
        return new SLResponsOBJ<>(r.value, r.errorMsg);
    }
    /**
     * contacts the Payment service to pay with creditCard amount of dollars
     *
     * @param creditCard the credit card to take information for paying
     * @param dollars    the amount to pay
     * @return response object with the recipe certification
     */


    /**
     * chechs if exists a cart after init system
     *
     * @return true is exists else false
     */
    public SLResponsOBJ<Boolean> cartExists(String uuid) {
        DResponseObj<Boolean> res = userManager.isCartExist(UUID.fromString(uuid));
        return new SLResponsOBJ<>(res);
    }



    /**
     * checks if system has guest "connected"
     *
     * @return true if yes else false
     */
    public SLResponsOBJ<Boolean> guestOnline(String uuid) {
        DResponseObj<Boolean> res = userManager.isOnline(UUID.fromString(uuid));
        return new SLResponsOBJ<>(res);
    }

    /**
     * check if the user is logged in to the system
     *
     * @param uuid the user to check
     * @return true if success else false
     */
    public SLResponsOBJ<Boolean> isLoggedIn(String uuid) {
        DResponseObj<Boolean> res = userManager.isLogged(UUID.fromString(uuid));
        return new SLResponsOBJ<>(res);
    }




    public SLResponsOBJ<Boolean> isOwner(String email , int storeId){
        DResponseObj<Store> s = market.getStore(storeId);
        if (s.errorOccurred()){
            return new SLResponsOBJ<Boolean> (false,s.errorMsg);
        }
        DResponseObj<Boolean> res =  userManager.isOwner(email,s.value);
        if(res.errorOccurred()){
            return new SLResponsOBJ<>(false,res.errorMsg);
        }
        return new SLResponsOBJ<>(res.value,-1);
    }




    /**
     * checks if store contains item in stock
     *
     * @param storeID id of store
     * @param itemID  id of item
     * @return true if contains item, else false
     */
    public SLResponsOBJ<Boolean> hasItem(int storeID, int itemID) {
        DResponseObj<Store> store = market.getStore(storeID);
        if (store.errorOccurred()) return new SLResponsOBJ<>(false, store.errorMsg);
        DResponseObj<Inventory> inv = store.value.getInventory();
        if (inv.errorOccurred()) return new SLResponsOBJ<>(false, inv.errorMsg);
        DResponseObj<Boolean> has = inv.value.haseItem(itemID);
        return new SLResponsOBJ<>(has);
    }

    /**
     * chechs if user is a contributor of store
     * <p>
     * the store to check on
     * user    the user to check if is a contributor
     *
     * @return true is user is a contributor else false
     */
    public SLResponsOBJ<Boolean> isFounder(int StoreID, String email) {
        DResponseObj<Boolean> res = userManager.isFounder(market.getStore(StoreID).value, email);
        return new SLResponsOBJ<>(res);
    }


    public SLResponsOBJ<Boolean> storeIsClosed(int storeId){
        DResponseObj<Boolean> res = market.isStoreClosed(storeId);
        if(res.errorOccurred()){
            return new SLResponsOBJ<>(res.errorMsg);
        }
        else return new SLResponsOBJ<>(res.value);

    }


    /**
     * checks if newUser is registered
     *
     * @param email email to check
     * @return true if registered else false
     */
    public SLResponsOBJ<Boolean> isMember(String email) {
        DResponseObj<Boolean> res = userManager.isMember(email);
        return new SLResponsOBJ<>(res);
    }

    public SLResponsOBJ<Integer> getProductQuantity(int storeID, int productID) {
        DResponseObj<Integer> res = market.getStore(storeID).getValue().getProductQuantity(productID);
        return new SLResponsOBJ<>(res);
    }

}
