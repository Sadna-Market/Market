package com.example.demo.ExternalService;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.concurrent.locks.StampedLock;

public class AbsExternalService {
    static Logger logger = Logger.getLogger(AbsExternalService.class);

    boolean connect=false;


    String name;
    int counterTIP=1;

    StampedLock stampedLock=new StampedLock();



    public AbsExternalService(String name) {
        this.name = name;
    }

    public DResponseObj<Boolean> isConnect() {
        long stamp= stampedLock.readLock();
        logger.debug("catch lock");
        try{
            boolean output=connect;
            return new DResponseObj<>(output,!output? ErrorCode.SERVICE_NOT_CONNECTED:-1);
        }
        finally {
            stampedLock.unlockRead(stamp);
            logger.debug("release lock");
        }
    }




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
            stampedLock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }

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
            stampedLock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }

    public DResponseObj<String> ping() {
        return new DResponseObj<>(name);
    }

    public void reset(){
        counterTIP = 1;
    }
}
