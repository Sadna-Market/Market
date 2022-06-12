package com.example.demo.configuration.StateInit;

import com.sun.xml.bind.v2.util.CollisionCheckStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonReader {
    public List<Map<String,Object>> register=new LinkedList<>();
    public List<Map<String,Object>> systemItems;
    public List<Map<String,Object>> login=new LinkedList<>();
    public Map<String,List<Map<String,String>>> stores=new HashMap<>();
    public Map<String,Map<String,List<Map<String,Object>>>> add_item=new HashMap<>();   //email->storename->{items}

    public Map<String,Map<String,List<Map<String,List<String>>>>> addManager =new HashMap<>();
    public Map<String,Map<String,List<String>>> addOwner =new HashMap<>();
    public List<String> Logoutandleave =new LinkedList<>();

    public List<Map<String,Object>> get_Register() {
        return register;
    }

    public List<Map<String, Object>> get_Login() {
        return login;
    }

    public List<Map<String,Object>> getsystemItems(){return systemItems;}

    public Map<String,List<Map<String,String>>> get_Open_store() {
        return stores;
    }

    public Map<String,Map<String,List<Map<String,Object>>>> get_Add_item() {
        return add_item;
    }


}
