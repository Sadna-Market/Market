package com.example.demo.DataAccess.Mappers;

import com.example.demo.DataAccess.Entity.DataShoppingBag;
import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.DataAccess.Repository.ShoppingBagRepository;
import com.example.demo.DataAccess.Repository.UserRepository;
import com.example.demo.DataAccess.Services.ShoppingBagService;
import com.example.demo.DataAccess.Services.StoreService;
import com.example.demo.DataAccess.Services.UserService;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.ShoppingBag;
import com.example.demo.Domain.UserModel.ShoppingCart;
import com.example.demo.Domain.UserModel.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserMapper {
    UserService userService ;
    ShoppingBagService shoppingBagService ;

    StoreService storeService;
    Map<String, User> users;
    private static class UserMapperWrapper {
        static UserMapper single_instance(UserService userService ,
                                          ShoppingBagService shoppingBagService ,
                                          StoreService storeService) {

         return    new UserMapper(userService, shoppingBagService, storeService);
        }
    }

    private UserMapper(UserService userService ,
                       ShoppingBagService shoppingBagService ,
                       StoreService storeService) {
        this.users = new ConcurrentHashMap<>();
    }

    public static UserMapper getInstance(UserService userService ,
                                         ShoppingBagService shoppingBagService ,
                                         StoreService storeService) {
        return UserMapperWrapper.single_instance(userService, shoppingBagService, storeService);
    }

    public User getUser(String email)
    {
        if(users.containsKey(email)){
            return users.get(email);
        }
        DataUser dataUser = userService.getUserByUsername(email);
        List<DataShoppingBag> dataShoppingBags = shoppingBagService.getUserShoppingBags(email);
        User user= convertToUserDomain(dataUser,dataShoppingBags);
        if(user == null){
            return null;
        }
        users.put(email,user);
        return user;

    }

    public Map<String,User> getAllUsers(){
        List<DataUser> dataUserList = userService.getAllUsers();
        Map<String ,User> users = new HashMap<>();
        for (DataUser dataUser: dataUserList
             ) {
            String userEmail = dataUser.getUsername();
            if(users.containsKey(dataUser.getUsername())){
                users.put(userEmail,users.get(userEmail));
            }
            else {
                List<DataShoppingBag> dataShoppingBags = shoppingBagService.getUserShoppingBags(userEmail);
                User user = convertToUserDomain(dataUser,dataShoppingBags);
                if(user == null){
                    continue;
                }
                users.put(userEmail,user);
            }
        }
        return users;
    }


    private User convertToUserDomain(DataUser dataUser,List<DataShoppingBag> dataShoppingBags){
        //make the Shopping bag hash;
        ConcurrentHashMap<Integer , ShoppingBag> shoppingBagHash = new ConcurrentHashMap<>();
        for(DataShoppingBag shoppingBag : dataShoppingBags)
        {
            ConcurrentHashMap<Integer, Integer> productQuantity =  new ConcurrentHashMap<>(shoppingBag.getProductQuantity());
            Integer storeId = shoppingBag.getShoppingBagId().getStoreId();
            if(storeId == null){
                throw new IllegalArgumentException();
            }
            Store store = StoreMapper.getInstance(storeService).getStore(storeId);
            if(store == null){
                throw new IllegalArgumentException(); //todo remove it to log and continue. need it for checks now
            }
            ShoppingBag bag = new ShoppingBag(store,dataUser.getUsername(),productQuantity);
            shoppingBagHash.put(storeId , bag);
        }
        ShoppingCart shoppingCart = new ShoppingCart(dataUser.getUsername(),shoppingBagHash);
        return new User(dataUser.getUsername(),dataUser.getPassword(),dataUser.getPhoneNumber(),dataUser.getDateOfBirth(),shoppingCart);
    }
}