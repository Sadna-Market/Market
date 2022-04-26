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

    public boolean isProductExistInStock(int productId, int quantity){
        ProductStore productStore = products.get(productId) ;
        return productStore != null && productStore.getQuantity() >= quantity;
    }

    public boolean addNewProduct(ProductType newProduct, int quantity, double price) {
        if(products.containsKey(newProduct.getProductID()))
            return false;
        else {
            ProductStore toAdd = new ProductStore(newProduct, quantity, price);
            products.put(newProduct.getProductID(), toAdd);
            return newProduct.addStore(getStoreId());
        }
    }

    public boolean removeProduct(int productId) {
        ProductStore removed = products.remove(productId);
        if(removed == null)
            return false;
        else{
            removed.getProductType().removeStore(getStoreId());
            return true;
        }

    }

    public boolean setProductQuantity(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if(productStore == null)
            return false;
        else{
            productStore.setQuantity(quantity);
            return true;
        }
    }
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