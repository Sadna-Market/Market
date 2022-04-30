package main.System.Server.Domain.ExternalServices;

import main.ExternalService.CreditCard;
import main.System.Server.Domain.Market.Purchase;
import main.System.Server.Domain.UserModel.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseUnitTest {

    Purchase purchase=new Purchase();

    @Test
    public void order(){
        assertFalse(purchase.order(new User(),new CreditCard("11212223331","12/26","123")).errorOccurred());
    }
}
