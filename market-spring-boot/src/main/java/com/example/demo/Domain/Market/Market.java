package com.example.demo.Domain.Market;

import Stabs.PurchaseStab;
import Stabs.StoreStab;
import Stabs.UserManagerStab;
import com.example.demo.DataAccess.Mappers.ProductTypeMapper;
import com.example.demo.DataAccess.Mappers.StoreMapper;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.*;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.UserModel.*;
import com.example.demo.ExternalService.ExternalService;
import com.example.demo.ExternalService.PaymentService;
import com.example.demo.ExternalService.SupplyService;
import com.example.demo.Service.ServiceObj.ServiceProductType;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

@Component
public class Market {
    /*************************************************fields************************************************************/
    static Logger logger = Logger.getLogger(Market.class);
    Purchase purchase;
    UserManager userManager;
    ConcurrentHashMap<Integer, Store> stores = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, Store> closeStores = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ProductType> productTypes = new ConcurrentHashMap<>();
    int productCounter = 1, storeCounter = 1;

    /*
    if you want to use with stores - acquire the lock - lock_stores
    if you want to use with ProductType, productCounter - acquire - lock_TP
     */
    private StampedLock lock_stores = new StampedLock(), lock_TP = new StampedLock();

    private final DataServices dataServices;

    /*************************************************constructors******************************************************/
    @Autowired
    public Market(UserManager userManager, DataServices dataServices) {
        this.dataServices = dataServices;
        this.userManager = userManager;
        ExternalService.getInstance();
        purchase = new Purchase();
//        initAllSoresFromTheDb ();

    }

    /*************************************************Functions*********************************************************/
    public DResponseObj<Boolean> isStoreClosed(int StoreID) {
        return new DResponseObj<>(closeStores.containsKey(StoreID));
    }
    public void initAllSoresFromTheDb(){
        if(dataServices!=null) {
            StoreMapper storeMapper = StoreMapper.getInstance();
            Map<Integer, Store> allStores = storeMapper.getAllStores();
            for (Integer storeId : allStores.keySet()) {
                Store s = allStores.get(storeId);
                if (s.isOpen().value) {
                    this.stores.put(storeId, s);
                } else {
                    this.closeStores.put(storeId, s);
                }
            }
        }
    }

    public void initAllProductTypes(){
        if(dataServices!=null) {
            List<ProductType> t = ProductTypeMapper.getInstance().getAllProductTypes();
            for(ProductType productType : t){
                if(!productTypes.containsKey(productType.getProductID())){
                    productTypes.put(productType.getProductID().value,productType);
                }
            }
        }
    }

    //1.1
    //pre: -
    //post: the external Services connect
    public DResponseObj<Boolean> init() {
        return paymentAndSupplyConnct();
    }

