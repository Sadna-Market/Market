package com.example.demo.DataAccess.Entity;


import javax.persistence.*;
import java.util.*;

@Entity(name = "Inventory")
@Table(name = "inventory")
public class DataInventory {

    @Id
    @SequenceGenerator(
            name = "inventory_sequence",
            sequenceName = "inventory_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_sequence")
    @Column(name = "inventory_id")
    private Integer inventoryId;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "inventory",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<DataProductStore> productStores = new HashSet<>();

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }


    public Integer getInventoryId() {
        return inventoryId;
    }

    public Set<DataProductStore> getProductStores() {
        return productStores;
    }

    public void setProductStores(Set<DataProductStore> productStores) {
        this.productStores = productStores;
    }

    public DataInventory() {
    }

    public void update(DataInventory other){
        this.inventoryId = other.getInventoryId();
        this.productStores.clear();
        this.productStores.addAll(other.getProductStores());
    }


}
