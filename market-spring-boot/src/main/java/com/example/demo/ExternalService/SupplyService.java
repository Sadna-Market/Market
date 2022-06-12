package com.example.demo.ExternalService;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.UserModel.User;
import org.apache.log4j.Logger;

import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
            DResponseObj<Integer> output=supplyByAPI(user.getEmail().getValue(),city,Street,apartment);
            DResponseObj<Integer> o= new DResponseObj<>();
            o.value=output.getValue();
            return o;
        }
        finally {
            stampedLock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }

    public DResponseObj<Integer> supplyByAPI(String name, String city, String street, int apartment){
        logger.debug("start Supply with API");
        if (name==null || city==null || street==null || name.equals("") || city.equals("") || street.equals("") || apartment<1)
            return new DResponseObj<>(ErrorCode.ADDRESS_NOTRIGHT);
        DResponseObj<Integer> output= new DResponseObj<>();
        Map<Object, Object> data = new HashMap<>();
        data.put("action_type","supply");
        data.put("name",name);
        data.put("address", street + " " + apartment);
        data.put("city", city);
        data.put("country", "Israel");
        data.put("zip", "801203");

        try {
            HttpResponse<String> response = sendByPost(data);
            int i=Integer.parseInt(response.body());
            if (i<=100000 && i>=10000){
                logger.info("SupplyAPI -success: " + response.body());
                output.value=i;
            }
            else{
                logger.warn("SupplyAPI - fail: "+response.body());
                output.setErrorMsg(ErrorCode.SUPPLY_FAIL);
                output.value=-1;
            }
            return output;
        }
        catch (Exception ignored){
            logger.error("SupplyAPI fail - Exception: "+ignored.getMessage());
            output.setErrorMsg(ErrorCode.SUPPLY_FAIL);
            return output;
        }
    }

    public DResponseObj<Boolean> cancelSupply(String tran_suppy){
        logger.debug("start cancel supply with API");
        if (tran_suppy==null || tran_suppy.equals(""))
            return new DResponseObj<>(ErrorCode.CANCELSUPPLYAPIFAIL);
        try{
            Integer i = Integer.parseInt(tran_suppy);
            if(i>100000 || i<10000)
                return new DResponseObj<>(ErrorCode.CANCELSUPPLYAPIFAIL);
        }
        catch (Exception e){
            return new DResponseObj<>(ErrorCode.CANCELSUPPLYAPIFAIL);
        }

        DResponseObj<Boolean> output= new DResponseObj<>();
        Map<Object, Object> data = new HashMap<>();
        data.put("action_type","cancel_supply");
        data.put("transaction_id",tran_suppy);

        try {
            HttpResponse<String> response = sendByPost(data);
            int i=Integer.parseInt(response.body());
            if (i==1){
                logger.info("cancel supplyAPI -success: " + response.body());
                output.value=true;
            }
            else{
                logger.warn("cancel supplyAPI - fail: "+response.body());
                output.setErrorMsg(ErrorCode.PAYAPIFAIL);
                output.value=false;
            }
            return output;
        }
        catch (Exception ignored){
            logger.error("cancel supplyAPI - Exception: "+ignored.getMessage());
            output.setErrorMsg(ErrorCode.HANDSHAKEFAIL);
            return output;
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
