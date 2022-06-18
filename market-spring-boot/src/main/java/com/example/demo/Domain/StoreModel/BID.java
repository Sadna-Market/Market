package com.example.demo.Domain.StoreModel;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BID {

    private String username;
    private int productID;
    private String productName;
    private int quantity;
    private int totalPrice;
    private int lastPrice;
    private StatusEnum status;
    private ConcurrentHashMap<String,Boolean> approves;

    public BID(String username, int productID, String productName, int quantity, int totalPrice, ConcurrentHashMap<String, Boolean> approves){
        this.username = username;
        this.productID = productID;
        this.productName = productName;
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
        return success == null ? new DResponseObj<>(false,ErrorCode.NOPERMISSION) :
                success ? new DResponseObj<>(false,ErrorCode.ALLREADYAPPROVEDTHISBID):
                new DResponseObj<>(true);
    }

    public void reject() {
        status = StatusEnum.BIDRejected;
    }
    public void counter(int newTotalPrice) {
        approves.replaceAll((K,V) -> V = false);
        lastPrice = newTotalPrice;
        status = StatusEnum.CounterBID;
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


    public void addManagerToList(String managerEmail) {
        if(!approves.containsKey(managerEmail))
            approves.put(managerEmail,false);
    }

    public void removeManagerFromList(String managerEmail) {
        approves.remove(managerEmail);
    }


    public String getUsername() {
        return username;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getLastPrice() {
        return lastPrice;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public String getStatusString() {
        return status.toString();
    }

    public ConcurrentHashMap<String, Boolean> getApproves() {
        return approves;
    }
}





