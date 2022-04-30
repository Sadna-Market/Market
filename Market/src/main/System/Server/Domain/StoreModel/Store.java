package main.System.Server.Domain.StoreModel;

import main.ErrorCode;
import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ProductType;

import main.System.Server.Domain.Market.userTypes;
import main.System.Server.Domain.Response.DResponse;
import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

public class Store {

    /////////////////////////////////////////////// Fields ////////////////////////////////////////////////////////
    private final int storeId;
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


    private static AtomicInteger nextStoreId = new AtomicInteger();
    private final StampedLock historyLock = new StampedLock();
    static Logger logger=Logger.getLogger(Store.class);

    /////////////////////////////////////////////// Constructors ///////////////////////////////////////////////////

    //requirement II.3.2
    public Store(String name, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, String founder){
        int storeId = nextStoreId.incrementAndGet();
        inventory = new Inventory(storeId);
        this.storeId = storeId;
        this.name = name;
        this.founder = founder;
        isOpen = true;
        rate = 5;
        numOfRated = 0;
        history = new ConcurrentHashMap<>();
        this.discountPolicy = discountPolicy; //this version is null
        this.buyPolicy = buyPolicy; //this version is null

    }

    /////////////////////////////////////////////// Methods ///////////////////////////////////////////////////////


    //requirement II.2.1
    public DResponseObj<String> getProductInStoreInfo(int productId){
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
    public DResponseObj<Boolean> addHistory(int TID, String user, ConcurrentHashMap<Integer,Integer> products, double finalPrice) {
        List<ProductStore> productsBuy = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Integer productID = entry.getKey();
            Integer productQuantity = entry.getValue();
            DResponseObj<ProductStore> productStore = inventory.getProductStoreAfterBuy(productID, productQuantity);
            if(productStore.errorOccurred()) {
                logger.warn("ProductId: " + productID + " not exist in inventory");
                return new DResponseObj<>(false,productStore.getErrorMsg());
            }
            productsBuy.add(productStore.getValue());
        }
        history.put(TID, new History(TID, finalPrice, productsBuy, user));
        logger.info("new history added to storeId: "+ storeId + " ,TID: " + TID);
        return new DResponseObj<>(true);
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public DResponseObj<ConcurrentHashMap<Integer, Integer>> checkBuyPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        if(buyPolicy == null)
            return new DResponseObj<>(productsInBag);
        else
            return buyPolicy.checkShoppingBag(user,productsInBag);
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public DResponseObj<Double> checkDiscountPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        if(discountPolicy == null)
            return new DResponseObj<>(0.0);
        else
            return discountPolicy.checkShoppingBag(user,productsInBag);
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


    //requirement II.4.4 & II.4.6 & II.4.7 (only owners)
    public void addPermission(Permission p){
        safePermission.add(p);
    }

    //requirement II.4.7 (only owners)
    public void removePermission(Permission p){
        safePermission.remove(p);
    }
    public DResponseObj<List<Permission>> getPermission(){
        return new DResponseObj<>(safePermission);
    }


    /////////////////////////////////////////////// Getters and Setters /////////////////////////////////////////////

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

    public DResponseObj<List<Permission>> getSafePermission() {
        return new DResponseObj<>(safePermission);
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

    public void openStoreAgain() {
        isOpen = true;
    }
}


