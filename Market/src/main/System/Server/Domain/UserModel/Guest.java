package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.Response.DResponseObj;

import java.util.UUID;

public class Guest {
    private ShoppingCart shoppingCart;
    public Guest() {
        this.shoppingCart = new ShoppingCart();
    }


    public DResponseObj<ShoppingCart> GetSShoppingCart() {
        return new DResponseObj<>( this.shoppingCart);
    }


}
