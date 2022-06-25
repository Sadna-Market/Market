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

public interface MarketBridge {
    /**
     * init all resources of system (External Services, cart..)
     * @return true if success else false
     * @param sysManager
     */
    ATResponseObj<Boolean> initSystem(User sysManager);

    /**
     * Discards all resources from init (doesn't change the memory)
     * @param uuid
     */
    void exitSystem(String uuid);

    /**
     * Checks if the system has a system manager
     * @return true if there is, else false
     */
    boolean hasSystemManager();

    /**
     * checks is the system has an External Service Payment connected
     * @return true if there is, else false
     */
    boolean hasPaymentService();

    /**
     * checks is the system has an External Service Supply connected
     * @return true if there is, else false
     */
    boolean hasSupplierService();

    /**
     * disconnects the service from system
     * @param service the service to disconnect
     */
    void disconnectExternalService(String service);

    /**
     * checks wether the service is connected to the system
     * @param service the service to check
     * @return true if the system has connection else false
     */
    boolean serviceIsAlive(String service);

    /**
     * contacts the Payment service to pay with creditCard amount of dollars
     * @param creditCard the credit card to take information for paying
     * @param dollars the amount to pay
     * @return response object with the recipe certification
     */
    ATResponseObj<String> pay(CreditCard creditCard, int dollars);

    /**
     * contacts the Supply service to supply derliver items to user
     * @param deliver the list of items to deliver
     * @param user the use to deliver
     * @return certificate for success supply
     */
    ATResponseObj<String> supply(List<ItemDetail> deliver, User user);

    /**
     * chechs if exists a cart after init system
     * @return true is exists else false
     * @param uuid
     */
    boolean cartExists(String uuid);

    /**
     * checks if system has guest "connected"
     * @return true if yes else false
     * @param uuid
     */
    boolean guestOnline(String uuid);

    /**
     * register a new user to the system
     *
     * @param uuid
     * @param username username - email
     * @param password password
     * @return true if success else false
     */
    boolean register(String uuid, String username, String password, String dateOfBirth);

    /**
     * checks if newUser is registered
     * @param newUser new user to check
     * @return true if registered else false
     */
    boolean isMember(User newUser);

    /**
     * logs in the user to the system
     *
     * @param uuid
     * @param member the user to login
     * @return true if sucess else false
     */
    ATResponseObj<String> login(String uuid, User member);

    /**
     * check if the user is logged in to the system
     * @param uuid the user to check
     * @return true if success else false
     */
    boolean isLoggedIn(String uuid);

    /**
     * change the password of a user in the system
     * @param member the member to change the password
     * @return true if success else false
     */
    public boolean changePassword(String uuid , User member , String newPassword) ;

    /**
     * get information of a store and its products
     * @param storeID the store we want to know about
     * @return info of the store
     */
    ATResponseObj<String> getStoreInfo(int storeID);

    /**
     * given item details, search for this item or related by category and keywords
     * @param itemName item to search for
     * @param category category of the item
     * @param keyWords words that can relate to the items specifications
     * @return list of result items
     */
    ATResponseObj<List<Integer>> searchItems(String itemName, int category, List<String> keyWords);

    /**
     * given a list of items, filter them by rank,price range, store rank, category.
     * @param items list of items to filter
     * @param productRank filter param
     * @param priceRange filter param
     * @param category filter param
     * @param storeRank filter param
     * @return filtered list
     */
    ATResponseObj<List<Integer>> filterSearchResults(List<Integer> items, int productRank, int[] priceRange, int category, int storeRank);

    /**
     * add to cart the item that is in storeID
     *
     * @param uuid
     * @param storeID the store that the item is related to
     * @param item the item to add to the cart
     * @return true if success, else false
     */
    boolean addToCart(String uuid, int storeID, ItemDetail item);

    /**
     * gets the cart of the current user
     * @return list of list of items - Cart that has shopping bags related to different stores
     * @param uuid
     */
    ATResponseObj<List<List<Integer>>> getCart(String uuid);

    /**
     * query to verify quantity of item in storeID
     * @param storeID store that has the item
     * @param item item to find out the quantity
     * @return quantity of the item in the store
     */
    int getAmountOfProductInStore(int storeID, ItemDetail item);

    /**
     * removes the item from the cart of current user's cart
     *
     * @param uuid
     * @param item item to remove
     * @param storeID
     * @return true if success, else false
     */
    boolean removeProductFromCart(String uuid, ItemDetail item, int storeID);

    /**
     * update the item of current user's cart to new quantity
     *
     * @param uuid
     * @param item the item to update
     * @param newQuantity new quantity
     * @param storeID
     * @return true if success, else false
     */
    boolean updateProductQuantity(String uuid, ItemDetail item, int newQuantity, int storeID);

    /**
     * resets all memory from ram (cart,members,history purchases...)
     */
    void resetMemory();

