package main.System.Server.Domain.Market;

import main.System.Server.Domain.UserComponent.Response.StoreResponse;

public class Store {
    Inventory inventory ;
    int StoreId;

    public int getStoreId(){
        return StoreId;
    }

    public Store GetStoreInfo() {
        return null;
    }


    public boolean isProductExistInStock(int productId, int quantity){
        return inventory.isProductExistInStock(productId,quantity);
    }
}
