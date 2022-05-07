package main.Service;

import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.StoreModel.ProductStore;

public class ServiceItem {
     public int quantity;
     public double price;
     public int itemID;
     public String name;
     public ServiceItem(int quantity, double price, int itemID, String name) {
          this.quantity = quantity;
          this.price = price;
          this.itemID = itemID;
          this.name = name;
     }
     public ServiceItem(ProductStore productStore) {
          this.quantity = productStore.getQuantity().getValue();
          this.price = productStore.getPrice().getValue();
          this.itemID = productStore.getProductType().getProductID().getValue();
     }

}
