package com.example.demo.api.apiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class apiUser {
    public String email;
    public String Password;
    public String phoneNumber;

    public apiUser( String email, String Password, String phoneNumber){
        this.email=email;
        this.Password=Password;
        this.phoneNumber=phoneNumber;
    }
}
