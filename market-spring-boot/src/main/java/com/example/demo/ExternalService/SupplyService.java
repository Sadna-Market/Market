package com.example.demo.ExternalService;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.UserModel.User;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class SupplyService extends AbsExternalService {
    static Logger logger = Logger.getLogger(SupplyService.class);



    ConcurrentHashMap<Integer,String> list=new ConcurrentHashMap<>();
    public DResponseObj<Integer> supply(User user, String city, String Street, int apartment , ConcurrentHashMap<Integer,Integer> hashMap) {
        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            list.put(counterTIP,new Date().toString());
            logger.info("new supply");
            int output=counterTIP++;
            DResponseObj<Integer> o= new DResponseObj<>();
            o.value=output;
            return o;
        }
        finally {
            stampedLock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }




    private static class SupplyServiceWrapper{
        static  SupplyService INSTANSE = new SupplyService();
    }
    private SupplyService(){super("Supply");}

    public static SupplyService getInstance(){
        return SupplyService.SupplyServiceWrapper.INSTANSE;
    }

    public void reset(){
        super.reset();
        list = new ConcurrentHashMap<>();
        connect();
    }
}
