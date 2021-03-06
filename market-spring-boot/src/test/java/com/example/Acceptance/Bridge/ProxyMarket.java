package com.example.Acceptance.Bridge;


import com.example.Acceptance.Obj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.ServiceBID;
import com.example.demo.Service.ServiceObj.ServiceCreditCard;
import com.example.demo.Service.ServiceObj.ServiceDetailsPurchase;
import com.example.demo.Service.ServiceObj.ServiceStore;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.HashMap;
import java.util.List;

public class ProxyMarket implements MarketBridge {
    private final MarketBridge realMarket;

    public ProxyMarket() {
        realMarket = new RealMarket();
    }

    /**
     * init all resources of system (External Services, cart..)
     *
     * @param sysManager
     * @return true if success else false
     */
    public ATResponseObj<Boolean> initSystem(User sysManager) {
        return realMarket.initSystem(sysManager);
    }

    /**
     * Discards all resources from init (doesn't change the memory)
     *
     * @param uuid
     */
    public void exitSystem(String uuid) {
        realMarket.exitSystem(uuid);
    }

    /**
     * Checks if the system has a system manager
     *
     * @return true if there is, else false
     */
    public boolean hasSystemManager() {
        return realMarket.hasSystemManager();
    }

    /**
     * checks is the system has an External Service Payment connected
     *
     * @return true if there is, else false
     */
    public boolean hasPaymentService() {
        return realMarket.hasPaymentService();
    }

    /**
     * checks is the system has an External Service Supply connected
     *
     * @return true if there is, else false
     */
    public boolean hasSupplierService() {
        return realMarket.hasSupplierService();
    }

    /**
     * disconnects the service from system
     *
     * @param service the service to disconnect
     */
    public void disconnectExternalService(String service) {
        realMarket.disconnectExternalService(service);
    }

    /**
     * checks wether the service is connected to the system
     *
     * @param service the service to check
     * @return true if the system has connection else false
     */
    public boolean serviceIsAlive(String service) {
        return realMarket.serviceIsAlive(service);
    }

    /**
     * contacts the Payment service to pay with creditCard amount of dollars
     *
     * @param creditCard the credit card to take information for paying
     * @param dollars    the amount to pay
     * @return response object with the recipe certification
     */
    public ATResponseObj<String> pay(CreditCard creditCard, int dollars) {
        return realMarket.pay(creditCard, dollars);
    }

    /**
     * contacts the Supply service to supply derliver items to user
     *
     * @param deliver the list of items to deliver
     * @param user    the use to deliver
     * @return certificate for success supply
     */
    public ATResponseObj<String> supply(List<ItemDetail> deliver, User user) {
        return realMarket.supply(deliver, user);
    }

    /**
     * chechs if exists a cart after init system
     *
     * @param uuid
     * @return true is exists else false
     */
    public boolean cartExists(String uuid) {
        return realMarket.cartExists(uuid);
    }

    /**
     * checks if system has guest "connected"
     *
     * @param uuid
     * @return true if yes else false
     */
    public boolean guestOnline(String uuid) {
        return realMarket.guestOnline(uuid);
    }

    /**
     * register a new user to the system
     *
     * @param uuid
     * @param username username - email
     * @param password password
     * @return true if success else false
     */
    public boolean register(String uuid, String username, String password, String dateOfBirth) {
        return realMarket.register(uuid, username, password,dateOfBirth);
    }

    /**
     * checks if newUser is registered
     *
     * @param newUser new user to check
     * @return true if registered else false
     */
    public boolean isMember(User newUser) {
        return realMarket.isMember(newUser);
    }

    /**
     * logs in the user to the system
     *
     * @param uuid
     * @param member the user to login
     * @return true if sucess else false
     */
    public ATResponseObj<String> login(String uuid, User member) {
        return realMarket.login(uuid, member);
    }

    /**
     * check if the user is logged in to the system
     *
     * @param uuid the user to check
     * @return true if success else false
     */
    public boolean isLoggedIn(String uuid) {
        return realMarket.isLoggedIn(uuid);
    }

