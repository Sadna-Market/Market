package com.example.demo.DataAccess.Entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "Store")
@Table(name = "stores")
public class DataStore {

    @Id
    @SequenceGenerator(
            name = "store_sequence",
            sequenceName = "store_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "store_sequence"
    )
    @Column(name = "store_id")
    private Integer storeId;

    @Column(
            name = "store_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @OneToOne(cascade = {CascadeType.REMOVE, /*when the store gets deleted then the inventory gets deleted*/
            CascadeType.PERSIST /* when saving the store then save the inventory too */},
            fetch = FetchType.EAGER) /*when store is fetched from db then fetch the inventory too*/
    @JoinColumn( /* The @JoinColumn annotation combined with a @OneToOne mapping indicates that a given column in the owner entity refers to a primary key in the reference entity*/
            name = "inventory_id", /* The name of the foreign key column in the DataStore entity is specified by name property.*/
            nullable = false,
            referencedColumnName = "inventory_id",
            foreignKey = @ForeignKey(
                    name = "inventory_fk"
            )
    )
    private DataInventory inventory;


    @Column(
            name = "founder",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String founder;

    @Column(
            name = "is_open",
            nullable = false)
    private Boolean isOpen;

    @Column(
            name = "rate",
            nullable = false)
    private Integer rate; // between 0-10

    @Column(
            name = "num_of_rated",
            nullable = false)
    private Integer numOfRated;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "store",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<DataHistory> history = new HashSet<>();

    @OneToOne(cascade = {CascadeType.REMOVE, /*when the store gets deleted then the DiscountPolicy gets deleted*/
            CascadeType.PERSIST /* when saving the store then save the DiscountPolicy too */},
            fetch = FetchType.EAGER) /*when store is fetched from db then fetch the DiscountPolicy too*/
    @JoinColumn( /* The @JoinColumn annotation combined with a @OneToOne mapping indicates that a given column in the owner entity refers to a primary key in the reference entity*/
            name = "discount_policy_id", /* The name of the foreign key column in the DataStore entity is specified by name property.*/
            nullable = false,
            referencedColumnName = "discount_policy_id",
            foreignKey = @ForeignKey(
                    name = "discount_policy_fk"
            )
    )
    private DataDiscountPolicy discountPolicy;

    @OneToOne(cascade = {CascadeType.REMOVE, /*when the store gets deleted then the BuyPolicy gets deleted*/
            CascadeType.PERSIST /* when saving the store then save the BuyPolicy too */},
            fetch = FetchType.EAGER) /*when store is fetched from db then fetch the BuyPolicy too*/
    @JoinColumn( /* The @JoinColumn annotation combined with a @OneToOne mapping indicates that a given column in the owner entity refers to a primary key in the reference entity*/
            name = "buy_policy_id", /* The name of the foreign key column in the DataStore entity is specified by name property.*/
            nullable = false,
            referencedColumnName = "buy_policy_id",
            foreignKey = @ForeignKey(
                    name = "buy_policy_fk"
            )
    )
    private DataBuyPolicy buyPolicy;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "store", /* this is the name of the field in the "Many" side to be referenced to */
            orphanRemoval = true,
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST}
    )
    private Set<DataPermission> permissions = new HashSet<>(); // all the permission that have in this store

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "stores_product_types",
            joinColumns = {@JoinColumn(name = "store_id", foreignKey = @ForeignKey(
                    name = "store_fk"
            ))},
            inverseJoinColumns = {@JoinColumn(name = "product_type_id", foreignKey = @ForeignKey(
                    name = "product_type_fk"
            ))})
    private Set<DataProductType> productTypes = new HashSet<>();

    /**
     * Constructor
     */
    public DataStore() {
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataInventory getInventory() {
        return inventory;
    }

    public void setInventory(DataInventory inventory) {
        this.inventory = inventory;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getNumOfRated() {
        return numOfRated;
    }

    public void setNumOfRated(Integer numOfRated) {
        this.numOfRated = numOfRated;
    }

    public DataDiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(DataDiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public DataBuyPolicy getBuyPolicy() {
        return buyPolicy;
    }

    public void setBuyPolicy(DataBuyPolicy buyPolicy) {
        this.buyPolicy = buyPolicy;
    }

    public Set<DataHistory> getHistory() {
        return history;
    }

    public void setHistory(Set<DataHistory> history) {
        this.history = history;
    }

    public Set<DataPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<DataPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<DataProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(Set<DataProductType> productTypes) {
        this.productTypes = productTypes;
    }
}
