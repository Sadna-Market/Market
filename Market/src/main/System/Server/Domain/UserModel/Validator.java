package main.System.Server.Domain.UserModel;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static Validator validations=null;
    private Validator(){

    }
    public static Validator getInstance(){
        if(validations==null){
            validations=new Validator();
        }
        return validations;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        System.out.println(result+ " emailll");
        return result;
    }

    public static boolean isValidPhoneNumber(String phone) {
        String regex = "^(?=.*[0-9])"
                + ".{10}$";
        Pattern p = Pattern.compile(regex);
        if (phone == null) {
            return false;
        }

        Matcher m = p.matcher(phone);

        return m.matches();
    }

    public static boolean isValidCreditCard(String CreditCard) {
        String regex = "^(?=.*[0-9])"
                + ".{16}$";
        Pattern p = Pattern.compile(regex);
        if (CreditCard == null) {
            return false;
        }

        Matcher m = p.matcher(CreditCard);

        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isValidCreditCard("1234567891234567"));
        System.out.println(isValidCreditDate("1234"));
        System.out.println(isValidPhoneNumber("0538265477"));

    }

    public static boolean isValidCreditDate(String CreditDATE) {
        String regex = "^(?=.*[0-9])"
                + ".{4}$";
        Pattern p = Pattern.compile(regex);
        if (CreditDATE == null) {
            return false;
        }

        Matcher m = p.matcher(CreditDATE);

        return m.matches();
    }



    public static boolean isValidPassword(String password)
    {
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        if (password == null) {
            return false;
        }

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        System.out.println("passs "+ m.matches());

        // Return if the password
        // matched the ReGex
        return m.matches();
    }
}
