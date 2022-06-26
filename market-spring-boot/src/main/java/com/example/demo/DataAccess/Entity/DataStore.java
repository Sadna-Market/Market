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

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "store"
    )
    private Set<DataProductStore> productStores = new HashSet<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "store"
    )
    private Set<DataShoppingBag> shoppingBags = new HashSet<>();


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
            mappedBy = "store"
    )
    private Set<DataHistory> history = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, /*when the store gets deleted then the DiscountPolicy gets deleted*/
            /*CascadeType.PERSIST /* when saving the store then save the DiscountPolicy too */
            fetch = FetchType.EAGER,
            mappedBy = "store",
            orphanRemoval = true) /*when store is fetched from db then fetch the DiscountPolicy too*/
    private Set<DataDiscountRule> discountRules;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, /*when the store gets deleted then the DiscountPolicy gets deleted*/
            /*CascadeType.PERSIST /* when saving the store then save the DiscountPolicy too */
            fetch = FetchType.EAGER,
            mappedBy = "store",
            orphanRemoval = true) /*when store is fetched from db then fetch the DiscountPolicy too*/
    private Set<DataBuyRule> buyRules;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "store",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<DataBID> bids = new HashSet<>();

//    @OneToMany(
//            fetch = FetchType.EAGER,
//            mappedBy = "store", /* this is the name of the field in the "Many" side to be referenced to */
//            orphanRemoval = true,
//            cascade = {CascadeType.REMOVE, CascadeType.PERSIST}
//    )
//    private Set<DataPermission> permissions = new HashSet<>(); // all the permission that have in this store


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

    public Set<DataHistory> getHistory() {
        return history;
    }

    public void setHistory(Set<DataHistory> history) {
        this.history = history;
    }

//    public Set<DataPermission> getPermissions() {
//        return permissions;
//    }
//
//    public void setPermissions(Set<DataPermission> permissions) {
//        this.permissions = permissions;
//    }

    //    public Set<DataProductType> getProductTypes() {
//        return productTypes;
//    }
//
//    public void setProductTypes(Set<DataProductType> productTypes) {
//        this.productTypes = productTypes;
//    }
    public void update(DataStore other) {
        this.storeId = other.getStoreId();
        this.founder = other.getFounder();
        this.productStores.clear();
        this.productStores.addAll(other.getProductStores());
        this.isOpen = other.getOpen();
        this.name = other.getName();
        this.numOfRated = other.getNumOfRated();
        this.rate = other.getRate();
        this.history.clear();
        this.history.addAll(other.getHistory());
//        this.permissions.clear();
//        this.permissions.addAll(other.getPermissions());
    }

    public Set<DataProductStore> getProductStores() {
        return productStores;
    }

    public void setProductStores(Set<DataProductStore> productStores) {
        productStores.forEach(dataProductStore -> dataProductStore.setStore(this));
        this.productStores = productStores;
    }

    public Set<DataDiscountRule> getDiscountRules() {
        return discountRules;
    }

    public void setDiscountRules(Set<DataDiscountRule> discountRule) {
        this.discountRules = discountRule;
    }

    public Set<DataBuyRule> getBuyRules() {
        return buyRules;
    }

    public void setBuyRules(Set<DataBuyRule> buyRules) {
        this.buyRules = buyRules;
    }


    @Override
    public String toString() {
        return "DataStore{" +
                "storeId=" + storeId +
                ", name='" + name + '\'' +
                ", productStores=" + productStores +
                ", founder='" + founder + '\'' +
                ", isOpen=" + isOpen +
                ", rate=" + rate +
                ", numOfRated=" + numOfRated +
                ", history=" + history +
                ", discountRules=" + discountRules +
                ", buyRules=" + buyRules +
                '}';
    }

    public Set<DataBID> getBids() {
        return bids;
    }

    public void setBids(Set<DataBID> bids) {
        this.bids = bids;
    }
}
