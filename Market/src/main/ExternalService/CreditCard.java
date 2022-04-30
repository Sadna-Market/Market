package main.ExternalService;

public class CreditCard {
    String CreditCard;
    String CreditDate;
    String pin;

    @Override
    public String toString() {
        return "CreditCard{" +
                "CreditCard='" + CreditCard + '\'' +
                ", CreditDate='" + CreditDate + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    public CreditCard(String creditCard, String creditDate, String pin) {
        CreditCard = creditCard;
        CreditDate = creditDate;
        this.pin = pin;
    }
}
