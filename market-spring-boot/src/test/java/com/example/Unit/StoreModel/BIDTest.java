package com.example.Unit.StoreModel;

import com.example.demo.Domain.StoreModel.BID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class BIDTest {

    BID b;
    ConcurrentHashMap<String,Boolean> approves = new ConcurrentHashMap<>();
    @BeforeEach
    void setUp() {
        approves.put("niv@gmail.com",false);
        b = new BID(1,"dor@gmail.com",2,"Sony",5,100,approves);
    }

    @Test
    void approveS() {
        assertFalse(b.getApproves().get("niv@gmail.com"));
        assertTrue(b.approve("niv@gmail.com").value);
        assertTrue(b.getApproves().get("niv@gmail.com"));
        assertTrue(b.approve("niv@gmail.com").errorOccurred());
        assertTrue(b.getApproves().get("niv@gmail.com"));
    }
    @Test
    void approveF() {
        assertFalse(b.approve("l@gmail.com").value);
        assertNull(b.getApproves().get("l@gmail.com"));
    }

    @Test
    void rejectS() {
        assertEquals(b.getStatus(), BID.StatusEnum.WaitingForApprovals);
        b.reject("niv@gmail.com");
        assertEquals(b.getStatus(), BID.StatusEnum.BIDRejected);
    }


    @Test
    void counterS() {
        assertTrue(b.approve("niv@gmail.com").value);
        assertTrue(b.getApproves().get("niv@gmail.com"));
        assertEquals(b.getStatus(), BID.StatusEnum.WaitingForApprovals);
        b.counter("niv@gmail.com", 222);
        assertEquals(b.getStatus(), BID.StatusEnum.CounterBID);
        assertEquals(b.getLastPrice(),222);
        assertEquals(b.getTotalPrice(),100);
        assertTrue(b.getApproves().get("niv@gmail.com"));
    }

/*    @Test
    void counterF() {
        assertEquals(b.getStatus(), BID.StatusEnum.WaitingForApprovals);
        b.counter(22);
        assertEquals(b.getStatus(), BID.StatusEnum.WaitingForApprovals);
        assertEquals(b.getLastPrice(),100);
        assertEquals(b.getTotalPrice(),100);
    }*/

    @Test
    void responseCounterT() {
        b.responseCounter(true);
        assertEquals(b.getStatus(), BID.StatusEnum.WaitingForApprovals);
    }
    @Test
    void responseCounterF() {
        b.responseCounter(false);
        assertEquals(b.getStatus(), BID.StatusEnum.BIDRejected);
    }

    @Test
    void allApproved() {
        assertFalse(b.allApproved());
        assertTrue(b.approve("niv@gmail.com").value);
        assertTrue((b.allApproved()));
    }


    @Test
    void addManagerToListS() {
        assertEquals(1, b.getApproves().size());
        assertNull(b.getApproves().get("daniel@gmail.com"));
        b.addManagerToList("daniel@gmail.com");
        assertEquals(2, b.getApproves().size());
        assertFalse(b.getApproves().get("daniel@gmail.com"));
    }

    @Test
    void addManagerToListF() {
        assertEquals(1, b.getApproves().size());
        assertFalse(b.getApproves().get("niv@gmail.com"));
        b.addManagerToList("niv@gmail.com");
        assertEquals(1, b.getApproves().size());
        assertFalse(b.getApproves().get("niv@gmail.com"));
    }

    @Test
    void removeManagerToListS() {
        assertEquals(1, b.getApproves().size());
        b.removeManagerFromList("niv@gmail.com");
        assertEquals(0, b.getApproves().size());
        assertNull(b.getApproves().get("niv@gmail.com"));
    }

    @Test
    void removeManagerToListF() {
        assertEquals(1, b.getApproves().size());
        assertFalse(b.getApproves().get("niv@gmail.com"));
        b.removeManagerFromList("daniel@gmail.com");
        assertEquals(1, b.getApproves().size());
        assertFalse(b.getApproves().get("niv@gmail.com"));
    }
}