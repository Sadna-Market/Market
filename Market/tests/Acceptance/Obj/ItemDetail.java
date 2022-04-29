package Acceptance.Obj;

import java.util.List;

public class ItemDetail {
    public int itemID;
    public int quantity;
    public int price;
    public String name;
    public String category;
    public List<String> ketwords;

    public ItemDetail(String name, int id, int quantity, int price, List<String> ketwords, String category) {
        this.name = name;
        itemID = id;
        this.quantity = quantity;
        this.price = price;
        this.ketwords = ketwords;
        this.category = category;
    }
    public  ItemDetail(String name, int id, int quantity, int price){
        this.name = name;
        itemID = id;
        this.quantity = quantity;
        this.price = price;
    }
}
