package main.ExternalService;

import main.System.Server.Domain.Response.DResponseObj;

//threadsafe
public class PaymentService {
    public DResponseObj<Integer> pay(CreditCard card, double v) {
        return null;
    }

    private static class PaymentServiceWrapper{
       static  PaymentService INSTANSE = new PaymentService();
   }
   private PaymentService(){}

   public static PaymentService getInstance(){
       return PaymentServiceWrapper.INSTANSE;
   }
}
