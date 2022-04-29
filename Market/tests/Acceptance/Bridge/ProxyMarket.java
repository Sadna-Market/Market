package Acceptance.Bridge;

import Acceptance.Obj.*;

import java.util.List;

public class ProxyMarket implements MarketBridge{
    private final MarketBridge realMarket;
    public ProxyMarket(){
        realMarket = new RealMarket();
    }

    /**
     * init all resources of system (External Services, cart..)
     *
     * @return true if success else false
     */
    public boolean initSystem() {
        return false;
    }

    /**
     * Discards all resources from init (doesn't change the memory)
     */
    public void exitSystem() {

    }

    /**
     * Checks if the system has a system manager
     *
     * @return true if there is, else false
     */
    public boolean hasSystemManager() {
        return false;
    }

    /**
     * checks is the system has an External Service Payment connected
     *
     * @return true if there is, else false
     */
    public boolean hasPaymentService() {
        return false;
    }

    /**
     * checks is the system has an External Service Supply connected
     *
     * @return true if there is, else false
     */
    public boolean hasSupplierService() {
        return false;
    }

    /**
     * deletes all system managers( just for the testing)
     */
    public void deleteSystemManagers() {

    }

    /**
     * disconnects the service from system
     *
     * @param service the service to disconnect
     */
    public void disconnectExternalService(String service) {

    }

    /**
     * checks wether the service is connected to the system
     *
     * @param service the service to check
     * @return true if the system has connection else false
     */
    public boolean serviceIsAlive(String service) {
        return false;
    }

    /**
     * contacts the Payment service to pay with creditCard amount of dollars
     *
     * @param creditCard the credit card to take information for paying
     * @param dollars    the amount to pay
     * @return response object with the recipe certification
     */
    public ATResponseObj<String> pay(CreditCard creditCard, int dollars) {
        return new ATResponseObj<>("");
    }

    /**
     * contacts the Supply service to supply derliver items to user
     *
     * @param deliver the list of items to deliver
     * @param user    the use to deliver
     * @return certificate for success supply
     */
    public ATResponseObj<String> supply(List<ItemDetail> deliver, User user) {
        return new ATResponseObj<>("");
    }

    /**
     * chechs if exists a cart after init system
     *
     * @return true is exists else false
     */
    public boolean cartExists() {
        return false;
    }

    /**
     * checks if system has guest "connected"
     *
     * @return true if yes else false
     */
    public boolean guestOnline() {
        return false;
    }

    /**
     * register a new user to the system
     *
     * @param username username - email
     * @param password password
     * @return true if success else false
     */
    public boolean register(String username, String password) {
        return false;
    }

    /**
     * checks if newUser is registered
     *
     * @param newUser new user to check
     * @return true if registered else false
     */
    public boolean isMember(User newUser) {
        return false;
    }

    /**
     * logs in the user to the system
     *
     * @param member the user to login
     * @return true if sucess else false
     */
    public boolean login(User member) {
        return false;
    }

    /**
     * check if the user is logged in to the system
     *
     * @param member the user to check
     * @return true if success else false
     */
    public boolean isLoggedIn(User member) {
        return false;
    }

    /**
     * change the password of a user in the system
     *
     * @param member  the member to change the password
     * @param newPass the new password
     * @return true if success else false
     */
    public boolean changePassword(User member, String newPass) {
        return false;
    }

    /**
     * get information of a store and its products
     *
     * @param storeID the store we want to know about
     * @return info of the store
     */
    public ATResponseObj<String> getStoreInfo(int storeID) {
        return new ATResponseObj<>("");
    }

