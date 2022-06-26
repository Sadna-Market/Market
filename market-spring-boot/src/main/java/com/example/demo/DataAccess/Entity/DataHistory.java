package com.example.demo.DataAccess.Entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "History")
@Table(name = "history")
public class DataHistory {
    @Id
    @SequenceGenerator(
            name = "history_sequence",
            sequenceName = "history_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "history_sequence"
    )
    @Column(name = "history_id")
    private Integer historyId;

    @Column(name = "TID")
    private Integer TID;

    @Column(name = "supply_id")
    private Integer supplyId;

    @Column(name = "final_price")
    private Double finalPrice;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "history",
            cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<DataProductStoreHistory> products = new HashSet<>();

    @Column(name = "store_id")
    private Integer store;

    @Column(name = "username")
    private String user;

    public DataHistory() {
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

//    public Set<DataProductStore> getProducts() {
//        return products;
//    }
//
//    public void setProducts(Set<DataProductStore> products) {
//        this.products = products;
//    }

    public Set<DataProductStoreHistory> getProducts() {
        return products;
    }

    public void setProducts(Set<DataProductStoreHistory> products) {
        this.products.clear();
        this.products = products;
    }

    public Integer getTID() {
        return TID;
    }

    public void setTID(Integer TID) {
        this.TID = TID;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
