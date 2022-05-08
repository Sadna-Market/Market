package main.System.Server.Domain.StoreModel;

import main.ErrorCode;
import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.Response.ProductResponse;
import org.apache.log4j.Logger;

import java.util.List;
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

    public DResponseObj<Boolean> haseItem(int itemId){
        return products.containsKey(itemId)? new DResponseObj<>(true,-1): new DResponseObj<>(false,ErrorCode.PRODUCT_DOESNT_EXIST_IN_THE_STORE);
    }


    /////////////////////////////////////////// Methods /////////////////////////////////////////////////////////////

    public DResponseObj<Boolean> isProductExistInStock(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.info("productId:" + productId + " not exist in stock");
            return new DResponseObj<>(false,ErrorCode.PRODUCTNOTEXISTINSTORE);
        }
        long stamp = productStore.getProductLock().readLock();
        try {
            if (productStore.getQuantity().getValue() >= quantity) {
                logger.info("productId:" + productId + " exist in stock");
                return new DResponseObj<>(true);
            }
            logger.info("productId:" + productId + " exist in stock but not enough quantity");
            return new DResponseObj<>(false,ErrorCode.NOT_AVAILABLE_QUANTITY);
        }finally {
            productStore.getProductLock().unlockRead(stamp);
        }
    }

    public DResponseObj<Boolean> addNewProduct(ProductType newProduct, int quantity, double price) {
        DResponseObj<Integer> productStore = newProduct.getProductID();
        if (products.containsKey(productStore.getValue())) {
            logger.warn("try to add productId:" + newProduct.getProductID() + " but inventory contains this product");
            return new DResponseObj<>(false,ErrorCode.PRODUCTALLREADYINSTORE);
        } else {
            ProductStore toAdd = new ProductStore(newProduct, quantity, price);
            products.put(productStore.getValue(), toAdd);
            logger.info("Inventory added productId:" + newProduct.getProductID());
            return newProduct.addStore(storeId);
        }
    }

    public DResponseObj<Boolean> removeProduct(int productId) {
        ProductStore removed = products.remove(productId);
        if (removed == null) {
            logger.warn("try to remove productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(false,ErrorCode.PRODUCTNOTEXISTINSTORE);
        } else {
            DResponseObj<Boolean> success = removed.getProductType().removeStore(storeId);
            if(success.getValue())
                logger.info("Inventory remove productId:" + productId);
            return success;
        }

    }

    public DResponseObj<Integer> setProductQuantityForBuy(int productId, int quantity) {
        ProductStore productStore = products.get(productId);
        if (productStore == null) {
            logger.warn("try to set quantity for buy, productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
        } else {
            long stamp = productStore.getProductLock().writeLock();
            try {
                int quantityInInventory = productStore.getQuantity().getValue();
                 if(quantityInInventory == 0) return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
                if(quantityInInventory>=quantity) {
                    productStore.setQuantity(quantityInInventory - quantity);
                    logger.info("Inventory set quantity of productId:" + productId + "to " + (quantityInInventory-quantity));
                    return new DResponseObj<>(quantity,-1);
                }else {
                    productStore.setQuantity(0);
                    logger.info("Inventory set quantity of productId:" + productId + "to " + 0);
                    return new DResponseObj<>(quantityInInventory,-1);
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
            return new DResponseObj<>(false,ErrorCode.PRODUCTNOTEXISTINSTORE);
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
            return new DResponseObj<>(false,ErrorCode.PRODUCTNOTEXISTINSTORE);
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

    public DResponseObj<Boolean> tellProductStoreIsClose() {
        for (Map.Entry<Integer, ProductStore> entry : products.entrySet()) {
            DResponseObj<Boolean> success = entry.getValue().getProductType().removeStore(storeId);
            if (success.errorOccurred()) {
                logger.warn("not success to remove storeID from productTypeID: " + entry.getValue().getProductType().getProductID());
                return success;
            }
        }
        return new DResponseObj<>(true);
    }

    public DResponseObj<ProductStore> getProductStoreAfterBuy(Integer productID, Integer productQuantity) {
        ProductStore productStore = products.get(productID);
        if (productStore == null)
            return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
        else{
            long stamp = productStore.getProductLock().readLock();
            try {
                ProductType pt = productStore.getProductType();
                DResponseObj<Double> price = productStore.getPrice();
                if(price.errorOccurred())
                    return new DResponseObj<>(productStore,price.getErrorMsg());
                ProductStore copy = new ProductStore(pt, productQuantity, price.getValue());
                return new DResponseObj<>(copy);
            }finally {
                productStore.getProductLock().unlockRead(stamp);
            }
        }

    }

    public DResponseObj<Double> getPrice(Integer productID) {
        ProductStore productStore = products.get(productID);
        if (productStore == null){
            logger.warn("try to get price of productId:" + productID + " but inventory not contains this product");
            return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
        }
        else{
            long stamp = productStore.getProductLock().readLock();
            try{
               return productStore.getPrice();
            }finally {
                productStore.getProductLock().unlockRead(stamp);
            }
        }
    }



    public DResponseObj<Integer> getQuantity(int productId) {
        ProductStore productStore = products.get(productId);
        if (productStore == null){
            logger.warn("try to get quantity of productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
        }
        else{
            long stamp = productStore.getProductLock().readLock();
            try{
                return productStore.getQuantity();
            }finally {
                productStore.getProductLock().unlockRead(stamp);
            }
        }
    }

    public DResponseObj<ProductStore> getProductInfo(int productId) {
        ProductStore productStore = products.get(productId);
        if (productStore == null){
            logger.warn("productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
        }
            return new DResponseObj<>(productStore);
    }

    public DResponseObj<Boolean> addQuantity(int productID, int quantityToAdd) {
        ProductStore productStore = products.get(productID);
        if (productStore == null){
            logger.warn("try to get price of productId:" + productID + " but inventory not contains this product");
            return new DResponseObj<>(null,ErrorCode.PRODUCTNOTEXISTINSTORE);
        }
        else{
            long stamp = productStore.getProductLock().readLock();
            try{
                    return productStore.addQuantity(quantityToAdd);
            }finally {
                productStore.getProductLock().unlockRead(stamp);
            }
        }
    }


    ////////////////////////////////////////// Getters and Setters //////////////////////////////////////////////////

    public DResponseObj<ConcurrentHashMap<Integer,ProductStore>> getProducts() {
        return new DResponseObj<>(products);
    }


}