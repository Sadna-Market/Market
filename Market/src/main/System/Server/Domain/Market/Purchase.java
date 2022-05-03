package main.System.Server.Domain.Market;

import main.ErrorCode;
import main.ExternalService.CreditCard;
import main.ExternalService.PaymentService;
import main.ExternalService.SupplyService;
import main.System.Server.Domain.StoreModel.History;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Purchase {
    static Logger logger=Logger.getLogger(Purchase.class);



    public DResponseObj<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> order(Guest user,String email, String city , String Street, int apartment , CreditCard c){
        //check card
        //DResponseObj<Boolean> creditcard=checkCard(c);
        //if (creditcard.errorOccurred()) return new DResponseObj<>(creditcard.getErrorMsg());
        //init
        DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>> bagsRes=getBags(user);
        if (bagsRes.errorOccurred()) return new DResponseObj<>(bagsRes.getErrorMsg());
        ConcurrentHashMap<Integer, ShoppingBag> bags=bagsRes.getValue();


        double totalPrice=0.;
        ConcurrentHashMap<Store, ConcurrentHashMap<Integer, Integer>> totalProducts=new ConcurrentHashMap<>();
        List<Integer> totalSupply = new ArrayList<>();
        ConcurrentHashMap<Store, Double> prices=new ConcurrentHashMap<>();


         for (Integer i: bags.keySet()){
             ShoppingBag currBag = bags.get(i);
             DResponseObj<Store> getStore = currBag.getStore();
             DResponseObj<ConcurrentHashMap<Integer, Integer>> checkBags=currBag.getProductQuantity();
             if (!getStore.errorOccurred() && !checkBags.errorOccurred()) {
                 Store curStore=getStore.getValue();
                 ConcurrentHashMap<Integer, Integer> crrAmount = catchItems(curStore, checkBags.getValue());

                 //check abount Discount
                 DResponseObj<ConcurrentHashMap<Integer, Integer>> DRpolicy = curStore.checkBuyPolicy(email,crrAmount);
                 Double discount=0.;
                 if (!DRpolicy.errorOccurred()){
                     DResponseObj<Double> DRdiscount = curStore.checkDiscountPolicy(email,DRpolicy.getValue());
                     if (!DRdiscount.errorOccurred()) discount =DRdiscount.getValue();
                 }

                 //rollback if pay or price didnt work
                 DResponseObj<Double> price = curStore.calculateBagPrice(crrAmount);
                 if (price.errorOccurred()) rollBack(curStore,crrAmount);
                 else{

                     DResponseObj<Integer> supply = createSupply(user,city,Street,apartment,crrAmount);
                     if (!supply.errorOccurred()) {
                         //supply
                         totalPrice += price.getValue()-discount;
                         totalProducts.put(curStore,crrAmount);
                         prices.put(curStore,price.getValue()-discount);
                         totalSupply.add(supply.getValue());
//                         PaymentService p = PaymentService.getInstance();
//                         DResponseObj<Integer> TIP = p.pay(c, price.getValue() - discount);
//                         if (TIP.errorOccurred()) rollBack(getStore.getValue(), crrAmount);
//                         else {
//                             getStore.getValue().addHistory(TIP.getValue(), email, crrAmount, price.getValue() - discount);
//                             output.put(i, crrAmount);
//                             logger.info("this order success");
//                         }
                     }else{
                         rollBack(getStore.getValue(), crrAmount);
                         logger.warn("supply didnt work");
                     }
                 }
             }
         }


         PaymentService paymentService = PaymentService.getInstance();
         List<History> histories = new ArrayList<>();
         DResponseObj<Integer> TIP = paymentService.pay(c,totalPrice);
         int counterMinus=-1;
         if (!TIP.errorOccurred()){
             ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> output=new ConcurrentHashMap<>();
             for (Store store: totalProducts.keySet()){
                 DResponseObj<Integer> storeID = store.getStoreId();
                 if (storeID.errorOccurred()) {
                     logger.error("not exist name for this Store.");
                     output.put(counterMinus--,totalProducts.get(store));
                 }
                 else output.put(storeID.getValue(),totalProducts.get(store));
                 DResponseObj<History> historyDResponseObj =store.addHistory(TIP.getValue(), email, totalProducts.get(store), prices.get(store));
                 if (historyDResponseObj.errorOccurred())   logger.error("the History for This Store, didnt save");
                 else histories.add(historyDResponseObj.getValue());
             }
             DResponseObj<Boolean> checkHistory = addHistories(histories,user);
             if (checkHistory.errorOccurred()) logger.error("User didnt get his Histories.");
             return new DResponseObj<>(output);
         }
         else{
             logger.error("The payment is failure");
             for (Store store: totalProducts.keySet())
                 rollBack(store,totalProducts.get(store));
             return new DResponseObj<>(ErrorCode.PAYMENT_FAIL);
         }
    }


    private DResponseObj<Integer> createSupply(Guest user, String city, String street, int apartment, ConcurrentHashMap<Integer, Integer> crrAmount) {
        SupplyService s=SupplyService.getInstance();
        DResponseObj<Integer> d= s.supply(user,city,street,apartment,crrAmount);
        return d.errorOccurred()? new DResponseObj<>(d.getErrorMsg()) : d;
    }

    //goal: to get storeID and Shopping bag of this store.
    private DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>>  getBags(Guest user){
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
    
    private DResponseObj<Boolean> addHistories(List<History> histories,User u){
        return u.addHistoies(histories);
    }
    private DResponseObj<Boolean> addHistories(List<History> histories,Guest u){
        return new DResponseObj<>(true);
    }



}
