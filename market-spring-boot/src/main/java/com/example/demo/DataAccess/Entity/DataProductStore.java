package com.example.demo.DataAccess.Entity;


import javax.persistence.*;

@Entity(name = "ProductStore")
@Table(name = "product_store")
public class DataProductStore {

    @Id
    @SequenceGenerator(
            name = "product_store_sequence",
            sequenceName = "productStore_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_store_sequence"
    )
    @Column(name = "product_store_id")
    private Integer productStoreId;

    @ManyToOne
    @JoinColumn(name = "product_type_id",
            nullable = false,
            referencedColumnName = "product_type_id",
            foreignKey = @ForeignKey(
                    name = "product_type_fk"
            ))
    private DataProductType productType;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "inventory",
            nullable = false,
            referencedColumnName = "inventory_id",
            foreignKey = @ForeignKey(
                    name = "inventory_fk"
            ))
    private DataInventory inventory;

    @ManyToOne
    @JoinColumn(name = "history_id",
            nullable = false,
            referencedColumnName = "history_id",
            foreignKey = @ForeignKey(name = "store_history_fk")
    )
    private DataHistory history;

    public DataInventory getInventory() {
        return inventory;
    }

    public void setInventory(DataInventory inventory) {
        this.inventory = inventory;
    }

    public DataProductStore() {
    }

    public void setProductType(DataProductType productType) {
        this.productType = productType;
    }

    public DataProductType getProductType() {
        return productType;
    }

    public Integer getProductStoreId() {
        return productStoreId;
    }

    public void setProductStoreId(Integer productStoreId) {
        this.productStoreId = productStoreId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
