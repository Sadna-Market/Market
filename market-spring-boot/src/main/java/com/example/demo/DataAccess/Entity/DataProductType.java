package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "ProductType")
@Table(name = "product_type")
public class DataProductType {
    @Id
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

    @ManyToMany(mappedBy = "productTypes")
    public Set<DataStore> stores = new HashSet<>();

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

    public Set<DataStore> getStores() {
        return stores;
    }

    public void setStores(Set<DataStore> stores) {
        this.stores = stores;
    }
}