package com.example.Acceptance.Obj;

import com.example.demo.Service.ServiceObj.ServiceUser;

public class User {
    public String username;
    public  String password;
    public String name;
    public String phone_number;
    public Address addr;
    public String dateOfBirth;
    public User(String name,String usern, String p, Address adr, String num, String dateOfBirth){
        username = usern;
        password = p;
        this.name = name;
        phone_number = num;
        this.dateOfBirth = dateOfBirth;
        addr = adr;
    }
    public User(ServiceUser user){
        this.username = user.email;
        this.password = user.password;
        this.name = "";
        this.phone_number = user.phone;
        this.addr = new Address(user.city,user.Street,user.apartment);
        this.dateOfBirth = user.dateOfBirth;
    }
}
