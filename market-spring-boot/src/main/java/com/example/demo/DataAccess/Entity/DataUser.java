package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "User")
@Table(name = "users")
public class DataUser {

    @Id
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "phone_number", nullable = false, columnDefinition = "TEXT")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne
    @JoinColumn(name = "data_shopping_cart_id", foreignKey = @ForeignKey(name = "data_shopping_cart_fk"))
    private DataShoppingCart dataShoppingCart;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "userHistory",
            orphanRemoval = true
    )
    private Set<DataHistory> histories = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "grantee",
            orphanRemoval = true
    )
   private Set<DataPermission> accessPermission = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "grantor",
            orphanRemoval = true
    )
   private Set<DataPermission> grantorPermission = new HashSet<>();


    public void setDataShoppingCart(DataShoppingCart dataShoppingCart) {
        this.dataShoppingCart = dataShoppingCart;
    }

    public DataShoppingCart getDataShoppingCart() {
        return dataShoppingCart;
    }

    public DataUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<DataHistory> getHistories() {
        return histories;
    }

    public void setHistories(Set<DataHistory> histories) {
        this.histories = histories;
    }

    public Set<DataPermission> getAccessPermission() {
        return accessPermission;
    }

    public void setAccessPermission(Set<DataPermission> accessPermission) {
        this.accessPermission = accessPermission;
    }

    public Set<DataPermission> getGrantorPermission() {
        return grantorPermission;
    }

    public void setGrantorPermission(Set<DataPermission> grantorPermission) {
        this.grantorPermission = grantorPermission;
    }
}
