package main.ExternalService;

public class CreditCard {
    String CreditCard;
    String CreditDate;
    String pin;

    public String getCreditCard() {
        return CreditCard;
    }

    public String getCreditDate() {
        return CreditDate;
    }

    public String getPin() {
        return pin;
    }

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