    /**
     * given item details, search for this item or related by category and keywords
     *
     * @param itemName item to search for
     * @param category category of the item
     * @param keyWords words that can relate to the items specifications
     * @return list of result items
     */
    public ATResponseObj<List<ItemDetail>> searchItems(String itemName, String category, List<String> keyWords) {
        return new ATResponseObj<>("");
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
    public ATResponseObj<List<ItemDetail>> filterSearchResults(List<ItemDetail> items, int productRank, String priceRange, String category, int storeRank) {
        return new ATResponseObj<>("");
    }

    /**
     * add to cart the item that is in storeID
     *
     * @param storeID the store that the item is related to
     * @param item    the item to add to the cart
     * @return true if success, else false
     */
    public boolean addToCart(int storeID, ItemDetail item) {
        return false;
    }

    /**
     * gets the cart of the current user
     *
     * @return list of list of items - Cart that has shopping bags related to different stores
     */
    public ATResponseObj<List<List<ItemDetail>>> getCart() {
        return new ATResponseObj<>("");
    }

    /**
     * query to verify quantity of item in storeID
     *
     * @param storeID store that has the item
     * @param item    item to find out the quantity
     * @return quantity of the item in the store
     */
    public int getAmountOfProductInStore(int storeID, ItemDetail item) {
        return 0;
    }

    /**
     * removes the item from the cart of current user's cart
     *
     * @param item item to remove
     * @return true if success, else false
     */
    public boolean removeProductFromCart(ItemDetail item) {
        return false;
    }

    /**
     * update the item of current user's cart to new quantity
     *
     * @param item        the item to update
     * @param newQuantity new quantity
     * @return true if success, else false
     */
    public boolean updateProductQuantity(ItemDetail item, int newQuantity) {
        return false;
    }

    /**
     * resets all memory from ram (cart,members,history purchases...)
     */
    public void resetMemory() {

    }

    /**
     * adds a system manager to the system (the highest permission)
     *
     * @param sysManager manager to assign
     */
    public void addSystemManager(User sysManager) {

    }

    /**
     * creates a new store with a owner
     *
     * @param owner owner of the store to be created
     * @return Response- msg error if occurred else id of the store that was created
     */
    public ATResponseObj<Integer> addStore(User owner) {
        return new ATResponseObj<>("");
    }

    /**
     * adds the item to the store
     *
     * @param storeID the store that the item will be added to
     * @param item    the item to add
     * @return true if success else false
     */
    public boolean addItemToStore(int storeID, ItemDetail item) {
        return false;
    }

    /**
     * logout from connected user
     *
     * @return true if success else false
     */
    public boolean logout() {
        return false;
    }

    /**
     * purchase the current cart of the user
     *
     * @param creditCard credit card details to take the money for payment service
     * @param address    address to send the items for supply service
     * @return certificated of payment and supply
     */
    public ATResponseObj<String> purchaseCart(CreditCard creditCard, Address address) {
        return new ATResponseObj<>("");
    }

    /**
     * query to get the history of all purchases of a store with storeID
     *
     * @param storeID the id of the store to get the history
     * @return list of all purchases accepted certificates
     */
    public ATResponseObj<List<String>> getHistoryPurchase(int storeID) {
        return new ATResponseObj<>("");
    }

    /**
     * chechs if user is a contributor of store
     *
     * @param storeID the store to check on
     * @param user    the user to check if is a contributor
     * @return true is user is a contributor else false
     */
    public boolean isContributor(int storeID, User user) {
        return false;
    }

    /**
     * checks if store contains item in stock
     *
     * @param storeID id of store
     * @param itemID  id of item
     * @return true if contains item, else false
     */
    public boolean hasItem(int storeID, int itemID) {
        return false;
    }

    /**
     * removes a product from store
     *
     * @param storeID the store in which to remove the product from
     * @param item    the product to remove
     * @return true if success, else false
     */
    public boolean removeProductFromStore(int storeID, ItemDetail item) {
        return false;
    }

    /**
     * update product details of a store
     *
     * @param storeID         the store to update its product
     * @param existingProduct the product to update
     * @param updatedProduct  the product details to update
     * @return true if success, else false
     */
    public boolean updateProductInStore(int storeID, ItemDetail existingProduct, ItemDetail updatedProduct) {
        return false;
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
     * @param storeID  store id to add owner
     * @param owner    existing owner of the store
     * @param newOwner new owner to add
     * @return true if success, else false
     */
    public boolean assignNewOwner(int storeID, User owner, User newOwner) {
        return false;
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
     * @param storeID    store id to add owner
     * @param user       existing owner/manager of the store
     * @param newManager new manager to add
     * @return true if success, else false
     */
    public boolean assignNewManager(int storeID, User user, User newManager) {
        return false;
    }

    /**
     * update/change permission of manager in store
     *
     * @param permission the new permission
     * @param onOf       turn on or off
     * @param manager    the manager
     * @param storeID    store id
     * @return true if success else false
     */
    public boolean updatePermission(String permission, boolean onOf, User manager, int storeID) {
        return false;
    }

    /**
     * close the store
     *
     * @param storeID store id
     * @return true if success, else false
     */
    public boolean closeStore(int storeID) {
        return false;
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
     * @param storeID id of store
     * @param user    the user
     * @return info of user's roles
     */
    public ATResponseObj<String> getUserRoleInfo(int storeID, User user) {
        return new ATResponseObj<>("");
    }

    /**
     * query to get info of any user member in the system
     *
     * @param user the user
     * @return info of user
     */
    public ATResponseObj<String> getBuyerInfo(User user) {
        return new ATResponseObj<>("");
    }
}
