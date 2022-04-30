package main.System.Server.Domain.Market;

import main.ErrorCode;
import main.ExternalService.CreditCard;
import main.ExternalService.PaymentService;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Purchase {
    static Logger logger=Logger.getLogger(Purchase.class);


    public DResponseObj<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> order(User user, CreditCard c){
        //check card
        //DResponseObj<Boolean> creditcard=checkCard(c);
        //if (creditcard.errorOccurred()) return new DResponseObj<>(creditcard.getErrorMsg());


        //init
        DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>> bagsRes=getBags(user);
        if (bagsRes.errorOccurred()) return new DResponseObj<>(bagsRes.getErrorMsg());
        DResponseObj<String> DRemail =user.getEmail();
        if (DRemail.errorOccurred()) return new DResponseObj<>(DRemail.getErrorMsg());
        String email=DRemail.getValue();
        ConcurrentHashMap<Integer, ShoppingBag> bags=bagsRes.getValue();
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> output=new ConcurrentHashMap<>();


         for (Integer i: bags.keySet()){
             ShoppingBag currBag = bags.get(i);
             DResponseObj<Store> getStore = currBag.getStore();
             DResponseObj<ConcurrentHashMap<Integer, Integer>> checkBags=currBag.getProductQuantity();
             if (!getStore.errorOccurred() && !checkBags.errorOccurred()) {
                 ConcurrentHashMap<Integer, Integer> crrAmount = catchItems(getStore.getValue(), checkBags.getValue());

                 //check abount Discount
                 DResponseObj<ConcurrentHashMap<Integer, Integer>> DRpolicy = getStore.value.checkBuyPolicy(email,crrAmount);
                 Double discount=0.;
                 if (!DRpolicy.errorOccurred()){
                     DResponseObj<Double> DRdiscount = getStore.value.checkDiscountPolicy(email,DRpolicy.getValue());
                     if (!DRdiscount.errorOccurred()) discount =DRdiscount.getValue();
                 }
                 //rollback if pay or price didnt work
                 DResponseObj<Double> price = getStore.value.calculateBagPrice(crrAmount);
                 if (price.errorOccurred()) rollBack(getStore.getValue(),crrAmount);
                 else{
                     //supply
                     PaymentService p=PaymentService.getInstance();
                     DResponseObj<Integer> TIP=p.pay(c,price.getValue()-discount);
                     if (TIP.errorOccurred()) rollBack(getStore.getValue(),crrAmount);
                     else{
                         getStore.getValue().addHistory(TIP.getValue(),email,crrAmount,price.getValue()-discount);
                         output.put(i,crrAmount);
                         logger.info("this order success");
                     }
                 }
             }
         }
        return new DResponseObj<>(output);
    }

    //goal: to get storeID and Shopping bag of this store.
    private DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>>  getBags(User user){
        DResponseObj<ShoppingCart> getCart =user.GetSShoppingCart();
        if (getCart.errorOccurred()) return new DResponseObj<>(getCart.getErrorMsg());
        DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>> getBags = getCart.getValue().getHashShoppingCart();
        if (getBags.errorOccurred()) return new DResponseObj<>(getBags.getErrorMsg());
        return new DResponseObj(getBags.getValue());
    }

    //catch the items and understand how much we took.
    private ConcurrentHashMap<Integer, Integer> catchItems(Store store,ConcurrentHashMap<Integer, Integer> hashMap){
        ConcurrentHashMap<Integer, Integer> output = new ConcurrentHashMap<>();
        for (Integer j: hashMap.keySet()){
            Integer amount = hashMap.get(j);
            DResponseObj<Integer> amonutCanBuy = store.setProductQuantityForBuy(j,amount);
            if (!amonutCanBuy.errorOccurred()) output.put(j,amonutCanBuy.getValue());
        }
        return output;
    }

    private DResponseObj<Boolean> rollBack(Store store,ConcurrentHashMap<Integer, Integer> hashMap){
        logger.warn("rool back");
        DResponseObj<Boolean> ans=store.rollbackProductQuantity(hashMap);
        return new DResponseObj<>(ErrorCode.ROLLBACK);
    }

    private DResponseObj<Boolean> checkCard(CreditCard c){
        if (!c.getCreditCard().matches("^[0-9]+$")) return new DResponseObj<>(ErrorCode.ILLEGALCARD);
        //if (!c.getCreditDate().matches("^[0-1][0-9]$")) return new DResponseObj<>(ErrorCode.ILLEGALCARD);
        if (!c.getPin().matches("^[1-9]&&[0-9]&&[0-9]]")) return new DResponseObj<>(ErrorCode.ILLEGALCARD);
        return new DResponseObj<>(true);
    }


}
