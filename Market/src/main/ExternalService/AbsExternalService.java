package main.ExternalService;

import main.ErrorCode;
import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class AbsExternalService implements IExternalService {
    static Logger logger = Logger.getLogger(AbsExternalService.class);

    boolean connect=false;


    String name;
    int counterTIP=1;

    StampedLock stampedLock=new StampedLock();



    public AbsExternalService(String name) {
        this.name = name;
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
}