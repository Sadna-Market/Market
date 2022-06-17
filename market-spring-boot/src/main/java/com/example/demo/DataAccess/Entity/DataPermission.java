package com.example.demo.DataAccess.Entity;

import com.example.demo.DataAccess.CompositeKeys.PermissionId;
import com.example.demo.DataAccess.Enums.PermissionType;
import com.example.demo.DataAccess.Enums.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Permission")
@Table(name = "permissions")
public class DataPermission {

    @EmbeddedId
    private PermissionId permissionId;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "storeId_fk"))
    private DataStore store;

    @ManyToOne
    @MapsId("granteeId")
    @JoinColumn(name = "grantee",
            nullable = false,
            referencedColumnName = "username",
            foreignKey = @ForeignKey(name = "user_grantee_fk"))
    private DataUser grantee;

    @ManyToOne
    @MapsId("grantorId")
    @JoinColumn(name = "grantor",
            nullable = false,
            referencedColumnName = "username",
            foreignKey = @ForeignKey(name = "user_grantor_fk"))
    private DataUser grantor;

    @Enumerated(EnumType.STRING)
    private UserType granteeType;

    @Enumerated(EnumType.STRING)
    private UserType grantorType;

    @ElementCollection(targetClass = PermissionType.class)
    @JoinTable(name = "permission_types")
    @Column(name = "grantee_permission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<PermissionType> granteePermissionTypes = new HashSet<>();


    public DataPermission() {
    }

    public PermissionId getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(PermissionId permissionId) {
        this.permissionId = permissionId;
    }

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }

    public DataUser getGrantee() {
        return grantee;
    }

    public void setGrantee(DataUser grantee) {
        this.grantee = grantee;
    }

    public DataUser getGrantor() {
        return grantor;
    }

    public void setGrantor(DataUser grantor) {
        this.grantor = grantor;
    }

    public UserType getGranteeType() {
        return granteeType;
    }

    public void setGranteeType(UserType granteeType) {
        this.granteeType = granteeType;
    }

    public UserType getGrantorType() {
        return grantorType;
    }

    public void setGrantorType(UserType grantorType) {
        this.grantorType = grantorType;
    }


    public Set<PermissionType> getGranteePermissionTypes() {
        return granteePermissionTypes;
    }

    public void setGranteePermissionTypes(Set<PermissionType> granteePermissionTypes) {
        this.granteePermissionTypes = granteePermissionTypes;
    }
}
