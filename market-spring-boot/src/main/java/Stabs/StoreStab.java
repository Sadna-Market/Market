
package Stabs;


import com.example.demo.Domain.Market.Permission;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class StoreStab extends Store {

    int rate;
    String name;
    public StoreStab(int s , String name, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, String founder) {
        super(s,name, discountPolicy, buyPolicy, founder);
    }
    public StoreStab(int s){
        super(s,"",new DiscountPolicy(),new BuyPolicy(),"");
    }


    /////////////////////////////////////////////// Methods ///////////////////////////////////////////////////////

    public DResponseObj<ProductStore> getProductInStoreInfo(int productId){
        DResponseObj<ProductStore> output=new DResponseObj<>();
     //   output.value="yaki";
        return output;
    }

    //requirement II.2.1
    public DResponseObj<ConcurrentHashMap<Integer,ProductStore>> GetStoreProducts() {
        return new DResponseObj<>(new ConcurrentHashMap<>());
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
    public DResponseObj<Double> getProductPrice(int productId) {
        return new DResponseObj<>(8.0);
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
    public DResponseObj<List<Integer>> getTIDHistory(){
        return new DResponseObj<>(new ArrayList<>());
    }

    //requirement II.2.5
    public DResponseObj<Boolean> addHistory(int TID, String user, HashMap<Integer,Integer> products, double finalPrice) {
        return new DResponseObj<>(true);
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public DResponseObj<ConcurrentHashMap<Integer, Integer>> checkBuyPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        return new DResponseObj<>(new ConcurrentHashMap<>());
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public DResponseObj<Double> checkDiscountPolicy(String user,  ConcurrentHashMap<Integer, Integer> productsInBag){
        return new DResponseObj<>(1.0);
    }

    //requirement II.2.5
    //productsInBag <productID,quantity>
    public DResponseObj<Double> calculateBagPrice(ConcurrentHashMap<Integer, Integer> productsInBag){
        return new DResponseObj<>(8.0);
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
    public DResponseObj<List<Permission>> getPermission(){
        return new DResponseObj<>(new ArrayList<>());
    }


    /////////////////////////////////////////////// Getters and Setters /////////////////////////////////////////////


    public DResponseObj<Inventory> getInventory() {
        return null;
    }

    public DResponseObj<String> getName() {
        return new DResponseObj<>("yaki");
    }

    public DResponseObj<Boolean> isOpen() {
        return new DResponseObj<>(true);
    }

    public DResponseObj<Integer> getRate() {
        DResponseObj<Integer> i= new DResponseObj<>();
        i.value=rate;
        return i;
    }

    public DiscountPolicy getDiscountPolicy() {
        return new DiscountPolicy();

    }

    public BuyPolicy getBuyPolicy() {
        return new BuyPolicy();
    }

    public DResponseObj<String> getFounder() {
        return new DResponseObj<>("Yaki");
    }

    public DResponseObj<List<Permission>> getSafePermission() {
        return new DResponseObj<>(new ArrayList<>());
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

    public DResponseObj<ConcurrentHashMap<Integer, History>> getHistory() {
        return new DResponseObj<>(new ConcurrentHashMap<>());
    }

    public void openStoreAgain() {
        return;
    }

}
