package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.StoreModel.Store;

public class ShoppingBag {
    Store store;


    //todo maybe need to remove store, this is add new assosiation to the uml
    public Store getStore() {
    return store;
    }
    public boolean addProduct(int productId , int quantity){
        return false;
    }

    public boolean setProductQuantity(int productId, int quantity)
    {
        return false;
    }

    public boolean removeProductFromShoppingBag(int productId){
        return false;
    }
}
