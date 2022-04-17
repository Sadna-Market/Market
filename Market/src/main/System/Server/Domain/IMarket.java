package main.System.Server.Domain;


import main.System.Server.Domain.Market.BuyPolicy;
import main.System.Server.Domain.Market.BuyStrategy;
import main.System.Server.Domain.Market.DiscountPolicy;
import main.System.Server.Domain.UserComponent.Response.ProductResponse;
import main.System.Server.Domain.UserComponent.Response.ShoppingCartResponse;
import main.System.Server.Domain.UserComponent.Response.StoreResponse;

import java.util.List;

//Api of all
public interface IMarket {
    //1.1
    public boolean InitMarket();

    //todo i just declare all the funcs, in the futer we will change the passing args and the return value acording to the drishot.

    // 2.1.1 when a user enter to the system he recognized us a guest visitor
    public boolean GuestVisit();

    //2.1.2
    public boolean GuestLeave(int guestId);

    //2.1.3
    public boolean AddNewMember(String email, int Password);

    //2.1.4
    public boolean Login(String email , int password);

    //2.2.1
    public StoreResponse GetStoreInfo(int StoreID);

    //2.2.2
    public List<ProductResponse> ProductSearch(String productName, String category);

    //2.2.3
    public boolean AddProductToShoppingBag(int userId,int storeId,int productId , int quantity) ;
    //todo in the use case we write that the func does not takes any args. but i think it mast have user id/email to identife the correct user.
    //2.2.4.1
    public ShoppingCartResponse GetShoppingCart(int userId);

    //2.2.4.2
    //todo update the id of use case in the pdf its not correct, and change the description its shopping bag !not cart!
    public boolean RemoveProductFromShoppingBag(int userId,int storeId, int productId);
    //2.2.4.3
    //todo soppingbag/sopping cart??
    public boolean setProductQuantityShoppingBag(int userId, int productId, int storeId,int quantity);

    //2.2.5
    public boolean orderShoppingCart(int userId);

    //2.3.1
    public boolean Logout(int userId);


    //2.3.2
    public boolean OpenNewStore(int userId, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy);


    //2.4.1.1
    public boolean AddNewProductToStore(int productId,String productName,String categori, double price, int quantity,String description);


    //2.4.1.2
    public boolean DeleteProductFromStore(int UserId ,int productId);

    //2.4.1.3
    public boolean SetProductInSore(int UserId, int productId);


    //2.4.4
    public boolean AddNewStoreOwner(int UserId,int StoreId, int newOwnerId);


    //2.4.6
    public  boolean AddNewStoreManger(int UserId, int StoreId, int newMangerId);

    //2.4.7
    public boolean SetMangerPermissions(int UserId, int StoreId, int ManagerId);

    //2.4.9
    public boolean DeleteStore(int UserId, int StoreId);


    //2.4.11
    public boolean getStoreRoles(int UserId, int StoreId);


    //2.6.5 && //2.4.13
    public boolean getStoreOrderHistory(int UserId,int StoreId);


    //todo 2.5 use case







}
