package com.example.demo.service;


public class ServiceStore {
     int storeId;
     String name;
     String founder;
     boolean isOpen;
     int rate; // between 0-10
    //private int numOfRated;



    public String toString() {
        return "storeId : "+ storeId +"\n" + "" +
                "store name : "+ name +"\n" + "" +
                "founder : "+ founder +"\n" + "" +
                "isOpen : "+ isOpen +"\n" + "" +
                "rate : "+ rate +"\n" + "" +
                "";
    }


}
