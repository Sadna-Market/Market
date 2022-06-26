package com.example.demo.DataAccess.Entity;

public class DataStatistics {

    private String currentDate;

    private int guestCounter;

    private int registeredCounter;

    private int managerCounter;

    private int ownerCounter;

    private int adminCounter;

    public DataStatistics(){
    }

    public DataStatistics(String currentDate, int guestCounter, int registeredCounter, int managerCounter, int ownerCounter, int adminCounter) {
        this.currentDate = currentDate;
        this.guestCounter = guestCounter;
        this.registeredCounter = registeredCounter;
        this.managerCounter = managerCounter;
        this.ownerCounter = ownerCounter;
        this.adminCounter = adminCounter;
    }



    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getGuestCounter() {
        return guestCounter;
    }

    public void setGuestCounter(int guestCounter) {
        this.guestCounter = guestCounter;
    }

    public int getRegisteredCounter() {
        return registeredCounter;
    }

    public void setRegisteredCounter(int registeredCounter) {
        this.registeredCounter = registeredCounter;
    }

    public int getManagerCounter() {
        return managerCounter;
    }

    public void setManagerCounter(int managerCounter) {
        this.managerCounter = managerCounter;
    }

    public int getOwnerCounter() {
        return ownerCounter;
    }

    public void setOwnerCounter(int ownerCounter) {
        this.ownerCounter = ownerCounter;
    }

    public int getAdminCounter() {
        return adminCounter;
    }

    public void setAdminCounter(int adminCounter) {
        this.adminCounter = adminCounter;
    }
}
