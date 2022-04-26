package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.UserModel.ShoppingBag;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
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

        //requirement II.2.3 & II.2.4.2 (before add product to shoppingBag check quantity
    public boolean isProductExistInStock(int productId, int quantity){
        return inventory.isProductExistInStock(productId ,quantity);
    }

    //requirement II.4.1 (only owners)
    public boolean addNewProduct(ProductType productType, int quantity, double price) {
        return inventory.addNewProduct(productType, quantity, price);
    }

    //requirement II.4.1 (only owners)
    public boolean removeProduct(int productId) {
        return inventory.removeProduct(productId);
    }

}


