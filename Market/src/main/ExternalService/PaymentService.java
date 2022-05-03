package main.ExternalService;

import main.System.Server.Domain.Response.DResponseObj;

import java.util.concurrent.ConcurrentHashMap;

public class PaymentService extends AbsExternalService{
    ConcurrentHashMap<Integer,Double> list=new ConcurrentHashMap<>();
    static boolean isConnected =true;

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


    public static boolean isConnected(){
        return isConnected;
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
