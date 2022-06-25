package com.example.demo.DataAccess.Entity;

import com.example.demo.DataAccess.CompositeKeys.BIDID;
import com.example.demo.DataAccess.Enums.PermissionType;
import com.example.demo.DataAccess.Enums.Status;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "BID")
@Table(name = "bid")
public class DataBID {

    @EmbeddedId
    private BIDID id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "totalPrice")
    private Integer totalPrice;

    @Column(name = "lastPrice")
    private Integer lastPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "owner_approces_bid")
    @Column(name = "approved", nullable = false)
    Map<String,Boolean> approves = new HashMap<>();

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id",
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(
                    name = "store_fk"
            ))
    private DataStore store;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Integer lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, Boolean> getApproves() {
        return approves;
    }

    public void setApproves(Map<String, Boolean> approves) {
        this.approves = approves;
    }

    public BIDID getId() {
        return id;
    }

    public void setId(BIDID id) {
        this.id = id;
    }

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }

    public void update(DataBID dataBID) {
        this.id = dataBID.getId();
        this.lastPrice = dataBID.getLastPrice();
        this.productName = dataBID.getProductName();
        this.quantity = dataBID.getQuantity();
        this.status = dataBID.getStatus();
        this.totalPrice = dataBID.getTotalPrice();
        this.store = dataBID.getStore();
        this.approves.clear();
        this.approves = dataBID.getApproves();
    }
}
