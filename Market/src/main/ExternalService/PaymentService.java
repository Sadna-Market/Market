package main.ExternalService;

import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class PaymentService extends AbsExternalService{
    static Logger logger = Logger.getLogger(PaymentService.class);

    ConcurrentHashMap<Integer,Double> list=new ConcurrentHashMap<>();
    static boolean isConnected =true;

    public DResponseObj<Integer> pay(String cardNumber, String exp, String pin, double v) {
        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            list.put(counterTIP,v);
            logger.info("creditCart #"+createString(cardNumber,exp,pin) +" pay: "+v);
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


    private String createString(String cardNumber, String exp, String pin){
        return "CreditCard{" +
                "cardNumber='" + cardNumber + '\'' +
                ", exp='" + exp + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }
}
