package main.System.Server.Domain.Market;

import main.ErrorCode;

import main.ExternalService.CreditCard;
import main.ExternalService.PaymentService;
import main.ExternalService.SupplyService;
import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.StoreModel.History;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.ShoppingBag;
import main.System.Server.Domain.UserModel.ShoppingCart;
import main.System.Server.Domain.UserModel.User;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Purchase {
    static Logger logger=Logger.getLogger(Purchase.class);




    //pre:user is connect, credit card is valid.
    //post: the paying of all the cart is successes.
    public DResponseObj<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> order(User user, String city , String Street, int apartment,
                                                                                               CreditCard c){
        //get email and bags of the user.
        DResponseObj<Tuple<String,ConcurrentHashMap<Integer, ShoppingBag>>> emailAndBags = getEmailAndShoppingBags(user);
        if (emailAndBags.errorOccurred()) return new DResponseObj<>(emailAndBags.getErrorMsg());
        String email = emailAndBags.getValue().item1;
        ConcurrentHashMap<Integer, ShoppingBag> bags = emailAndBags.getValue().item2;


        //this is the memory that we need to do the payment in one time.
        Double totalPrice=0.;
        // per Store we save what we bought.
        ConcurrentHashMap<Store, ConcurrentHashMap<Integer, Integer>> totalProducts=
                new ConcurrentHashMap<>();
        //List of the supply TIP.
        List<Integer> totalSupply = new ArrayList<>();
        // per Store we keep the price for history
        ConcurrentHashMap<Store, Double> prices=new ConcurrentHashMap<>();
        ConcurrentHashMap<Store, Integer> deliveries=new ConcurrentHashMap<>();


        for (Integer bagID: bags.keySet()) {
            //get the store and the bag that we will work on that.
            ShoppingBag currBag = bags.get(bagID);
            DResponseObj<Store> getStore = currBag.getStore();
            DResponseObj<ConcurrentHashMap<Integer, Integer>> checkBags = currBag.getProductQuantity();

            if (!getStore.errorOccurred() && !checkBags.errorOccurred()) {
                Store curStore = getStore.getValue();

                // catch the items.
                DResponseObj<ConcurrentHashMap<Integer, Integer>> DRcrrAmount =
                        catchItems(curStore, checkBags.getValue());
                if (DRcrrAmount.errorOccurred()) continue;
                ConcurrentHashMap<Integer, Integer> crrAmount = DRcrrAmount.getValue();

                //check price and policy
                DResponseObj<Double> Dprice = getPriceAfterDiscount(curStore, email, crrAmount);
                if (Dprice.errorOccurred()) continue;
                Double price = Dprice.getValue();

                //check supply
                DResponseObj<Integer> supply = createSupply(user, city, Street, apartment, crrAmount);

                //if the supply does not work, we will do rollback.
                if (supply.errorOccurred()) {
                    logger.warn("supplyService does not work");
                    DResponseObj<Boolean> rollBack = rollBack(curStore, crrAmount);
                    if (rollBack.errorOccurred()) logger.error("rollback does not work!!!");
                    continue;
                }
                deliveries.put(curStore,supply.getValue());

                //update the vars
                totalPrice += price; // add to the total after discount.
                totalProducts.put(curStore, crrAmount);
                prices.put(curStore, price);
                totalSupply.add(supply.getValue());

            }
        }
        //check if can pay
        DResponseObj<Integer> TIP= buy(c,totalPrice,totalProducts);
        if (TIP.errorOccurred()) return new DResponseObj<>(TIP.getErrorMsg());

        //for store that we can not find the ID.
        int counterMinus=-1;
        List<History> histories = new ArrayList<>();
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> output=new ConcurrentHashMap<>();


        for (Store store: totalProducts.keySet()){

            //look for StoreID for thee output.
            DResponseObj<Integer> storeID = store.getStoreId();
            if (storeID.errorOccurred()) {
                logger.error("not exist name for this Store.");
                output.put(counterMinus--,totalProducts.get(store));
            }
            else
                output.put(storeID.getValue(),totalProducts.get(store));

            //create History for this store.
            DResponseObj<History> historyDResponseObj =store.addHistory(TIP.getValue(),deliveries.get(store), email, totalProducts.get(store), prices.get(store));
            if (historyDResponseObj.errorOccurred())   logger.error("the History for This Store, didnt save");
            else histories.add(historyDResponseObj.getValue());
        }

        // add all Histories to the User.
        DResponseObj<Boolean> checkHistory = user.addHistoies(histories);
        if (checkHistory.errorOccurred()) logger.error("User didnt get his Histories.");
        return new DResponseObj<>(output);
    }


    /*************************************************private methods*****************************************************/

    //target: to buy all the products if error then rollback.
    private DResponseObj<Integer> buy(CreditCard c,Double totalPrice,
                                      ConcurrentHashMap<Store, ConcurrentHashMap<Integer, Integer>> totalProducts){
        PaymentService paymentService = PaymentService.getInstance();
        List<History> histories = new ArrayList<>();
        DResponseObj<Integer> TIP = paymentService.pay(c,totalPrice);
        if (TIP.errorOccurred()){
            logger.error("The payment is failure");
            for (Store store: totalProducts.keySet())
                rollBack(store,totalProducts.get(store));
            return new DResponseObj<>(ErrorCode.PAYMENT_FAIL);
        }
        return new DResponseObj<>(TIP.getValue(),-1);
    }

    //target: to get the price after all the process with store.
    private DResponseObj<Double> getPriceAfterDiscount(Store store,String email,
                                                       ConcurrentHashMap<Integer, Integer> crrAmount) {
        //check about Discount in the Store.
        DResponseObj<ConcurrentHashMap<Integer, Integer>> DRpolicy = store.checkBuyPolicy(email, crrAmount);
        Double discount = 0.;
        if (DRpolicy.errorOccurred()) {
            logger.warn("the policy of this store didnt work");
            return new DResponseObj<>(DRpolicy.getErrorMsg());
        }

        DResponseObj<Double> DRdiscount = store.checkDiscountPolicy(email, DRpolicy.getValue());
        if (!DRdiscount.errorOccurred()) discount = DRdiscount.getValue();

        DResponseObj<Double> price = store.calculateBagPrice(crrAmount);
        if (price.errorOccurred()) {
            //rollback if price didnt work
            logger.warn("the price didnt work - roll back");
            rollBack(store, crrAmount);
            return new DResponseObj<>(ErrorCode.ROLLBACK);
        }
        return new DResponseObj(price.getValue() - discount);
    }

    //target: to find the email and the bags of the user.
    private DResponseObj<Tuple<String,ConcurrentHashMap<Integer, ShoppingBag>>> getEmailAndShoppingBags(User user){
        DResponseObj<ConcurrentHashMap<Integer, ShoppingBag>> bagsRes=getBags(user);
        if (bagsRes.errorOccurred()) return new DResponseObj<>(bagsRes.getErrorMsg());
        ConcurrentHashMap<Integer, ShoppingBag> bags=bagsRes.getValue();
        DResponseObj<String> Demail= user.getEmail();
        if (Demail.errorOccurred()) return new DResponseObj<>(Demail.getErrorMsg());
        String email = Demail.getValue();
        return new DResponseObj<>(new Tuple(email,bags));
    }

    private DResponseObj<Integer> createSupply(User user, String city, String street, int apartment, ConcurrentHashMap<Integer, Integer> crrAmount) {
        SupplyService s=SupplyService.getInstance();
        DResponseObj<Integer> d= s.supply(user,city,street,apartment,crrAmount);
        return d.errorOccurred()? new DResponseObj<>(d.getErrorMsg()) : d;
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
    private DResponseObj<ConcurrentHashMap<Integer, Integer>> catchItems(Store store, ConcurrentHashMap<Integer, Integer> hashMap){
        ConcurrentHashMap<Integer, Integer> output = new ConcurrentHashMap<>();
        for (Integer j: hashMap.keySet()){
            Integer amount = hashMap.get(j);
            DResponseObj<Integer> amonutCanBuy = store.setProductQuantityForBuy(j,amount);
            if (!amonutCanBuy.errorOccurred()) output.put(j,amonutCanBuy.getValue());
        }
        return new DResponseObj<>(output);
    }

    private DResponseObj<Boolean> rollBack(Store store,ConcurrentHashMap<Integer, Integer> hashMap){
        logger.warn("rool back");
        DResponseObj<Boolean> ans=store.rollbackProductQuantity(hashMap);
        return new DResponseObj<>(ErrorCode.ROLLBACK);
    }


    class Tuple<E,T>{
        E item1;
        T item2;

        public Tuple(E item1, T item2) {
            this.item1 = item1;
            this.item2 = item2;
        }
    }



}
