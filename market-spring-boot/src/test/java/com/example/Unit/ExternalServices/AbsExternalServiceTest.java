package com.example.Unit.ExternalServices;

import com.example.demo.ExternalService.AbsExternalService;
import com.example.demo.ExternalService.PaymentService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbsExternalServiceTest {

    AbsExternalService test = PaymentService.getInstance();

    @Test
    void handShake() {
        for (int i=0; i<10;i++){
            assertTrue(test.handShake().getValue());
        }
    }
}