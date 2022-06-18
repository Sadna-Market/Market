package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.util.*;

@Entity(name = "ProductType")
@Table(name = "product_type")
public class DataProductType {
    @Id
    @SequenceGenerator(
            name = "product_type_sequence",
            sequenceName = "product_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_type_sequence"
    )
    @Column(name = "product_type_id", nullable = false)
    private Integer productTypeId;

    @Column(name = "rate")
    private Integer rate = 0;

    @Column(name = "counter_rates")
    private Integer counter_rates = 0;

    @Column(name = "product_name", columnDefinition = "TEXT", nullable = false)
    private String productName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private int category;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "store")
    @CollectionTable(name = "product_type_and_store",
            joinColumns = {@JoinColumn(name = "product_type_id", foreignKey = @ForeignKey(name = "product_type_fk"))})
    private Set<Integer> stores = new HashSet<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "productType",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<DataProductStore> productStores = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "productType",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<DataProductStoreHistory> productStoreHistory = new HashSet<>();

    public DataProductType() {
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getCounter_rates() {
        return counter_rates;
    }

    public void setCounter_rates(Integer counter_rates) {
        this.counter_rates = counter_rates;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Set<Integer> getStores() {
        return stores;
    }

    public void setStores(Set<Integer> stores) {
        this.stores = stores;
    }

    public void update(DataProductType other) {
        this.productTypeId = other.getProductTypeId();
        this.productName = other.getProductName();
        this.category = other.getCategory();
        this.counter_rates = other.getCounter_rates();
        this.description = other.getDescription();
        this.rate = other.getRate();
        this.stores.clear();
        this.stores.addAll(other.getStores());
        this.productStores.clear();
        this.productStores.addAll(other.getProductStores());
    }

    public Set<DataProductStore> getProductStores() {
        return productStores;
    }

    public void setProductStores(Set<DataProductStore> productStores) {
        this.productStores = productStores;
    }

    public Set<DataProductStoreHistory> getProductStoreHistory() {
        return productStoreHistory;
    }

    public void setProductStoreHistory(Set<DataProductStoreHistory> productStoreHistory) {
        this.productStoreHistory = productStoreHistory;
    }
}