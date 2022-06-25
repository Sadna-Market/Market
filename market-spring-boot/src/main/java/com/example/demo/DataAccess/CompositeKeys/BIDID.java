package com.example.demo.DataAccess.CompositeKeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BIDID implements Serializable {

    @Column(name = "username")
    private String username;

    @Column(name = "storeId")
    private Integer storeId;

    @Column(name = "productTypeId")
    private Integer productTypeId;

    public BIDID() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BIDID bidid = (BIDID) o;
        return Objects.equals(getUsername(), bidid.getUsername()) && Objects.equals(getStoreId(), bidid.getStoreId()) && Objects.equals(getProductTypeId(), bidid.getProductTypeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getStoreId(), getProductTypeId());
    }
}
