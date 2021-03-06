export const errorCode = new Map([
    [10, 'NOTLOGGED'],
    [11, 'NOTONLINE'],
    [12, 'NOTMEMBER'],
    [13, 'NOTVALIDINPUT'],
    [14, 'NOTSTRING'],
    [15, 'NEGATIVENUMBER'],
    [16, 'NOTUSER'],
    [17, 'GRANTEANDGRANTORSAMEUSER'],
    [18, 'ALLREADYOWNER'],
    [19, 'NOTOWNER'],
    [20, 'ALLREADYMANAGER'],
    [21, 'NOPERMISSION'],
    [22, 'MISTAKEPERMISSIONTYPE'],
    [23, 'ALLREADYHAVESYSTEMMANAGER'],
    [24, 'NOTLEGALDATE'],
    [25, 'NOT_MOVED_PRICE_FOR_DISCOUNT'],
    [27, 'PRODUCT_NOT_FOR_SELL_NOW'],
    [28, 'TRY_TO_BUY_MORE_THEN_MAX_QUANTITY_TO_BUY'],
    [29, 'NOT_PASS_THE_MIN_QUANTITY_TO_BUY'],
    [30, 'PRODUCTNOTEXIST'],
    [31, 'PRODUCTALLREADYINSTORE'],
    [32, 'PRODUCTNOTEXISTINSTORE'],
    [33, 'NOT_AVAILABLE_QUANTITY'],
    [34, 'BUY_RULE_NOT_EXIST'],
    [35, 'BUY_RULE_IS_NULL'],
    [36, 'TRY_TO_BUY_TOO_MANY_FROM_CATEGORY'],
    [37, 'BUYER_TOO_YOUNG_TO_BUY_THIS_PRODUCT'],
    [38, 'BUYER_CAN_NOT_BUY_THIS_HOUR'],
    [39, 'USER_CAN_NOT_BUY_IN_THIS_STORE'],
    [41, 'STORESTAYINTHEPRODUCTTYPE'],
    [42, 'CANNOTCLOSESTORE'],
    [44, 'DISCOUNT_RULE_NOT_EXIST'],
    [50, 'SERVICEALREADYCONNECT'],
    // [50, 'SERVICEALREADYDISCONNECT'],
    [51, 'EXISTEXTERNALSERVICEWITHTHISNAME'],
    [52, 'NOTEXISTEXTERNALSERVICEWITHTHISNAME'],
    [53, 'ILLEGALCARD'],
    [54, 'EXTERNAL_SERVICE_ERROR'],
    [55, 'SERVICE_NOT_CONNECTED'],
    [60, 'PAYMENT_FAIL'],
    [61, 'CARD_NUMBER_ILLEGAL'],
    [62, 'CARD_EXP_ILLEGAL'],
    [63, 'CARD_PIN_ILLEGAL'],
    [64, 'PRODUCT_DOESNT_EXIST_IN_THE_STORE'],
    [65, 'CART_FAIL'],
    [66, 'NO_STORE_IN_BAG'],
    [67, 'EMPTY_CART'],
    [68, 'ORDER_FAIL'],
    [100, 'NOT_VALID_PASSWORD'],
    [101, 'NOT_VALID_EMILE'],
    [102, 'NOT_VALID_PHONE'],
    [103, 'INVALID_PERMISSION_TYPE'],

    ])
 export default errorCode;