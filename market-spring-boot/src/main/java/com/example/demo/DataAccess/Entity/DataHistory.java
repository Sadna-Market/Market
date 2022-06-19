package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "History")
@Table(name = "history")
public class DataHistory {
    @Id
    @Column(name = "history_id")
    private Integer historyId;

    @Column(name = "supply_id")
    private Integer supplyId;

    @Column(name = "final_price")
    private Double finalPrice;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "history",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private Set<DataProductStoreHistory> products = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "store_id",
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(
                    name = "store_fk"
            ))
    private DataStore store;

    @ManyToOne
    @JoinColumn(name = "username",
            referencedColumnName = "username",
            foreignKey = @ForeignKey(
                    name = "user_fk"
            ))
    private DataUser user;

    public DataStore getDataStore() {
        return store;
    }

    public void setDataStore(DataStore dataStore) {
        this.store = dataStore;
    }

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

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }


    public DataUser getUser() {
        return user;
    }

    public void setUser(DataUser userHistory) {
        this.user = userHistory;
    }

    public Set<DataProductStoreHistory> getProducts() {
        return products;
    }

    public void setProducts(Set<DataProductStoreHistory> products) {
        products.forEach(dataProductStoreHistory -> {
            dataProductStoreHistory.setHistory(this);
        });
        this.products = products;
    }
}
