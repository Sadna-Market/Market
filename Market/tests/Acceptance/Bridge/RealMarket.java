package Acceptance.Bridge;

import Acceptance.Obj.*;
import main.Service.*;

import java.util.*;
import java.util.stream.Collectors;

public class RealMarket implements MarketBridge {
    private TestableFacade market;

    public RealMarket() {
        market = new TestableFacade();
    }

    /**
     * init all resources of system (External Services, cart..)
     *
     * @param sysManager
     * @return true if success else false
     */
    public ATResponseObj<String> initSystem(User sysManager) {
        SLResponsOBJ<String> response = market.initMarket(sysManager.username, sysManager.password, sysManager.phone_number);
        return response.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(response.value,null);
    }

    /**
     * Discards all resources from init (doesn't change the memory)
     *
     * @param uuid
     */
    public void exitSystem(String uuid) {
        SLResponsOBJ<Boolean> res = market.guestLeave(uuid);
    }

    /**
     * Checks if the system has a system manager
     *
     * @return true if there is, else false
     */
    public boolean hasSystemManager() {
        return market.hasSystemManager().value;
    }

    /**
     * checks is the system has an External Service Payment connected
     *
     * @return true if there is, else false
     */
    public boolean hasPaymentService() {
        return market.hasPaymentService().value;
    }

    /**
     * checks is the system has an External Service Supply connected
     *
     * @return true if there is, else false
     */
    public boolean hasSupplierService() {
        return market.hasSupplierService().value;
    }

    /**
     * disconnects the service from system
     *
     * @param service the service to disconnect
     */
    public void disconnectExternalService(String service) {
        market.disconnectExternalService(service);
    }

    /**
     * checks wether the service is connected to the system
     *
     * @param service the service to check
     * @return true if the system has connection else false
     */
    public boolean serviceIsAlive(String service) {
        SLResponsOBJ<Boolean> res = market.serviceIsAlive(service);
        return res.value;
    }

    /**
     * contacts the Payment service to pay with creditCard amount of dollars
     *
     * @param creditCard the credit card to take information for paying
     * @param dollars    the amount to pay
     * @return response object with the recipe certification
     */
    public ATResponseObj<String> pay(CreditCard creditCard, int dollars) {
        SLResponsOBJ<String> res = creditCard ==null ? market.payment(null,dollars) :
                market.payment(new ServiceCreditCard(creditCard.num, creditCard.expire, creditCard.cvv), dollars);
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(res.value, null);
    }

    /**
     * contacts the Supply service to supply derliver items to user
     *
     * @param deliver the list of items to deliver
     * @param user    the use to deliver
     * @return certificate for success supply
     */
    public ATResponseObj<String> supply(List<ItemDetail> deliver, User user) {
        if(deliver == null) return new ATResponseObj<>("error");
        List<ServiceItem> lst = new ArrayList<>();
        deliver.forEach(i -> {
            lst.add(new ServiceItem(i.quantity, i.price, i.itemID,i.name));
        });
        ServiceUser su = new ServiceUser(user.username, user.password, user.phone_number, user.addr.city, user.addr.street, user.addr.apartment);
        SLResponsOBJ<Integer> res = market.supply(su, lst);
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(String.valueOf(res.value), null);
    }

    /**
     * chechs if exists a cart after init system
     *
     * @param uuid
     * @return true is exists else false
     */
    public boolean cartExists(String uuid) {
        SLResponsOBJ<Boolean> res = market.cartExists(uuid);
        return res.value;
    }

    /**
     * checks if system has guest "connected"
     *
     * @param uuid
     * @return true if yes else false
     */
    public boolean guestOnline(String uuid) {
        SLResponsOBJ<Boolean> res = market.guestOnline(uuid);
        return res.value;
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
        SLResponsOBJ<Boolean> response = market.addNewMember(uuid, username, password, "0522222222");
        return response.value;
    }

    /**
     * checks if newUser is registered
     *
     * @param newUser new user to check
     * @return true if registered else false
     */
    public boolean isMember(User newUser) {
        SLResponsOBJ<Boolean> res = market.isMember(newUser.username);
        return res.value;
    }

    /**
     * logs in the user to the system
     *
     * @param uuid
     * @param member the user to login
     * @return true if sucess else false
     */
    public ATResponseObj<String> login(String uuid, User member) {
        SLResponsOBJ<String> response = market.login(uuid, member.username, member.password);
        return response.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(response.value, null);
    }

    /**
     * check if the user is logged in to the system
     *
     * @param uuid the user to check
     * @return true if success else false
     */
    public boolean isLoggedIn(String uuid) {
        SLResponsOBJ<Boolean> res = market.isLoggedIn(uuid);
        return res.value;
    }

    /**
     * change the password of a user in the system
     *
     * @param member the member to change the password
     * @return true if success else false
     */
    public boolean changePassword(String uuid , User member , String newPassword) {
        SLResponsOBJ<Boolean> res = market.changePassword(uuid,member.username, member.password,newPassword);
        return res.value;
    }

