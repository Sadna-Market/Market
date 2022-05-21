package com.example.demo.Domain.AlertService;

public class Notification {
    public String getText() {
        return text;
    }

    public String text;

    public Notification() {
    }

    public Notification(String text) {
        this.text = text;
    }
}
