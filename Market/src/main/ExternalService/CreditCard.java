package main.ExternalService;

import main.System.Server.Domain.Response.DResponseObj;

public class CreditCard {
    final String cardNumber;
    final String exp;
    final String pin;

    public CreditCard(String cardNumber, String exp, String pin) {
        this.cardNumber = cardNumber;
        this.exp = exp;
        this.pin = pin;
    }

    public DResponseObj<String> getCardNumber() {
        return new DResponseObj<>(cardNumber);
    }

    public DResponseObj<String> getExp() {
        return new DResponseObj<>(exp);
    }

    public DResponseObj<String> getPin() {
        return new DResponseObj<>(pin);
    }
}
