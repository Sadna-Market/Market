package main.Service;

import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.StoreModel.ProductStore;

public class ServiceItem {
     public int quantity;
     public double price;
     public int itemID;
     public ServiceItem(int quantity, double price, int itemID) {
          this.quantity = quantity;
          this.price = price;
          this.itemID = itemID;
     }
     public ServiceItem(ProductStore productStore) {
          this.quantity = productStore.getQuantity().getValue();
          this.price = productStore.getPrice().getValue();
          this.itemID = productStore.getProductType().getProductID().getValue();
     }

}
