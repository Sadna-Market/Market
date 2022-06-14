package com.example.demo.DataAccess.Entity;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity(name = "ShoppingBags")
@Table(name = "shopping_bags")
public class DataShoppingBag {

    @EmbeddedId
    private ShoppingBagId shoppingBagId;

    @ManyToOne
    @JoinColumn(name = "shopping_cart",
            foreignKey = @ForeignKey(name = "shopping_cart_fk"))
    private DataShoppingCart dataShoppingCart;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id",
            foreignKey = @ForeignKey(
                    name = "store_fk"
            ))
    private DataStore store;


    @ElementCollection
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @CollectionTable(name = "shopping_bag_product_quantity",
            joinColumns = {@JoinColumn(name = "username", foreignKey = @ForeignKey(name = "user_fk")),
                    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "store_fk"))})
    private Map<Integer, Integer> productQuantity = new HashMap<>();

    public DataShoppingCart getDataShoppingCart() {
        return dataShoppingCart;
    }

    public void setDataShoppingCart(DataShoppingCart dataShoppingCart) {
        this.dataShoppingCart = dataShoppingCart;
    }

    public ShoppingBagId getShoppingBagId() {
        return shoppingBagId;
    }

    public void setShoppingBagId(ShoppingBagId shoppingBagId) {
        this.shoppingBagId = shoppingBagId;
    }

    public DataShoppingBag() {
    }
}