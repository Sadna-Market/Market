package com.example.demo.Service;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.*;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.*;
import com.example.demo.Domain.UserModel.*;
import com.example.demo.ExternalService.*;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

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
        ExternalService.getInstance().reset();
        PaymentService.getInstance().reset();
        SupplyService.getInstance().reset();
        PermissionManager.getInstance().reset();
        this.userManager = new UserManager();
        this.market = new Market(this.userManager);
        this.market.init();
    }

    /**
     * Checks if the system has a system manager
     *
     * @return true if there is, else false
     */
    public SLResponseOBJ<Boolean> hasSystemManager() {
        return new SLResponseOBJ<>(userManager.ishasSystemManager().value);
    }

    /**
     * checks is the system has an External Service Payment connected
     *
     * @return true if there is, else false
     */
    public SLResponseOBJ<Boolean> hasPaymentService() {
        DResponseObj<Boolean> res = PaymentService.getInstance().isConnect();
        return new SLResponseOBJ<>(res);
    }

    /**
     * checks is the system has an External Service Supply connected
     *
     * @return true if there is, else false
     */
    public SLResponseOBJ<Boolean> hasSupplierService() {
        DResponseObj<Boolean> res = SupplyService.getInstance().isConnect();
        return new SLResponseOBJ<>(res);
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
    public SLResponseOBJ<Boolean> serviceIsAlive(String service) {
        DResponseObj<AbsExternalService> ser = ExternalService.getService(service);
        if(ser.errorOccurred()) return new SLResponseOBJ<>(false,ser.errorMsg);
        DResponseObj<Boolean> isCon = ser.value.isConnect();
        return new SLResponseOBJ<>(isCon);
    }

    public SLResponseOBJ<Integer> supply(ServiceUser user, List<ServiceProductStore> items) {
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
        for (ServiceProductStore i : items) {
            if(market.searchProductByName(i.name).value.isEmpty())
                return new SLResponseOBJ<>(null,-2);
            a.put(i.itemID, i.quantity);
        }
        User u = user.getUser();

        DResponseObj<Integer> sup = SupplyService.getInstance().supply(u, user.city, user.Street, user.apartment, a);
        return sup.errorOccurred() ? new SLResponseOBJ<>(-1, ErrorCode.EXTERNAL_SERVICE_ERROR) : new SLResponseOBJ<>(sup);
    }


    public SLResponseOBJ<String> payment(ServiceCreditCard C, double amount) {
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

        if (C == null || amount <= 0 || C.creditCard.length() != 16 || C.creditDate.length() != 4 || C.pin.length() != 3) return new SLResponseOBJ<>("error", -2);
        DResponseObj<Integer> res = PaymentService.getInstance().pay(C.creditCard, C.creditDate, C.pin, amount);
        return res.errorOccurred() ? new SLResponseOBJ<>(null,res.errorMsg) : new SLResponseOBJ<>(res.value.toString(), -1);
    }

    public SLResponseOBJ<Boolean> connectService(String serviceName) {
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
        return new SLResponseOBJ<>(res.value != null, res.errorMsg);
    }

    public SLResponseOBJ<Boolean> disconnectService(String serviceName) {
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
        return new SLResponseOBJ<>(r.value, r.errorMsg);
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
    public SLResponseOBJ<Boolean> cartExists(String uuid) {
        DResponseObj<Boolean> res = userManager.isCartExist(UUID.fromString(uuid));
        return new SLResponseOBJ<>(res);
    }



    /**
     * checks if system has guest "connected"
     *
     * @return true if yes else false
     */
    public SLResponseOBJ<Boolean> guestOnline(String uuid) {
        DResponseObj<Boolean> res = userManager.isOnline(UUID.fromString(uuid));
        return new SLResponseOBJ<>(res);
    }

    /**
     * check if the user is logged in to the system
     *
     * @param uuid the user to check
     * @return true if success else false
     */
    public SLResponseOBJ<Boolean> isLoggedIn(String uuid) {
        DResponseObj<Boolean> res = userManager.isLogged(UUID.fromString(uuid));
        return new SLResponseOBJ<>(res);
    }









    /**
     * checks if store contains item in stock
     *
     * @param storeID id of store
     * @param itemID  id of item
     * @return true if contains item, else false
     */
    public SLResponseOBJ<Boolean> hasItem(int storeID, int itemID) {
        DResponseObj<Store> store = market.getStore(storeID);
        if (store.errorOccurred()) return new SLResponseOBJ<>(false, store.errorMsg);
        DResponseObj<Inventory> inv = store.value.getInventory();
        if (inv.errorOccurred()) return new SLResponseOBJ<>(false, inv.errorMsg);
        DResponseObj<Boolean> has = inv.value.haseItem(itemID);
        return new SLResponseOBJ<>(has);
    }

    /**
     * chechs if user is a contributor of store
     * <p>
     * the store to check on
     * user    the user to check if is a contributor
     *
     * @return true is user is a contributor else false
     */
    public SLResponseOBJ<Boolean> isFounder(int StoreID, String email) {
        DResponseObj<Boolean> res = userManager.isFounder(market.getStore(StoreID).value, email);
        return new SLResponseOBJ<>(res);
    }


    public SLResponseOBJ<Boolean> storeIsClosed(int storeId){
        DResponseObj<Boolean> res = market.isStoreClosed(storeId);
        return !res.value ? new SLResponseOBJ<>(false,-2) : new SLResponseOBJ<>(true,-1);
    }


    /**
     * checks if newUser is registered
     *
     * @param email email to check
     * @return true if registered else false
     */
    public SLResponseOBJ<Boolean> isMember(String email) {
        DResponseObj<Boolean> res = userManager.isMember(email);
        return new SLResponseOBJ<>(res);
    }

    public SLResponseOBJ<Integer> getProductQuantity(int storeID, int productID) {
        DResponseObj<Integer> res = market.getStore(storeID).getValue().getProductQuantity(productID);
        return new SLResponseOBJ<>(res);
    }


}
