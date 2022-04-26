package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.ProductType;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class Inventory {


    /////////////////////////////////////////// Fields //////////////////////////////////////////////////////////////
    private ConcurrentHashMap<Integer, ProductStore> products;  // productTypeID, ProductStore
    private final int storeId;

    static Logger logger = Logger.getLogger(Inventory.class);

    /////////////////////////////////////////// Constructors ////////////////////////////////////////////////////////
    public Inventory(int storeId) {
        products = new ConcurrentHashMap<>();
        this.storeId = storeId;
    }


    /////////////////////////////////////////// Methods /////////////////////////////////////////////////////////////

    public ProductStore getProduct(int productId) {
        return products.get(productId);
    }


    ////////////////////////////////////////// Getters and Setters //////////////////////////////////////////////////

    public ConcurrentHashMap<Integer, ProductStore> getProducts() {
        return products;
    }
    public int getStoreId() {
        return storeId;
    }
}