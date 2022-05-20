package com.example.Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class ConcurrentAssignOwnerTest extends MarketTests {
    String uuid;
    Boolean owner1Success = null;
    Boolean owner2Success = null;

    @BeforeEach
    public void setUp() {
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
        uuid = market.guestVisit();
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory();
        market = null; //for garbage collector
    }

    @Test
    @DisplayName("assign manager at the same time")
    public void assignSameTime() throws InterruptedException {
        User anotherOwner = generateUser();
        User toBeOwner = generateUser();
        assertTrue(market.register(uuid, anotherOwner.username, anotherOwner.password,anotherOwner.dateOfBirth));
        assertTrue(market.register(uuid, toBeOwner.username, toBeOwner.password,toBeOwner.dateOfBirth));

        //login as member
        ATResponseObj<String> memberID = market.login(uuid, member);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.assignNewOwner(uuid, existing_storeID, anotherOwner));
        uuid = market.logout(uuid).value;

        //action
        Thread owner1 = new Thread(() -> {
            ATResponseObj<String> id = market.login(market.guestVisit(), member);
            owner1Success = market.assignNewManager(id.value, existing_storeID, toBeOwner);
        });
        Thread owner2 = new Thread(() -> {
            ATResponseObj<String> id = market.login(market.guestVisit(), anotherOwner);
            owner2Success = market.assignNewManager(id.value, existing_storeID, toBeOwner);
        });
        owner2.start();
        owner1.start();
        owner2.join();
        owner1.join();
        assertTrue(owner1Success ^ owner2Success);


    }
}
