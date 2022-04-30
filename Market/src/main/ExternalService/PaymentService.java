package main.ExternalService;

import main.ErrorCode;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

//threadsafe
public class PaymentService extends AbsExternalService{
    ConcurrentHashMap<Integer,Double> list=new ConcurrentHashMap<>();

    public DResponseObj<Integer> pay(CreditCard card, double v) {
        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            list.put(counterTIP,v);
            logger.info("creditCart #"+card.toString() +" pay: "+v);
            int output=counterTIP++;
            return new DResponseObj<>(output-1,-1);
        }
        finally {
            stampedLock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }


    private static class PaymentServiceWrapper{
       static  PaymentService INSTANSE = new PaymentService();
   }
   private PaymentService(){
        super("Payment");
   }

   public static PaymentService getInstance(){
       return PaymentServiceWrapper.INSTANSE;
   }
}
