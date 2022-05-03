package main.ExternalService;


import main.System.Server.Domain.Response.DResponseObj;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

//threadsafe
public class SupplyService extends AbsExternalService{



    ConcurrentHashMap<Integer,String> list=new ConcurrentHashMap<>();
    public DResponseObj<Integer> supply(Guest user, String city, String Street, int apartment , ConcurrentHashMap<Integer,Integer> hashMap) {
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
}