    /**
     * change the password of a user in the system
     *
     * @param member the member to change the password
     * @return true if success else false
     */

    /**
     * get information of a store and its products
     *
     * @param storeID the store we want to know about
     * @return info of the store
     */
    public ATResponseObj<String> getStoreInfo(int storeID) {
        return realMarket.getStoreInfo(storeID);
    }

    /**
     * given item details, search for this item or related by category and keywords
     *
     * @param itemName item to search for
     * @param category category of the item
     * @param keyWords words that can relate to the items specifications
     * @return list of result items
     */
    public ATResponseObj<List<Integer>> searchItems(String itemName, int category, List<String> keyWords) {
        return realMarket.searchItems(itemName, category, keyWords);
    }

    public boolean changePassword(String uuid , User member , String newPassword) {
        return realMarket.changePassword(uuid,member,newPassword);
    }
        /**
         * given a list of items, filter them by rank,price range, store rank, category.
         *
         * @param items       list of items to filter
         * @param productRank filter param
         * @param priceRange  filter param
         * @param category    filter param
         * @param storeRank   filter param
         * @return filtered list
         */
    public ATResponseObj<List<Integer>> filterSearchResults(List<Integer> items, int productRank, int[] priceRange, int category, int storeRank) {
        return realMarket.filterSearchResults(items,productRank,priceRange,category,storeRank);
    }

    /**
     * add to cart the item that is in storeID
     *
     * @param uuid
     * @param storeID the store that the item is related to
     * @param item    the item to add to the cart
     * @return true if success, else false
     */
    public boolean addToCart(String uuid, int storeID, ItemDetail item) {
        return realMarket.addToCart(uuid, storeID, item);
    }

    /**
     * gets the cart of the current user
     *
     * @param uuid
     * @return list of list of items - Cart that has shopping bags related to different stores
     */
    public ATResponseObj<List<List<Integer>>> getCart(String uuid) {
        return realMarket.getCart(uuid);
    }

    /**
     * query to verify quantity of item in storeID
     *
     * @param storeID store that has the item
     * @param item    item to find out the quantity
     * @return quantity of the item in the store
     */
    public int getAmountOfProductInStore(int storeID, ItemDetail item) {
        return realMarket.getAmountOfProductInStore(storeID, item);
    }

    /**
     * removes the item from the cart of current user's cart
     *
     * @param uuid
     * @param item    item to remove
     * @param storeID
     * @return true if success, else false
     */
    public boolean removeProductFromCart(String uuid, ItemDetail item, int storeID) {
        return realMarket.removeProductFromCart(uuid, item, storeID);
    }

    /**
     * update the item of current user's cart to new quantity
     *
     * @param uuid
     * @param item        the item to update
     * @param newQuantity new quantity
     * @param storeID
     * @return true if success, else false
     */
    public boolean updateProductQuantity(String uuid, ItemDetail item, int newQuantity, int storeID) {
        return realMarket.updateProductQuantity(uuid, item, newQuantity, storeID);
    }

    /**
     * resets all memory from ram (cart,members,history purchases...)
     */
    public void resetMemory() {
        realMarket.resetMemory();
    }

    /**
     * creates a new store with a owner
     *
     * @param uuid
     * @param owner owner of the store to be created
     * @return Response- msg error if occurred else id of the store that was created
     */
    public ATResponseObj<Integer> addStore(String uuid, User owner) {
        return realMarket.addStore(uuid, owner);
    }

    /**
     * adds the item to the store
     *
     * @param uuid
     * @param storeID the store that the item will be added to
     * @param item    the item to add
     * @return true if success else false
     */
    public boolean addItemToStore(String uuid, int storeID, ItemDetail item) {
        return realMarket.addItemToStore(uuid, storeID, item);
    }

    /**
     * logout from connected user
     *
     * @param uuid
     * @return true if success else false
     */
    public ATResponseObj<String> logout(String uuid) {
        return realMarket.logout(uuid);
    }