    /**
     * get information of a store and its products
     *
     * @param storeID the store we want to know about
     * @return info of the store
     */
    public ATResponseObj<String> getStoreInfo(int storeID) {
        SLResponsOBJ<ServiceStore> response = market.getStore(storeID);
        String err = String.valueOf(response.errorMsg);
        return response.errorOccurred() ? new ATResponseObj<>(err) : new ATResponseObj<>(response.value.toString(),null);
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
        HashSet<Integer> items = new HashSet<>();
        SLResponsOBJ<List<Integer>> res = market.searchProductByName(itemName);
        if (!res.errorOccurred()) items.addAll(res.value);
        res = market.searchProductByCategory(1); //TODO: change category to string
        if (!res.errorOccurred()) items.addAll(res.value);
        for (String word : keyWords) {
            SLResponsOBJ<List<Integer>> result = market.searchProductByDesc(word);
            if (!result.errorOccurred()) items.addAll(result.value);
        }
        return items.isEmpty() ? new ATResponseObj<>("not found") : new ATResponseObj<>(items.stream().toList());
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
        Set<Integer> set = new HashSet<>();
        SLResponsOBJ<List<Integer>> res1 = market.searchProductByRate(items,productRank);
        if(res1.errorOccurred()) return new ATResponseObj<>("error");
        set.addAll(res1.value);
//        SLResponsOBJ<List<Integer>> res2 = market.searchProductByRangePrices(items,)
//        if(res2.errorOccurred()) return new ATResponseObj<>("error");
//        set.addAll(res2.value); //TODO: change
        SLResponsOBJ<List<Integer>> res3 = market.searchProductByCategory(items,category);
        if(res3.errorOccurred()) return new ATResponseObj<>("error");
        set.addAll(res3.value);
        SLResponsOBJ<List<Integer>> res4 = market.searchProductByStoreRate(items,storeRank);
        if(res4.errorOccurred()) return new ATResponseObj<>("error");
        set.addAll(res4.value);
        return new ATResponseObj<>(new ArrayList<>(set));
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
        SLResponsOBJ<Boolean> res;
        if(item == null)
            res = market.addProductToShoppingBag(uuid, storeID, -1, -1);
        else
            res = market.addProductToShoppingBag(uuid, storeID, item.itemID, item.quantity);
        return !res.errorOccurred();
    }

    /**
     * gets the cart of the current user
     *
     * @param uuid
     * @return list of list of items - Cart that has shopping bags related to different stores
     */
    public ATResponseObj<List<List<Integer>>> getCart(String uuid) {
        SLResponsOBJ<ServiceShoppingCard> res = market.getShoppingCart(uuid);
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(res.value.get());
    }

    /**
     * query to verify quantity of item in storeID
     *
     * @param storeID store that has the item
     * @param item    item to find out the quantity
     * @return quantity of the item in the store
     */
    public int getAmountOfProductInStore(int storeID, ItemDetail item) {
        SLResponsOBJ<Integer> res = market.getProductQuantity(storeID, item.itemID);
        return res.errorOccurred() ? -1 : res.value;
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
        SLResponsOBJ<Boolean> res = item == null ? market.removeProductFromShoppingBag(uuid,storeID,-1): market.removeProductFromShoppingBag(uuid, storeID, item.itemID);
        return res.value;
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
        SLResponsOBJ<Boolean> res = market.setProductQuantityShoppingBag(uuid, item.itemID, storeID, newQuantity);
        return res.value;
    }

    /**
     * resets all memory from ram (cart,members,history purchases...)
     */
    public void resetMemory() {
        market.reset();
    }

    /**
     * creates a new store with a owner
     *
     * @param uuid
     * @param owner owner of the store to be created
     * @return Response- msg error if occurred else id of the store that was created
     */
    public ATResponseObj<Integer> addStore(String uuid, User owner) {
        SLResponsOBJ<Integer> res = market.openNewStore(uuid, "moshe", owner.username, null, null, null);
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(res.value);
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
        SLResponsOBJ<Boolean> res = market.addNewProductToStore(uuid,storeID,item.itemID,item.price,item.quantity);
        return res.value;
    }

    /**
     * logout from connected user
     *
     * @param uuid
     * @return true if success else false
     */
    public ATResponseObj<String> logout(String uuid) {
        SLResponsOBJ<String> response = market.logout(uuid);
        return response.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(response.value, null);
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
        SLResponsOBJ<String> res = market.orderShoppingCart(uuid, address.city, address.street, address.apartment, new ServiceCreditCard(creditCard.num, creditCard.expire, creditCard.cvv));
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(res.value, null);
    }

    /**
     * query to get the history of all purchases of a store with storeID
     *
     * @param uuid
     * @param storeID the id of the store to get the history
     * @return list of all purchases accepted certificates
     */
    public ATResponseObj<List<String>> getHistoryPurchase(String uuid, int storeID) {
        SLResponsOBJ<List<ServiceHistory>> res = market.getStoreOrderHistory(uuid, storeID);
        if (res.errorOccurred()) return new ATResponseObj<>("error");
        List<String> tid = res.value.stream().map(h -> String.valueOf(h.getTID())).collect(Collectors.toList());
        return new ATResponseObj<>(tid);
    }

