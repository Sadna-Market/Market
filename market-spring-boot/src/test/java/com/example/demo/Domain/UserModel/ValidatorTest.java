package com.example.demo.Domain.UserModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    @Test
    void encryptDecrypt() {
        Validator validator = Validator.getInstance();
        String message = "Hello World! This is Encrypted";
        String a = System.getenv("iv");
        String encrypted = validator.encryptAES(message);
        System.out.println(encrypted);
        String decrypted = validator.decryptAES(encrypted);
        System.out.println(decrypted);
        assertEquals(message,decrypted);
    }

}