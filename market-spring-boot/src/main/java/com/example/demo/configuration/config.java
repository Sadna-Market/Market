package com.example.demo.configuration;


import com.example.demo.configuration.StateInit.JsonReader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class config {
    private static   String init_system_env = "env.json";
    private static   String state_system = "state_system.json";

    public static boolean isMakeState=true;

    private  JsonReader jsonReader = null;

    private  JsonInit jsonInit = null;
    private static config instance=null;

    public static config get_instance(){
        if(instance==null){
            return new config();
        }
        return instance;
    }
    public config(){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File(init_system_env);
            String absolutePath = file.getAbsolutePath();
            System.out.println(absolutePath);
            jsonInit = objectMapper.readValue(new File(absolutePath), JsonInit.class);


            if(jsonInit.initState) {
                file = new File(jsonInit.statePath);
            }
            else {
                file =new File(state_system);
            }
            absolutePath = file.getAbsolutePath();
            jsonReader = objectMapper.readValue(new File(absolutePath), JsonReader.class);

        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    public JsonReader getJsonReaderState (){
        return jsonReader;
    }

    public JsonInit getJsonInit (){
        return jsonInit;
    }


}
