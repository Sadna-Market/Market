package main.System.Server.Domain.ExternalServices;

import main.ExternalService.CreditCard;
import main.System.Server.Domain.Market.Purchase;
import main.System.Server.Domain.UserModel.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseUnitTest {

    Purchase purchase=new Purchase();
    String[] emails = {"yosi@gmail.com","kobi@gmail.com","shalom@gmai.com","aaaa"};
    String[] passwords = {"Yosi123$","Kobi123$","Shalom123$","11111"};
    String[] PhoneNum = {"0538265477","0538265477","0538265477","0538265477"};


    @Test
    public void order(){
        assertFalse(purchase.order(new User(emails[0],passwords[0],PhoneNum[0]),"aa","aa",1,new CreditCard("11212223331","12/26","123")).errorOccurred());
    }
}
