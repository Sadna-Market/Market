package com.example.demo.DataAccess.Services;

import com.example.Acceptance.Obj.ATResponseObj;
import com.example.Acceptance.Obj.Address;
import com.example.Acceptance.Obj.CreditCard;
import com.example.Acceptance.Obj.History;
import com.example.Acceptance.Obj.ItemDetail;
import com.example.demo.DataAccess.Entity.*;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.*;
import com.example.demo.Domain.UserModel.User;
import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceObj.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class HistoryServiceTest {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private ProductStoreService productStoreService;

    public int IPHONE_5_ID;
    public int IPHONE_6_ID;
    public int SCREEN_FULL_HD_ID;
    public int GALAXY_ID;
    private User member;
    private int existing_storeID;

    @Autowired
    Facade market;

    @Test
    void purchaseCart_Success() {
        //pre conditions
        populateItemsAndStore();
        String uuid = market.guestVisit().value;
        uuid = market.login(uuid,member.getEmail().value,"Shalom123$").value;
        market.addProductToShoppingBag(uuid,existing_storeID,IPHONE_5_ID,1);
        market.addProductToShoppingBag(uuid,existing_storeID,IPHONE_6_ID,1);

        CreditCard creditCard = new CreditCard("1111222233334444", "11/23", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        var res = market.orderShoppingCart(uuid,"sss","aaa",1,new ServiceCreditCard(creditCard.num,creditCard.expire,creditCard.cvv));
        assertFalse(res.errorOccurred());

        var sh = historyService.getAllHistoryByStoreId(existing_storeID);
        var uh = historyService.getAllHistoryByUsername(member.getEmail().value);

    }


    protected void populateItemsAndStore() {
        String uuid = market.guestVisit().value;
        uuid = market.login(uuid, "sysManager@gmail.com", "Shalom123$").value;
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        ItemDetail item3 = new ItemDetail("screenFULLHD", 3, 10, List.of("TV"), "screen");
        ItemDetail item4 = new ItemDetail("galaxyS10", 1, 10, List.of("phone"), "phone");
        IPHONE_5_ID = market.addNewProductType(uuid, "iphone5", "phone", 1).value;
        IPHONE_6_ID = market.addNewProductType(uuid, "iphone6", "phone", 1).value;
        SCREEN_FULL_HD_ID = market.addNewProductType(uuid, "SCREEN_FULL_HD_ID", "screen", 2).value;
        GALAXY_ID = market.addNewProductType(uuid, "GALAXY_ID", "phone", 1).value;
        uuid = market.logout(uuid).value;


        member = new User("niv@gmail.com", "SHalom123$", "0523222222", LocalDate.of(1993, 11, 11));
        var res = market.addNewMember(uuid,member.getEmail().value,"Shalom123$","0666666666","11/11/1993");
        assertFalse(res.errorOccurred());
        uuid = market.login(uuid, member.getEmail().value, "Shalom123$").value;
        existing_storeID = market.openNewStore(uuid, "moshe", member.getEmail().value,
                new ServiceDiscountPolicy(),
                new ServiceBuyPolicy(),
                new ServiceBuyStrategy()).value;
        market.addNewProductToStore(uuid,existing_storeID,IPHONE_5_ID,10,1);
        market.addNewProductToStore(uuid,existing_storeID,IPHONE_6_ID,60,1);
        market.addNewProductToStore(uuid,existing_storeID,SCREEN_FULL_HD_ID,10,3);
        uuid = market.logout(uuid).value;
    }
}