    /**
     * creates a new store with a owner
     *
     * @param uuid
     * @param owner owner of the store to be created
     * @return Response- msg error if occurred else id of the store that was created
     */
    ATResponseObj<Integer> addStore(String uuid, User owner);

    /**
     * adds the item to the store
     *
     * @param uuid
     * @param storeID the store that the item will be added to
     * @param item the item to add
     * @return  true if success else false
     */
    boolean addItemToStore(String uuid, int storeID, ItemDetail item);

    /**
     * logout from connected user
     * @return true if success else false
     * @param uuid
     */
    ATResponseObj<String> logout(String uuid);

    /**
     * purchase the current cart of the user
     *
     * @param uuid
     * @param creditCard credit card details to take the money for payment service
     * @param address address to send the items for supply service
     * @return certificated of payment and supply
     */
    ATResponseObj<ServiceDetailsPurchase> purchaseCart(String uuid, CreditCard creditCard, Address address);

    /**
     * query to get the history of all purchases of a store with storeID
     *
     * @param uuid
     * @param storeID the id of the store to get the history
     * @return list of all purchases accepted certificates
     */
    ATResponseObj<List<History>> getHistoryPurchase(String uuid, int storeID);

    /**
     * chechs if user is a contributor of store
     * @param storeID the store to check on
     * @param user the user to check if is a contributor
     * @return true is user is a contributor else false
     */
    boolean isContributor(int storeID, User user);

    /**
     * checks if store contains item in stock
     * @param storeID id of store
     * @param itemID id of item
     * @return true if contains item, else false
     */
    boolean hasItem(int storeID, int itemID);

    /**
     * removes a product from store
     *
     * @param uuid
     * @param storeID the store in which to remove the product from
     * @param item the product to remove
     * @return true if success, else false
     */
    boolean removeProductFromStore(String uuid, int storeID, ItemDetail item);

    /**
     * update product details of a store
     *
     * @param uuid
     * @param storeID the store to update its product
     * @param existingProduct the product to update
     * @param updatedProduct the product details to update
     * @return true if success, else false
     */
    boolean updateProductInStore(String uuid, int storeID, ItemDetail existingProduct, ItemDetail updatedProduct);

    /**
     * query to get item info from store
     * @param storeID id of the store
     * @param itemID the id of the item
     * @return item will detail
     */
    ATResponseObj<ItemDetail> getProduct(int storeID, int itemID);

    /**
     * check if user is owner of the store
     * @param storeID id of the store
     * @param user user info
     * @return true if is owner, else false
     */
    boolean isOwner(int storeID, User user);

    /**
     * assigns a new user to be owner of the store
     *
     * @param uuid
     * @param storeID store id to add owner
     * @param newOwner new owner to add
     * @return true if success, else false
     */
    boolean assignNewOwner(String uuid, int storeID, User newOwner);


    /**
     * remove store owner and the permission that this owner is grantor
     * @param UserId
     * @param StoreId
     * @param OwnerEmail
     * @return true if success, else false
     */
    boolean removeStoreOwner(String UserId, int StoreId, String OwnerEmail);


    /**
     * checks if user is manager in store
     * @param storeID id of store
     * @param user user to check if manager
     * @return true if user is manager else false
     */
    boolean isManager(int storeID, User user);

    /**
     * assigns a new user to be manager of the store
     *
     * @param uuid
     * @param storeID store id to add owner
     * @param newManager new manager to add
     * @return true if success, else false
     */
    boolean assignNewManager(String uuid, int storeID, User newManager);

    /**
     * update/change permission of manager in store
     *
     * @param uuid
     * @param permission the new permission
     * @param onOf turn on or off
     * @param manager the manager
     * @param storeID store id
     * @return true if success else false
     */
    boolean updatePermission(String uuid, String permission, boolean onOf, User manager, int storeID);

    /**
     * close the store
     *
     * @param uuid
     * @param storeID store id
     * @return true if success, else false
     */
    boolean closeStore(String uuid, int storeID);

    /**
     * reopen store
     * @param uuid
     * @param storeID
     * @return  true if success, else false
     */
    boolean reopenStore(String uuid, int storeID);

    /**
     * checks if store is closed
     * @param storeID id of store
     * @return true is store is closed, else false
     */
    boolean storeIsClosed(int storeID);

    /**
     * query to get role info of user in store
     * @param uuid the user
     * @param storeID id of store
     * @return info of user's roles
     */
    ATResponseObj<String> getUserRoleInfo(String uuid, int storeID);

    /**
     * query to get info of any user member in the system
     *
     * @param uuid
     * @param user the user
     * @return info of user
     */
    ATResponseObj<String> getBuyerInfo(String uuid, User user);

    /**
     * add product type to system
     *
     * @param uuid
     * @param item
     * @return id of the item
     */
    ATResponseObj<Integer> addProductType(String uuid, ItemDetail item);

