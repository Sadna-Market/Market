package main.System.Server.Domain;

import main.System.Server.Domain.StoreModel.Store;

public class ServiceStore {
    private final int storeId;
    private final String name;
    private String founder;
    private boolean isOpen;
    private int rate; // between 0-10
    //private int numOfRated;


    public ServiceStore(Store store){
        this.storeId=store.getStoreId();
        this.name=store.getName();
        this.founder=store.getFounder();
        this.isOpen=store.isOpen();
        this.rate=store.getRate();
        //this.numOfRated=store.
    }
    public String toString() {
        return "storeId : "+ storeId +"\n" + "" +
                "store name : "+ name +"\n" + "" +
                "founder : "+ founder +"\n" + "" +
                "isOpen : "+ isOpen +"\n" + "" +
                "rate : "+ rate +"\n" + "" +
                "";
    }
}
