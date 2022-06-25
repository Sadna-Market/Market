package com.example.demo.Domain.StoreModel;

import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Enums.PermissionType;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.Permission;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Market.permissionType;
import com.example.demo.Domain.Market.userTypes;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceObj.ServiceBID;
import org.apache.log4j.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

public class Store {

    /////////////////////////////////////////////// Fields ////////////////////////////////////////////////////////
    private int storeId;
    private final String name;
    private final Inventory inventory ;
    private String founder;
    private boolean isOpen;
    private int rate; // between 0-10
    private int numOfRated;
    private ConcurrentHashMap<Integer,History> history;
    private DiscountPolicy discountPolicy;
    private BuyPolicy buyPolicy;
    private List<Permission> permission = new ArrayList<>(); // all the permission that have in this store
    private List<Permission> safePermission = Collections.synchronizedList(permission);
    private ConcurrentLinkedDeque<BID> bids;

    private final StampedLock historyLock = new StampedLock();
    static Logger logger=Logger.getLogger(Store.class);
    private static DataServices dataServices;

    /////////////////////////////////////////////// Constructors ///////////////////////////////////////////////////

    //requirement II.3.2
    public Store(int storeId,String name, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, String founder){
        this.storeId = storeId;
        inventory = new Inventory(storeId);
        this.name = name;
        this.founder = founder;
        isOpen = true;
        rate = 5;
        numOfRated = 0;
        history = new ConcurrentHashMap<>();
        this.discountPolicy = discountPolicy; //this version is null
        this.buyPolicy = buyPolicy; //this version is null
        this.bids = new ConcurrentLinkedDeque<>();
    }

    //db
    public Store(String name, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, String founder){
        inventory = new Inventory(storeId);
        this.name = name;
        this.founder = founder;
        isOpen = true;
        rate = 5;
        numOfRated = 0;
        history = new ConcurrentHashMap<>();
        this.discountPolicy = discountPolicy; //this version is null
        this.buyPolicy = buyPolicy; //this version is null
        this.bids = new ConcurrentLinkedDeque<>();
    }

    /////////////////////////////////////////////// Methods ///////////////////////////////////////////////////////


    //requirement II.2.1
    public DResponseObj<ProductStore> getProductInStoreInfo(int productId){
        return inventory.getProductInfo(productId);
    }

    //requirement II.2.1
    public DResponseObj<ConcurrentHashMap<Integer,ProductStore>> GetStoreProducts() {
        DResponseObj<ConcurrentHashMap<Integer,ProductStore>> products = inventory.getProducts();
        if(products.errorOccurred())
            return products;
        return new DResponseObj<>(products.getValue());
    }

    //requirement II.2.1
    public DResponseObj<String> getStoreInfo(){
        return new DResponseObj<>("Store{" +
                "storeId=" + storeId +
                ", name='" + name + '\'' +
                ", founder='" + founder + '\'' +
                ", isOpen=" + isOpen +
                ", rate=" + rate +
                ", numOfRated=" + numOfRated +
                '}');
    }


    //requirement II.2.3 & II.2.4.2 (before add product to shoppingBag check quantity)
    public DResponseObj<Boolean> isProductExistInStock(int productId, int quantity){
        return inventory.isProductExistInStock(productId ,quantity);
    }


    //requirement II.4.1 (only owners)
    public DResponseObj<Boolean> addNewProduct(ProductType productType, int quantity, double price) {
        if(productType == null) {
            logger.warn("productType is null (store - addNewProduct");
            return new DResponseObj<>(false, ErrorCode.PRODUCTNOTEXIST);
        }
        else
            return inventory.addNewProduct(productType, quantity, price);
    }

    //requirement II.4.1 (only owners)
    public DResponseObj<Boolean> removeProduct(int productId) {
        return inventory.removeProduct(productId);
    }

    //requirement II.2.5
    // try to buy(set) the maximum quantity that exist
    public DResponseObj<Integer> setProductQuantityForBuy(int productId, int quantity) {
        return inventory.setProductQuantityForBuy(productId, quantity);
    }


