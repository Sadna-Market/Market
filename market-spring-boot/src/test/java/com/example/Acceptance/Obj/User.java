package com.example.Acceptance.Obj;

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
}
