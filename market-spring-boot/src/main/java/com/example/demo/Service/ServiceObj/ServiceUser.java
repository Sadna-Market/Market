package com.example.demo.Service.ServiceObj;


import com.example.demo.Domain.UserModel.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServiceUser {
    public String email;
    public String password;
    public String phone;
    public String city;
    public String Street;
    public int apartment;
    public String dateOfBirth;
    public ServiceUser(String email, String password, String phone, String city, String street, int apartment, String dateOfBirth) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.Street = street;
        this.apartment = apartment;
        this.dateOfBirth = dateOfBirth;
    }

    public User getUser(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDateObj = LocalDate.parse(dateOfBirth, dateTimeFormatter);  //String to LocalDate
        return new User(email,password,phone, localDateObj);
    }


}
