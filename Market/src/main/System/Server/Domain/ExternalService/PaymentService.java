package main.System.Server.Domain.ExternalService;

//threadsafe
public class PaymentService {
   private static class PaymentServiceWrapper{
       static  PaymentService INSTANSE = new PaymentService();
   }
   private PaymentService(){}

   public static PaymentService getInstance(){
       return PaymentServiceWrapper.INSTANSE;
   }
}