    /**
     * chechs if user is a contributor of store
     *
     * @param storeID the store to check on
     * @param user    the user to check if is a contributor
     * @return true is user is a contributor else false
     */
    public boolean isContributor(int storeID, User user) {
        SLResponsOBJ<Boolean> res = market.isFounder(storeID, user.username);
        return res.value;
    }

    /**
     * checks if store contains item in stock
     *
     * @param storeID id of store
     * @param itemID  id of item
     * @return true if contains item, else false
     */
    public boolean hasItem(int storeID, int itemID) {
        SLResponsOBJ<Boolean> res = market.hasItem(storeID, itemID);
        return res.value;
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
        SLResponsOBJ<Boolean> res = market.deleteProductFromStore(uuid, storeID, item.itemID);
        return res.value;
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
        SLResponsOBJ<Boolean> res = market.setProductPriceInStore(uuid, storeID, existingProduct.itemID, updatedProduct.price);
        if (res.errorOccurred()) return false;
        res = market.setProductQuantityInStore(uuid, storeID, existingProduct.itemID, updatedProduct.quantity);
        return res.value;
    }

    /**
     * query to get item info from store
     *
     * @param storeID id of the store
     * @param itemID  the id of the item
     * @return item will detail
     */
    public ATResponseObj<ItemDetail> getProduct(int storeID, int itemID) {
        //TODO: add ServiceProduct class and return it here
        return null;
    }

    /**
     * check if user is owner of the store
     *
     * @param storeID id of the store
     * @param user    user info
     * @return true if is owner, else false
     */
    public boolean isOwner(int storeID, User user) {
        SLResponsOBJ<Boolean> res = market.isOwner(user.username,storeID);
        return res.value;
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
        SLResponsOBJ<Boolean> res = market.addNewStoreOwner(uuid, storeID, newOwner.username);
        return res.value;
    }

    /**
     * checks if user is manager in store
     *
     * @param storeID id of store
     * @param user    user to check if manager
     * @return true if user is manager else false
     */
    public boolean isManager(int storeID, User user) {
        SLResponsOBJ<Boolean> res = market.isManager(user.username,storeID);
        return res.value;
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
        SLResponsOBJ<Boolean> res = market.addNewStoreManger(uuid, storeID, newManager.username);
        return res.value;
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
        SLResponsOBJ<Boolean> res = market.setManagerPermissions(uuid, storeID, manager.username, permission, onOf);
        return res.value;
    }

    /**
     * close the store
     *
     * @param uuid
     * @param storeID store id
     * @return true if success, else false
     */
    public boolean closeStore(String uuid, int storeID) {
        SLResponsOBJ<Boolean> res = market.closeStore(uuid, storeID);
        return res.value;
    }

    /**
     * checks if store is closed
     *
     * @param storeID id of store
     * @return true is store is closed, else false
     */
    public boolean storeIsClosed(int storeID) {
        SLResponsOBJ<Boolean> res = market.storeIsClosed(storeID);
        return res.value;
    }

    /**
     * query to get role info of user in store
     *
     * @param uuid    the user
     * @param storeID id of store
     * @return info of user's roles
     */
    public ATResponseObj<String> getUserRoleInfo(String uuid, int storeID) {
        SLResponsOBJ<HashMap<String, List<String>>> res = market.getStoreRoles(uuid, storeID);
        if (res.errorOccurred()) return new ATResponseObj<>("error");
        StringBuilder stb = new StringBuilder();
        res.value.forEach((k, v) -> {
            stb.append(k).append(": ");
            StringBuilder s = new StringBuilder();
            s.append("[");
            v.forEach(email -> s.append(email).append(","));
            s.append("]");
            stb.append(s.toString());
        });
        return new ATResponseObj<>(stb.toString(), null);
    }

    /**
     * query to get info of any user member in the system
     *
     * @param uuid
     * @param user the user
     * @return info of user
     */
    public ATResponseObj<String> getBuyerInfo(String uuid, User user) {
        SLResponsOBJ<List<List<ServiceHistory>>> userInfo = market.getUserInfo(uuid, user.username);
        if (userInfo.errorOccurred()) return new ATResponseObj<>("error");
        StringBuilder sb = new StringBuilder();
        userInfo.value.forEach(lst -> {
            lst.forEach(h -> {
                sb.append(h.toString()).append("\n\n");
            });
        });
        return new ATResponseObj<>(sb.toString());
    }

    @Override
    public ATResponseObj<Integer> addProductType(String uuid, ItemDetail item) {
        SLResponsOBJ<Integer> res = market.addNewProductType(uuid, item.name, "phone", 1); //TODO: change category to string
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(res.value);
    }

    /**
     * generates uuid
     *
     * @return return uuid
     */
    public String guestVisit() {
        return market.guestVisit().value;
    }

    /**
     * connects the specified service
     *
     * @param payment
     * @return
     */
    public boolean connectExternalService(String payment) {
        SLResponsOBJ<Boolean> res = market.connectService(payment);
        return res.value;
    }


}



