package main.ExternalService;

import main.ErrorCode;
import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

//threadsafe
public class PaymentService implements IExternalService{
    static Logger logger = Logger.getLogger(PaymentService.class);

    boolean connect=false;
    String name;
    int counterTIP=1;
    ConcurrentHashMap<Integer,Double> list=new ConcurrentHashMap<>();
    StampedLock stampedLock=new StampedLock();

    public PaymentService(String name) {
        this.name = name;
    }

    public DResponseObj<Integer> pay(CreditCard card, double v) {
        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            list.put(counterTIP,v);
            logger.info("creditCart #"+card.toString() +" pay: "+v);
            int output=counterTIP++;
            return new DResponseObj<>(output,-1);
        }
        finally {
            stampedLock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }

    @Override
    public DResponseObj<Boolean> isConnect() {
        long stamp= stampedLock.readLock();
        logger.debug("catch lock");
        try{
            boolean output=connect;
            return new DResponseObj<>(output);
        }
        finally {
            stampedLock.unlockRead(stamp);
            logger.debug("release lock");
        }
    }

    @Override
    public DResponseObj<Boolean> connect() {
        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            if (connect) {
                logger.warn("this service already connect");
                return new DResponseObj<>(ErrorCode.SERVICEALREADYCONNECT);
            }
            else{
                connect=true;
                return new DResponseObj<>(true);
            }
        }
        finally {
            stampedLock.unlockRead(stamp);
            logger.debug("release lock");
        }
    }

    @Override
    public DResponseObj<Boolean> disConnect() {
        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            if (!connect) {
                logger.warn("this service already DisConnect");
                return new DResponseObj<>(ErrorCode.SERVICEALREADYDISCONNECT);
            }
            else{
                connect=false;
                return new DResponseObj<>(true);
            }
        }
        finally {
            stampedLock.unlockRead(stamp);
            logger.debug("release lock");
        }
    }

    @Override
    public DResponseObj<String> ping() {
        return new DResponseObj<>(name);
    }

    private static class PaymentServiceWrapper{
       static  PaymentService INSTANSE = new PaymentService();
   }
   private PaymentService(){}

   public static PaymentService getInstance(){
       return PaymentServiceWrapper.INSTANSE;
   }
}
