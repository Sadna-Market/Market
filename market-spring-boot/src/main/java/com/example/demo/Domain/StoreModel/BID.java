package com.example.demo.Domain.StoreModel;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BID {

    private String username;
    private int productID;
    private int quantity;
    private int totalPrice;
    private int lastPrice;
    private StatusEnum status;
    private ConcurrentHashMap<String,Boolean> approves;

    public BID(String username, int productID, int quantity, int totalPrice, ConcurrentHashMap<String, Boolean> approves){
        this.username = username;
        this.productID = productID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.lastPrice = totalPrice;
        this.status = StatusEnum.WaitingForApprovals;
        this.approves = approves;
    }

    public enum StatusEnum {
        WaitingForApprovals,
        BIDRejected,
        CounterBID,
        BIDApproved,
        ProductBought;



    }
    public DResponseObj<Boolean> approve(String ownerEmail) {
        Boolean success = approves.replace(ownerEmail,true);
        return success == null ? new DResponseObj<>(false,ErrorCode.NOPERMISSION) : new DResponseObj<>(true);
    }
    public void reject() {
        status = StatusEnum.BIDRejected;
    }
    public void counter(int newTotalPrice) {
        approves.replaceAll((K,V) -> V = false);
        lastPrice = newTotalPrice;
    }

    public void responseCounter(boolean approve) {
        if(approve){
            status = StatusEnum.WaitingForApprovals;
        }else
            status = StatusEnum.BIDRejected;
    }

    public boolean allApproved(){
        for(Boolean b : approves.values()){
            if(!b)
                return false;
        }
        status = StatusEnum.BIDApproved;
        return true;

    }

    public void ChangeStatusProductBought() {
        status = StatusEnum.ProductBought;
    }



    public String getUsername() {
        return username;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public HashMap<String, Boolean> getApproves() {
        HashMap<String,Boolean> approvesList = new HashMap<>();
        approves.forEach((K,V) -> approvesList.put(K,V));
        return approvesList;
    }
}





