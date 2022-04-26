package main.Service;


import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.*;
import main.System.Server.Domain.UserModel.Response.ProductResponse;
import main.System.Server.Domain.UserModel.Response.ShoppingCartResponse;
import main.System.Server.Domain.UserModel.Response.StoreResponse;

import java.util.List;
import java.util.UUID;

//Api of all
public interface IMarket {
    //1.1
    public boolean InitMarket();

    //todo i just declare all the funcs, in the futer we will change the passing args and the return value acording to the drishot.

    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public UUID GuestVisit();

    //2.1.2
    public boolean GuestLeave(UUID guestId);

    //2.1.3
    public boolean AddNewMember(UUID uuid,String email, String Password,String phoneNumber,String CreditCared,String CreditDate);

    //2.1.4
    public boolean Login(UUID userid, String email, String password);

    //2.2.1
    public Store getStore(int StoreID);
    public String getInfoProductInStore(int storeID, int productID);

    //2.2.2
    public List<Integer> searchProductByName(String productName);
    public List<Integer> searchProductByDesc(String desc);
    public List<Integer> searchProductByRate(int rate);
    public List<Integer> searchProductByCategory(int category);
    public List<Integer> searchProductByStoreRate(int rate);
    public List<Integer> searchProductByRangePrices(int productId,int min,int max);


    //2.2.3
    public boolean AddProductToShoppingBag(UUID userId,int storeId,int productId , int quantity);
    //todo in the use case we write that the func does not takes any args. but i think it mast have user id/email to identife the correct user.
    //2.2.4.1
    public ShoppingCartResponse GetShoppingCart(UUID userId);
    //2.2.4.2
    //todo update the id of use case in the pdf its not correct, and change the description its shopping bag !not cart!
    public boolean RemoveProductFromShoppingBag(UUID userId,int storeId, int productId);    //2.2.4.3
    //todo soppingbag/sopping cart??
    public boolean setProductQuantityShoppingBag(UUID userId, int productId, int storeId,int quantity);
    //2.2.5
    public boolean orderShoppingCart(UUID userId);

    //2.3.1
    public boolean Logout(UUID userId);


    //2.3.2
    public boolean OpenNewStore(UUID userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy);


    //2.4.1.1
    public boolean AddNewProductToStore(UUID userId, int storeId, int productId, double price, int quantity) ;


    //2.4.1.2
    public boolean DeleteProductFromStore(UUID userId, int storeId, int productId);

    //2.4.1.3
    public boolean setProductPriceInStore(UUID userId, int storeId, int productId, double price) ;
    public boolean setProductQuantityInStore(UUID userId, int storeId, int productId,  int quantity);


    //2.4.4
    public boolean AddNewStoreOwner(UUID UserId, int StoreId, String OwnerEmail) ;


    //2.4.6
    public boolean AddNewStoreManger(UUID UserId, int StoreId,  String mangerEmil) ;

    //2.4.7
    public boolean SetMangerPermissions(UUID UserId, int StoreId, String mangerEmil, permissionType.permissionEnum per);

    //2.4.9
    public boolean closeStore(UUID UserId, int StoreId);


    //2.4.11
    public boolean getStoreRoles(int UserId, int StoreId);


    //2.6.5 && //2.4.13
    public List<History> getStoreOrderHistory(UUID UserId, int StoreId) ;


    //todo 2.5 use case







}
