package com.example.demo.DataAccess.Entity;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;

import javax.persistence.*;

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

    //TODO: add products hashmap <productid,quantity> something like that
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