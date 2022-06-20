package com.example.demo.ExternalService;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService extends AbsExternalService{
    static Logger logger = Logger.getLogger(PaymentService.class);

    ConcurrentHashMap<Integer,Double> list=new ConcurrentHashMap<>();
    static boolean isConnected =true;



    public DResponseObj<Integer> pay(String cardNumber, String exp, String pin, double v) {
        DResponseObj<Boolean> handShake = handShake();
        if(handShake.errorOccurred()){
            DResponseObj<Integer> output=new DResponseObj<>();
            output.setErrorMsg(handShake.errorMsg);
            return output;
        }
        if(!handShake.getValue()){
            DResponseObj<Integer> output=new DResponseObj<>();
            output.setErrorMsg(ErrorCode.HANDSHAKE_BEFOREPAY_FAIL);
            return output;
        }

        long stamp= stampedLock.writeLock();
        logger.debug("catch lock");
        try{
            list.put(counterTIP,v);
            logger.info("creditCart #"+createString(cardNumber,exp,pin) +" pay: "+v);
            DResponseObj<Integer> output = payByAPI(cardNumber,exp,pin,v);
            if (output.errorOccurred()) return output;
            return new DResponseObj<>(output.getValue(),-1);
        }
        finally {
            stampedLock.unlockWrite(stamp);
            logger.debug("release lock");
        }
    }

    public DResponseObj<Integer> payByAPI(String cardNumber, String exp, String pin, double v){
        logger.debug("start PAy with API");
        if (cardNumber==null || exp==null || pin==null || pin.length()!=3 || exp.length()!=5 || cardNumber.equals(""))
            return new DResponseObj<>(ErrorCode.CARD_NOTRIGHT);
        if(pin.equals("984"))
            return new DResponseObj<>(ErrorCode.UNEXPECTED984);
        if(pin.equals("986"))
            return new DResponseObj<>(ErrorCode.UNEXPECTED986);
        String[] splited = exp.split("/");
        if(splited.length!=2)
            return new DResponseObj<>(ErrorCode.CARD_NOTRIGHT);
        DResponseObj<Integer> output= new DResponseObj<>();
        Map<Object, Object> data = new HashMap<>();
        data.put("action_type","pay");
        data.put("card_number",cardNumber);
        data.put("month",splited[0]);
        data.put("year",splited[1]);
        data.put("holder","Rami Pozis");
        data.put("ccv",pin);
        data.put("id", "321157117");

        try {
            HttpResponse<String> response = sendByPost(data);
            int i=Integer.parseInt(response.body());
            if (i<=100000 && i>=10000){
                logger.info("payAPI -success: " + response.body());
                output.value=i;
            }
            else{
                logger.warn("payAPI - fail: "+response.body());
                output.setErrorMsg(ErrorCode.PAYAPIFAIL);
                output.value=-1;
            }
            return output;
        }
        catch (Exception ignored){
            logger.error("pay fail - Exception: "+ignored.getMessage());
            output.setErrorMsg(ErrorCode.HANDSHAKEFAIL);
            return output;
        }
    }

    public DResponseObj<Boolean> cancelPay(String tran_pay){
        logger.debug("start cancel pay with API");
        if (tran_pay==null || tran_pay.equals(""))
            return new DResponseObj<>(ErrorCode.CANCELPAYAPIFAIL);
        try{
            Integer i = Integer.parseInt(tran_pay);
            if(i>100000 || i<10000)
                return new DResponseObj<>(ErrorCode.CANCELPAYAPIFAIL);
        }
        catch (Exception e){
            return new DResponseObj<>(ErrorCode.CANCELPAYAPIFAIL);
        }

        DResponseObj<Boolean> output= new DResponseObj<>();
        Map<Object, Object> data = new HashMap<>();
        data.put("action_type","cancel_pay");
        data.put("transaction_id",tran_pay);

        try {
            HttpResponse<String> response = sendByPost(data);
            int i=Integer.parseInt(response.body());
            if (i==1){
                logger.info("payAPI -success: " + response.body());
                output.value=true;
            }
            else{
                logger.warn("payAPI - fail: "+response.body());
                output.setErrorMsg(ErrorCode.PAYAPIFAIL);
                output.value=false;
            }
            return output;
        }
        catch (Exception ignored){
            logger.error("pay fail - Exception: "+ignored.getMessage());
            output.setErrorMsg(ErrorCode.HANDSHAKEFAIL);
            return output;
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

    public void reset(){
        super.reset();
        list = new ConcurrentHashMap<>();
        isConnected = true;
    }
}
