package main.System.Server.Domain.Market;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class permissionType {
    static public List<permissionEnum> memberPermissions=new ArrayList<>
            (Arrays.asList(permissionEnum.Login,permissionEnum.GetStoreInfo,permissionEnum.ProductSearch,
                    permissionEnum.AddProductToShoppingBag,permissionEnum.order,permissionEnum.Logout,permissionEnum.OpenNewStore));

    static public List<permissionEnum> managerPermissions=new ArrayList<>
            (Arrays.asList(permissionEnum.Login,permissionEnum.GetStoreInfo,permissionEnum.ProductSearch,
                    permissionEnum.AddProductToShoppingBag,permissionEnum.order,permissionEnum.Logout,permissionEnum.OpenNewStore,permissionEnum.getStoreOrderHistory));

    static public List<permissionEnum> ownerPermissions=new ArrayList<>
            (Arrays.asList(permissionEnum.Login,permissionEnum.GetStoreInfo,permissionEnum.ProductSearch,
                    permissionEnum.AddProductToShoppingBag,permissionEnum.order,permissionEnum.Logout,permissionEnum.OpenNewStore,permissionEnum.getStoreOrderHistory,
                    permissionEnum.addNewProductToStore,permissionEnum.deleteProductFromStore,permissionEnum.setProductInStore, permissionEnum.addNewStoreOwner,
                    permissionEnum.addNewStoreManager,permissionEnum.setManagerPermissions,permissionEnum.deleteStore,permissionEnum.getStoreRoles));

   // static public List<permissionEnum> systemManagerPermissions=new ArrayList<>()


    public enum permissionEnum {

        //guest  מבקר-אורח

        //// 1 .כניסה:
        //// 2 .יציאה:
        //TODO 3 .רישום למערכת המסחר:
        Login, //4

        GetStoreInfo,  //1 .קבלת מידע על חנויות בשוק ועל המוצרים בחנויות.
        ProductSearch,  //2 .חיפוש מוצרים
        AddProductToShoppingBag, //3 .שמירת מוצרים
        //TODO 4 .בדיקת תכולת עגלת הקניות וביצוע שינויים
        order,//5 .רכישת עגלת הקניות,



        //member  //3 .פעולות קנייה של מבקר-מנוי:
        Logout,  //1 .ביטול זיהוי )logout :
        OpenNewStore,  //2 .פתיחת חנות:

        //next versions //
        //3 .כתיבת ביקורת
        //4 .דירוג מוצר וחנות
        //5 .שליחת שאלות/פניו
        //6 .הגשת תלונה
        //7 .קבלת מידע על היסטוריית רכישות אישית.
        //8 .קבלת מידע ושינוי פרטים מזהים.
        //9 .אבטחת רישום למערכת המסחר:

        // store manager
        //12 .קבלת מידע ומתן תגובה//next versions
        getStoreOrderHistory, //13 .קבלת מידע על היסטוריית רכישות בחנות.
        // , בהתאם להרשאות

        // owner    //4 .פעולות של מבקר-מנוי בתפקידו כבעל חנות

        // .ניהול מלאי:
        addNewProductToStore,
        deleteProductFromStore,
        setProductInStore,

        //TODO 2.שינוי סוגי וכללי )מדיניות( קניה והנחה של חנות
        //3 .קביעת אילוצי עקיבות עבור חנות://next versions
        addNewStoreOwner, //4 .מינוי בעל-חנות:

        //5 .הסרת מינוי בעל-חנות://next versions
        addNewStoreManager, //6 .מינוי מנהל-חנות:
        setManagerPermissions, //7 .שינוי הרשאות של מנהל-חנות:
        //8 .הסרת מינוי של מנהל-חנות//next versions
        deleteStore,//check founder or system manager //9 .סגירת חנות:
        //10 .פתיחת חנות שנסגרה//next versions
        getStoreRoles, //11 .בקשה למידע על תפקידים בחנות:


        addFounder,
        // system manager
        //1 .סגירת חנות לצמיתות://next versions:
        //2 .ביטול )הסרת( מנוי של השוק://next versions
        //next versions3// .קבלת מידע ומתן תגובה:
        //4 .קבלת מידע על היסטוריית רכישות בחנות:
        //next versions// 5 .קבלת מידע על התנהלות המערכת:
    }

}
