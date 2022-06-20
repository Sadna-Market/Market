package com.example.demo.DataAccess.CompositeKeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ShoppingBagId implements Serializable {

    @Column(name = "username")
    private String username;


    @Column(name = "store_id")
    private Integer storeId;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingBagId that = (ShoppingBagId) o;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getStoreId(), that.getStoreId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getStoreId());
    }
}
