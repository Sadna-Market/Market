package com.example.demo.Domain.StoreModel;

import com.example.demo.DataAccess.CompositeKeys.BIDID;
import com.example.demo.DataAccess.CompositeKeys.PermissionId;
import com.example.demo.DataAccess.Entity.DataBID;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Enums.Status;
import com.example.demo.DataAccess.Enums.UserType;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BID {

    private static DataServices dataServices;

    public static void setDataServices(DataServices dataService) { BID.dataServices = dataService;}

    static Logger logger=Logger.getLogger(BID.class);

    private int storeId;
    private String username;
    private int productID;
    private String productName;
    private int quantity;
    private int totalPrice;
    private int lastPrice;
    private StatusEnum status;
    private ConcurrentHashMap<String,Boolean> approves;

    public BID(int storeId,String username, int productID, String productName, int quantity, int totalPrice, ConcurrentHashMap<String, Boolean> approves){
        this.storeId = storeId;
        this.username = username;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.lastPrice = totalPrice;
        this.status = StatusEnum.WaitingForApprovals;
        this.approves = approves;
    }

    //for upload from db
    public BID(int storeId,String username, int productID, String productName, int quantity, int totalPrice, int lastPrice, Status stats,ConcurrentHashMap<String, Boolean> approves){
        this.storeId = storeId;
        this.username = username;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.lastPrice = lastPrice;
        this.status = StatusEnum.valueOf(stats.name());
        this.approves = approves;
    }



    public enum StatusEnum {
        WaitingForApprovals,
        BIDRejected,
        CounterBID,
        BIDApproved,
        ProductBought;
    }

    public DataBID getDataObject(DataStore dataStore) {
        DataBID dataBID = new DataBID();
        dataBID.setLastPrice(this.lastPrice);
        dataBID.setProductName(this.productName);
        dataBID.setTotalPrice(this.totalPrice);
        dataBID.setId(getBIDID(dataStore.getStoreId()));
        dataBID.setQuantity(this.quantity);
        dataBID.setStatus(Status.valueOf(this.status.name()));
        dataBID.setApproves(this.approves);
        dataBID.setStore(dataStore);
        return dataBID;
    }

    public BIDID getBIDID(int storeID){
        BIDID id= new BIDID();
        id.setStoreId(storeID);
        id.setProductTypeId(productID);
        id.setUsername(username);
        return id;
    }

    private void updateBIDInDB(){
        if (dataServices != null && dataServices.getBidService() != null) { //because no autowire in AT
            DataStore dataStore = dataServices.getStoreService().getStoreById(storeId);
            if(dataStore == null){
                logger.warn(String.format("store %d is not present in db", storeId));
                throw new IllegalArgumentException("fail to get data store for update BID to db");
            }
            else {
                DataBID dataBID = getDataObject(dataStore);
                if (!dataServices.getBidService().insertBID(dataBID)) {
                    logger.warn(String.format("fail to add BID to store %d - database", storeId));
                    throw new IllegalArgumentException("fail to update BID in db");
                }
            }
        }
    }

    public DResponseObj<Boolean> approve(String ownerEmail) {
        Boolean success = approves.replace(ownerEmail,true);
        updateBIDInDB();
        return success == null ? new DResponseObj<>(false,ErrorCode.NOPERMISSION) :
                success ? new DResponseObj<>(false,ErrorCode.ALLREADYAPPROVEDTHISBID):
                        new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> reject(String ownerEmail) {
        if(approves.containsKey(ownerEmail)) {
            status = StatusEnum.BIDRejected;
            updateBIDInDB();
            return new DResponseObj<>(true);
        }
        return new DResponseObj<>(false,ErrorCode.NOPERMISSION);
    }
    public DResponseObj<Boolean> counter(String ownerEmail, int newTotalPrice) {
        if(approves.containsKey(ownerEmail)) {
            approves.replaceAll((K, V) -> V = false);
            approves.replace(ownerEmail, true);
            lastPrice = newTotalPrice;
            status = StatusEnum.CounterBID;
            updateBIDInDB();
            return new DResponseObj<>(true);
        }
        return new DResponseObj<>(false);
    }

    public void responseCounter(boolean approve) {
        if(approve){
            status = StatusEnum.WaitingForApprovals;
        }else
            status = StatusEnum.BIDRejected;
        updateBIDInDB();
    }

    public boolean allApproved(){
        for(Boolean b : approves.values()){
            if(!b)
                return false;
        }
        status = StatusEnum.BIDApproved;
        updateBIDInDB();
        return true;
    }

    public boolean needToChangeToApproved(){
        if(!status.equals(StatusEnum.WaitingForApprovals)) return false;
        for(Boolean b : approves.values()){
            if(!b)
                return false;
        }
        status = StatusEnum.BIDApproved;
        updateBIDInDB();
        return true;
    }

    public void ChangeStatusProductBought() {
        status = StatusEnum.ProductBought;
        updateBIDInDB();
    }


    public void addManagerToList(String managerEmail) {
        if(!approves.containsKey(managerEmail) && (status.equals(StatusEnum.WaitingForApprovals) || (status.equals(StatusEnum.CounterBID)))) {
            approves.put(managerEmail, false);
            updateBIDInDB();
        }
    }

    public void removeManagerFromList(String managerEmail) {
        if (approves.containsKey(managerEmail) && (status.equals(StatusEnum.WaitingForApprovals) || (status.equals(StatusEnum.CounterBID)))){
            approves.remove(managerEmail);
            updateBIDInDB();
        }
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

    public void setUsername(String username) {
        this.username = username;
        updateBIDInDB();
    }

    public void setProductID(int productID) {
        this.productID = productID;
        updateBIDInDB();
    }

    public void setProductName(String productName) {
        this.productName = productName;
        updateBIDInDB();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateBIDInDB();
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        updateBIDInDB();
    }

    public void setLastPrice(int lastPrice) {
        this.lastPrice = lastPrice;
        updateBIDInDB();
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
        updateBIDInDB();
    }

    public void setApproves(ConcurrentHashMap<String, Boolean> approves) {
        this.approves = approves;
        updateBIDInDB();
    }
}





