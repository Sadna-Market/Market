package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "ShoppingCart")
@Table(name = "shopping_carts")
public class DataShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shopping_cart_sequence")
    @SequenceGenerator(name = "shopping_cart_sequence",
            sequenceName = "shopping_cart_sequence",
            allocationSize = 1)
    @Column(name = "shopping_cart_id")
    private Integer shoppingCartId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dataShoppingCart",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    Set<DataShoppingBag> shoppingBags = new HashSet<>();

    public void setShoppingBags(Set<DataShoppingBag> shoppingBags) {
        shoppingBags.forEach(dataShoppingBag -> dataShoppingBag.setDataShoppingCart(this));
        this.shoppingBags = shoppingBags;
    }

    public Set<DataShoppingBag> getShoppingBags() {
        return shoppingBags;
    }

    public DataShoppingCart() {
    }

    public Integer getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Integer shoppingCardId) {
        this.shoppingCartId = shoppingCardId;
    }

    public void update(DataShoppingCart other) {
        this.shoppingCartId = other.getShoppingCartId();
        this.shoppingBags.clear();
        this.shoppingBags.addAll(other.getShoppingBags());
    }

}