    /**
     * purchase the current cart of the user
     *
     * @param uuid
     * @param creditCard credit card details to take the money for payment service
     * @param address    address to send the items for supply service
     * @return certificated of payment and supply
     */
    public ATResponseObj<ServiceDetailsPurchase> purchaseCart(String uuid, CreditCard creditCard, Address address) {
        return realMarket.purchaseCart(uuid, creditCard, address);
    }

    /**
     * query to get the history of all purchases of a store with storeID
     *
     * @param uuid
     * @param storeID the id of the store to get the history
     * @return list of all purchases accepted certificates
     */
    public ATResponseObj<List<History>> getHistoryPurchase(String uuid, int storeID) {
        return realMarket.getHistoryPurchase(uuid, storeID);
    }

    /**
     * chechs if user is a contributor of store
     *
     * @param storeID the store to check on
     * @param user    the user to check if is a contributor
     * @return true is user is a contributor else false
     */
    public boolean isContributor(int storeID, User user) {
        return realMarket.isContributor(storeID, user);
    }

    /**
     * checks if store contains item in stock
     *
     * @param storeID id of store
     * @param itemID  id of item
     * @return true if contains item, else false
     */
    public boolean hasItem(int storeID, int itemID) {
        return realMarket.hasItem(storeID, itemID);
    }

    /**
     * removes a product from store
     *
     * @param uuid
     * @param storeID the store in which to remove the product from
     * @param item    the product to remove
     * @return true if success, else false
     */
    public boolean removeProductFromStore(String uuid, int storeID, ItemDetail item) {
        return realMarket.removeProductFromStore(uuid, storeID, item);
    }

    /**
     * update product details of a store
     *
     * @param uuid
     * @param storeID         the store to update its product
     * @param existingProduct the product to update
     * @param updatedProduct  the product details to update
     * @return true if success, else false
     */
    public boolean updateProductInStore(String uuid, int storeID, ItemDetail existingProduct, ItemDetail updatedProduct) {
        return realMarket.updateProductInStore(uuid, storeID, existingProduct, updatedProduct);
    }

    /**
     * query to get item info from store
     *
     * @param storeID id of the store
     * @param itemID  the id of the item
     * @return item will detail
     */
    public ATResponseObj<ItemDetail> getProduct(int storeID, int itemID) {
        return realMarket.getProduct(storeID,itemID);
    }

    /**
     * check if user is owner of the store
     *
     * @param storeID id of the store
     * @param user    user info
     * @return true if is owner, else false
     */
    public boolean isOwner(int storeID, User user) {
        return realMarket.isOwner(storeID,user);
    }

    /**
     * assigns a new user to be owner of the store
     *
     * @param uuid
     * @param storeID  store id to add owner
     * @param newOwner new owner to add
     * @return true if success, else false
     */
    public boolean assignNewOwner(String uuid, int storeID, User newOwner) {
        return realMarket.assignNewOwner(uuid, storeID, newOwner);
    }


    /**
     * remove store owner and the permission that this owner is grantor
     *
     * @param userId
     * @param storeId
     * @param ownerEmail
     * @return true if success, else false
     */
    @Override
    public boolean removeStoreOwner(String userId, int storeId, String ownerEmail) {
        return realMarket.removeStoreOwner(userId,storeId,ownerEmail);
    }


    /**
     * checks if user is manager in store
     *
     * @param storeID id of store
     * @param user    user to check if manager
     * @return true if user is manager else false
     */
    public boolean isManager(int storeID, User user) {
        return realMarket.isManager(storeID,user);
    }

    /**
     * assigns a new user to be manager of the store
     *
     * @param uuid
     * @param storeID    store id to add owner
     * @param newManager new manager to add
     * @return true if success, else false
     */
    public boolean assignNewManager(String uuid, int storeID, User newManager) {
        return realMarket.assignNewManager(uuid, storeID, newManager);
    }

    /**
     * update/change permission of manager in store
     *
     * @param uuid
     * @param permission the new permission
     * @param onOf       turn on or off
     * @param manager    the manager
     * @param storeID    store id
     * @return true if success else false
     */
    public boolean updatePermission(String uuid, String permission, boolean onOf, User manager, int storeID) {
        return realMarket.updatePermission(uuid, permission, onOf, manager, storeID);
    }

