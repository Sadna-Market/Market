package com.example.demo.Domain.AlertService;

import com.example.demo.DataAccess.Entity.DataNotification;

import java.time.LocalDate;

public class Notification {

    private String sentTo;
    private String text;
    private LocalDate sentDate;

    public Notification() {
    }

    public Notification(String text) {
        sentDate = LocalDate.now();
        this.text = text;
    }

    public Notification(String text, String sendTo) {
        sentDate = LocalDate.now();
        this.text = text;
        this.sentTo = sendTo;
    }

    public String getText() {
        return "[" + sentDate.toString() + "] " + text;
    }

    public DataNotification getDataObject(){
        DataNotification dataNotification = new DataNotification();
        dataNotification.setMessage(text);
        dataNotification.setSentDate(sentDate);
        dataNotification.setUsername(sentTo);
        return dataNotification;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }
}
