package main.Service;

import main.System.Server.Domain.StoreModel.Store;

public class ServiceStore {
    private final int storeId;
    private final String name;
    private String founder;
    private boolean isOpen;
    private int rate; // between 0-10
    //private int numOfRated;


    public ServiceStore(Store store){
        this.storeId=store.getStoreId().getValue();
        this.name=store.getName().getValue();
        this.founder=store.getFounder().getValue();
        this.isOpen=store.isOpen().getValue();
        this.rate=store.getRate().getValue();
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