    //requirement II.4.1 & II.2.5 (only owners)
    //if change to quantity 0 not delete product (need to find the product price later)
    public DResponseObj<Boolean> setProductQuantity(int productId, int quantity) {
        return inventory.setProductQuantity(productId, quantity);
    }

    //requirement II.4.1  (only owners)
    public DResponseObj<Boolean> setProductPrice(int productId, double price) {
        return inventory.setProductPrice(productId, price);
    }

    //requirement II.2.2
    public DResponseObj<Double> getProductPrice(int productId) {
        return inventory.getPrice(productId);
    }

    public DResponseObj<Integer> getProductQuantity(int productId) {
        return inventory.getQuantity(productId);
    }


    //requirement II.4.13 & II.6.4 (only system manager)
    public DResponseObj<List<History>> getStoreOrderHistory() {
        return new DResponseObj<>(new ArrayList<>(history.values()));
    }

    //requirement II.4.13 & II.6.4 (only system manager)
    public DResponseObj<List<History>> getUserHistory(String user) {
        List<History> userHistory = new ArrayList<>();
        long stamp = historyLock.readLock();
        try {
            for (Map.Entry<Integer, History> entry : history.entrySet()) {
                if(entry.getValue().getUser().equals(user))
                    userHistory.add(entry.getValue());
            }
            return new DResponseObj<>(userHistory);
        }finally {
            historyLock.unlockRead(stamp);
        }

    }

    //niv tests
    public DResponseObj<List<Integer>> getTIDHistory(){
        List<Integer> TIDHistory = new ArrayList<>();
        long stamp = historyLock.readLock();
        try{
            for (History h: getStoreOrderHistory().value) {
                TIDHistory.add(h.getTID());
            }
            return new DResponseObj<>(TIDHistory);
        }finally {
            historyLock.unlockRead(stamp);
        }
    }

