package com.example.demo.Domain;

import com.example.demo.Domain.Response.DResponseObj;

public class ErrorCode {
    public static int STORE_IS_CLOSED = 8;
    public static int CAN_NOT_REMOVE_FOUNDER_PERMISSION = 9;
    public static int NOTLOGGED = 10;
    public static int NOTONLINE = 11;
    public static int NOTMEMBER = 12;
    public static int NOTVALIDINPUT = 13;
    public static int NOTSTRING = 14;
    public static int NEGATIVENUMBER = 15;
    public static int NOTUSER = 16;
    public static int GRANTEANDGRANTORSAMEUSER = 17;

    public static int ALLREADYOWNER = 18;
    public static int NOTOWNER = 19;
    public static int ALLREADYMANAGER = 20;
    public static int NOPERMISSION = 21;
    public static int MISTAKEPERMISSIONTYPE = 22;
    public static int ALLREADYHAVESYSTEMMANAGER = 23;
    public static int NOTLEGALDATE = 24;
    public static int NOT_MOVED_PRICE_FOR_DISCOUNT =25;
    public static int INVALID_ARGS_FOR_RULE = 26;
    public static int PRODUCT_NOT_FOR_SELL_NOW = 27;
    public static int TRY_TO_BUY_MORE_THEN_MAX_QUANTITY_TO_BUY = 28;
    public static int NOT_PASS_THE_MIN_QUANTITY_TO_BUY = 29;
    public static int PRODUCTNOTEXIST = 30;
    public static int PRODUCTALLREADYINSTORE = 31;
    public static int PRODUCTNOTEXISTINSTORE = 32;
    public static int NOT_AVAILABLE_QUANTITY = 33;
    public static int BUY_RULE_NOT_EXIST = 34;
    public static int BUY_RULE_IS_NULL = 35;
    public static int TRY_TO_BUY_TOO_MANY_FROM_CATEGORY = 36;
    public static int BUYER_TOO_YOUNG_TO_BUY_THIS_PRODUCT = 37;
    public static int BUYER_CAN_NOT_BUY_THIS_HOUR = 38;
    public static int USER_CAN_NOT_BUY_IN_THIS_STORE = 39;
    public static int STORENOTINTHEPRODUCTTYPE = 40;
    public static int STORESTAYINTHEPRODUCTTYPE = 41;
    public static int CANNOTCLOSESTORE = 42;
    public static int ROLLBACK =43;
    public static int DISCOUNT_RULE_NOT_EXIST = 44;
    public static int INVALID_PERECNT_DISCOUNT = 45;
    public static int INVALIDQUANTITY =46;
    public static int BIDNOTEXISTS =47;
    public static int NOT_MANAGER = 48;
    public static final int BIDALLREADYEXISTS = 49;
    public static int SERVICEALREADYCONNECT = 50;
    public static int SERVICEALREADYDISCONNECT = 50;
    public static int EXISTEXTERNALSERVICEWITHTHISNAME =51;
    public static int NOTEXISTEXTERNALSERVICEWITHTHISNAME =52;
    public static int ILLEGALCARD = 53;
    public static int EXTERNAL_SERVICE_ERROR =54;
    public static int SERVICE_NOT_CONNECTED = 55;
    public static final int STATUSISNOTWAITINGAPPROVES = 56;
    public static final int STATUSISNOTCOUNTERBID = 57;
    public static final int STATUSISNOTBIDAPPROVED = 58;


    public static int PAYMENT_FAIL = 60;
    public static int CARD_NUMBER_ILLEGAL = 61;
    public static int CARD_EXP_ILLEGAL = 62;
    public static int CARD_PIN_ILLEGAL = 63;

    public static int PRODUCT_DOESNT_EXIST_IN_THE_STORE = 64;
    public static int CART_FAIL = 65;
    public static int NO_STORE_IN_BAG = 66;
    public static int EMPTY_CART = 67;
    public static int ORDER_FAIL = 68;
    public static int NOT_VALID_PASSWORD = 100;
    public static int NOT_VALID_EMILE = 101;
    public static int NOT_VALID_PHONE = 102;
    public static int INVALID_PERMISSION_TYPE = 103;
    public static final int FAIL_DELETE_STORE = 104;
    public static int NOTADMIN = 300;

    public static int HANDSHAKEFAIL = 400;
    public static int HANDSHAKE_BEFOREPAY_FAIL = 401;
    public static int CARD_NOTRIGHT = 402;
    public static int PAYAPIFAIL = 403;
    public static int CANCELPAYAPIFAIL = 404;
    public static int ADDRESS_NOTRIGHT = 405;
    public static int SUPPLY_FAIL = 406;
    public static int CANCELSUPPLYAPIFAIL = 407;


}
