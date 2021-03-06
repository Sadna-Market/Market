package com.example.demo.Domain.StoreModel;

//import com.example.demo.DataAccess.Entity.DataInventory;
import com.example.demo.DataAccess.Entity.DataProductStore;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.UserModel.ShoppingBag;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {
    private static DataServices dataServices;


    /////////////////////////////////////////// Fields //////////////////////////////////////////////////////////////
    private ConcurrentHashMap<Integer, ProductStore> products;  // productTypeID, ProductStore
    private int storeId;

    static Logger logger = Logger.getLogger(Inventory.class);

    /////////////////////////////////////////// Constructors ////////////////////////////////////////////////////////
    public Inventory(int storeId) {
        products = new ConcurrentHashMap<>();
        this.storeId = storeId;
    }
    public Inventory() {
        products = new ConcurrentHashMap<>();
    }


    //for upload store from db
    public Inventory(int storeId,ConcurrentHashMap<Integer, ProductStore> products){
        this.storeId = storeId;
        this.products = products;
    }

    /////////////////////////////////////////// Methods /////////////////////////////////////////////////////////////

    public DResponseObj<Boolean> haseItem(int itemId){
        return products.containsKey(itemId)? new DResponseObj<>(true,-1): new DResponseObj<>(false, ErrorCode.PRODUCT_DOESNT_EXIST_IN_THE_STORE);
    }


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
            //db
            if(dataServices!=null && dataServices.getProductStoreService()!=null) {
                var dataProductStore = toAdd.getDataObject();
                if(!dataServices.getProductStoreService().insertProductStore(dataProductStore, storeId)){
                    logger.error(String.format("failed to add product store to store %d",storeId));
                }
            }
            products.put(productStore.getValue(), toAdd);
            logger.info("Inventory added productId:" + newProduct.getProductID().value);
            return newProduct.addStore(storeId);
        }
    }

    public void addNewProductStore(ProductType productType,ProductStore productStore){
        products.put(productType.getProductID().value,productStore);
    }

    public DResponseObj<Boolean> removeProduct(int productId) {
        ProductStore removed = products.remove(productId);
        if (removed == null) {
            logger.warn("try to remove productId:" + productId + " but inventory not contains this product");
            return new DResponseObj<>(false,ErrorCode.PRODUCTNOTEXISTINSTORE);
        } else {
            //db
            if(dataServices!=null && dataServices.getProductStoreService()!=null) {
                if(!dataServices.getProductStoreService().deleteProductStore(productId, storeId)){
                    logger.error(String.format("failed to add product store to store %d",storeId));
                }
            }
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
                    //db
                    updatedProductStoreOrPrice(productId, quantityInInventory - quantity, productStore);
                    return new DResponseObj<>(quantity,-1);
                }else {
                    productStore.setQuantity(0);
                    logger.info("Inventory set quantity of productId:" + productId + "to " + 0);
                    updatedProductStoreOrPrice(productId, 0, productStore);
                    return new DResponseObj<>(quantityInInventory,-1);
                }
            }finally {
                productStore.getProductLock().unlockWrite(stamp);
            }
        }
    }

    private void updatedProductStoreOrPrice(int productId, int quantity, ProductStore productStore) {
        if (dataServices != null && dataServices.getProductStoreService() != null) {
            if (!dataServices.getProductStoreService().updateProductStore(productId, storeId, productStore.getPrice().value, quantity)) {
                logger.error(String.format("failed to update price %d of product %d",
                        quantity,
                        productId));
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
                //db
                updatedProductStoreOrPrice(productId, quantity, productStore);
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
                //db
                updatedProductStoreOrPrice(productId,-1,productStore);
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

    public DResponseObj<Boolean> tellProductStoreIsReopen() {
        for (Map.Entry<Integer, ProductStore> entry : products.entrySet()) {
            DResponseObj<Boolean> success = entry.getValue().getProductType().addStore(storeId);
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

    public DResponseObj<List<ProductStore>> getAllProducts(){
        List<ProductStore> lst = new ArrayList<>();
        products.forEach((pid, productStore) -> {
            lst.add(productStore);
        });
        return new DResponseObj<>(lst,-1);
    }

    public void setProducts(ConcurrentHashMap<Integer, ProductStore> products) {
        this.products = products;
    }

    public DResponseObj<ConcurrentHashMap<Integer,ProductStore>> getProducts() {
        return new DResponseObj<>(products);
    }
    public static void setDataServices(DataServices dataServices) {
        Inventory.dataServices = dataServices;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

//    public DataInventory getDataObject() {
//        DataInventory dataInventory = new DataInventory();
//        dataInventory.setInventoryId(this.storeId);
//        Set<DataProductStore> productStores = new HashSet<>();
//        this.products.forEach((integer, productStore) -> {
//            productStores.add(productStore.getDataObject());
//        });
//        dataInventory.setProductStores(productStores);
//        return dataInventory;
//    }
}