package com.example.demo.Service.ServiceObj;

import com.example.demo.Domain.StoreModel.BID;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceBID {

    public String username;
    public int productID;
    public String productName;
    public int quantity;
    public int originPrice;
    public int counterPrice;
    public String status;
    public ConcurrentHashMap<String,Boolean> approves;

    public ServiceBID(BID bid){
        this.username = bid.getUsername();
        this.productID = bid.getProductID();
        this.productName = bid.getProductName();
        this.quantity = bid.getQuantity();
        this.originPrice = bid.getTotalPrice();
        this.counterPrice = bid.getLastPrice();
        this.approves = bid.getApproves();
        switch (bid.getStatusString()) {
            case "WaitingForApprovals" -> status = "Waiting";
            case "BIDRejected" -> status = "Rejected";
            case "CounterBID" -> status = "Counter";
            case "BIDApproved" -> status = "Approved";
            case "ProductBought" -> status = "Bought";
        }
    }
}
