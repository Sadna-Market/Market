package main.ExternalService;

public class CreditCard {
    final String cardNumber;
    final String exp;
    final String pin;

    public CreditCard(String cardNumber, String exp, String pin) {
        this.cardNumber = cardNumber;
        this.exp = exp;
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExp() {
        return exp;
    }

    public String getPin() {
        return pin;
    }
}
