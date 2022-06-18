package com.example.demo.DataAccess.Entity;

import javax.persistence.*;

@Entity(name = "ProductStoreHistory")
@Table(name = "productStoreHistory")
public class DataProductStoreHistory {

    @Id
    @SequenceGenerator(
            name = "product_store_history_sequence",
            sequenceName = "product_store_history_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_store_history_sequence"
    )
    @Column(name = "product_store_history_id")
    private Integer productStoreHistoryId;

    @ManyToOne
    @JoinColumn(name = "product_type_id",
            nullable = false,
            referencedColumnName = "product_type_id",
            foreignKey = @ForeignKey(
                    name = "product_type_fk"
            ))
    private DataProductType productType;

    @ManyToOne
    @JoinColumn(name = "history_id",
            nullable = false,
            referencedColumnName = "history_id",
            foreignKey = @ForeignKey(name = "history_fk"))
    private DataHistory history;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    public Integer getProductStoreHistoryId() {
        return productStoreHistoryId;
    }

    public void setProductStoreHistoryId(Integer productStoreHistoryId) {
        this.productStoreHistoryId = productStoreHistoryId;
    }

    public DataProductType getProductType() {
        return productType;
    }

    public void setProductType(DataProductType productType) {
        this.productType = productType;
    }

    public DataHistory getHistory() {
        return history;
    }

    public void setHistory(DataHistory history) {
        this.history = history;
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
