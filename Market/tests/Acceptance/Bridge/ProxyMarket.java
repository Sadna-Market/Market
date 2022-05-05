package Acceptance.Bridge;

import Acceptance.Obj.*;

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
    public ATResponseObj<String> initSystem(User sysManager) {
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
    public boolean register(String uuid, String username, String password) {
        return realMarket.register(uuid, username, password);
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
    public ATResponseObj<List<Integer>> searchItems(String itemName, String category, List<String> keyWords) {
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
    public ATResponseObj<List<Integer>> filterSearchResults(List<Integer> items, int productRank, String priceRange, String category, int storeRank) {
        return null;
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
    public ATResponseObj<List<List<ItemDetail>>> getCart(String uuid) {
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
    public ATResponseObj<String> purchaseCart(String uuid, CreditCard creditCard, Address address) {
        return realMarket.purchaseCart(uuid, creditCard, address);
    }

    /**
     * query to get the history of all purchases of a store with storeID
     *
     * @param uuid
     * @param storeID the id of the store to get the history
     * @return list of all purchases accepted certificates
     */
    public ATResponseObj<List<String>> getHistoryPurchase(String uuid, int storeID) {
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
        return new ATResponseObj<>("");
    }

    /**
     * check if user is owner of the store
     *
     * @param storeID id of the store
     * @param user    user info
     * @return true if is owner, else false
     */
    public boolean isOwner(int storeID, User user) {
        return false;
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
     * checks if user is manager in store
     *
     * @param storeID id of store
     * @param user    user to check if manager
     * @return true if user is manager else false
     */
    public boolean isManager(int storeID, User user) {
        return false;
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
     * checks if store is closed
     *
     * @param storeID id of store
     * @return true is store is closed, else false
     */
    public boolean storeIsClosed(int storeID) {
        return false;
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
        return new ATResponseObj<>("");
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
}
