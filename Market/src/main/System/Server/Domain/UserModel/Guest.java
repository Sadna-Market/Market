package main.System.Server.Domain.UserModel;

import java.util.UUID;

public class Guest {
    private ShoppingCart shoppingCart;
    public Guest() {
        this.shoppingCart = new ShoppingCart();
    }


    public synchronized ShoppingCart getShoppingCart()
    {

        return shoppingCart;
    }


}
