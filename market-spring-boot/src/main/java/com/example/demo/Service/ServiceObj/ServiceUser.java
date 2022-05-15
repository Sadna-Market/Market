package com.example.demo.Service.ServiceObj;


import com.example.demo.Domain.UserModel.User;

public class ServiceUser {
    public String email;
    public String password;
    public String phone;
    public String city;
    public String Street;
    public int apartment;
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
