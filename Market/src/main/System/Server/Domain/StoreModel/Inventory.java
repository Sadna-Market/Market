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

    public boolean isProductExistInStock(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.info("productId:" + productId + " not exist in stock");
            return false;
        } else if (productStore.getQuantity() >= quantity) {
            logger.info("productId:" + productId + " exist in stock");
            return true;
        }
        logger.info("productId:" + productId + " exist in stock but not enough quantity");
        return false;
    }

    public boolean addNewProduct(ProductType newProduct, int quantity, double price) {
        if (products.containsKey(newProduct.getProductID())) {
            logger.warn("try to add productId:" + newProduct.getProductID() + " but inventory contains this product");
            return false;
        } else {
            ProductStore toAdd = new ProductStore(newProduct, quantity, price);
            products.put(newProduct.getProductID(), toAdd);
            logger.info("Inventory added productId:" + newProduct.getProductID());
            return newProduct.addStore(getStoreId());
        }
    }

    public boolean removeProduct(int productId) {
        ProductStore removed = products.remove(productId);
        if (removed == null) {
            logger.warn("try to remove productId:" + productId + " but inventory not contains this product");
            return false;
        } else {
            removed.getProductType().removeStore(getStoreId());
            logger.info("Inventory remove productId:" + productId);
            return true;
        }

    }

    public boolean setProductQuantity(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.warn("try to set quantity productId:" + productId + " but inventory not contains this product");
            return false;
        } else {
            productStore.setQuantity(quantity);
            logger.info("Inventory set quantity of productId:" + productId + "to " + quantity);
            return true;
        }
    }

    public boolean setProductPrice(int productId, double price) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.warn("try to set price productId:" + productId + " but inventory not contains this product");
            return false;
        }
        else {
            productStore.setPrice(price);
            logger.info("Inventory set price of productId:" + productId + "to " + price);
            return true;
        }
    }

    public boolean tellProductStoreIsClose() {
        for (Map.Entry<Integer, ProductStore> entry : getProducts().entrySet()) {
            boolean success = entry.getValue().getProductType().removeStore(getStoreId());
            if (!success) {
                logger.warn("not success to remove storeID from productTypeID: " + entry.getValue().getProductType().getProductID());
                return false;
            }
        }
        return true;
    }

    public ProductStore getProductStoreAfterBuy(Integer productID, Integer productQuantity) {
        ProductStore productStore = getProducts().get(productID);
        return (productStore == null) ? null : new ProductStore(productStore.getProductType(), productQuantity, productStore.getPrice());

    }

    public StampedLock getProductLock(int productID) {
        ProductStore productStore = products.get(productID);
        return productStore == null ? null : productStore.getProductLock();
    }

    public Double getPrice(Integer productID) {
        ProductStore productStore = products.get(productID);
        return productStore == null ? null : productStore.getPrice();
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