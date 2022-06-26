package com.example.demo.DataAccess.Entity;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Entity(name = "ShoppingBags")
@Table(name = "shopping_bags")
public class DataShoppingBag {

    @EmbeddedId
    private ShoppingBagId shoppingBagId;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id",
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(
                    name = "store_fk"
            ))
    private DataStore store;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username",
            referencedColumnName = "username",
            foreignKey = @ForeignKey(
                    name = "user_fk"
            ))
    private DataUser user;


    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @CollectionTable(name = "shopping_bag_product_quantity",
            joinColumns ={@JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "store_fk")),
                    @JoinColumn(name = "username", foreignKey = @ForeignKey(name = "user_fk"))})
    private Map<Integer, Integer> productQuantity = new HashMap<>();

    public ShoppingBagId getShoppingBagId() {
        return shoppingBagId;
    }

    public void setShoppingBagId(ShoppingBagId shoppingBagId) {
        this.shoppingBagId = shoppingBagId;
    }

    public DataShoppingBag() {
    }


    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }

    public Map<Integer, Integer> getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Map<Integer, Integer> productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void update(DataShoppingBag shoppingBag) {
        this.productQuantity.clear();
        this.productQuantity.putAll(shoppingBag.getProductQuantity());
    }

    public DataUser getUser() {
        return user;
    }

    public void setUser(DataUser user) {
        this.user = user;
    }
}