    /**
     * close the store
     *
     * @param uuid
     * @param storeID store id
     * @return true if success, else false
     */
    public boolean closeStore(String uuid, int storeID) {
        return realMarket.closeStore(uuid, storeID);
    }

    /**
     * reopen store
     *
     * @param uuid
     * @param storeID
     * @return true if success, else false
     */
    @Override
    public boolean reopenStore(String uuid, int storeID) {
        return realMarket.reopenStore(uuid,storeID);
    }

    /**
     * checks if store is closed
     *
     * @param storeID id of store
     * @return true is store is closed, else false
     */
    public boolean storeIsClosed(int storeID) {
        return realMarket.storeIsClosed(storeID);
    }

    /**
     * query to get role info of user in store
     *
     * @param uuid    the user
     * @param storeID id of store
     * @return info of user's roles
     */
    public ATResponseObj<String> getUserRoleInfo(String uuid, int storeID) {
        return realMarket.getUserRoleInfo(uuid, storeID);
    }

    /**
     * query to get info of any user member in the system
     *
     * @param uuid
     * @param user the user
     * @return info of user
     */
    public ATResponseObj<String> getBuyerInfo(String uuid, User user) {
        return realMarket.getBuyerInfo(uuid,user);
    }

    /**
     * add product type to system
     *
     * @param uuid
     * @param item
     * @return id of the item
     */
    public ATResponseObj<Integer> addProductType(String uuid, ItemDetail item) {
        return realMarket.addProductType(uuid, item);
    }

    /**
     * generates uuid
     *
     * @return return uuid
     */
    public String guestVisit() {
        return realMarket.guestVisit();
    }

    /**
     * connects the specified service
     *
     * @param payment
     * @return
     */
    public boolean connectExternalService(String payment) {
        return realMarket.connectExternalService(payment);
    }

    /**
     * add buy rule to store
     * @param uuid
     * @param storeId
     * @param buyRule
     * @return
     */
    public boolean addNewBuyRule(String uuid, int storeId, BuyRuleSL buyRule) {
        return realMarket.addNewBuyRule(uuid,storeId,buyRule);
    }

    /**
     * remove buy rule from store
     * @param uuid
     * @param storeId
     * @param buyRuleID
     * @return
     */
    @Override
    public boolean removeBuyRule(String uuid, int storeId, int buyRuleID) {
        return realMarket.removeBuyRule(uuid,storeId,buyRuleID);
    }

    /**
     * add discount rule to store
     * @param uuid
     * @param storeId
     * @param discountRule
     * @return
     */
    @Override
    public boolean addNewDiscountRule(String uuid, int storeId, DiscountRuleSL discountRule) {
        return realMarket.addNewDiscountRule(uuid,storeId,discountRule);
    }

    /**
     * remove discount rule from store
     * @param uuid
     * @param storeId
     * @param discountRuleID
     * @return
     */
    @Override
    public boolean removeDiscountRule(String uuid, int storeId, int discountRuleID) {
        return realMarket.removeDiscountRule(uuid,storeId,discountRuleID);
    }

    /**
     * get buy rule of this store
     *
     * @param uuid
     * @param storeId
     * @return list of all buy rules
     */
    @Override
    public ATResponseObj<List<BuyRuleSL>> getBuyPolicy(String uuid, int storeId) {
        return realMarket.getBuyPolicy(uuid,storeId);
    }

    /**
     * get discount rule of this store
     *
     * @param uuid
     * @param storeId
     * @return list of all discount rules
     */
    @Override
    public ATResponseObj<List<DiscountRuleSL>> getDiscountPolicy(String uuid, int storeId) {
        return realMarket.getDiscountPolicy(uuid,storeId);
    }

    /**
     * gets all logged in members info in the market
     *
     * @param uuid
     * @return
     */
    public ATResponseObj<List<User>> getLoggedInMembers(String uuid) {
        return realMarket.getLoggedInMembers(uuid);
    }

    /**
     * gets all logged out members info in the market
     *
     * @param uuid
     * @return
     */
    public ATResponseObj<List<User>> getLoggedOutMembers(String uuid) {
        return realMarket.getLoggedOutMembers(uuid);
    }

