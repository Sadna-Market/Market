package com.example.Acceptance.Bridge;


import com.example.Acceptance.Obj.*;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceObj.*;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.*;
import com.example.demo.Service.TestableFacade;

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
    public ATResponseObj<Boolean> initSystem(User sysManager) {
        SLResponseOBJ<Boolean> response = market.initMarket(sysManager.username, sysManager.password, sysManager.phone_number,sysManager.dateOfBirth);
        return response.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(response.value, null);
    }

    /**
     * Discards all resources from init (doesn't change the memory)
     *
     * @param uuid
     */
    public void exitSystem(String uuid) {
        SLResponseOBJ<Boolean> res = market.guestLeave(uuid);
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
        SLResponseOBJ<Boolean> res = market.serviceIsAlive(service);
        return !res.errorOccurred();
    }

    /**
     * contacts the Payment service to pay with creditCard amount of dollars
     *
     * @param creditCard the credit card to take information for paying
     * @param dollars    the amount to pay
     * @return response object with the recipe certification
     */
    public ATResponseObj<String> pay(CreditCard creditCard, int dollars) {
        SLResponseOBJ<String> res = creditCard == null ? market.payment(null, dollars) :
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
        if (deliver == null) return new ATResponseObj<>("error");
        List<ServiceProductStore> lst = new ArrayList<>();
        deliver.forEach(i -> {
            lst.add(new ServiceProductStore(i.quantity, i.price, i.itemID, i.name));
        });
        ServiceUser su = new ServiceUser(user.username, user.password, user.phone_number, user.addr.city, user.addr.street, user.addr.apartment,user.dateOfBirth);
        SLResponseOBJ<Integer> res = market.supply(su, lst);
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(String.valueOf(res.value), null);
    }

    /**
     * chechs if exists a cart after init system
     *
     * @param uuid
     * @return true is exists else false
     */
    public boolean cartExists(String uuid) {
        SLResponseOBJ<Boolean> res = market.cartExists(uuid);
        return !res.errorOccurred();
    }

    /**
     * checks if system has guest "connected"
     *
     * @param uuid
     * @return true if yes else false
     */
    public boolean guestOnline(String uuid) {
        SLResponseOBJ<Boolean> res = market.guestOnline(uuid);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> response = market.addNewMember(uuid, username, password, "0522222222",dateOfBirth);
        return !response.errorOccurred();
    }

    /**
     * checks if newUser is registered
     *
     * @param newUser new user to check
     * @return true if registered else false
     */
    public boolean isMember(User newUser) {
        SLResponseOBJ<Boolean> res = market.isMember(newUser.username);
        return !res.errorOccurred();
    }

    /**
     * logs in the user to the system
     *
     * @param uuid
     * @param member the user to login
     * @return true if sucess else false
     */
    public ATResponseObj<String> login(String uuid, User member) {
        SLResponseOBJ<String> response = market.login(uuid, member.username, member.password);
        return response.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(response.value, null);
    }

    /**
     * check if the user is logged in to the system
     *
     * @param uuid the user to check
     * @return true if success else false
     */
    public boolean isLoggedIn(String uuid) {
        SLResponseOBJ<Boolean> res = market.isLoggedIn(uuid);
        return !res.errorOccurred();
    }

    /**
     * change the password of a user in the system
     *
     * @param member the member to change the password
     * @return true if success else false
     */
    public boolean changePassword(String uuid, User member, String newPassword) {
        SLResponseOBJ<Boolean> res = market.changePassword(uuid, member.username, member.password, newPassword);
        return !res.errorOccurred();
    }

    /**
     * get information of a store and its products
     *
     * @param storeID the store we want to know about
     * @return info of the store
     */
    public ATResponseObj<String> getStoreInfo(int storeID) {
        SLResponseOBJ<ServiceStore> response = market.getStore(storeID);
        String err = String.valueOf(response.errorMsg);
        return response.errorOccurred() ? new ATResponseObj<>(err) : new ATResponseObj<>(response.value.toString(), null);
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
        SLResponseOBJ<List<Integer>> res1 = market.searchProductByName(itemName);
        if (!res1.errorOccurred()) items.addAll(res1.value);
        SLResponseOBJ<List<Integer>> res2 = market.searchProductByCategory(category); //TODO: change category to string
        if (!res2.errorOccurred()) items.addAll(res2.value);
        if (keyWords != null) {
            for (String word : keyWords) {
                SLResponseOBJ<List<Integer>> result = market.searchProductByDesc(word);
                if (!result.errorOccurred()) items.addAll(result.value);
                items.addAll(result.value);
            }
        }
        return new ATResponseObj<>(new ArrayList<>(items));
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
        SLResponseOBJ<List<Integer>> res1 = market.filterByRate(items, productRank);
        if (res1.errorOccurred()) return new ATResponseObj<>("error");
        Set<Integer> set = new HashSet<>(res1.value);
        SLResponseOBJ<List<Integer>> res2 = market.filterByRangePrices(items,priceRange[0],priceRange[1]);
        if(res2.errorOccurred()) return new ATResponseObj<>("error");
        set.addAll(res2.value);
        SLResponseOBJ<List<Integer>> res3 = market.filterByCategory(items, category);
        if (res3.errorOccurred()) return new ATResponseObj<>("error");
        set.addAll(res3.value);
        SLResponseOBJ<List<Integer>> res4 = market.filterByStoreRate(items, storeRank);
        if (res4.errorOccurred()) return new ATResponseObj<>("error");
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
        SLResponseOBJ<Boolean> res;
        if (item == null)
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
        SLResponseOBJ<ServiceShoppingCart> res = market.getShoppingCart(uuid);
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
        SLResponseOBJ<Integer> res = market.getProductQuantity(storeID, item.itemID);
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
        SLResponseOBJ<Boolean> res = item == null ? market.removeProductFromShoppingBag(uuid, storeID, -1) : market.removeProductFromShoppingBag(uuid, storeID, item.itemID);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res = market.setProductQuantityShoppingBag(uuid, item.itemID, storeID, newQuantity);
        return !res.errorOccurred();
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
        SLResponseOBJ<Integer> res;
        String storeName = owner == null ? "null" : owner.name;
        if (owner == null)
            res = market.openNewStore(uuid, String.format("store of %s",storeName), null, null, null, null);
        else
            res = market.openNewStore(uuid, String.format("store of %s",storeName), owner.username, null, null, null);
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
        SLResponseOBJ<Boolean> res;
        if (item == null) {
            res = market.addNewProductToStore(uuid, storeID, -1, -1, -1);
        } else
            res = market.addNewProductToStore(uuid, storeID, item.itemID, item.price, item.quantity);
        return !res.errorOccurred();
    }

    /**
     * logout from connected user
     *
     * @param uuid
     * @return true if success else false
     */
    public ATResponseObj<String> logout(String uuid) {
        SLResponseOBJ<String> response = market.logout(uuid);
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
    public ATResponseObj<ServiceDetailsPurchase> purchaseCart(String uuid, CreditCard creditCard, Address address) {
        SLResponseOBJ<ServiceDetailsPurchase> res = market.orderShoppingCart(uuid,
                address.city,
                address.street,
                address.apartment,
                new ServiceCreditCard(creditCard.num, creditCard.expire, creditCard.cvv));
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(res.value, null);
    }

    /**
     * query to get the history of all purchases of a store with storeID
     *
     * @param uuid
     * @param storeID the id of the store to get the history
     * @return list of all purchases accepted certificates
     */
    public ATResponseObj<List<History>> getHistoryPurchase(String uuid, int storeID) {
        SLResponseOBJ<List<ServiceHistory>> res = market.getStoreOrderHistory(uuid, storeID);
        if (res.errorOccurred()) return new ATResponseObj<>("error");
//        List<String> tid = res.value.stream().map(h -> String.valueOf(h.getTID())).collect(Collectors.toList());
          List<History> histories = res.value.stream().map(History::new).collect(Collectors.toList());

        return new ATResponseObj<>(histories);
    }

    /**
     * chechs if user is a contributor of store
     *
     * @param storeID the store to check on
     * @param user    the user to check if is a contributor
     * @return true is user is a contributor else false
     */
    public boolean isContributor(int storeID, User user) {
        SLResponseOBJ<Boolean> res = market.isFounder(storeID, user.username);
        return !res.errorOccurred();
    }

    /**
     * checks if store contains item in stock
     *
     * @param storeID id of store
     * @param itemID  id of item
     * @return true if contains item, else false
     */
    public boolean hasItem(int storeID, int itemID) {
        SLResponseOBJ<Boolean> res = market.hasItem(storeID, itemID);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res;
        if (item == null) {
            res = market.deleteProductFromStore(uuid, storeID, -1);
        } else res = market.deleteProductFromStore(uuid, storeID, item.itemID);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res = market.setProductPriceInStore(uuid, storeID, existingProduct == null? -1: existingProduct.itemID,updatedProduct == null? -1: updatedProduct.price);
        if (res.errorOccurred()) return false;
        res = market.setProductQuantityInStore(uuid, storeID, existingProduct == null? -1: existingProduct.itemID, updatedProduct == null ? -1 : updatedProduct.quantity);
        return !res.errorOccurred();
    }

    /**
     * query to get item info from store
     *
     * @param storeID id of the store
     * @param itemID  the id of the item
     * @return item will detail
     */
    public ATResponseObj<ItemDetail> getProduct(int storeID, int itemID) {
        SLResponseOBJ<ServiceProductStore> res = market.getInfoProductInStore(storeID, itemID);
        return res.errorOccurred() ? new ATResponseObj<>("error") : new ATResponseObj<>(new ItemDetail(res.value));
    }

    /**
     * check if user is owner of the store
     *
     * @param storeID id of the store
     * @param user    user info
     * @return true if is owner, else false
     */
    public boolean isOwner(int storeID, User user) {
        SLResponseOBJ<Boolean> res = market.isOwner(user.username, storeID);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res = market.addNewStoreOwner(uuid, storeID, newOwner == null ? null : newOwner.username);
        return !res.errorOccurred();
    }


    /**
     * remove store owner and the permission that this owner is grantor
     *
     * @param UserId
     * @param StoreId
     * @param OwnerEmail
     * @return true if success, else false
     */
    @Override
    public boolean removeStoreOwner(String UserId, int StoreId, String OwnerEmail) {
        SLResponseOBJ<Boolean> res = market.removeStoreOwner(UserId, StoreId, OwnerEmail);
        return !res.errorOccurred();
    }


    /**
     * checks if user is manager in store
     *
     * @param storeID id of store
     * @param user    user to check if manager
     * @return true if user is manager else false
     */
    public boolean isManager(int storeID, User user) {
        SLResponseOBJ<Boolean> res = market.isManager(user.username, storeID);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res = market.addNewStoreManger(uuid, storeID, newManager == null ? null : newManager.username);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res;
        if (permission == null)
            res = market.setManagerPermissions(uuid, storeID, manager.username, null, onOf);
        else
            res = market.setManagerPermissions(uuid, storeID, manager.username, permission, onOf);
        return !res.errorOccurred();
    }

    /**
     * close the store
     *
     * @param uuid
     * @param storeID store id
     * @return true if success, else false
     */
    public boolean closeStore(String uuid, int storeID) {
        SLResponseOBJ<Boolean> res = market.closeStore(uuid, storeID);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res = market.reopenStore(uuid, storeID);
        return !res.errorOccurred();
    }

    /**
     * checks if store is closed
     *
     * @param storeID id of store
     * @return true is store is closed, else false
     */
    public boolean storeIsClosed(int storeID) {
        SLResponseOBJ<Boolean> res = market.storeIsClosed(storeID);
        return !res.errorOccurred();
    }

    /**
     * query to get role info of user in store
     *
     * @param uuid    the user
     * @param storeID id of store
     * @return info of user's roles
     */
    public ATResponseObj<String> getUserRoleInfo(String uuid, int storeID) {
        SLResponseOBJ<HashMap<String, List<String>>> res = market.getStoreRoles(uuid, storeID);
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
        SLResponseOBJ<List<List<ServiceHistory>>> userInfo = market.getUserInfo(uuid, user.username);
        if (userInfo.errorOccurred()) return new ATResponseObj<>("error");
        StringBuilder sb = new StringBuilder();
        userInfo.value.forEach(lst -> {
            lst.forEach(h -> {
                sb.append(h.toString()).append("\n\n");
            });
        });
        return new ATResponseObj<>(sb.toString(),null);
    }

    @Override
    public ATResponseObj<Integer> addProductType(String uuid, ItemDetail item) {
        System.out.println("ooooooo "+uuid);
        SLResponseOBJ<Integer> res = market.addNewProductType(uuid, item.name, "phone", 1); //TODO: change category to string
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
        SLResponseOBJ<Boolean> res = market.connectService(payment);
        return !res.errorOccurred();
    }

    /**
     * add buy rule to this store
     *
     * @param uuid
     * @param storeId
     * @param buyRule
     * @return
     */
    @Override
    public boolean addNewBuyRule(String uuid, int storeId, BuyRuleSL buyRule) {
        SLResponseOBJ<Boolean> res = market.addNewBuyRule(uuid, storeId, buyRule);
        return !res.errorOccurred();
    }

    /**
     * remove buy rule to this store
     *
     * @param uuid
     * @param storeId
     * @param buyRuleID
     * @return
     */
    @Override
    public boolean removeBuyRule(String uuid, int storeId, int buyRuleID) {
        SLResponseOBJ<Boolean> res = market.removeBuyRule(uuid, storeId, buyRuleID);
        return !res.errorOccurred();
    }

    /**
     * add discount rule to this store
     *
     * @param uuid
     * @param storeId
     * @param discountRule
     * @return
     */
    @Override
    public boolean addNewDiscountRule(String uuid, int storeId, DiscountRuleSL discountRule) {
        SLResponseOBJ<Boolean> res = market.addNewDiscountRule(uuid, storeId, discountRule);
        return !res.errorOccurred();
    }

    /**
     * remove discount rule to this store
     *
     * @param uuid
     * @param storeId
     * @param discountRuleID
     * @return
     */
    @Override
    public boolean removeDiscountRule(String uuid, int storeId, int discountRuleID) {
        SLResponseOBJ<Boolean> res = market.removeDiscountRule(uuid, storeId, discountRuleID);
        return !res.errorOccurred();
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
        SLResponseOBJ<List<BuyRuleSL>> res = market.getBuyPolicy(uuid,storeId);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<List<DiscountRuleSL>> res = market.getDiscountPolicy(uuid,storeId);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
    }

    /**
     * gets all logged in members info in the market
     *
     * @param uuid
     * @return
     */
    public ATResponseObj<List<User>> getLoggedInMembers(String uuid) {
        SLResponseOBJ<List<ServiceUser>> res = market.getloggedInMembers(uuid);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        List<User> users = new ArrayList<>();
        res.value.forEach(serviceUser -> {
            users.add(new User(serviceUser));
        });
        return new ATResponseObj<>(users);
    }

    /**
     * gets all logged out members info in the market
     *
     * @param uuid
     * @return
     */
    public ATResponseObj<List<User>> getLoggedOutMembers(String uuid) {
        SLResponseOBJ<List<ServiceUser>> res = market.getloggedOutMembers(uuid);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        List<User> users = new ArrayList<>();
        res.value.forEach(serviceUser -> {
            users.add(new User(serviceUser));
        });
        return new ATResponseObj<>(users);
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
        SLResponseOBJ<Boolean> res = market.cancelMembership(uuid,cancelUser.username);
        return !res.errorOccurred();
    }

    public ATResponseObj<List<ServiceStore>> getAllStores() {
        SLResponseOBJ<List<ServiceStore>> res = market.getAllStores();
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value,null);
    }

    public boolean isOwnerUUID(String uuid, int storeID) {
        SLResponseOBJ<Boolean> res = market.isOwnerUUID(uuid,storeID);
        return !res.errorOccurred();
    }

    public boolean isSysManagerUUID(String uuid) {
        SLResponseOBJ<Boolean> res = market.isSystemManagerUUID(uuid);
        return !res.errorOccurred();
    }

    public boolean isManagerUUID(String uuid, int storeID) {
        SLResponseOBJ<Boolean> res = market.isManagerUUID(uuid,storeID);
        return !res.errorOccurred();
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
        SLResponseOBJ<Boolean> res = market.createBID(uuid,storeID,productID,quantity,totalPrice);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<Boolean>  res = market.removeBID(uuid,storeID,productID);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<Boolean>  res = market.approveBID(uuid,userEmail,storeID,productID);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<Boolean>  res = market.rejectBID(uuid,userEmail,storeID,productID);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<Boolean>  res = market.counterBID(uuid,userEmail,storeID,productID,newTotalPrice);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<Boolean>  res = market.responseCounterBID(uuid,storeID,productID,approve);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<Boolean> res = market.BuyBID(userId,storeID,productID,city,adress,apartment,creditCard);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<String> res = market.getBIDStatus(uuid,userEmail,storeID,productID);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<HashMap<Integer, List<ServiceBID>>> res = market.getAllOffersBIDS(uuid,storeID);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
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
        SLResponseOBJ<List<ServiceBID>> res = market.getMyBIDs(uuid,storeID);
        if(res.errorOccurred()) return new ATResponseObj<>(null,String.valueOf(res.errorMsg));
        return new ATResponseObj<>(res.value);
    }

    public void modifyMessages(String uuid) {
        market.modifyDelayMessages(uuid);
    }


}



