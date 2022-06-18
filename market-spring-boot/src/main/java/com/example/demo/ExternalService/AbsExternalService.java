package com.example.demo.ExternalService;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.configuration.config;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.StampedLock;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;

public class AbsExternalService {
    static Logger logger = Logger.getLogger(AbsExternalService.class);

    boolean connect=false;
    String URL= "https://cs-bgu-wsep.herokuapp.com/";
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();


    String name;
    int counterTIP=1;

    StampedLock stampedLock=new StampedLock();



    public AbsExternalService(String name) {

        config c = config.get_instance();
        if(c.getJsonInit().url!=null||!c.getJsonInit().url.equals("")){
            this.URL=c.getJsonInit().url;
        }
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

    public DResponseObj<Boolean> handShake(){
        logger.debug("start HandShake");
        DResponseObj<Boolean> output= new DResponseObj<>();
        Map<Object, Object> data = new HashMap<>();
        data.put("action_type","handshake");

        try {
            HttpResponse<String> response = sendByPost(data);
            if (Objects.equals(response.body(), "OK")){
            logger.info("hand shake -success: " + response.body());
            output.value=true;
            }
            else{
                logger.warn("hand shake - fail:"+response.body());
                output.value=false;
            }
            return output;
        }
        catch (Exception ignored){
            logger.error("hand shake fail - Exception: "+ignored.getMessage());
            output.setErrorMsg(ErrorCode.HANDSHAKEFAIL);
            return output;
        }
    }

    protected HttpResponse<String> sendByPost(Map<Object, Object> data) throws IOException, InterruptedException {
        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create(URL))
                .setHeader("User-Agent","Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    protected static java.net.http.HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return java.net.http.HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
