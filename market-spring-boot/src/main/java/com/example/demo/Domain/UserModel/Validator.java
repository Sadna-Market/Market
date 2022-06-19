package com.example.demo.Domain.UserModel;

import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final Logger logger = Logger.getLogger(Validator.class);
    private static Validator validations=null;
    private static final String INITIAL_VECTOR = System.getenv("iv");
    private static final String SECRET = System.getenv("secret");
    private Validator(){

    }
    public static Validator getInstance(){
        if(validations==null){
            validations=new Validator();
        }
        return validations;
    }

    public static DResponseObj<Boolean> isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
       // System.out.println(result+ " emailll");
        return new DResponseObj<>( result,-1);
    }

    public static DResponseObj<Boolean> isValidPhoneNumber(String phone) {
        String regex = "^(?=.*[0-9])"
                + ".{10}$";
        Pattern p = Pattern.compile(regex);
        if (phone == null||phone=="") {
            return new DResponseObj<>(false);
        }

        Matcher m = p.matcher(phone);

        return new DResponseObj<>(m.matches());
    }

    public static DResponseObj<Boolean> isValidPin(String pin) {
        String regex = "^(?=.*[0-9])"
                + ".{3}$";
        Pattern p = Pattern.compile(regex);
        if (pin == null||pin=="") {
            return new DResponseObj<>(false);
        }

        Matcher m = p.matcher(pin);

        return new DResponseObj<>(m.matches());
    }

    public static DResponseObj<Boolean> isValidCreditCard(String CreditCard) {
        String regex = "^(?=.*[0-9])"
                + ".{16}$";
        Pattern p = Pattern.compile(regex);
        if (CreditCard == null||CreditCard=="") {
            return new DResponseObj<>(false);
        }

        Matcher m = p.matcher(CreditCard);

        return new DResponseObj<>(m.matches());
    }

    public static void main(String[] args) {
        System.out.println(isValidCreditCard("1234567891234567"));
        System.out.println(isValidCreditDate("1234"));
        System.out.println(isValidPhoneNumber("0538265477"));

    }

    public static DResponseObj<Boolean> isValidCreditDate(String CreditDATE) {
        String regex = "^(0[1-9]|1[0-2]){1}\\/?([0-9]{2})$";
        Pattern p = Pattern.compile(regex);
        if (CreditDATE == null||CreditDATE=="") {
            return new DResponseObj<>(false);
        }

        Matcher m = p.matcher(CreditDATE);

        return new DResponseObj<>(m.matches());
    }



    public static DResponseObj<Boolean> isValidPassword(String password)
    {
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[!@#&()–[{}]:;',?/*~$^+=<>])"
                + "(?=\\S+$).{8,20}$";
        if (password == null||password=="") {
            return new DResponseObj<>(false,-1);
        }

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return new DResponseObj<>( false,-1);
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

       // System.out.println("passs "+ m.matches());

        // Return if the password
        // matched the ReGex
        return new DResponseObj<>( m.matches(),-1);
    }
    public String encryptAES(String plaintText){
        try{
            IvParameterSpec iv = new IvParameterSpec(INITIAL_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8),"AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,iv);
            byte[] encrypted = cipher.doFinal(plaintText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        }catch (Exception e){
            logger.error(String.format("failed to encrypt password, ERROR: %s",e.getMessage()));
            return plaintText;
        }
    }
    public String decryptAES(String encrypted){
        try{
            IvParameterSpec iv = new IvParameterSpec(INITIAL_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8),"AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        }catch (Exception e){
            logger.error(String.format("failed to decrypt password, ERROR: %s",e.getMessage()));
            return encrypted;
        }
    }
}
