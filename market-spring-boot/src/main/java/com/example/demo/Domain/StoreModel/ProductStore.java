package com.example.demo.Domain.StoreModel;

//import com.example.demo.DataAccess.Entity.DataInventory;
import com.example.demo.DataAccess.Entity.DataHistory;
import com.example.demo.DataAccess.Entity.DataProductStore;
import com.example.demo.DataAccess.Entity.DataProductStoreHistory;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;
import java.util.concurrent.locks.StampedLock;

public class ProductStore {
    private final StampedLock productLock = new StampedLock();
    private ProductType productType;
    private int quantity;
    private double price;

    static Logger logger=Logger.getLogger(ProductStore.class);

    public ProductStore(ProductType productType, int quantity,double price){
        this.productType = productType;
        this.quantity = quantity;
        this.price = price;
    }

    public StampedLock getProductLock() {
        return productLock;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public DResponseObj<Integer> getQuantity() {
        return new DResponseObj<>(quantity,-1);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DResponseObj<Boolean> addQuantity(int toAdd){
        this.quantity += toAdd;
        return new DResponseObj<>(true);

    }

    public DResponseObj<Double> getPrice() {
        return new DResponseObj<>(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "ProductStore{" +
                "product ID=" + productType.getProductID() +
                ", product name=" + productType.getProductName() +
                ", quantity=" + quantity +
                ", price=" + price +
                ", product category=" + productType.getCategory() +
                ", product description=" + productType.getDescription() +
                ", product rate=" + productType.getRate() +
                '}';
    }

    public DataProductStore getDataObject(){
        DataProductStore dataProductStore = new DataProductStore();
        dataProductStore.setProductType(this.productType.getDataObject());
        dataProductStore.setPrice(this.price);
        dataProductStore.setQuantity(this.quantity);
        return dataProductStore;
    }

    public DataProductStoreHistory getDataHistoryObject(DataHistory history){
        DataProductStoreHistory h = new DataProductStoreHistory();
        h.setProductType(this.productType.getDataObject());
        h.setHistory(history);
        h.setQuantity(this.quantity);
        h.setPrice(this.price);
        return h;
    }
}
