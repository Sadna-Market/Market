package com.example.Acceptance.Obj;

import com.example.demo.Service.ServiceObj.ServiceHistory;
import com.example.demo.Service.ServiceObj.ServiceProductStore;
import com.example.demo.Service.ServiceObj.ServiceUser;

import java.util.ArrayList;
import java.util.List;

public class History {
    private final int TID;
    private final double finalPrice;
    private final String user;

    public History(ServiceHistory history){
        this.TID = history.getTID();
        this.finalPrice = history.getFinalPrice();
        this.user = history.getUser();
    }

    public int getTID() {
        return TID;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public String getUser() {
        return user;
    }

}