    /**
     * generates uuid
     * @return return uuid
     */
    String guestVisit();

    /**
     * connects the specified service
     * @param payment
     * @return
     */
    boolean connectExternalService(String payment);

    /**
     * add buy rule to this store
     * @param uuid
     * @param storeId
     * @param buyRule
     * @return
     */
    boolean addNewBuyRule(String uuid, int storeId, BuyRuleSL buyRule);

    /**
     * remove buy rule to this store
     * @param uuid
     * @param storeId
     * @param buyRuleID
     * @return
     */
    boolean removeBuyRule(String uuid, int storeId, int buyRuleID);





    /**
     * add discount rule to this store
     * @param uuid
     * @param storeId
     * @param discountRule
     * @return
     */
    boolean addNewDiscountRule(String uuid, int storeId, DiscountRuleSL discountRule);

    /**
     * remove discount rule to this store
     * @param uuid
     * @param storeId
     * @param discountRuleID
     * @return
     */
    boolean removeDiscountRule(String uuid, int storeId, int discountRuleID);

    /**
     * get buy rule of this store
     * @param uuid
     * @param storeId
     * @return list of all buy rules
     */
    ATResponseObj<List<BuyRuleSL>> getBuyPolicy(String uuid, int storeId);


    /**
     * get discount rule of this store
     * @param uuid
     * @param storeId
     * @return list of all discount rules
     */
    ATResponseObj<List<DiscountRuleSL>> getDiscountPolicy(String uuid, int storeId);



    /**
     * gets all logged in members info in the market
     * @param uuid
     * @return
     */
    ATResponseObj<List<User>> getLoggedInMembers(String uuid);

    /**
     * gets all logged out members info in the market
     * @param uuid
     * @return
     */
    ATResponseObj<List<User>> getLoggedOutMembers(String uuid);

    /**
     * Cancel a membership in the market
     * This can only be done by the System manager
     * Note: if the user to cancel is the founder of a store then store will be removed from the market and Owners/Managers will be informed.
     * @param uuid the uuid of the System manager
     * @param cancelUser the user to cancel
     * @return true if success, else false
     */
    boolean cancelMembership(String uuid, User cancelUser);

    ATResponseObj<List<ServiceStore>> getAllStores();

    boolean isOwnerUUID(String uuid, int storeID);
    boolean isSysManagerUUID(String uuid);
    boolean isManagerUUID(String uuid, int storeID);

    /**
     * create new BID in store by user
     * @param uuid
     * @param storeID
     * @param productID
     * @param quantity
     * @param totalPrice
     * @return  true if success, else false
     */
    public ATResponseObj<Boolean> createBID(String uuid, int storeID, int productID, int quantity, int totalPrice);

    /**
     * remove BID from store
     * @param uuid
     * @param storeID
     * @param productID
     * @return  true if success, else false
     */
    public ATResponseObj<Boolean> removeBID(String uuid, int storeID, int productID);

    /**
     * manager/owner approve BID
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return  true if success, else false
     */
    public ATResponseObj<Boolean> approveBID(String uuid, String userEmail, int storeID, int productID);

    /**
     * manager/owner reject BID
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return true if success, else false
     */
    public ATResponseObj<Boolean> rejectBID(String uuid, String userEmail, int storeID, int productID);

    /**
     * manager/owner counter BID with new price
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @param newTotalPrice
     * @return true if success, else false
     */
    public ATResponseObj<Boolean> counterBID(String uuid, String userEmail, int storeID, int productID, int newTotalPrice);

    /**
     * user response to counter BID (approve/reject)
     * @param uuid
     * @param storeID
     * @param productID
     * @param approve
     * @return true if success, else false
     */
    public ATResponseObj<Boolean> responseCounterBID(String uuid, int storeID, int productID , boolean approve);

    /**
     * user buy BID that approved by all owners
     * @param userId
     * @param storeID
     * @param productID
     * @param city
     * @param adress
     * @param apartment
     * @param creditCard
     * @return  true if success, else false
     */
    public ATResponseObj<Boolean> BuyBID(String userId,int storeID, int productID, String city, String adress, int apartment, ServiceCreditCard creditCard);

    /**
     * get status of specific BID
     * @param uuid
     * @param userEmail
     * @param storeID
     * @param productID
     * @return  true if success, else false
     */
    public ATResponseObj<String> getBIDStatus(String uuid, String userEmail, int storeID, int productID);


    /**
     * get all bids in the store if has permission
     * @param uuid
     * @param storeID
     * @return list of bids or error msg
     */
    public ATResponseObj<HashMap<Integer,List<ServiceBID>>> getAllOffersBIDS(String uuid, int storeID);


    /**
     * get all bids of user in the store
     * @param uuid
     * @param storeID
     * @return list of bids or error msg
     */
    public ATResponseObj<List<ServiceBID>> getMyBIDs(String uuid, int storeID);

    public void modifyMessages(String uuid);


    }
