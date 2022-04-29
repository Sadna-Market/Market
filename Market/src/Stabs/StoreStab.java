package Stabs;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.Response.DResponseObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class StoreStab extends Store {

    int rate;
    String name;
    public StoreStab(String name, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, String founder) {
        super(name, discountPolicy, buyPolicy, founder);
    }
    public StoreStab(){
        super("",new DiscountPolicy(),new BuyPolicy(),"");
    }


    /////////////////////////////////////////////// Methods ///////////////////////////////////////////////////////

    public DResponseObj<String> getProductInStoreInfo(int productId){
        DResponseObj<String> output=new DResponseObj<>();
        output.value="yaki";
        return output;
    }

    //requirement II.2.1
    public List<ProductStore> GetStoreProducts() {
        return new ArrayList<>(new ArrayList<>());
    }

    //requirement II.2.3 & II.2.4.2 (before add product to shoppingBag check quantity
    public DResponseObj<Boolean> isProductExistInStock(int productId, int quantity){
        return new DResponseObj<>(true);
    }


    //requirement II.4.1 (only owners)
    public DResponseObj<Boolean> addNewProduct(ProductType productType, int quantity, double price) {
        return new DResponseObj<>(true);
    }

    //requirement II.4.1 (only owners)
    public DResponseObj<Boolean> removeProduct(int productId) {
        return new DResponseObj<>(true);
    }

    //requirement II.4.1 & II.2.5 (only owners)
    //if change to quantity 0 not delete product (need to find the product price later)
    public DResponseObj<Boolean> setProductQuantity(int productId, int quantity) {
        return new DResponseObj<>(true);
    }

    //requirement II.4.1  (only owners)
    public DResponseObj<Boolean> setProductPrice(int productId, double price) {
        return new DResponseObj<>(true);
    }

    //requirement II.2.2
    public Double getProductPrice(int productId) {
        return 8.0;
    }

    //requirement II.4.13 & II.6.4 (only system manager)
    public DResponseObj<List<History>> getStoreOrderHistory() {
        return new DResponseObj<>(new ArrayList<>());
    }

    //requirement II.4.13 & II.6.4 (only system manager)
    public DResponseObj<List<History>> getUserHistory(String user) {
        return new DResponseObj<>(new ArrayList<>());
    }

    //niv tests
    public List<Integer> getTIDHistory(){
        return new ArrayList<>();
    }

    //requirement II.2.5
    public DResponseObj<Boolean> addHistory(int TID, String user, HashMap<Integer,Integer> products, double finalPrice) {
        return new DResponseObj<>(true);
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public ConcurrentHashMap<Integer, Integer> checkBuyPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        return new ConcurrentHashMap<>();
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public double checkDiscountPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        return 1.0;
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public double calculateBagPrice(ConcurrentHashMap<Integer, Integer> productsInBag){
        return 8.0;
    }

    //requirement II.2.5
    //return null if products not exist
    public StampedLock getProductLock(int productID){
        return null;
    }

    //requirement II.4.9  (only owners)
    public DResponseObj<Boolean> closeStore() {
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> newStoreRate(int rate){

        this.rate=rate;
        return new DResponseObj<>(true);
    }


/*    //requirement II.4.11  (only owners)
    public List<String> getStoreManagers(){
        List<String> managers = new ArrayList<>();
        for(Permission p: getSafePermission()){
            if(p.getGranteeType() == userTypes.manager)
                managers.add(p.getGrantee().getEmail());
        }
        return managers;
    }

    //requirement II.4.11  (only owners)
    public List<String> getStoreOwners(){
        List<String> owners = new ArrayList<>();
        for(Permission p: getSafePermission()){
            if(p.getGranteeType() == userTypes.owner)
                owners.add(p.getGrantee().getEmail());
        }
        return owners;
    }*/

    //requirement II.4.4 & II.4.6 & II.4.7 (only owners)
    public void addPermission(Permission p){
    }

    //requirement II.4.7 (only owners)
    public void removePermission(Permission p){
        ;
    }
    public List<Permission> getPermission(){
        return new ArrayList<>();
    }


    /////////////////////////////////////////////// Getters and Setters /////////////////////////////////////////////

    public int getStoreId(){
        return 8;
    }

    public Inventory getInventory() {
        return null;
    }

    public String getName() {
        return "yaki";
    }

    public DResponseObj<Boolean> isOpen() {
        return new DResponseObj<>(true);
    }

    public int getRate() {
        return rate;
    }

    public DiscountPolicy getDiscountPolicy() {
        return new DiscountPolicy();

    }

    public BuyPolicy getBuyPolicy() {
        return new BuyPolicy();
    }

    public String getFounder() {
        return "Yaki";
    }

    public List<Permission> getSafePermission() {
        return new ArrayList<>();
    }

    public void setHistory(ConcurrentHashMap<Integer,History> history) {

    }

    //requirement II.4.2  (only owners)
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {

    }

    //requirement II.4.2  (only owners)
    public void setBuyPolicy(BuyPolicy buyPolicy) {

    }

    public void setFounder(String founder) {

    }

    public ConcurrentHashMap<Integer, History> getHistory() {
        return new ConcurrentHashMap<>();
    }

    public void openStoreAgain() {
        return;
    }

}
