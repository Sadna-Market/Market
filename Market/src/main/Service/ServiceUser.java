package main.Service;

import main.System.Server.Domain.UserModel.User;

public class ServiceUser {
    String email;
    String password;
    String phone;
    String city;
    String Street;
    int apartment;
    public ServiceUser(String email, String password, String phone, String city, String street, int apartment) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        Street = street;
        this.apartment = apartment;
    }


    public User getUser(){
        return new User(email,password,phone);
    }
}
