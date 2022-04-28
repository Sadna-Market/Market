package main.System.Server.Domain.StoreModel;

import main.ErrorCode;
import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ProductType;

import main.System.Server.Domain.UserModel.Response.ATResponseObj;
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

    public ProductStore getProductInStoreInfo(int productId){
        return inventory.getProduct(productId);
    }

    //requirement II.2.1
    public List<ProductStore> GetStoreProducts() {
        return new ArrayList<>(inventory.getProducts().values());
    }

    //requirement II.2.1
    public String getStoreInfo(){
        return "Store{" +
                "storeId=" + storeId +
                ", name='" + name + '\'' +
                ", founder='" + founder + '\'' +
                ", isOpen=" + isOpen +
                ", rate=" + rate +
                ", numOfRated=" + numOfRated +
                '}';
    }

    //requirement II.2.3 & II.2.4.2 (before add product to shoppingBag check quantity
    public ATResponseObj<Boolean> isProductExistInStock(int productId, int quantity){
        return inventory.isProductExistInStock(productId ,quantity);
    }


    //requirement II.4.1 (only owners)
    public ATResponseObj<Boolean> addNewProduct(ProductType productType, int quantity, double price) {
        if(productType == null) {
            logger.warn("productType is null (store - addNewProduct");
            return new ATResponseObj<>(false, ""+ErrorCode.PRODUCTNOTEXIST);
        }
        else
            return inventory.addNewProduct(productType, quantity, price);
    }

    //requirement II.4.1 (only owners)
    public ATResponseObj<Boolean> removeProduct(int productId) {
        return inventory.removeProduct(productId);
    }

    //requirement II.4.1 & II.2.5 (only owners)
    //if change to quantity 0 not delete product (need to find the product price later)
    public ATResponseObj<Boolean> setProductQuantity(int productId, int quantity) {
        return inventory.setProductQuantity(productId, quantity);
    }

    //requirement II.4.1  (only owners)
    public ATResponseObj<Boolean> setProductPrice(int productId, double price) {
        return inventory.setProductPrice(productId, price);
    }

    //requirement II.2.2
    public Double getProductPrice(int productId) {
        return inventory.getPrice(productId);
    }

    //requirement II.4.13 & II.6.4 (only system manager)
    public ATResponseObj<List<History>> getStoreOrderHistory() {
        return new ATResponseObj<>(new ArrayList<>(history.values()));
    }

    //requirement II.4.13 & II.6.4 (only system manager)
    public ATResponseObj<List<History>> getUserHistory(String user) {
        List<History> userHistory = new ArrayList<>();
        long stamp = historyLock.readLock();
        try {
            for (Map.Entry<Integer, History> entry : history.entrySet()) {
                if(entry.getValue().getUser().equals(user))
                    userHistory.add(entry.getValue());
            }
            return new ATResponseObj<>(userHistory);
        }finally {
            historyLock.unlockRead(stamp);
        }

    }

    //niv tests
    public List<Integer> getTIDHistory(){
        List<Integer> TIDHistory = new ArrayList<>();
        long stamp = historyLock.readLock();
        try{
            for (History h: getStoreOrderHistory().value) {
                TIDHistory.add(h.getTID());
            }
            return TIDHistory;
        }finally {
            historyLock.unlockRead(stamp);
        }

    }

    //requirement II.2.5
    public boolean addHistory(int TID, String user, HashMap<Integer,Integer> products, double finalPrice) {
        List<ProductStore> productsBuy = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Integer productID = entry.getKey();
            Integer productQuantity = entry.getValue();
            ProductStore productStore = inventory.getProductStoreAfterBuy(productID, productQuantity);
            if(productStore == null) {
                logger.warn("ProductId: " + productID + " not exist in inventory");
                return false;
            }
            productsBuy.add(productStore);
        }
        history.put(TID, new History(TID, finalPrice, productsBuy, user));
        logger.info("new history added to storeId: "+ storeId + " ,TID: " + TID);
        return true;
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public ConcurrentHashMap<Integer, Integer> checkBuyPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        if(buyPolicy == null)
            return productsInBag;
        else
            return buyPolicy.checkShoppingBag(user,productsInBag);
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public double checkDiscountPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        if(discountPolicy == null)
            return 0.0;
        else
            return discountPolicy.checkShoppingBag(user,productsInBag);
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public double calculateBagPrice(ConcurrentHashMap<Integer, Integer> productsInBag){
        double bagPrice = 0.0;
        for (Map.Entry<Integer, Integer> entry : productsInBag.entrySet()) {
            Integer productID = entry.getKey();
            Integer productQuantity = entry.getValue();
            bagPrice += (inventory.getPrice(productID))*productQuantity;
        }
        return bagPrice;
    }


    //requirement II.4.9  (only owners)
    public ATResponseObj<Boolean> closeStore() {
        boolean success = inventory.tellProductStoreIsClose();
        isOpen = false;
        //sends message to managers.
        logger.info("storeId: "+ storeId + " closed");
        return new ATResponseObj<>(success);
    }

    public boolean newStoreRate(int rate){
        if(rate < 0 | rate > 10){
            logger.warn("store rate is not between 0-10");
            return false;
        }
        numOfRated++;
        if (numOfRated == 1)
            this.rate = rate;
        else
            this.rate = ((this.rate*(numOfRated-1)) + rate) / numOfRated;
        logger.info("storeId: "+storeId+" rate update to: " + rate );
        return true;
    }


/*    //requirement II.4.11  (only owners)
    public List<String> getStoreManagers(){
        List<String> managers = new ArrayList<>();
        for(Permission p: getSafePermission()){
            if(p.getGranteeType() == userTypes.manager)
                managers.add(p.getGrantee().getEmail());
        }
        return managers;
    }

    //requirement II.4.11  (only owners)
    public List<String> getStoreOwners(){
        List<String> owners = new ArrayList<>();
        for(Permission p: getSafePermission()){
            if(p.getGranteeType() == userTypes.owner)
                owners.add(p.getGrantee().getEmail());
        }
        return owners;
    }*/

    //requirement II.4.4 & II.4.6 & II.4.7 (only owners)
    public void addPermission(Permission p){
        safePermission.add(p);
    }

    //requirement II.4.7 (only owners)
    public void removePermission(Permission p){
        safePermission.remove(p);
    }
    public List<Permission> getPermission(){
        return safePermission;
    }


    /////////////////////////////////////////////// Getters and Setters /////////////////////////////////////////////

    public int getStoreId(){
        return storeId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getRate() {
        return rate;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;

    }

    public BuyPolicy getBuyPolicy() {
        return buyPolicy;
    }

    public String getFounder() {
        return founder;
    }

    public List<Permission> getSafePermission() {
        return safePermission;
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

    public ConcurrentHashMap<Integer, History> getHistory() {
        return history;
    }

    public void openStoreAgain() {
        isOpen = true;
    }
}