    /**
     * Cancel a membership in the market
     * This can only be done by the System manager
     * Note: if the user to cancel is the founder of a store then store will be removed from the market and Owners/Managers will be informed.
     *
     * @param uuid       the uuid of the System manager
     * @param cancelUser the user to cancel
     * @return true if success, else false
     */
    public boolean cancelMembership(String uuid, User cancelUser) {
        return realMarket.cancelMembership(uuid,cancelUser);
    }

    public ATResponseObj<List<ServiceStore>> getAllStores() {
        return realMarket.getAllStores();
    }

    public boolean isOwnerUUID(String uuid, int storeID) {
        return realMarket.isOwnerUUID(uuid,storeID);
    }

    public boolean isSysManagerUUID(String uuid) {
        return realMarket.isSysManagerUUID(uuid);
    }

    public boolean isManagerUUID(String uuid, int storeID) {
        return realMarket.isManagerUUID(uuid,storeID);
    }

    /**
     * create new BID in store by user
     *
     * @param uuid
     * @param storeID
     * @param productID
     * @param quantity
     * @param totalPrice
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<Boolean> createBID(String uuid, int storeID, int productID, int quantity, int totalPrice) {
        return realMarket.createBID(uuid,storeID,productID,quantity,totalPrice);
    }

    /**
     * remove BID from store
     *
     * @param uuid
     * @param storeID
     * @param productID
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<Boolean> removeBID(String uuid, int storeID, int productID) {
        return realMarket.removeBID(uuid,storeID,productID);
    }

    /**
     * manager/owner approve BID
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<Boolean> approveBID(String uuid, String userEmail, int storeID, int productID) {
        return realMarket.approveBID(uuid,userEmail,storeID,productID);
    }

    /**
     * manager/owner reject BID
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<Boolean> rejectBID(String uuid, String userEmail, int storeID, int productID) {
        return realMarket.rejectBID(uuid,userEmail,storeID,productID);
    }

    /**
     * manager/owner counter BID with new price
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @param newTotalPrice
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<Boolean> counterBID(String uuid, String userEmail, int storeID, int productID, int newTotalPrice) {
        return realMarket.counterBID(uuid,userEmail,storeID,productID,newTotalPrice);
    }

    /**
     * user response to counter BID (approve/reject)
     *
     * @param uuid
     * @param storeID
     * @param productID
     * @param approve
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<Boolean> responseCounterBID(String uuid, int storeID, int productID, boolean approve) {
        return realMarket.responseCounterBID(uuid,storeID,productID,approve);
    }

    /**
     * user buy BID that approved by all owners
     *
     * @param userId
     * @param storeID
     * @param productID
     * @param city
     * @param adress
     * @param apartment
     * @param creditCard
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<Boolean> BuyBID(String userId, int storeID, int productID, String city, String adress, int apartment, ServiceCreditCard creditCard) {
        return realMarket.BuyBID(userId,storeID,productID,city,adress,apartment,creditCard);
    }

    /**
     * get status of specific BID
     *
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return true if success, else false
     */
    @Override
    public ATResponseObj<String> getBIDStatus(String uuid, String userEmail, int storeID, int productID) {
        return realMarket.getBIDStatus(uuid,userEmail,storeID,productID);
    }

    /**
     * get all bids in the store if has permission
     *
     * @param uuid
     * @param storeID
     * @return list of bids or error msg
     */
    @Override
    public ATResponseObj<HashMap<Integer, List<ServiceBID>>> getAllOffersBIDS(String uuid, int storeID) {
        return realMarket.getAllOffersBIDS(uuid,storeID);
    }

    /**
     * get all bids of user in the store
     *
     * @param uuid
     * @param storeID
     * @return list of bids or error msg
     */
    @Override
    public ATResponseObj<List<ServiceBID>> getMyBIDs(String uuid, int storeID) {
        return realMarket.getMyBIDs(uuid,storeID);
    }

    @Override
    public void modifyMessages(String uuid) {
        realMarket.modifyMessages(uuid);
    }
}
