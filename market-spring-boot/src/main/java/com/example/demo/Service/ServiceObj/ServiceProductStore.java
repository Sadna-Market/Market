package com.example.demo.Service.ServiceObj;


import com.example.demo.Domain.StoreModel.ProductStore;

public class ServiceProductStore {
     public int quantity;
     public double price;
     public int itemID;
     public String name;
     public ServiceProductStore(int quantity, double price, int itemID, String name) {
          this.quantity = quantity;
          this.price = price;
          this.itemID = itemID;
          this.name = name;
     }
     public ServiceProductStore(ProductStore productStore){
          this.quantity = productStore.getQuantity().getValue();
          this.price = productStore.getPrice().getValue();
          this.itemID = productStore.getProductType().getProductID().getValue();
     }

}
