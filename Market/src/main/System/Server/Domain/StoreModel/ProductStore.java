package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.ProductType;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //TODO:

    @Override
    public String toString() {
        return "ProductStore{" +
                "productLock=" + productLock +
                ", productType=" + productType +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
