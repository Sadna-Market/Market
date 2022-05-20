package com.example.demo.Service.ServiceObj;


import com.example.demo.ExternalService.CreditCard;


public class ServiceCreditCard {
        public String creditCard;
        public String creditDate;
        public String pin;


        public String getCreditDate() {
            return creditDate;
        }

        public String getPin() {
            return pin;
        }

        @Override
        public String toString() {
            return "CreditCard{" +
                    "CreditCard='" + creditCard + '\'' +
                    ", CreditDate='" + creditDate + '\'' +
                    ", pin='" + pin + '\'' +
                    '}';
        }

        public ServiceCreditCard(String creditCard, String creditDate, String pin) {
            this.creditCard = creditCard;
            this.creditDate = creditDate;
            this.pin = pin;
        }

    public CreditCard getCreditCard(){
        return new CreditCard(this.creditCard,this.creditDate,this.pin);
    }

}
