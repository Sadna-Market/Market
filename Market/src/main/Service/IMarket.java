package main.Service;


import main.System.Server.Domain.StoreModel.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//Api of all
public interface IMarket {
    //1.1
    public SLResponsOBJ<Boolean> initMarket(String email, String Password, String phoneNumber);



    //todo i just declare all the funcs, in the futer we will change the passing args and the return value acording to the drishot.

    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public SLResponsOBJ<String> guestVisit();



    //2.1.2
    public SLResponsOBJ<Boolean> guestLeave(String guestId);

    //2.1.3
    public SLResponsOBJ<Boolean> addNewMember(String uuid, String email, String Password, String phoneNumber) ;

    //2.1.4
    public SLResponsOBJ<Boolean> login(String userid, String email, String password);

    //2.2.1
    public SLResponsOBJ<ServiceStore> getStore(int StoreID);

    public SLResponsOBJ<String> getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public SLResponsOBJ<List<Integer>> searchProductByName(String productName);

    public SLResponsOBJ<List<Integer>> searchProductByDesc(String desc);

    public SLResponsOBJ<List<Integer>> searchProductByRate(int rate);

    public SLResponsOBJ<List<Integer>> searchProductByCategory(int category);

    public SLResponsOBJ<List<Integer>> searchProductByStoreRate(int rate);

    public SLResponsOBJ<List<Integer>> searchProductByRangePrices(int productId, int min, int max);

    public SLResponsOBJ<Integer> addNewProductType(UUID uuid, String name , String description, int category);

    //2.2.3
    public SLResponsOBJ<Boolean> addProductToShoppingBag(String userId, int storeId, int productId, int quantity);

    //todo in the use case we write that the func does not takes any args. but i think it mast have user id/email to identife the correct user.
    //2.2.4.1
    public SLResponsOBJ<ServiceShoppingCard> getShoppingCart(String userId);

    //2.2.4.2
    //todo update the id of use case in the pdf its not correct, and change the description its shopping bag !not cart!
    public SLResponsOBJ<Boolean> removeProductFromShoppingBag(String userId, int storeId, int productId);    //2.2.4.3

    //todo soppingbag/sopping cart??
    public SLResponsOBJ<Boolean> setProductQuantityShoppingBag(String userId, int productId, int storeId, int quantity);

    //2.2.5
    public SLResponsOBJ<Boolean> orderShoppingCart(String userId, String city, String adress,int apartment ,ServiceCreditCard creditCard) ;

    //2.3.1
    public SLResponsOBJ<Boolean> logout(String userId);


    //2.3.2
    public SLResponsOBJ<Integer> openNewStore(String userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy);


    //2.4.1.1
    public SLResponsOBJ<Boolean> addNewProductToStore(String userId, int storeId, int productId, double price, int quantity);


    //2.4.1.2
    public SLResponsOBJ<Boolean> deleteProductFromStore(String userId, int storeId, int productId);

    //2.4.1.3
    public SLResponsOBJ<Boolean> setProductPriceInStore(String userId, int storeId, int productId, double price);

    public SLResponsOBJ<Boolean> setProductQuantityInStore(String userId, int storeId, int productId, int quantity);


    //2.4.4
    public SLResponsOBJ<Boolean> addNewStoreOwner(String UserId, int StoreId, String OwnerEmail);


    //2.4.6
    public SLResponsOBJ<Boolean> addNewStoreManger(String UserId, int StoreId, String mangerEmil);

    //2.4.7
    public SLResponsOBJ<Boolean> setManagerPermissions(String userId, int storeId, String
            mangerEmil, String per ,boolean onof);
    //2.4.9
    public SLResponsOBJ<Boolean> closeStore(String UserId, int StoreId);


    //2.4.11
    public SLResponsOBJ<HashMap<String,List<String>>> getStoreRoles(String UserId, int StoreId);



    //2.6.5 && //2.4.13
    public SLResponsOBJ<List<ServiceHistory>> getStoreOrderHistory(String UserId, int StoreId);

    public SLResponsOBJ<List<List<ServiceHistory>>> getUserInfo(String userID, String email);


    //todo 2.5 use case


}