    //2.2.1
    //pre: -
    //post: get info from valid store and product.
    public DResponseObj<ProductStore> getInfoProductInStore(int storeID, int productID) {
        if(isStoreClosed(storeID).value) return new DResponseObj<>(null,ErrorCode.STORE_IS_CLOSED);
        DResponseObj<ProductType> p = getProductType(productID);

        if (p.errorOccurred()) {
            DResponseObj<ProductStore> output = new DResponseObj<>();
            output.setErrorMsg(p.getErrorMsg());
            return output;
        }


        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) {
            DResponseObj<ProductStore> output = new DResponseObj<>();
            output.setErrorMsg(s.getErrorMsg());
            return output;
        }
        DResponseObj<ProductStore> str = s.getValue().getProductInStoreInfo(productID);
        return str;
    }


    //2.2.2
    //pre: -
    //post: get all the open stores that the arg is apart of their names
    public DResponseObj<List<Integer>> searchProductByName(String name) {

        List<Integer> pIDs = getProductTYpeIDs(getProductTypes());
        return searchProductByName(pIDs, name);
    }

    public DResponseObj<ServiceProductType> getProductTypeInfo(Integer productTypeId) {
        if(productTypes.containsKey(productTypeId)) {
            return new DResponseObj<>(new ServiceProductType(productTypes.get(productTypeId)));
        }
        return new DResponseObj<>(ErrorCode.PRODUCTNOTEXIST);
    }

    //2.2.2
    //pre: -
    //post: get all the open stores that the arg is apart of their names
    public DResponseObj<List<Integer>> searchProductByName(List<Integer> list, String name) {
        if (name == null) {
            logger.warn("the name is null");
            DResponseObj<List<Integer>> output = new DResponseObj<>();
            output.setErrorMsg(ErrorCode.NOTVALIDINPUT);
            return output;
        }

        List<Integer> output = new ArrayList<>();
        for (Integer i : list) {
            DResponseObj<ProductType> productTypeDR = getProductType(i);
            if (!productTypeDR.errorOccurred()) {
                ProductType productType = productTypeDR.getValue();
                DResponseObj<Boolean> checkIfExist = productType.containName(name);
                if (!checkIfExist.errorOccurred() && checkIfExist.value) {
                    output.add(i);
                }
            }

        }
        return new DResponseObj<>(output);
    }

    //2.2.2
    //pre: -
    //post: get all the open stores that the arg is apart of their names
    public DResponseObj<List<Integer>> filterByName(List<Integer> list, String name) {
        if (name == null) {
            logger.warn("the name is null");
            DResponseObj<List<Integer>> output = new DResponseObj<>();
            output.setErrorMsg(ErrorCode.NOTVALIDINPUT);
            return output;
        }
        List<Integer> output = new ArrayList<>();

        for (Integer productID : list) {
            DResponseObj<ProductType> productDR = getProductType(productID);
            if (productDR.errorOccurred()) {
                continue;
            }
            DResponseObj<Boolean> containName = productDR.getValue().containName(name);
            if (containName.errorOccurred()) continue;
            if (!containName.getValue()) {
                logger.debug("this product not contain the name");
                continue;
            }
            output.add(productID);
        }
        return new DResponseObj<>(output);
    }


    //2.2.2
    //pre: -
    //post: get all the open stores that the arg is apart of their description
    public DResponseObj<List<Integer>> searchProductByDesc(String desc) {
        List<Integer> pIDs = getProductTYpeIDs(getProductTypes());
        return searchProductByDesc(pIDs, desc);
    }

    //2.2.2
    //pre: -
    //post: get all the open stores that the arg is apart of their description
    public DResponseObj<List<Integer>> searchProductByDesc(List<Integer> list, String desc) {
        if (desc == null) {
            logger.warn("description arrived null");
            DResponseObj<List<Integer>> output = new DResponseObj<>();
            output.setErrorMsg(ErrorCode.NOTVALIDINPUT);
            return output;
        }
        List<Integer> output = new ArrayList<>();
        for (Integer i : list) {
            DResponseObj<ProductType> productDR = getProductType(i);
            if (!productDR.errorOccurred()) {
                ProductType productType = productDR.getValue();
                DResponseObj<Boolean> exist = productType.containDesc(desc);
                if (!exist.errorOccurred() && exist.getValue()) {
                    output.add(i);
                }
            }

        }
        return new DResponseObj<>(output);
    }

    public DResponseObj<List<Integer>> filterByDesc(List<Integer> list, String desc) {
        if (desc == null) {
            logger.warn("the name is null");
            DResponseObj<List<Integer>> output = new DResponseObj<>();
            output.setErrorMsg(ErrorCode.NOTVALIDINPUT);
            return output;
        }
        List<Integer> output = new ArrayList<>();

        for (Integer productID : list) {
            DResponseObj<ProductType> productDR = getProductType(productID);
            if (productDR.errorOccurred()) {
                continue;
            }
            DResponseObj<Boolean> containDesc = productDR.getValue().containDesc(desc);
            if (containDesc.errorOccurred()) continue;
            if (!containDesc.getValue()) {
                logger.debug("this product not contain the name");
                continue;
            }
            output.add(productID);
        }
        return new DResponseObj<>(output);
    }

    //2.2.2
    //pre: -
    //post: get all the products that them rate higher or equal to the arg(arg>0)
    public DResponseObj<List<Integer>> searchProductByRate(int minRate) {
        List<Integer> pIDs = getProductTYpeIDs(getProductTypes());
        return searchProductByRate(pIDs, minRate);
    }

    //2.2.2
    //pre: -
    //post: get all the products that them rate higher or equal to the arg(arg>0)
    public DResponseObj<List<Integer>> searchProductByRate(List<Integer> list, int minRate) {
        if (minRate < 0 || minRate > 10) {
            logger.warn("rate is invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        List<Integer> output = new ArrayList<>();
        for (Integer i : list) {
            DResponseObj<ProductType> p = getProductType(i);
            if (!p.errorOccurred()) {
                ProductType productType = p.getValue();
                DResponseObj<Integer> rate = productType.getRate();
                if (!rate.errorOccurred() && rate.getValue() >= minRate) {
                    output.add(i);
                }
            }
        }
        return new DResponseObj<>(output);
    }

    public DResponseObj<List<Integer>> filterByRate(List<Integer> list, int minRate) {
        if (minRate < 0 || minRate > 10) {
            logger.warn("rate is invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        List<Integer> output = new ArrayList<>();

        for (Integer productID : list) {
            DResponseObj<ProductType> productDR = getProductType(productID);
            if (productDR.errorOccurred()) {
                continue;
            }
            DResponseObj<Integer> isRate = productDR.getValue().getRate();
            if (isRate.errorOccurred()) continue;
            if (isRate.getValue() < minRate) {
                logger.debug("this product not pass the min Rate");
                continue;
            }
            output.add(productID);
        }
        return new DResponseObj<>(output);
    }

    //2.2.2
    //pre: -
    //post: get all the open stores that their rate higher or equal to the arg(arg>0)
    public DResponseObj<List<Integer>> searchProductByStoreRate(int rate) {


        List<Integer> pIDs = getStoreIDs(getStores());
        return searchProductByStoreRate(pIDs, rate);
    }

    //2.2.2
    //pre: -
    //post: get all the open stores that their rate higher or equal to the arg(arg>0)
    public DResponseObj<List<Integer>> searchProductByStoreRate(List<Integer> list, int rate) {
        if (rate < 0 || rate > 10) {
            logger.warn("rate is invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }

        List<Integer> output = new ArrayList<>();
        for (Integer istore : list) {
            DResponseObj<Store> store = getStore(istore);
            if (!store.errorOccurred()) {
                DResponseObj<Integer> getRate = store.getValue().getRate();
                if (!getRate.errorOccurred() && getRate.getValue() >= rate)
                    output.add(istore);
            }
        }
        return new DResponseObj<>(output);
    }

    public DResponseObj<List<Integer>> filterByStoreRate(List<Integer> list, int minRate) {
        if (minRate < 0 || minRate > 10) {
            logger.warn("rate is invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        List<Integer> output = new ArrayList<>();


        for (Integer productID : list) {
            DResponseObj<List<Integer>> storePassRate = searchProductByStoreRate(minRate);
            for (Integer storeID : storePassRate.getValue()) {
                DResponseObj<Store> getStore = getStore(storeID);
                if (getStore.errorOccurred()) continue;
                DResponseObj<Boolean> isExist = getStore.getValue().isProductExistInStock(productID, 1);
                if (!isExist.errorOccurred() && !isExist.getValue()) {
                    logger.debug("Store #" + storeID + " include product #" + productID);
                    output.add(productID);
                    break;
                }
            }
        }
        return new DResponseObj<>(output);
    }


    //2.2.2
    //pre: -
    //post: get all the products that them price is between min and max
    public DResponseObj<List<Integer>> searchProductByRangePrices(int productID, int min, int max) {

        List<Integer> pIDs = getStoreIDs(getStores());
        return searchProductByRangePrices(pIDs, productID, min, max);
    }

    //2.2.2
    //pre: -
    //post: get all the products that them price is between min and max
    public DResponseObj<List<Integer>> searchProductByRangePrices(List<Integer> list, int productID, int min, int max) {
        if (min > max) {
            logger.warn("min bigger then max - invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        DResponseObj<ProductType> getProduct = getProductType(productID);
        if (getProduct.errorOccurred()) return new DResponseObj<>(getProduct.getErrorMsg());

        List<Integer> output = new ArrayList<>();
        for (Integer istore : list) {
            DResponseObj<Store> store = getStore(istore);
            if (!store.errorOccurred()) {
                DResponseObj<Double> getPrice = store.getValue().getProductPrice(productID);
                if (!getPrice.errorOccurred()) {
                    Double price = getPrice.getValue();
                    if (price != null && (price <= (double) max & price >= (double) min))
                        output.add(istore);
                }
            }
        }
        return new DResponseObj<>(output);
    }

    public DResponseObj<List<Integer>> filterByRangePrices(List<Integer> list, int min, int max) {
        if (min > max) {
            logger.warn("min bigger then max - invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        List<Integer> output = new ArrayList<>();

        List<Integer> storesID = getStoreIDs(getStores());
        for (Integer productID : list) {
            DResponseObj<List<Integer>> storePassRate = searchProductByRangePrices(storesID, productID, min, max);
            if (!storePassRate.errorOccurred() && storePassRate.getValue().size() > 0) {
                logger.debug("found store that contain this product");
                output.add(productID);
            }
        }
        return new DResponseObj<>(output);
    }

    //2.2.2
    //pre: -
    //post: get all the products that them price is between min and max
    public DResponseObj<List<Integer>> searchProductByCategory(int category) {
        if (category < 0) {
            logger.warn("new categoryID is illegal");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }

        List<ProductType> searchOn = getProductTypes();
        List<Integer> output = new ArrayList<>();
        for (ProductType p : searchOn) {
            DResponseObj<Integer> cat = p.getCategory();
            DResponseObj<Integer> pID = p.getProductID();
            if (!cat.errorOccurred() && !pID.errorOccurred() && category == cat.getValue()) output.add(pID.getValue());
        }
        return new DResponseObj<>(output);
    }

    //2.2.2
    //pre: -
    //post: get all the products that them price is between min and max
    public DResponseObj<List<Integer>> searchProductByCategory(List<Integer> list, int category) {
        if (category < 0) {
            logger.warn("new categoryID is illegal");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        List<Integer> output = new ArrayList<>();
        for (Integer iproduct : list) {
            DResponseObj<ProductType> product = getProductType(iproduct);
            if (!product.errorOccurred()) {
                DResponseObj<Integer> cat = product.getValue().getCategory();
                if (!cat.errorOccurred())
                    output.add(iproduct);
            }
        }
        return new DResponseObj<>(output);
    }

    public DResponseObj<List<Integer>> filterByCategory(List<Integer> list, int category) {
        if (category < 0 || category > 10) {
            logger.warn("category is invalid");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        List<Integer> output = new ArrayList<>();

        for (Integer productID : list) {
            DResponseObj<ProductType> productDR = getProductType(productID);
            if (productDR.errorOccurred()) {
                continue;
            }
            DResponseObj<Integer> isCategory = productDR.getValue().getCategory();
            if (isCategory.errorOccurred()) continue;
            if (isCategory.getValue() != category) {
                logger.debug("this product #" + productID + " from category #" + isCategory.getValue() + ".");
                continue;
            }
            output.add(productID);
        }
        return new DResponseObj<>(output);
    }


    //2.2.3
    //pre: user is online
    //post: add <quantity> times this product from this store
    public DResponseObj<Boolean> AddProductToShoppingBag(UUID userId, int StoreId, int ProductId, int quantity) {
        if (isStoreClosed(StoreId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);

        //get user
        DResponseObj<User> user = userManager.getOnlineUser(userId);
        if (user.errorOccurred()) return new DResponseObj<>(user.getErrorMsg());

        //check if the productExist
        DResponseObj<ProductType> productTypeDResponseObj = getProductType(ProductId);
        if (productTypeDResponseObj.errorOccurred()) return new DResponseObj<>(productTypeDResponseObj.getErrorMsg());

        //get cart
        DResponseObj<ShoppingCart> cart = user.getValue().GetSShoppingCart();
        if (cart.errorOccurred()) return new DResponseObj<>(cart.getErrorMsg());

        int currQuantity = 0;
        ShoppingBag shoppingBag = cart.value.getHashShoppingCart().value.get(StoreId);
        if (shoppingBag != null) {
            DResponseObj<Integer> curr = shoppingBag.getProductQuantity(ProductId);
            if (curr.value != null) currQuantity = curr.value;
        }

        //check the store
        DResponseObj<Store> store = getStore(StoreId);
        if (store.errorOccurred()) return new DResponseObj<>(store.getErrorMsg());
        DResponseObj<Boolean> isExist = store.getValue().isProductExistInStock(ProductId, quantity + (currQuantity));
        if (isExist.errorOccurred()) return new DResponseObj<>(isExist.getErrorMsg());
        if (!isExist.getValue()) {
            logger.warn("the quantity is not exist in the Store");
            return new DResponseObj<>(ErrorCode.PRODUCT_DOESNT_EXIST_IN_THE_STORE);
        }


        //get args for the
        DResponseObj<Boolean> add = cart.getValue().addNewProductToShoppingBag(ProductId, store.getValue(), quantity);
        if (add.errorOccurred()) return new DResponseObj<>(add.getErrorMsg());
        if (!add.getValue()) {
            logger.warn("the Cart didnt add this product");
            return new DResponseObj<>(ErrorCode.CART_FAIL);
        }
        return new DResponseObj<>(true);
    }


    public DResponseObj<Integer> getStoreRate(UUID uuid, int Store) {
        if (!isOnline(uuid).value) return new DResponseObj<>(null, ErrorCode.NOTONLINE);
        if (!stores.containsKey(Store)) return new DResponseObj<>(null, ErrorCode.STORE_IS_NOT_EXIST);
        return stores.get(Store).getRate();
    }

    public DResponseObj<Boolean> newStoreRate(UUID uuid, int Store, int rate) {
        if (!isOnline(uuid).value) return new DResponseObj<>(ErrorCode.NOTONLINE);
        if (!stores.containsKey(Store)) return new DResponseObj<>(null, ErrorCode.STORE_IS_NOT_EXIST);
        return stores.get(Store).newStoreRate(rate);
    }

    //2.2.5
    //pre: user is online
    //post: start process of sealing with the User
    public DResponseObj<DetailsPurchase> order(UUID userId, String City, String Street, int apartment, String cardNumber, String exp, String pin) {
        //check valid Card
        DResponseObj<Boolean> checkValidCard = checkValidCard(cardNumber, exp, pin);
        if (checkValidCard.errorOccurred()) return new DResponseObj<>(checkValidCard.getErrorMsg());

        //get User
        DResponseObj<User> user = userManager.getOnlineUser(userId);
        if (user.errorOccurred()) return new DResponseObj<>(user.getErrorMsg());

        //check PaymentService AND check SupplyService
//         DResponseObj<Boolean> checkServices = paymentAndSupplyConnct(); //TODO: ask YAKI
//        if (checkServices.errorOccurred()) return new DResponseObj<>(checkServices.getErrorMsg());
//        if (!checkServices.getValue()){
//            logger.warn("this External Services dont connect.");
//            return new DResponseObj<>(ErrorCode.EXTERNAL_SERVICE_ERROR);
//        }

        //check Cart
        DResponseObj<ShoppingCart> shoppingCart = userManager.getUserShoppingCart(userId);
        if (shoppingCart.errorOccurred()) return new DResponseObj<>(shoppingCart.getErrorMsg());
        if (shoppingCart.value.getHashShoppingCart().value.isEmpty())
            return new DResponseObj<>(null, ErrorCode.EMPTY_CART);
        DResponseObj<DetailsPurchase> res = purchase.order(user.getValue(), City, Street, apartment, cardNumber, exp, pin);
        if (res.errorOccurred()) return new DResponseObj<>(null, ErrorCode.ORDER_FAIL);
        notifyOwnersPurchase(user.value, res.value.getBoughtInStores());
        userManager.resetShoppingCart(userId);
        return res;
    }

    private void notifyOwnersPurchase(User buyer, List<Integer> stores) {
        //in the future we can make a message factory. for now lets keep it simple.
        logger.info("notifying all stores that had a purchase.");
        String msg = String.format("User: %s has purchased items from your store", buyer.getEmail().value);
        stores.forEach(storeID -> {
            logger.info(String.format("notifying store[%d] owners of purchase", storeID));
            Store store = this.stores.get(storeID);
            List<User> owners = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.owner).value;
            userManager.notifyUsers(owners, msg);
        });
    }

    private void notifyOwnersPurchaseBID(User buyer, int storeId) {
        String msg = String.format("User: %s has purchased his offer from your store", buyer.getEmail().value);
        logger.info(String.format("notifying store[%d] owners of purchaseBID", storeId));
        Store store = this.stores.get(storeId);
        List<User> owners = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.owner).value;
        userManager.notifyUsers(owners, msg);
    }


    //2.3.2
    //pre: user is Member
    //post: new Store add to the market
    public DResponseObj<Integer> OpenNewStore(UUID userId, String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy) {
        DResponseObj<Boolean> checkUM = userManager.isLogged(userId);
        if (checkUM.errorOccurred() || !checkUM.getValue()) return new DResponseObj<>(null, ErrorCode.NOTLOGGED);

        long stamp = lock_stores.writeLock();
        logger.debug("catch the WriteLock");
        try {
            int storeId = storeCounter++;
            Store store = new Store(name, discountPolicy, buyPolicy, founder);
            //db
            if(dataServices!= null && dataServices.getStoreService() != null){
                var dataStore = store.getDataObject();
                dataServices.getStoreService().insertStore(dataStore);
                storeId = dataStore.getStoreId();
            }
            store.setStoreId(storeId);
            store.getInventory().value.setStoreId(storeId);
            stores.put(storeId, store);
            userManager.addFounder(userId, store);
            logger.info("new Store join to the Market");
            return new DResponseObj<>(store.getStoreId().value, -1);
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released the WriteLock");
        }
    }

    //2.4.1.1
    //pre: user is Owner
    //post: product that his ProductType exist in the market, exist in this store.
    public DResponseObj<Boolean> addNewProductToStore(UUID userId, int storeId, int productId, double price, int quantity) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.addNewProductToStore, productId);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;
        ProductType productType = result.getValue().item2;
        return store.addNewProduct(productType, quantity, price);
    }


    public DResponseObj<Integer> addNewProductType(UUID uuid, String name, String description, int category) {
        DResponseObj<Boolean> isManager = isSystemMan(uuid);
        if (isManager.errorOccurred()) return new DResponseObj<>(isManager.getErrorMsg());

        long stamp = lock_TP.writeLock();
        logger.debug("catch the WriteLock");
        try {
            for (ProductType productType : productTypes.values()) {
                DResponseObj<String> nameProduct = productType.getProductName();
                if (nameProduct.errorOccurred() || name.equals(nameProduct.getValue()))
                    return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
            }
            int productID = productCounter++;
            ProductType p = new ProductType(name, description, category);
            //db
            if (dataServices != null && dataServices.getProductTypeService() != null) {
                var dataProductType = p.getDataObject();
                if (!dataServices.getProductTypeService().insertProductType(dataProductType)) {
                    logger.error(String.format("failed to persist product type [%s] in db", name));
                    return new DResponseObj<>(ErrorCode.DB_ERROR);
                }
                productID = dataProductType.getProductTypeId();
            }
            //
            p.setProductID(productID);
            productTypes.put(productID, p);

            return new DResponseObj<>(productID, -1);
        } finally {
            lock_TP.unlockWrite(stamp);
            logger.debug("released the WriteLock");
        }
    }

    //2.4.1.2
    //pre: user is Owner
    //post: product that his ProductType  exist in the market, not exist anymore in this store.
    public DResponseObj<Boolean> deleteProductFromStore(UUID userId, int storeId, int productId) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.deleteProductFromStore, productId);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;
        ProductType productType = result.getValue().item2;

        DResponseObj<Integer> getPID = productType.getProductID();
        if (getPID.errorOccurred()) return new DResponseObj<>(getPID.getErrorMsg());
        return store.removeProduct(getPID.getValue());
    }


    //2.4.1.3
    //pre: user is Owner of the store
    //post: the price of this product in this store changed.
    public DResponseObj<Boolean> setProductPriceInStore(UUID userId, int storeId, int productId, double price) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.setProductPriceInStore, productId);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;
        if(store == null) {
            return new DResponseObj<>(null, ErrorCode.STORE_IS_NOT_EXIST);
        }
        return store.setProductPrice(productId, price);
    }


    //2.4.1.3
    //pre: user is Owner of the store
    //post: the quantity of this product in this store changed.
    public DResponseObj<Boolean> setProductQuantityInStore(UUID userId, int storeId, int productId, int quantity) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.setProductPriceInStore, productId);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;
        if(store==null)
        {
            return new DResponseObj<>(null, ErrorCode.STORE_IS_NOT_EXIST);
        }

        return store.setProductQuantity(productId, quantity);
    }

    //2.4.2
    //pre: the store exist in the system.
    //post: buy rule added to this store
    public DResponseObj<Boolean> addNewBuyRule(UUID userId, int storeId, BuyRule buyRule) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.addNewBuyRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.addNewBuyRule(buyRule);
    }

    //2.4.2
    //pre: the store exist in the system.
    //post: buy rule removed from this store
    public DResponseObj<Boolean> removeBuyRule(UUID userId, int storeId, int buyRuleID) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.removeBuyRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.removeBuyRule(buyRuleID);
    }

    //2.4.2
    //pre: the store exist in the system.
    //post: discount rule added to this store
    public DResponseObj<Boolean> addNewDiscountRule(UUID userId, int storeId, DiscountRule discountRule) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.addNewDiscountRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.addNewDiscountRule(discountRule);
    }

    //2.4.2
    //pre: the store exist in the system.
    //post: discount rule removed from this store
    public DResponseObj<Boolean> removeDiscountRule(UUID userId, int storeId, int discountRuleID) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.removeDiscountRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.removeDiscountRule(discountRuleID);
    }

    public DResponseObj<Boolean> combineANDORDiscountRules(UUID userId, int storeId, String operator, List<Integer> rules, int category, int discount) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.addNewDiscountRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.combineANDORDiscountRules(operator, rules, category, discount);
    }

    public DResponseObj<Boolean> combineXorDiscountRules(UUID userId, int storeId, String decision, List<Integer> rules) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.addNewDiscountRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.combineXORDiscountRules(rules, decision);
    }


    public DResponseObj<List<BuyRule>> getBuyPolicy(UUID userId, int storeId) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> login = userManager.getLoggedUser(userId);
        if (login.errorOccurred()) return new DResponseObj<>(login.getErrorMsg());
        DResponseObj<Store> s = getStore(storeId);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        return s.value.getBuyPolicy();
    }

    public DResponseObj<List<DiscountRule>> getDiscountPolicy(UUID userId, int storeId) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> login = userManager.getLoggedUser(userId);
        if (login.errorOccurred()) return new DResponseObj<>(login.getErrorMsg());
        DResponseObj<Store> s = getStore(storeId);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        return s.value.getDiscountPolicy();
    }


    public DResponseObj<BuyRule> getBuyRuleByID(UUID userId, int storeId, int buyRuleID) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.addNewBuyRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.getBuyRuleByID(buyRuleID);
    }

    public DResponseObj<DiscountRule> getDiscountRuleByID(UUID userId, int storeId, int discountRuleID) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.addNewDiscountRule);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        return store.getDiscountRuleByID(discountRuleID);
    }
    //2.4.4
    //pre: the store exist in the system.
    //post: other user became to be owner on this store.
    public DResponseObj<Boolean> addNewStoreOwner(UUID userId, int storeId, String newOnerEmail) {
        userManager.getDbUserIfNeed(newOnerEmail);
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.addNewStoreOwner, null);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;

        return userManager.addNewStoreOwner(userId, store, newOnerEmail);
    }

    //2.4.5
    //pre: the store exist in the system and uuid is grantor of owner
    //post: the owner is not owner.
    public DResponseObj<Boolean> removeStoreOwner(UUID userId, int storeId, String ownerEmail) {
        userManager.getDbUserIfNeed(ownerEmail);

        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.removeStoreOwner);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        DResponseObj<Boolean> removed = userManager.removeStoreOwner(userId, store, ownerEmail);
        if (!removed.errorOccurred()) checkStoreBIDAllApproved(store);
        return removed;
    }


    public DResponseObj<Boolean> removeStoreMenager(UUID userId, int storeId, String ownerEmail) {
        userManager.getDbUserIfNeed(ownerEmail);
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> result = checkValidRules(userId, storeId, permissionType.permissionEnum.removeStoreOwner);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue();
        DResponseObj<Boolean> removed = userManager.removeStoreManager(userId, store, ownerEmail);
        if (!removed.errorOccurred()) checkStoreBIDAllApproved(store);
        return removed;
    }

    //2.4.6
    //pre: the store exist in the system.
    //post: other user became to be manager on this store.
    public DResponseObj<Boolean> addNewStoreManager(UUID userId, int storeId, String newMangermail) {
        userManager.getDbUserIfNeed(newMangermail);

        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.addNewStoreManager, null);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;

        return userManager.addNewStoreManager(userId, store, newMangermail);
    }

    //2.4.7
    //pre: the store exist in the system.
    //post: other user that already manager became to be manager on this store with other permissions.
    public DResponseObj<Boolean> setManagerPermissions(UUID userId, int storeId, String mangerMail, permissionType.permissionEnum perm, boolean onof) {
        userManager.getDbUserIfNeed(mangerMail);

        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.setManagerPermissions, null);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;
        return userManager.setManagerPermissions(userId, store, mangerMail, perm, onof);
    }

    //2.4.9
    //pre: the store exist in the system, the user is founder of this store.
    //post: the market move this store to the closeStores, users can not see this store again(until she will be open).
    public DResponseObj<Boolean> closeStore(UUID userId, int storeId) {
        if (isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(userId);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeId);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        Store store = s.value;
        if(store == null) {
            return new DResponseObj<>(null, ErrorCode.STORE_IS_NOT_EXIST);
        }
        if (!store.getFounder().value.equals(logIN.value.getEmail().value)) {
            logger.warn("only founder can close store");
            return new DResponseObj<>(false, ErrorCode.NOPERMISSION);
        }
        DResponseObj<Boolean> checkCloseStore = store.closeStore();
        if (checkCloseStore.errorOccurred()) return checkCloseStore;
        if (!checkCloseStore.getValue()) {
            logger.warn("Store return that can not close this store");
            return new DResponseObj<>(ErrorCode.CANNOTCLOSESTORE);
        }
        DResponseObj<Integer> getStoreID = store.getStoreId();
        if (getStoreID.errorOccurred()) return new DResponseObj<>(getStoreID.getErrorMsg());
        long stamp = lock_stores.writeLock();
        logger.debug("catch WriteLock");
        try {
            Store st = stores.remove(getStoreID.value);
            closeStores.put(getStoreID.getValue(), st);
            logger.info("market update that Store #" + storeId + " close");
            //db
            if(dataServices!=null && dataServices.getStoreService()!=null){
                if(!dataServices.getStoreService().updateStoreIsOpen(storeId,false)){
                    logger.error(String.format("failed to updated store %d to close in db",storeId));
                }
            }
            notifyOwnersAndManagersStoreClosed(st, "close");
            return new DResponseObj<>(true);
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released WriteLock");
        }
    }

    public DResponseObj<Boolean> reopenStore(UUID userId, int storeId) {
        if (!isStoreClosed(storeId).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_NOT_CLOSED);
        Store s = closeStores.get(storeId);
        DResponseObj<User> logIN = userManager.getLoggedUser(userId);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        if (!s.getFounder().value.equals(logIN.value.getEmail().value)) {
            logger.warn("only founder can reopen store");
            return new DResponseObj<>(false, ErrorCode.NOPERMISSION);
        }
        DResponseObj<Boolean> ReopenStore = s.reopenStore();
        if (ReopenStore.errorOccurred()) {
            logger.warn("Store return that can not close this store");
            return ReopenStore;
        }

        long stamp = lock_stores.writeLock();
        logger.debug("catch WriteLock");
        try {
            Store st = closeStores.remove(storeId);
            stores.put(storeId, st);
            logger.info("market update that Store #" + storeId + " reopen");
            //db
            if(dataServices!=null && dataServices.getStoreService()!=null){
                if(!dataServices.getStoreService().updateStoreIsOpen(storeId,true)){
                    logger.error(String.format("failed to updated store %d to close in db",storeId));
                }
            }
            notifyOwnersAndManagersStoreClosed(s, "reopen");
            return new DResponseObj<>(true);
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released WriteLock");
        }
    }

    private void notifyOwnersAndManagersStoreClosed(Store store, String closeOpen) {
        logger.info(String.format("notifying owners/managers of %s store %d", closeOpen, store.getStoreId().value));
        String msg = String.format("Store [%d] was %s by %s.", store.getStoreId().value, closeOpen, store.getFounder().value);
        List<User> ownersAndManagers = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.owner).value;
        List<User> managersOfStore = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.manager).value;
        ownersAndManagers.addAll(managersOfStore);
        userManager.notifyUsers(ownersAndManagers, msg);
    }


    private void notifyOwnersAndManagersWithPermBID(Store store, String msg) {
        logger.info("notifying owners&managers with permission about BID");
        List<User> ownersAndManagers = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.owner).value;
        List<User> managersOfStore = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.manager).value;
        for(User m : managersOfStore) {
            if (!PermissionManager.getInstance().hasPermission(permissionType.permissionEnum.ManageBID, m, store).value) {
                managersOfStore.remove(m);
                if(managersOfStore.size()==0){
                    break;
                }
            }
        }
        ownersAndManagers.addAll(managersOfStore);  // after filter managers of stores
        userManager.notifyUsers(ownersAndManagers, msg);
    }
    //2.4.11
    //pre: the store exist in the system.
    //post: market ask UserManager about this user with this store.

    public DResponseObj<HashMap<String, List<String>>> getStoreRoles(UUID userId, int storeId) {
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.getStoreRoles, null);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;

        return store.getStoreRoles();
    }
    //2.4.13
    //pre: the store exist in the system. the user is owner of this store.
    //post: market ask the store about that.

    public DResponseObj<List<History>> getStoreOrderHistory(UUID userId, int storeId) {
        DResponseObj<Tuple<Store, ProductType>> result = checkValid(userId, storeId, permissionType.permissionEnum.getStoreOrderHistory, null);
        if (result.errorOccurred()) return new DResponseObj<>(result.getErrorMsg());
        Store store = result.getValue().item1;

        return store.getStoreOrderHistory();
    }


    //2.4.13 & 2.6.4 (only system manager)
    //pre: this store exist in the system.

    //post: market ask the store about that with USerEmail.

    public DResponseObj<List<List<History>>> getUserInfo(String userID, String email) {
        DResponseObj<Boolean> isMem = userManager.isMember(email);
        if (isMem.errorOccurred()) return new DResponseObj<>(null, isMem.errorMsg);
        DResponseObj<User> logIN = userManager.getLoggedUser(UUID.fromString(userID));
        if (logIN.errorOccurred()) return new DResponseObj<>(null, ErrorCode.NOTLOGGED);
        DResponseObj<Boolean> result = PermissionManager.getInstance().hasPermission(permissionType.permissionEnum.getUserInfo, logIN.value, null);
        if (result.errorOccurred()) return new DResponseObj<>(null, result.errorMsg);
        DResponseObj<Boolean> isOnline = userManager.isOnline(UUID.fromString(userID));
        if (isOnline.errorOccurred()) return new DResponseObj<>(isOnline.getErrorMsg());
        List<List<History>> res = new LinkedList<>();
        for (Store s : stores.values()) {
            DResponseObj<List<History>> list = s.getUserHistory(email);
            if (!list.errorOccurred() && !list.getValue().isEmpty()) res.add(list.getValue());

        }

        return new DResponseObj<>(res);
    }
    //2.2.1
    //pre: the store exist in the system.

    //post: market receive this store to the user.

    public DResponseObj<Store> getStore(int storeID) {

        if (dataServices == null && (storeID <= 0 | storeID >= storeCounter)) {
            logger.warn("the StoreID is illegal");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = lock_stores.readLock();
        logger.debug("catch the ReadLock.");
        try {
            Store s = stores.get(storeID);
            if(s == null) {
                return new DResponseObj<>(null, ErrorCode.STORE_IS_NOT_EXIST);
            }
            return new DResponseObj<>(s);
        } finally {
            lock_stores.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    public DResponseObj<Boolean> isFounder(UUID uuid, int storeId) {
        DResponseObj<User> user = userManager.getLoggedUser(uuid);
        if (user.errorOccurred()) return new DResponseObj<>(user.getErrorMsg());
        DResponseObj<String> email = user.getValue().getEmail();
        if (email.errorOccurred()) return new DResponseObj<>(email.getErrorMsg());
        DResponseObj<Store> s = getStore(storeId);
        if (s.errorOccurred() || s.value == null) return new DResponseObj<>(s.getErrorMsg());
        return new DResponseObj<>(s.value.getFounder().value.equals(email.value));
    }


    /*************************************************private methods*****************************************************/


    //target: check if for this UUID is System Manager.
    private DResponseObj<Boolean> isSystemMan(UUID uuid) {
        //get user online.
        DResponseObj<User> user = userManager.getLoggedUser(uuid);
        if (user.errorOccurred()) return new DResponseObj<>(user.getErrorMsg());

        //get email
        DResponseObj<String> demail = user.getValue().getEmail();
        if (demail.errorOccurred()) return new DResponseObj<>(demail.getErrorMsg());

        //check System maneger permission
        PermissionManager permissionManager = PermissionManager.getInstance();
        DResponseObj<Boolean> isSystemManager = permissionManager.isSystemManager(demail.getValue());
        if (isSystemManager.errorOccurred()) return new DResponseObj<>(isSystemManager.getErrorMsg());
        if (!isSystemManager.getValue()) {
            logger.warn("this user is not SystemManger - the ack canceled.");
            return new DResponseObj<>(ErrorCode.NOPERMISSION);
        }
        return new DResponseObj<>(true);
    }

    //target: this func chack that the card is valid

    private DResponseObj<Boolean> checkValidCard(String cardNumber, String exp, String pin) {

        //check card number
        DResponseObj<Boolean> cardNumberValid = Validator.isValidCreditCard(cardNumber);
        if (cardNumberValid.errorOccurred()) return new DResponseObj<>(cardNumberValid.getErrorMsg());
        if (!cardNumberValid.getValue()) {
            logger.warn("this CreditCard is invalid - card Number");
            return new DResponseObj<>(ErrorCode.CARD_NUMBER_ILLEGAL);
        }

        //check card exp
        DResponseObj<Boolean> cardExpValid = Validator.isValidCreditDate(exp);
        if (cardExpValid.errorOccurred()) return new DResponseObj<>(cardExpValid.getErrorMsg());
        if (!cardExpValid.getValue()) {
            logger.warn("this CreditCard is invalid - card Exp");
            return new DResponseObj<>(ErrorCode.CARD_EXP_ILLEGAL);
        }

        //check card pin
        DResponseObj<Boolean> cardPinValid = Validator.isValidPin(pin);
        if (cardPinValid.errorOccurred()) return new DResponseObj<>(cardPinValid.getErrorMsg());
        if (!cardPinValid.getValue()) {
            logger.warn("this CreditCard is invalid - pin Exp");
            return new DResponseObj<>(ErrorCode.CARD_PIN_ILLEGAL);
        }

        return new DResponseObj<>(true);
    }

    private DResponseObj<Tuple<Store, ProductType>> checkValid(UUID userId, int storeId, permissionType.permissionEnum permissionEnum, Integer productId) {
        DResponseObj<User> logIN = userManager.getLoggedUser(userId);// yakii whay not gett loged ???
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeId);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        PermissionManager permissionManager = PermissionManager.getInstance();
        DResponseObj<Boolean> hasPer = permissionManager.hasPermission(permissionEnum, logIN.getValue(), s.getValue());
        if (hasPer.errorOccurred()) return new DResponseObj<>(hasPer.getErrorMsg());
        if (!hasPer.getValue()) {
            logger.warn("this user does not have permission to " + permissionEnum.name() + "in the store #" + storeId);
            return new DResponseObj<>(ErrorCode.NOPERMISSION);
        }
        if (productId == null) return new DResponseObj<>(new Tuple(s.getValue(), null));

        DResponseObj<ProductType> p = getProductType(productId);
        if (p.errorOccurred()) return new DResponseObj<>(p.getErrorMsg());

        return new DResponseObj<>(new Tuple(s.getValue(), p.getValue()));
    }

    private DResponseObj<Store> checkValidRules(UUID userId, int storeId, permissionType.permissionEnum permissionEnum) {
        DResponseObj<User> logIN = userManager.getLoggedUser(userId);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeId);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        PermissionManager permissionManager = PermissionManager.getInstance();
        DResponseObj<Boolean> hasPer = permissionManager.hasPermission(permissionEnum, logIN.getValue(), s.getValue());
        if (hasPer.errorOccurred()) return new DResponseObj<>(hasPer.getErrorMsg());
        if (!hasPer.getValue()) {
            logger.warn("this user does not have permission to " + permissionEnum.name() + "in the store #" + storeId);
            return new DResponseObj<>(ErrorCode.NOPERMISSION);
        }

        return new DResponseObj<>(s.getValue());
    }


    private DResponseObj<ProductType> getProductType(int productID) {
        if (productID < 0) {
            logger.warn("productID is illegal");
            return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
        }
        long stamp = lock_TP.readLock();
        logger.debug("catch the ReadLock.");
        try {
            ProductType p = productTypes.get(productID);
            if (p == null) {
                logger.warn("the productID not exist in the system");
                return new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
            }
            return new DResponseObj<>(p);
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("released the ReadLock.");
        }
    }

    private DResponseObj<Boolean> isOnline(UUID userId) {
        DResponseObj<Boolean> online = userManager.isOnline(userId);
        if (online.errorOccurred()) return online;
        if (!online.getValue()) {
            logger.warn("the user not online");
            return new DResponseObj<>(ErrorCode.NOTONLINE);
        }
        return new DResponseObj<>(true);
    }

    private List<ProductType> getProductTypes() {
        List<ProductType> output = new ArrayList<>();
        long stamp = lock_TP.readLock();
        logger.debug("catch the readLock");
        try {
            output.addAll(productTypes.values());
            return output;
        } finally {
            lock_TP.unlockRead(stamp);
            logger.debug("release the radLock");
        }
    }

    private List<Store> getStores() {
        List<Store> output = new ArrayList<>();
        long stamp = lock_stores.readLock();
        logger.debug("catch the readLock");
        try {
            output.addAll(stores.values());
            return output;
        } finally {
            lock_stores.unlockRead(stamp);
            logger.debug("release the radLock");
        }
    }

    private List<Integer> getStoresIDs(List<Store> searchOn) {
        List<Integer> pIDs = new ArrayList<>();
        for (Store p : searchOn) {
            DResponseObj<Integer> pID = p.getStoreId();
            if (!pID.errorOccurred()) pIDs.add(pID.getValue());
        }
        return pIDs;
    }

    private List<Integer> getProductTYpeIDs(List<ProductType> searchOn) {
        List<Integer> pIDs = new ArrayList<>();
        for (ProductType p : searchOn) {
            DResponseObj<Integer> pID = p.getProductID();
            if (!pID.errorOccurred()) pIDs.add(pID.getValue());
        }
        return pIDs;

    }

    private List<Integer> getStoreIDs(List<Store> searchOn) {
        List<Integer> pIDs = new ArrayList<>();
        for (Store p : searchOn) {
            DResponseObj<Integer> pID = p.getStoreId();
            if (!pID.errorOccurred())
                pIDs.add(pID.getValue());
        }
        return pIDs;
    }

    private DResponseObj<Boolean> paymentAndSupplyConnct() {
        PaymentService p = PaymentService.getInstance();
        DResponseObj<String> check = p.ping();
        if (check.errorOccurred()) return new DResponseObj<>(check.getErrorMsg());
        DResponseObj<Boolean> isPayConnect = p.connect();
        if (isPayConnect.errorOccurred()) return new DResponseObj<>(isPayConnect.getErrorMsg());
        if (!isPayConnect.getValue()) {
            logger.warn("Payment didnt connect");
        }

        SupplyService supplyService = SupplyService.getInstance();
        check = supplyService.ping();
        if (check.errorOccurred()) return new DResponseObj<>(check.getErrorMsg());
        DResponseObj<Boolean> isSupplyConnect = supplyService.connect();
        if (isSupplyConnect.errorOccurred()) return new DResponseObj<>(isSupplyConnect.getErrorMsg());
        if (!isSupplyConnect.getValue()) {
            logger.warn("Supply didnt connect");
        }
        return new DResponseObj<>(true);
    }

    public DResponseObj<List<Store>> getAllStores() {
        List<Store> allStores = new ArrayList<>();
        stores.forEach((storeID, store) -> {
            allStores.add(store);
        });
        closeStores.forEach((integer, store) -> {
            allStores.add(store);
        });
        return new DResponseObj<>(allStores, -1);
    }

    public DResponseObj<List<ProductType>> getAllProductTypes() {
        List<ProductType> lst = new ArrayList<>();
        productTypes.forEach((ptid, pt) -> {
            lst.add(pt);
        });
        return new DResponseObj<>(lst);
    }

    public DResponseObj<List<ProductStore>> getAllProductsInStore(int storeID) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        Store store = stores.get(storeID);
        return store.getInventory().value.getAllProducts();
    }

    /**
     * before the call of this function - all permissions know that this store will be deleted for good
     *
     * @param storesToDelete
     * @return
     */
    public SLResponseOBJ<Boolean> deleteStoresFromMarket(List<Store> storesToDelete) {
        logger.info("deleting stores from the market");
        boolean b = true;
        long stamp = lock_stores.writeLock();
        List<Integer> storeIds = new ArrayList<>();
        for (Store store : storesToDelete) { //delete from memory for good
            storeIds.add(store.getStoreId().value);
            Store s = stores.remove(store.getStoreId().value);
            b = b & (s != null);
            if (s == null) {
                logger.warn(String.format("store %d is not in stores map", store.getStoreId().value));
            }
        }
        if (!b) return new SLResponseOBJ<>(false, ErrorCode.FAIL_DELETE_STORE);
        //notify the owners/managers that has permissions in those stores that the store is deleted.
        notifyOwnersAndManagersStoreDeleted(storesToDelete);
        //db
        if(dataServices!=null && dataServices.getStoreService()!=null){
            if (!dataServices.getStoreService().deleteStores(storeIds)) {
                logger.error("failed to delete list of stores from db");
            }
        }
        //update the product types
        productTypes.forEach((integer, productType) -> {
            storesToDelete.forEach(store -> {
               productType.removeStore(store.getStoreId().value);
            });
        });
        lock_stores.unlockWrite(stamp);
        return new SLResponseOBJ<>(true, -1);
    }


    private void notifyOwnersAndManagersStoreDeleted(List<Store> storesToDelete) {
        storesToDelete.forEach(store -> {
            logger.info(String.format("notifying owners/managers of deletion of store %d", store.getStoreId().value));
            String msg = String.format("Store [%d] was deleted permanently by System Manager", store.getStoreId().value);
            List<User> ownersAndManagers = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.owner).value;
            List<User> managersOfStore = PermissionManager.getInstance().getAllUserByTypeInStore(store, userTypes.manager).value;
            ownersAndManagers.addAll(managersOfStore);
            userManager.notifyUsers(ownersAndManagers, msg);
        });

    }

    public DResponseObj<Boolean> createBID(UUID userId, int storeID, int productID, int quantity, int totalPrice) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(userId);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String email = logIN.value.getEmail().value;
        DResponseObj<Boolean> created = s.value.createBID(email, productID, quantity, totalPrice);
        if (created.errorOccurred()) return created;
        logger.info(String.format("created new BID by [%s] in storeID [%d]", email, storeID));
        String msg = String.format("[%s] create new offer in storeID [%d] for productID [%d], quantity [%d], total price [%d].", email, storeID, productID, quantity, totalPrice);
        notifyOwnersAndManagersWithPermBID(s.value, msg);
        logger.info(String.format("notify all owners and managers about new BID of [%s]", email));
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> removeBID(UUID userId, int storeID, int productID) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(userId);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String email = logIN.value.getEmail().value;
        DResponseObj<Boolean> removed = s.value.removeBID(email, productID);
        if (removed.errorOccurred()) return removed;
        logger.info(String.format("remove BID by [%s] in storeID [%d] , productID [%d]", email, storeID, productID));
        String msg = String.format("[%s] remove his offer in storeID [%d] for productID [%d]", email, storeID, productID);
        notifyOwnersAndManagersWithPermBID(s.value, msg);
        logger.info(String.format("notify all owners and managers about remove BID of [%s]", email));
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> approveBID(UUID owner, String userEmail, int storeID, int productID) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(owner);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String ownerEmail = logIN.value.getEmail().value;
        DResponseObj<Boolean> approved = s.value.approveBID(ownerEmail, userEmail, productID);
        if (approved.errorOccurred()) return approved;
        logger.info(String.format("[%s] approved BID of [%s] in storeID [%d] , productID [%d]", ownerEmail, userEmail, storeID, productID));
        allApproved(s.value, userEmail, productID);
        return new DResponseObj<>(true);
    }

    private void allApproved(Store s, String userEmail, int productID) {
        if (s.allApprovedBID(userEmail, productID)) {
            String msg = String.format("your BID in storeID [%d] for ProductID [%d] approved, pay and get the product", s.getStoreId().value, productID);
            notifyUser(userEmail, msg);
        }
    }

    private void checkStoreBIDAllApproved(Store store) {
        DResponseObj<HashMap<String, List<Integer>>> allApproved = store.checkStoreBIDAllApproved();
        allApproved.value.forEach((userEmail, productsID) -> {
            productsID.forEach(id -> {
                String msg = String.format("your BID in storeID [%d] for ProductID [%d] approved, pay and get the product", store.getStoreId().value, id);
                notifyUser(userEmail, msg);
            });
        });
    }

    public DResponseObj<Boolean> rejectBID(UUID owner, String userEmail, int storeID, int productID) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(owner);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String ownerEmail = logIN.value.getEmail().value;
        DResponseObj<Boolean> rejected = s.value.rejectBID(ownerEmail, userEmail, productID);
        if (rejected.errorOccurred()) return rejected;
        logger.info(String.format("[%s] reject BID of [%s] in storeID [%d] , productID [%d]", ownerEmail, userEmail, storeID, productID));
        String msg = String.format("your BID in storeID [%d] for ProductID [%d] was rejected", storeID, productID);
        notifyUser(userEmail, msg);
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> counterBID(UUID owner, String userEmail, int storeID, int productID, int newTotalPrice) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(owner);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String ownerEmail = logIN.value.getEmail().value;
        DResponseObj<Boolean> countered = s.value.counterBID(ownerEmail, userEmail, productID, newTotalPrice);
        if (countered.errorOccurred()) return countered;
        logger.info(String.format("[%s] countered BID of [%s] in storeID [%d] , productID [%d] to new total price [%d]", ownerEmail, userEmail, storeID, productID, newTotalPrice));
        String msg = String.format("your got counter BID in storeID [%d] for ProductID [%d], the new total price is [%d]", storeID, productID, newTotalPrice);
        notifyUser(userEmail, msg);
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> responseCounterBID(UUID user, int storeID, int productID, boolean approve) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(user);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String userEmail = logIN.value.getEmail().value;
        DResponseObj<Boolean> responseCounterBID = s.value.responseCounterBID(userEmail, productID, approve);
        if (responseCounterBID.errorOccurred()) return responseCounterBID;
        logger.info(String.format("[%s] accept countered BID in storeID [%d] , productID [%d]", userEmail, storeID, productID));
        String msgToOwners;
        msgToOwners = approve ? String.format("[%s] approved his countered BID in storeID [%d] for productID [%d]", userEmail, storeID, productID) :
                String.format("[%s] rejected his countered BID in storeID [%d] for productID [%d]", userEmail, storeID, productID);
        notifyOwnersAndManagersWithPermBID(s.value, msgToOwners);
        if(approve && s.value.allApprovedBID(userEmail,productID)){
            String msg = String.format("your BID in storeID [%d] for ProductID [%d] approved, pay and get the product", s.value.getStoreId().value, productID);
            notifyUser(userEmail, msg);
        }
        return new DResponseObj<>(true);
    }

    public DResponseObj<String> getBIDStatus(UUID owner, String userEmail, int storeID, int productID) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> s = checkValidRules(owner, storeID, permissionType.permissionEnum.ManageBID);
        if (s.errorOccurred()) return new DResponseObj<>(null, s.getErrorMsg());
        DResponseObj<String> approves = s.value.getBIDStatus(userEmail, productID);
        if (approves.errorOccurred()) return approves;
        logger.info(String.format("got approves list - BID in storeID [%d] , productID [%d]", storeID, productID));
        return new DResponseObj<>(approves.value);
    }


    public DResponseObj<Boolean> buyBID(UUID userId, int storeID, int productID, String city, String adress, int apartment, String cardNumber, String exp, String pin) {
        //check valid Card
        DResponseObj<Boolean> checkValidCard = checkValidCard(cardNumber, exp, pin);
        if (checkValidCard.errorOccurred()) return new DResponseObj<>(checkValidCard.getErrorMsg());

        //get User
        DResponseObj<User> user = userManager.getOnlineUser(userId);
        if (user.errorOccurred()) return new DResponseObj<>(user.getErrorMsg());

        //check PaymentService AND check SupplyService
//         DResponseObj<Boolean> checkServices = paymentAndSupplyConnct();
//        if (checkServices.errorOccurred()) return new DResponseObj<>(checkServices.getErrorMsg());
//        if (!checkServices.getValue()){
//            logger.warn("this External Services dont connect.");
//            return new DResponseObj<>(ErrorCode.EXTERNAL_SERVICE_ERROR);
//        }

        //check BID
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        DResponseObj<BID> canBuyBID = s.value.canBuyBID(user.getValue().getEmail().value, productID); //return quantity to buy
        if (canBuyBID.errorOccurred()) return new DResponseObj<>(canBuyBID.getErrorMsg());
        int quantity = canBuyBID.value.getQuantity();
        int finalPrice = canBuyBID.value.getLastPrice();
        ShoppingBag BID = new ShoppingBag(s.value,user.value.getEmail().value);
        BID.addProduct(productID, quantity);
        DResponseObj<Boolean> res = purchase.orderBID(user.getValue(), storeID, BID, finalPrice, city, adress, apartment, cardNumber, exp, pin);
        if (res.errorOccurred()) return new DResponseObj<>(null, ErrorCode.ORDER_FAIL);
        canBuyBID.value.ChangeStatusProductBought();
        notifyOwnersPurchaseBID(user.value, storeID);
        return res;

    }

    public DResponseObj<List<BID>> getMyBIDs(UUID user, int storeID) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(user);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String userEmail = logIN.value.getEmail().value;
        DResponseObj<List<BID>> myBids = s.value.getMyBIDs(userEmail);
        if (myBids.errorOccurred()) return myBids;
        logger.info(String.format("[%s] get his BIDs in storeID [%d] ]", userEmail, storeID));
        return new DResponseObj<>(myBids.value);
    }

    public DResponseObj<HashMap<Integer, List<BID>>> getAllOffersBIDs(UUID owner, int storeID) {
        if (isStoreClosed(storeID).value) return new DResponseObj<>(null, ErrorCode.STORE_IS_CLOSED);
        DResponseObj<User> logIN = userManager.getLoggedUser(owner);
        if (logIN.errorOccurred()) return new DResponseObj<>(logIN.getErrorMsg());
        DResponseObj<Store> s = getStore(storeID);
        if (s.errorOccurred()) return new DResponseObj<>(s.getErrorMsg());
        String ownerEmail = logIN.value.getEmail().value;
        DResponseObj<HashMap<Integer, List<BID>>> bids = s.value.getAllOffersBIDs();
        if (bids.errorOccurred()) return bids;
        logger.info(String.format("[%s] get all bids of storeID [%d]", ownerEmail, storeID));
        return bids;
    }

    private void notifyUser(String userEmail, String msg) {
        DResponseObj<User> userBID = userManager.getMember(userEmail);
        if (userBID.errorOccurred()) return;
        List<User> l = new ArrayList<>();
        l.add(userBID.value);
        userManager.notifyUsers(l, msg);
    }



    class Tuple<E, T> {
        E item1;
        T item2;

        public Tuple(E item1, T item2) {
            this.item1 = item1;
            this.item2 = item2;
        }
    }

    /*************************************************for testing*********************************************************/

    /* forbidden to use with this function except Test*/
    public void setForTesting() {
        userManager = new UserManagerStab();
        initMarketTest();
    }

    public void setForIntegrationTestingWithUserManager() {
        userManager = new UserManager();
        initMarketTest();

    }

    public DResponseObj<Integer> getRate(UUID uuid, int productTypeID) {

        if (userManager.isOnline(uuid).value) {
            if (!productTypes.containsKey(productTypeID)) {
                return new DResponseObj<>(null, ErrorCode.PRODUCTNOTEXIST);
            }

            ProductType p = productTypes.get(productTypeID);
            DResponseObj<Integer> rat = p.getRate();
            return rat;
        } else return new DResponseObj<>(ErrorCode.NOTONLINE);
    }

    public DResponseObj<Boolean> setRate(UUID uuid, int productTypeID, int rate) {
        if (userManager.isOnline(uuid).value) {
            if (!productTypes.containsKey(productTypeID)) {
                return new DResponseObj<>(null, ErrorCode.PRODUCTNOTEXIST);
            }

            ProductType p = productTypes.get(productTypeID);
            DResponseObj<Boolean> rat = p.setRate(rate);
            return rat;
        } else return new DResponseObj<>(ErrorCode.NOTONLINE);

    }

    public void setForIntegrationTestingWithStore() {
        userManager = new UserManagerStab();
        purchase = new PurchaseStab();

        for (int i = 0; i < 10; i++) {
            ProductType p = new ProductType(productCounter, "product" + i, "hello", 3);
            p.setRate(productCounter);
            p.setCategory(productCounter % 3);
            productTypes.put(productCounter++, p);
        }

        for (int i = 0; i < 10; i++) {
            Store store = OpenNewStore("name" + i, "founder" + 1, new DiscountPolicy(), new BuyPolicy());
            for (ProductType product : productTypes.values()) {
                store.addNewProduct(product, 5, 10.2);
            }
            store.newStoreRate(i + 1);
        }


    }

    private void initMarketTest() {
        init();
        purchase = new PurchaseStab();
        for (int i = 0; i < 10; i++) {
            ProductType p = new ProductType(productCounter, "product" + i, "hello", 3);
            p.setRate(i);
            p.setCategory(i % 2 == 0 ? 2 : 1);
            productTypes.put(productCounter++, p);
        }
        for (int i = 0; i < 10; i++) {
            Store s = new StoreStab(storeCounter);
            s.addNewProduct(getProductType(1).getValue(), 100, 0.5);
            s.newStoreRate(storeCounter);
            stores.put(storeCounter++, s);
        }
    }

    private Store OpenNewStore(String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy) {

        long stamp = lock_stores.writeLock();
        logger.debug("catch the WriteLock");
        try {
            Store store = new Store(storeCounter++, name, discountPolicy, buyPolicy, founder);
            stores.put(store.getStoreId().value, store);
            logger.info("new Store join to the Market");
            return store;
        } finally {
            lock_stores.unlockWrite(stamp);
            logger.debug("released the WriteLock");
        }
    }
}
