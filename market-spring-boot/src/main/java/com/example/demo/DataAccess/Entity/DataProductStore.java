package com.example.demo.DataAccess.Entity;


import javax.persistence.*;


@Entity(name = "ProductStore")
@Table(name = "product_store")
public class DataProductStore {

    @Id
    @SequenceGenerator(
            name = "product_store_sequence",
            sequenceName = "product_store_sequence",
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
    @JoinColumn(name = "store",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(
                    name = "store_fk"
            ))
    private DataStore store;


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


    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }
}
