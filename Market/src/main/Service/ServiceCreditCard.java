package main.Service;

import main.ExternalService.CreditCard;

public class ServiceCreditCard {
        String CreditCard;
        String CreditDate;
        String pin;


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

        public ServiceCreditCard(String creditCard, String creditDate, String pin) {
            CreditCard = creditCard;
            CreditDate = creditDate;
            this.pin = pin;
        }


    public main.ExternalService.CreditCard getCreditCard(){
        return new CreditCard(this.CreditCard,this.CreditDate,this.pin);
    }

}
