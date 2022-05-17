package com.example.demo.ExternalService;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class ExternalService {
    static Logger logger=Logger.getLogger(ExternalService.class);
    static List<String> names= new ArrayList<>();
    static StampedLock lock=new StampedLock();
    static ConcurrentHashMap<String,AbsExternalService> services=new ConcurrentHashMap<>();

    private static class ExternalServiceWrapper{
        static  ExternalService INSTANSE = new ExternalService();
    }
    private ExternalService(){
        services.put("Payment",PaymentService.getInstance());
        services.put("Supply",SupplyService.getInstance());
        names.add("Payment");
        names.add("Supply");
    }
    public static DResponseObj<AbsExternalService> newService(String name){
        long stamp = lock.writeLock();
        logger.debug("catch lock");
        try{
            if (names.contains(name)){
                logger.warn("this name exist in the system");
                return new DResponseObj<>(ErrorCode.EXISTEXTERNALSERVICEWITHTHISNAME);}
            names.add(name);
            AbsExternalService newService= new AbsExternalService(name);
            services.put(name,newService);
            return new DResponseObj<>(newService);
        }
        finally {
            lock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }

    public static DResponseObj<AbsExternalService> getService(String name){
        long stamp = lock.readLock();
        logger.debug("catch lock");
        try{
            if (!names.contains(name)){
                logger.warn("this name not exist in the system");
                return new DResponseObj<>(ErrorCode.NOTEXISTEXTERNALSERVICEWITHTHISNAME);}
            return new DResponseObj<>(services.get(name));
        }
        finally {
            lock.unlockRead(stamp);
            logger.debug("release lock");
        }
    }



    public static ExternalService getInstance(){
        return ExternalService.ExternalServiceWrapper.INSTANSE;
    }

    public void reset(){
        services  = new ConcurrentHashMap<>();
        names = new ArrayList<>();
        services.put("Payment",PaymentService.getInstance());
        services.put("Supply",SupplyService.getInstance());
        names.add("Payment");
        names.add("Supply");
    }
}
