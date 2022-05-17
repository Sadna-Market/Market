package Stabs;

import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Response.DResponseObj;


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


    public ProductTypeStab(int productID, String productName, String description,int category) {
        super(productID, productName, description,category);
    }

    public DResponseObj<Integer> getRate() {
            return new DResponseObj<>(1,1);
    }

    public DResponseObj<Boolean> setRate(int r) {
        return new DResponseObj<>(true,1);
    }

    public DResponseObj<Boolean> storeExist(int storeID){
        return new DResponseObj<>(true,1);
    }

    public DResponseObj<Boolean> removeStore(int storeID){
        return new DResponseObj<>(true,1);
    }

    public DResponseObj<List<Integer>> getStores() {
        return new DResponseObj<>(null,1);
    }

    public DResponseObj<Boolean> addStore(int storeID){
        return new DResponseObj<>(true,1);

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public DResponseObj<Integer> getProductID() {
        return new DResponseObj<>(super.getProductID().getValue(),-1);
    }

    public DResponseObj<String> getProductName() {
        return new DResponseObj<>(productName);
    }

    public DResponseObj<String> getDescription() {
        return new DResponseObj<>(description);
    }

    public DResponseObj<Integer> getCategory() {
        return new DResponseObj<>(category);
    }

    public DResponseObj<Boolean> containName(String name){
        return new DResponseObj<>(productName.contains(name));
    }
    public DResponseObj<Boolean> containDesc(String desc){
        return new DResponseObj<>(description.contains(desc));
    }

}
