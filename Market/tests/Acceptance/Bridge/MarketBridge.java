package Acceptance.Bridge;

import Acceptance.Obj.ATResponseObj;
import Acceptance.Obj.CreditCard;
import Acceptance.Obj.ItemDetail;
import Acceptance.Obj.User;

import java.util.List;

public interface MarketBridge {
    /**
     * init all resources of system (External Services, cart..)
     * @return true if success else false
     */
    boolean initSystem();

    /**
     * Discards all resources from init (doesn't change the memory)
     */
    void exitSystem();

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
     * deletes all system managers( just for the testing)
     */
    void deleteSystemManagers();

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
     */
    boolean cartExists();

    /**
     * checks if system has guest "connected"
     * @return true if yes else false
     */
    boolean guestOnline();

    /**
     * register a new user to the system
     * @param username username - email
     * @param password password
     * @return true if success else false
     */
    boolean register(String username, String password);

    /**
     * checks if newUser is registered
     * @param newUser new user to check
     * @return true if registered else false
     */
    boolean isMember(User newUser);

    /**
     * logs in the user to the system
     * @param member the user to login
     * @return true if sucess else false
     */
    boolean login(User member);

    /**
     * check if the user is logged in to the system
     * @param member the user to check
     * @return true if success else false
     */
    boolean isLoggedIn(User member);

    /**
     * change the password of a user in the system
     * @param member the member to change the password
     * @param newPass the new password
     * @return true if success else false
     */
    boolean changePassword(User member, String newPass);

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
    ATResponseObj<List<ItemDetail>> searchItems(String itemName, String category, List<String> keyWords);

    /**
     * given a list of items, filter them by rank,price range, store rank, category.
     * @param items list of items to filter
     * @param productRank filter param
     * @param priceRange filter param
     * @param category filter param
     * @param storeRank filter param
     * @return filtered list
     */
    ATResponseObj<List<ItemDetail>> filterSearchResults(List<ItemDetail> items, int productRank, String priceRange, String category, int storeRank);

    /**
     * add to cart the item that is in storeID
     * @param storeID the store that the item is related to
     * @param item the item to add to the cart
     * @return true if success, else false
     */
    boolean addToCart(int storeID, ItemDetail item);

    /**
     * gets the cart of the current user
     * @return list of list of items - Cart that has shopping bags related to different stores
     */
    ATResponseObj<List<List<ItemDetail>>> getCart();

    /**
     * query to verify quantity of item in storeID
     * @param storeID store that has the item
     * @param item item to find out the quantity
     * @return quantity of the item in the store
     */
    int getAmountOfProductInStore(int storeID, ItemDetail item);

    /**
     * removes the item from the cart of current user's cart
     * @param item item to remove
     * @return true if success, else false
     */
    boolean removeProductFromCart(ItemDetail item);
}
