package Stabs;

import main.System.Server.Domain.Market.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

public class ProductTypeStab extends ProductType {

    int productID;
    int rate=0, counter_rates=0;
    String productName;
    String description;
    int category;
    List<Integer> stores=new ArrayList<>();

    public ProductTypeStab(int productID, String productName,String description) {
        super(productID, productName, description);
    }


    public int getRate() {
        return 1;
    }

    public boolean setRate(int r) {
        return true;
    }

    public boolean storeExist(int storeID){
       return true;
    }

    public boolean removeStore(int storeID){
        return true;
    }

    public boolean addStore(int storeID){
        return true;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public boolean containName(String name){
        return productName.contains(name);
    }
    public boolean containDesc(String desc){
        return description.contains(desc);
    }


}
