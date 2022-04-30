package main.Service;

import main.ExternalService.ExternalService;
import main.ExternalService.PaymentService;
import main.ExternalService.SupplyService;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.UserManager;

import java.net.ServerSocket;
import java.util.LinkedList;
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
    }

    /**
     * Checks if the system has a system manager
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean> hasSystemManager() {
        return new SLResponsOBJ<>( userManager.ishasSystemManager().value);
    }

    /**
     * checks is the system has an External Service Payment connected
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean> hasPaymentService() {
        return new SLResponsOBJ (PaymentService.getInstance().isConnect());
    }

    /**
     * checks is the system has an External Service Supply connected
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean>  hasSupplierService() {
            return new SLResponsOBJ (SupplyService.getInstance().isConnect());
        }



    /**
     * disconnects the service from system
     *
     * @param service the service to disconnect
     */
    public void disconnectExternalService(String service) {
        ExternalService.getService(service).value.disConnect();
    }

    /**
     * checks wether the service is connected to the system
     *
     * @param service the service to check
     * @return true if the system has connection else false
     */
    public SLResponsOBJ<Boolean> serviceIsAlive(String service) {
        return new SLResponsOBJ<>( ExternalService.getService(service).value.isConnect().value);
    }

    public SLResponsOBJ<String> supply(ServiceUser user,LinkedList<ServiseItem> items) {
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
        ConcurrentHashMap<Integer,Integer> a= new ConcurrentHashMap<>();
        for(ServiseItem i : items){
            a.put(i.itemID,i.quantity);
        }
        User u = user.getUser();
        return new SLResponsOBJ(SupplyService.getInstance().supply(u,user.city,user.Street,user.apartment,a));
    }


    public SLResponsOBJ<Boolean> payment(ServiceCreditCard C,double amount) {
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


        return new SLResponsOBJ(PaymentService.getInstance().pay(C.getCreditCard(),amount).value);
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
        return new SLResponsOBJ(ExternalService.newService(serviceName).value);
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

        return new SLResponsOBJ(ExternalService.getService(serviceName).value.disConnect().value);
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
    public SLResponsOBJ<Boolean> cartExists(UUID uuid) {
        return new SLResponsOBJ<>( userManager.isCartExist(uuid).value);
    }

    /**
     * checks if system has guest "connected"
     *
     * @return true if yes else false
     */
    public SLResponsOBJ<Boolean> guestOnline(UUID uuid) {
        return new SLResponsOBJ<>( userManager.isOnline(uuid));
    }

    /**
     * check if the user is logged in to the system
     * @param uuid the user to check
     * @return true if success else false
     */
    public SLResponsOBJ<Boolean> isLoggedIn(UUID uuid){
        return new SLResponsOBJ<>(userManager.isLogged(uuid));
    }

    /**
     * checks if store contains item in stock
     *
     * @param storeID id of store
     * @param itemID  id of item
     * @return true if contains item, else false
     */
    public SLResponsOBJ<Boolean> hasItem(int storeID, int itemID) {
        return new  SLResponsOBJ(market.getStore(storeID).value.getInventory().value.haseItem(itemID).value);
    }

    /**
     * chechs if user is a contributor of store
     *
     *   the store to check on
     *  user    the user to check if is a contributor
     * @return true is user is a contributor else false
     */
    public SLResponsOBJ<Boolean> isFounder(UUID uid, int StoreID) {
        return new SLResponsOBJ<>(userManager.isFounder(uid,market.getStore(StoreID).value).value);
    }

    /**
     * checks if store is closed
     *
     * @param storeID id of store
     * @return true is store is closed, else false
     */
    public SLResponsOBJ<Boolean> storeIsClosed(int storeID) {
        return new SLResponsOBJ(  market.isStoreClosed(storeID).value);
    }



}
