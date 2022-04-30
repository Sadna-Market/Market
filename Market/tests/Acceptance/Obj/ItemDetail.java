package Acceptance.Obj;

import main.Service.ServiceItem;

import java.util.List;

public class ItemDetail {
    public int itemID;
    public int quantity;
    public double price;
    public String name;
    public String category;
    public List<String> ketwords;

    public ItemDetail(String name, int quantity, int price, List<String> ketwords, String category) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.ketwords = ketwords;
        this.category = category;
    }
    public  ItemDetail(String name, int quantity, int price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    public ItemDetail(ServiceItem s){
        this.itemID = s.itemID;
        this.price = s.price;
        this.quantity = s.quantity;
    }
}
