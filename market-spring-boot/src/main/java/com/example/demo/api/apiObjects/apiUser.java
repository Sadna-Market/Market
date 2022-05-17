package com.example.demo.api.apiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class apiUser {
        public String email;
        public String Password;
        public String phoneNumber;
        public String city;
        public String adress;
        public int apartment;
        public String CreditCard;
        public String CreditDate;
        public String pin;

        public apiUser( String email, String Password, String phoneNumber,String city, String adress
                , int apartment
                , String CreditCard
                , String CreditDate
                , String pin){
            this.email=email;
            this.Password=Password;
            this.phoneNumber=phoneNumber;
            this.city=city;
            this.adress=adress;
            this.apartment=apartment;
            this.CreditCard=CreditCard;
            this.CreditDate=CreditDate;
            this.pin=pin;
        }
    }
