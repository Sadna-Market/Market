package main.ExternalService;


import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.User;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

//threadsafe
public class SupplyService extends AbsExternalService{

    ConcurrentHashMap<Integer,String> list=new ConcurrentHashMap<>();
    public DResponseObj<String> supply(User user,String city, String Street, int apartment , ConcurrentHashMap<Integer,Integer> hashMap) {
        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            list.put(counterTIP,new Date().toString());
            logger.info("new supply");
            int output=counterTIP++;
            return new DResponseObj("ok",-1);
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
