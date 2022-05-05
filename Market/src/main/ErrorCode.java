package main;

public class ErrorCode {
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
   public static int NOT_MANAGER = 53;


   public static int PRODUCTNOTEXIST = 30;
   public static int PRODUCTALLREADYINSTORE = 31;
   public static int PRODUCTNOTEXISTINSTORE = 32;


   public static int STORENOTINTHEPRODUCTTYPE = 40;
   public static int STORESTAYINTHEPRODUCTTYPE = 41;
   public static int CANNOTCLOSESTORE = 42;
   public static int ROLLBACK =43;

   public static int SERVICEALREADYCONNECT = 50;
   public static int SERVICEALREADYDISCONNECT = 50;
   public static int EXISTEXTERNALSERVICEWITHTHISNAME =51;
   public static int NOTEXISTEXTERNALSERVICEWITHTHISNAME =52;

   public static int ILLEGALCARD = 53;

   public static int EXTERNAL_SERVICE_ERROR =54;

   public static int PAYMENT_FAIL = 60;
   public static int CARD_NUMBER_ILLEGAL = 61;
   public static int CARD_EXP_ILLEGAL = 62;
   public static int CARD_PIN_ILLEGAL = 63;
   public static int NOT_VALID_PASSWORD = 100;
   public static int NOT_VALID_EMILE = 101;
   public static int NOT_VALID_PHONE = 102;

}
