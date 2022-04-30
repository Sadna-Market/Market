package main.ExternalService;

import main.ErrorCode;
import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

public class ExternalService {
    static Logger logger=Logger.getLogger(ExternalService.class);
    static List<String> names= new ArrayList<>();
    static StampedLock lock=new StampedLock();

    private static class ExternalServiceWrapper{
        static  ExternalService INSTANSE = new ExternalService();
    }
    private ExternalService(){
    }
    public static DResponseObj<AbsExternalService> newService(String name){
        long stamp = lock.writeLock();
        logger.debug("catch lock");
        try{
            if (names.contains(name)){
                logger.warn("this name exist in the system");
                return new DResponseObj<>(ErrorCode.EXISTEXTERNALSERVICEWITHTHISNAME);}
            names.add(name);
            return new DResponseObj<>(new AbsExternalService(name));
        }
        finally {
            lock.unlockWrite(stamp);
            logger.debug("release lock");
        }

    }

    public static ExternalService getInstance(){
        return ExternalService.ExternalServiceWrapper.INSTANSE;
    }
}
