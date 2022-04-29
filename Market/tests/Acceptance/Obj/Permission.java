package Acceptance.Obj;

public class Permission {
    public Permission(){}
    //         Permissions type:
    //Guest:
    public static final String GET_STORE_INFO = "GetStoreInfo";
    public static final String PRODUCT_SEARCH = "ProductSearch";
    public static final String ADD_PRODUCT_TO_SHOPPING_BAG = "AddProductToShoppingBag";
    public static final String ORDER = "order";
    //Member:
    public static final String LOGIN = "Login";
    public static final String LOGOUT = "Logout";
    public static final String OPEN_NEW_STORE = "OpenNewStore";
    //StoreManager:
    public static final String GET_ORDER_HISTORY = "getOrderHistory";
    //Owner:
    public static final String ADD_NEW_PRODUCT_TO_STORE = "addNewProductToStore";
    public static final String DELETE_PRODUCT_FROM_STORE = "deleteProductFromStore";
    public static final String SET_PRODUCT_IN_STORE = "setProductInStore";
    public static final String ADD_NEW_STORE_OWNER = "addNewStoreOwner";
    public static final String ADD_NEW_STORE_MANAGER = "addNewStoreManager";
    public static final String SET_MANAGER_PERMISSIONS = "setManagerPermissions";
    public static final String DELETE_STORE = "deleteStore";
    public static final String GET_STORE_ROLES = "getStoreRoles";
    public static final String ADD_FOUNDER = "addFounder";
    //System Manager:
}
