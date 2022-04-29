package main.System.Server.Domain.StoreModel;

import main.ErrorCode;
import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public DResponseObj<Boolean> isProductExistInStock(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.info("productId:" + productId + " not exist in stock");
            return new DResponseObj<>(false);
        }
        long stamp = productStore.getProductLock().readLock();
        try {
            if (productStore.getQuantity() >= quantity) {
                logger.info("productId:" + productId + " exist in stock");
                return new DResponseObj<>(true);
            }
            logger.info("productId:" + productId + " exist in stock but not enough quantity");
            return new DResponseObj<>(false);
        }finally {
            productStore.getProductLock().unlockRead(stamp);
        }
    }

    public DResponseObj<Boolean> addNewProduct(ProductType newProduct, int quantity, double price) {
        if (products.containsKey(newProduct.getProductID())) {
            logger.warn("try to add productId:" + newProduct.getProductID() + " but inventory contains this product");
            return new DResponseObj<>(false,""+ ErrorCode.PRODUCTALLREADYINSTORE);
        } else {
            ProductStore toAdd = new ProductStore(newProduct, quantity, price);
            products.put(newProduct.getProductID(), toAdd);
            logger.info("Inventory added productId:" + newProduct.getProductID());
            return newProduct.addStore(getStoreId());
        }
    }

    public DResponseObj<Boolean> removeProduct(int productId) {
        ProductStore removed = products.remove(productId);
        if (removed == null) {
            logger.warn("try to remove productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(false,""+ErrorCode.PRODUCTNOTEXISTINSTORE);
        } else {
            DResponseObj<Boolean> success = removed.getProductType().removeStore(getStoreId());
            if(success.getValue())
                logger.info("Inventory remove productId:" + productId);
            return success;
        }

    }

    public Integer setProductQuantityForBuy(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.warn("try to set quantity for buy, productId:" + productId + " but inventory not contains this product");
            return null;
        } else {
            long stamp = productStore.getProductLock().writeLock();
            try {
                int quantityInInventory = productStore.getQuantity();
                if(quantityInInventory>=quantity) {
                    productStore.setQuantity(quantityInInventory - quantity);
                    logger.info("Inventory set quantity of productId:" + productId + "to " + (quantityInInventory-quantity));
                    return quantity;
                }else {
                    productStore.setQuantity(0);
                    logger.info("Inventory set quantity of productId:" + productId + "to " + 0);
                    return quantityInInventory;
                }
            }finally {
                productStore.getProductLock().unlockWrite(stamp);
            }
        }
    }

    public DResponseObj<Boolean> setProductQuantity(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.warn("try to set quantity productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(false,""+ErrorCode.PRODUCTNOTEXISTINSTORE);
        } else {
            long stamp = productStore.getProductLock().writeLock();
            try {
                productStore.setQuantity(quantity);
                logger.info("Inventory set quantity of productId:" + productId + "to " + quantity);
                return new DResponseObj<>(true);
            }finally {
                productStore.getProductLock().unlockWrite(stamp);
            }
        }
    }

    public DResponseObj<Boolean> setProductPrice(int productId, double price) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.warn("try to set price productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(false,""+ErrorCode.PRODUCTNOTEXISTINSTORE);
        }
        else {
            long stamp = productStore.getProductLock().writeLock();
            try{
                productStore.setPrice(price);
                logger.info("Inventory set price of productId:" + productId + "to " + price);
                return new DResponseObj<>(true);
            }finally {
                productStore.getProductLock().unlockWrite(stamp);
            }
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
        if (productStore == null)
            return null;
        else{
            long stamp = productStore.getProductLock().readLock();
            try {
                ProductType pt = productStore.getProductType();
                double price = productStore.getPrice();
                return new ProductStore(pt, productQuantity, price);
            }finally {
                productStore.getProductLock().unlockRead(stamp);
            }
        }

    }

    public Double getPrice(Integer productID) {
        ProductStore productStore = products.get(productID);
        if (productStore == null)
            return null;
        else{
            long stamp = productStore.getProductLock().readLock();
            try{
               return productStore.getPrice();
            }finally {
                productStore.getProductLock().unlockRead(stamp);
            }
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