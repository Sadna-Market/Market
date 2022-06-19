package com.example.demo.DataAccess.CompositeKeys;

import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Entity.DataUser;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PermissionId implements Serializable {
    @Column(name = "grantee_id")
    private String granteeId;

    @Column(name = "grantor_id")
    private String grantorId;

    @Column(name = "store_id")
    private Integer storeId;

    public PermissionId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionId that = (PermissionId) o;
        return Objects.equals(getGranteeId(), that.getGranteeId()) && Objects.equals(getGrantorId(), that.getGrantorId()) && Objects.equals(getStoreId(), that.getStoreId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGranteeId(), getGrantorId(), getStoreId());
    }

    public String getGranteeId() {
        return granteeId;
    }

    public void setGranteeId(String granteeId) {
        this.granteeId = granteeId;
    }

    public String getGrantorId() {
        return grantorId;
    }

    public void setGrantorId(String grantorId) {
        this.grantorId = grantorId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}