    //requirement II.2.5
    public DResponseObj<History> addHistory(int TID, int SupplyID , String user, ConcurrentHashMap<Integer,Integer> products, double finalPrice) {
        List<ProductStore> productsBuy = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Integer productID = entry.getKey();
            Integer productQuantity = entry.getValue();
            DResponseObj<ProductStore> productStore = inventory.getProductStoreAfterBuy(productID, productQuantity);
            if(productStore.errorOccurred()) {
                logger.warn("ProductId: " + productID + " not exist in inventory");
                return new DResponseObj<>(productStore.getErrorMsg());
            }
            productsBuy.add(productStore.getValue());
        }
        History newHistory = new History(TID, SupplyID, finalPrice, productsBuy, user);
        history.put(TID, newHistory);
        //db
        if(dataServices!= null && dataServices.getHistoryService()!=null) {
            var dataHistory = newHistory.getDataObject();
            var productStoreHistorys = productsBuy.stream().map(p -> p.getDataHistoryObject(dataHistory)).collect(Collectors.toSet());
            dataHistory.setProducts(productStoreHistorys);
            dataHistory.setStore(this.storeId);
            if(!dataServices.getHistoryService().insertHistory(dataHistory)){
                logger.error(String.format("failed to persist history tid %d",dataHistory.getTID()));
            }

        }
        logger.info("new history added to storeId: "+ storeId + " ,TID: " + TID);
        return new DResponseObj<>(newHistory);
    }

    //requirement II.2.5
    //productsInBag <Product,quantity>
    public DResponseObj<Double> checkBuyAndDiscountPolicy(String user, int age, ConcurrentHashMap<Integer, Integer> productsInBag){
        ConcurrentHashMap<ProductStore, Integer> productsInBag2 = new ConcurrentHashMap<>();
        for (Map.Entry<Integer, Integer> e : productsInBag.entrySet())
            productsInBag2.put(inventory.getProductInfo(e.getKey()).getValue(), e.getValue());
        if(buyPolicy == null)
            return checkDiscountPolicy(user,age,productsInBag2);
        else {
            DResponseObj<Boolean> passBuyPolicy = buyPolicy.checkBuyPolicyShoppingBag(user, age, productsInBag2);
            if(!passBuyPolicy.getValue()) return new DResponseObj<>(passBuyPolicy.getErrorMsg());
            return checkDiscountPolicy(user,age,productsInBag2);
        }
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    private DResponseObj<Double> checkDiscountPolicy(String user, int age, ConcurrentHashMap<ProductStore, Integer> productsInBag){
        if(discountPolicy == null)
            return new DResponseObj<>(0.0);
        else
            return discountPolicy.checkDiscountPolicyShoppingBag(user,age,productsInBag);
    }



    //requirement II.2.5
    //productsInBag <productID,quantity>
    public DResponseObj<Double> calculateBagPrice(ConcurrentHashMap<Integer, Integer> productsInBag){
        double bagPrice = 0.0;
        for (Map.Entry<Integer, Integer> entry : productsInBag.entrySet()) {
            Integer productID = entry.getKey();
            Integer productQuantity = entry.getValue();
            DResponseObj<Double> price = (inventory.getPrice(productID));
            if(price.errorOccurred())
                return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
            else
                bagPrice += (price.getValue()*productQuantity);
        }
        return new DResponseObj<>(bagPrice);
    }


    //requirement II.2.5
    //productsInBag <productID,quantity>
    public DResponseObj<Boolean> rollbackProductQuantity(ConcurrentHashMap<Integer,Integer> productsInBag){
        for (Map.Entry<Integer, Integer> entry : productsInBag.entrySet()) {
            int productID = entry.getKey();
            int quantityToAdd = entry.getValue();
            DResponseObj<Boolean> add = inventory.addQuantity(productID,quantityToAdd);
            if(add.errorOccurred()) return add;
        }
        return new DResponseObj<>(true);
    }

    //requirement II.4.9  (only owners)
    public DResponseObj<Boolean> closeStore() {
        DResponseObj<Boolean> success = inventory.tellProductStoreIsClose();
        if (success.errorOccurred())
            return success;
        else {
            isOpen = false;
            //sends message to managers.
            logger.info("storeId: " + storeId + " closed");
            return new DResponseObj<>(success.getValue());
        }
    }

    public DResponseObj<Boolean> newStoreRate(int rate){
        if(rate < 0 | rate > 10){
            logger.warn("store rate is not between 0-10");
            return new DResponseObj<>(false,ErrorCode.NOTVALIDINPUT);
        }
        numOfRated++;
        if (numOfRated == 1)
            this.rate = rate;
        else
            this.rate = ((this.rate*(numOfRated-1)) + rate) / numOfRated;
        //db
        if (dataServices != null && dataServices.getStoreService() != null) {
            if (!dataServices.getStoreService().updateStoreRate(storeId, rate, numOfRated)) {
                logger.error(String.format("failed to updated rate %d of store %s in db", rate, name));
            }
        }
        logger.info("storeId: "+storeId+" rate update to: " + rate );
        return new DResponseObj<>(true);
    }


    //requirement II.4.11  (only owners)
    public DResponseObj<HashMap<String,List<String>>> getStoreRoles(){

        List<String> managers = new ArrayList<>();
        List<String> owners = new ArrayList<>();
        List<String> founder = new ArrayList<>();
        founder.add(this.founder);
        for(Permission p: getSafePermission().getValue()){
            DResponseObj<userTypes> type = p.getGranteeType();
            if(type.errorOccurred())
                return new DResponseObj<>(null,type.getErrorMsg());
            if(type.getValue().equals(userTypes.manager))
                managers.add(p.getGrantee().getValue().getEmail().getValue());
            else if(type.getValue().equals(userTypes.owner))
                owners.add(p.getGrantee().getValue().getEmail().getValue());
        }
        HashMap<String,List<String>> roles = new HashMap<>();
        roles.put("manager",managers); roles.put("owner",owners); roles.put("founder",founder);
        return new DResponseObj<>(roles);
    }


    //requirement II.4.2
    public DResponseObj<Boolean> addNewBuyRule(BuyRule buyRule) {
        if(buyPolicy == null)
            buyPolicy = new BuyPolicy();
        return buyPolicy.addNewBuyRule(buyRule,storeId);
    }

    //requirement II.4.2
    public DResponseObj<Boolean> removeBuyRule(int buyRuleID) {
        if(buyPolicy == null)
            buyPolicy = new BuyPolicy();
        return buyPolicy.removeBuyRule(buyRuleID);
    }

    //requirement II.4.2
    public DResponseObj<Boolean> addNewDiscountRule(DiscountRule discountRule) {
        if(discountPolicy == null)
            discountPolicy = new DiscountPolicy();
        return discountPolicy.addNewDiscountRule(discountRule,storeId);
    }

    //requirement II.4.2
    public DResponseObj<Boolean> removeDiscountRule(int discountRuleID) {
        if(discountPolicy == null)
            discountPolicy = new DiscountPolicy();
        return discountPolicy.removeDiscountRule(discountRuleID);
    }

    //requirement II.4.2
    public DResponseObj<Boolean> combineANDORDiscountRules(String operator, List<Integer> rules, int category, int discount) {
        if(discountPolicy == null) return new DResponseObj<>(false,ErrorCode.INVALID_ARGS_FOR_RULE);
        return discountPolicy.combineANDORDiscountRules(operator,rules,category,discount,storeId);
    }

    //requirement II.4.2
    public DResponseObj<Boolean> combineXORDiscountRules(List<Integer> rules, String decision) {
        if(discountPolicy == null) return new DResponseObj<>(false,ErrorCode.INVALID_ARGS_FOR_RULE);
        return discountPolicy.combineXORDiscountRules(rules,decision,storeId);
    }


    //requirement II.4.4 & II.4.6 & II.4.7 (only owners)
    public void addPermission(Permission p){
        safePermission.add(p);
        if(p.getgranteePermissionTypes().value.contains(permissionType.permissionEnum.ManageBID))
            bids.forEach(b -> b.addManagerToList(p.getGrantee().value.getEmail().value));
    }

    //requirement II.4.7 (only owners)
    public void removePermission(Permission p){
        safePermission.remove(p);
        if(p.getgranteePermissionTypes().value.contains(permissionType.permissionEnum.ManageBID))
            bids.forEach(b -> b.removeManagerFromList(p.getGrantee().value.getEmail().value));
    }
    public DResponseObj<List<Permission>> getPermission(){
        return new DResponseObj<>(safePermission);
    }


    private List<String> getOwners(){
        List<String> owners = new ArrayList<>();
        owners.add(founder);
        getSafePermission().value.forEach(p -> {
            DResponseObj<userTypes> type = p.getGranteeType();
            if(!type.errorOccurred() && type.getValue().equals(userTypes.owner))
                owners.add(p.getGrantee().getValue().getEmail().getValue());});
        return owners;
    }

    public DResponseObj<Boolean> createBID(String email, int productID, int quantity, int totalPrice) {
        DResponseObj<Boolean> quantityEx = inventory.isProductExistInStock(productID,quantity);
        if(quantityEx.errorOccurred()) return new DResponseObj<>(false,quantityEx.errorMsg);
        BID exist = findBID(email,productID);
        if(exist != null && ((exist.getStatus().equals(BID.StatusEnum.WaitingForApprovals)) || (exist.getStatus().equals(BID.StatusEnum.CounterBID)))) return new DResponseObj<>(false,ErrorCode.BIDALLREADYEXISTS);
        ConcurrentHashMap<String,Boolean> approves =  createApprovesHashMap();
        DResponseObj<ProductStore> productStore = inventory.getProductInfo(productID);
        if(productStore.errorOccurred()) return new DResponseObj<>(false,ErrorCode.PRODUCTNOTEXISTINSTORE);
        String productName = productStore.value.getProductType().getProductName().value;
        BID b = new BID(email,productID,productName,quantity,totalPrice,approves);
        if(exist != null) bids.remove(exist);
        bids.add(b);
        return new DResponseObj<>(true);
    }

    private ConcurrentHashMap<String,Boolean> createApprovesHashMap(){
        ConcurrentHashMap<String,Boolean> approves = new ConcurrentHashMap<>();
        getOwners().forEach(email -> {
            if(!approves.containsKey(email))
                approves.put(email,false);
        });
        return approves;
    }

    public DResponseObj<Boolean> removeBID(String email, int productID) {
        for(BID b : bids){
            if(b.getUsername().equals(email) && b.getProductID()==productID){
                bids.remove(b);
                return new DResponseObj<>(true);
            }
        }
        return new DResponseObj<>(false, ErrorCode.BIDNOTEXISTS);
    }


    public boolean allApprovedBID(String userEmail, int productID) {
        BID b = findBID(userEmail,productID);
        if (b==null) return false;
        return b.allApproved();
    }

    public DResponseObj<HashMap<String, List<Integer>>> checkStoreBIDAllApproved() {
        HashMap<String, List<Integer>> allApproved = new HashMap<>();
        bids.forEach(b -> {
            if(b.needToChangeToApproved())
                if(allApproved.containsKey(b.getUsername()))
                    allApproved.get(b.getUsername()).add(b.getProductID());
                else{
                    List<Integer> productsID = new ArrayList<>();
                    productsID.add(b.getProductID());
                    allApproved.put(b.getUsername(),productsID);
                }
        });
        return new DResponseObj<>(allApproved);
    }

    public DResponseObj<Boolean> approveBID(String ownerEmail, String userEmail, int productID) {
        BID b = findBID(userEmail,productID);
        if (b==null) return new DResponseObj<>(false,ErrorCode.BIDNOTEXISTS);
        if (b.getStatus() != BID.StatusEnum.WaitingForApprovals) return new DResponseObj<>(false,ErrorCode.STATUSISNOTWAITINGAPPROVES);
        return b.approve(ownerEmail);
    }

    private BID findBID(String userEmail, int productID){
        for(BID b : bids){
            if(b.getUsername().equals(userEmail) && b.getProductID() == productID){
                return b;
            }
        }
        return null;
    }

    public DResponseObj<Boolean> rejectBID(String ownerEmail, String userEmail, int productID) {
        BID b = findBID(userEmail,productID);
        if (b==null) return new DResponseObj<>(false,ErrorCode.BIDNOTEXISTS);
        if (b.getStatus() != BID.StatusEnum.WaitingForApprovals) return new DResponseObj<>(false,ErrorCode.STATUSISNOTWAITINGAPPROVES);
        return b.reject(ownerEmail);
    }

    public DResponseObj<Boolean> counterBID(String ownerEmail, String userEmail, int productID, int newTotalPrice) {
        BID b = findBID(userEmail,productID);
        if (b==null) return new DResponseObj<>(false,ErrorCode.BIDNOTEXISTS);
        if (b.getStatus() != BID.StatusEnum.WaitingForApprovals) return new DResponseObj<>(false,ErrorCode.STATUSISNOTWAITINGAPPROVES);
        return b.counter(ownerEmail,newTotalPrice);
    }

    public DResponseObj<Boolean> responseCounterBID(String userEmail, int productID, boolean approve) {
        BID b = findBID(userEmail,productID);
        if (b==null) return new DResponseObj<>(false,ErrorCode.BIDNOTEXISTS);
        if (b.getStatus() != BID.StatusEnum.CounterBID) return new DResponseObj<>(false,ErrorCode.STATUSISNOTCOUNTERBID);
        b.responseCounter(approve);
        return new DResponseObj<>(true);
    }

    public DResponseObj<String> getBIDStatus(String userEmail, int productID) {
        BID b = findBID(userEmail,productID);
        if (b==null) return new DResponseObj<>(null,ErrorCode.BIDNOTEXISTS);
        return new DResponseObj<>(b.getStatusString());
    }

    public DResponseObj<List<BID>> getMyBIDs(String userEmail) {
        List<BID> myBIDs = new ArrayList<>();
        bids.forEach(b -> {
            if(b.getUsername().equals(userEmail))
                myBIDs.add(b);
        });
        return new DResponseObj<>(myBIDs);
    }

    public DResponseObj<HashMap<Integer, List<BID>>> getAllOffersBIDs() {
        HashMap<Integer,List<BID>> BIDs = new HashMap<>();
        bids.forEach(b -> {
            if(!BIDs.containsKey(b.getProductID())){
                List<BID> l = new ArrayList<>();
                l.add(b);
                BIDs.put(b.getProductID(),l);
            }else{
                BIDs.get(b.getProductID()).add(b);
            }
        });
        return new DResponseObj<>(BIDs);
    }

    public DResponseObj<BID> canBuyBID(String userEmail, int productID) {
        BID b = findBID(userEmail,productID);
        if (b==null) return new DResponseObj<>(null,ErrorCode.BIDNOTEXISTS);
        if (!b.getStatus().equals(BID.StatusEnum.BIDApproved)) return new DResponseObj<>(null,ErrorCode.STATUSISNOTBIDAPPROVED);
        DResponseObj<Boolean> existsInv = isProductExistInStock(productID,b.getQuantity());
        if (!existsInv.value) return new DResponseObj<>(null,existsInv.getErrorMsg());
        return new DResponseObj<>(b);
    }

    public DResponseObj<Boolean> reopenStore() {
        DResponseObj<Boolean> success = inventory.tellProductStoreIsReopen();
        if (success.errorOccurred())
            return success;
        else {
            isOpen = true;
            logger.info("storeId: " + storeId + " reopen");
            return new DResponseObj<>(success.getValue());
        }
    }

    /////////////////////////////////////////////// Getters and Setters /////////////////////////////////////////////

    public DResponseObj<Inventory> getInventory(){
        return new DResponseObj<>(inventory);
    }

    public DResponseObj<Integer> getStoreId(){
        return new DResponseObj<>(storeId,-1);
    }

    public DResponseObj<String> getName() {
        return new DResponseObj<>(name);
    }

    public DResponseObj<Boolean> isOpen() {
        return new DResponseObj<>(isOpen);
    }

    public DResponseObj<Integer> getRate() {
        return new DResponseObj<>(rate,-1);
    }
    public DResponseObj<String> getFounder() {
        return new DResponseObj<>(founder);
    }

    public ConcurrentLinkedDeque<BID> getBids() {
        return bids;
    }

    public DResponseObj<List<Permission>> getSafePermission() {
        return new DResponseObj<>(safePermission);
    }
    public int getBuyRulesSize(){
        return buyPolicy.rulesSize();
    }

    public int getDiscountRulesSize(){
        return discountPolicy.rulesSize();
    }
    public void setHistory(ConcurrentHashMap<Integer,History> history) {
        this.history = history;
    }

    //requirement II.4.2  (only owners)

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    //requirement II.4.2  (only owners)

    public void setBuyPolicy(BuyPolicy buyPolicy) {
        this.buyPolicy = buyPolicy;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public DResponseObj<ConcurrentHashMap<Integer, History>> getHistory() {
        return new DResponseObj<>(history);
    }


    public DResponseObj<List<BuyRule>> getBuyPolicy() {
        return buyPolicy == null ? new DResponseObj<>(new ArrayList<>()) :
                new DResponseObj<>(new ArrayList<>(buyPolicy.getRules().values()));
    }

    public DResponseObj<List<DiscountRule>> getDiscountPolicy() {
        return discountPolicy == null ? new DResponseObj<>(new ArrayList<>()) :
                new DResponseObj<>(new ArrayList<>(discountPolicy.getRules().values()));
    }


    public DataStore getDataObject() {
        DataStore dataStore = new DataStore();
        dataStore.setStoreId(this.storeId);
        dataStore.setName(this.name);
        dataStore.setRate(this.rate);
        dataStore.setOpen(this.isOpen);
        dataStore.setNumOfRated(this.numOfRated);
        dataStore.setFounder(this.founder);
        return dataStore;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public static void setDataServices(DataServices dataServices){
        Store.dataServices = dataServices;
    }
}


