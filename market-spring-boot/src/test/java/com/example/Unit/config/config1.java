package com.example.Unit.config;

import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceObj.ServiceStore;
import com.example.demo.Service.ServiceObj.ServiceUser;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class config1 {

  Facade f= new Facade();
  List<String> members = new LinkedList<>();
    List<String> membersloged = new LinkedList<>();
    List<String> Stores = new LinkedList<>();
    @BeforeEach
    void setUP() {
        members.add("u1@gmail.com");
        members.add("u2@gmail.com");
        members.add("u3@gmail.com");
        members.add("u4@gmail.com");
        members.add("u5@gmail.com");
        membersloged.add("u1@gmail.com");
        membersloged.add("u2@gmail.com");
        membersloged.add("u3@gmail.com");
        membersloged.add("u4@gmail.com");
        Stores.add("s1");
    }


    @Test
    public void config(){
        SLResponseOBJ<String> UUID = f.guestVisit();
        SLResponseOBJ<String> res=f.login(UUID.value,"sysManager@gmail.com","Shalom123$");
        System.out.println(res.errorMsg);
        SLResponseOBJ<List<String>> res2= f.getAllMembers(res.value);
        System.out.println(res2.errorMsg);
        List<String> me=res2.value;
        for(String m :members) {
            assertTrue(me.contains(m));
        }
        SLResponseOBJ<List<ServiceUser>> loges= f.getloggedInMembers(res.value);
        List<String> log = new LinkedList<>();
        for (ServiceUser s:loges.value){
            log.add(s.email);
        }
        assertFalse(log.contains("u5@gmail.com"));
        for(String m :membersloged) {
            assertTrue(log.contains(m));
        }
        List<ServiceStore> s= f.getAllStores().value;
        List<String> STORENAMES =new LinkedList<>();
        for (ServiceStore st :s){
            STORENAMES.add(st.name);

        }
        for(String name :Stores){
            assertTrue( STORENAMES.contains(name));
        }


    }
}
