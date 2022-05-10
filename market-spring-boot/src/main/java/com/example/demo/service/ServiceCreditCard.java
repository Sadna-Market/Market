package com.example.demo.service;


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


}
