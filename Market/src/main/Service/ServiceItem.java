package main.Service;

import main.System.Server.Domain.Market.ProductType;

public class ServiceItem {
     public int quantity;
     public double price;
     public int itemID;
     public ServiceItem(int quantity, double price, int itemID) {
          this.quantity = quantity;
          this.price = price;
          this.itemID = itemID;
     }

}
