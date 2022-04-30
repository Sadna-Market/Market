package main.Service;

import main.System.Server.Domain.UserModel.User;

public class ServiceUser {
    String email;
    String password;
    String phone;
    String city;
    String Street;
    int apartment;
    public User getUser(){
        return new User(email,password,phone);
    }
}
