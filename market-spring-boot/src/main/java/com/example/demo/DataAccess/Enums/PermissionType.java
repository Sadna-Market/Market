package com.example.demo.DataAccess.Enums;

public enum PermissionType {
    // ------------------------------1 .פעולות כלליות של מבקר-אורח:-----------------------------------
    guestVisit,//// 1 .כניסה:
    guestLeave,//// 2 .יציאה:
    addNewMember,// 3 .רישום למערכת המסחר:
    login, // 4. כניסה מזוהה

// ------------------------------2 .פעולות קנייה של מבקר-אורח:----------------------------------

    getStoreInfo,  //1 .קבלת מידע על חנויות בשוק ועל המוצרים בחנויות. //TODO דור אמור להוסיף , לבדוק שזה אותו שם ושזיש את זה גם בפסייד
    getInfoProductInStore,
    productSearch,  //2 .חיפוש מוצרים
    searchProductByName,
    searchProductByDesc,
    searchProductByRate,
    searchProductByCategory,
    searchProductByRangePrices,

    addProductToShoppingBag, //3 .שמירת מוצרים
    getShoppingCart,// 4 .בדיקת תכולת עגלת הקניות וביצוע שינויים
    removeProductFromShoppingBag,
    setProductQuantityShoppingBag,
    orderShoppingCart,//5 .רכישת עגלת הקניות,

// -----------------------------3 .פעולות קנייה של מבקר-מנוי:----------------------------------


    logout,  //1 .ביטול זיהוי )logout :
    openNewStore,  //2 .פתיחת חנות:

    //next versions //
    //3 .כתיבת ביקורת
    //4 .דירוג מוצר וחנות
    //5 .שליחת שאלות/פניו
    //6 .הגשת תלונה
    //7 .קבלת מידע על היסטוריית רכישות אישית.
    //8 .קבלת מידע ושינוי פרטים מזהים.
    //9 .אבטחת רישום למערכת המסחר:


    //12 .קבלת מידע ומתן תגובה//next versions
    getStoreOrderHistory, //13 .קבלת מידע על היסטוריית רכישות בחנות.
    // + בהתאם להרשאות

// -----------------------------4 .פעולות של מבקר-מנוי בתפקידו כבעל חנות:----------------------------------

    // 1.ניהול מלאי:
    addNewProductToStore,
    deleteProductFromStore,
    setProductPriceInStore,
    setProductQuantityInStore,
//        AddNewStoreOwner,
//        AddNewStoreManger,

    addNewBuyRule,
    removeBuyRule,
    addNewDiscountRule,
    removeDiscountRule,
    ManageBID,
    //3 .קביעת אילוצי עקיבות עבור חנות://next versions
    addNewStoreOwner, //4 .מינוי בעל-חנות:
    removeStoreOwner,
    //5 .הסרת מינוי בעל-חנות://next versions
    addNewStoreManager, //6 .מינוי מנהל-חנות:
    setManagerPermissions, //7 .שינוי הרשאות של מנהל-חנות:
    //8 .הסרת מינוי של מנהל-חנות//next versions
    closeStore, //9 .סגירת חנות:
    reopenStore,
    //10 .פתיחת חנות שנסגרה//next versions
    getStoreRoles, //11 .בקשה למידע על תפקידים בחנות:

    // system manager
    getUserInfo,
    getAllLoggedInUsers,
    getAllLoggedOutUsers,
    cancelMembership,
    //1 .סגירת חנות לצמיתות://next versions:
    //next versions3// .קבלת מידע ומתן תגובה:
    //4 .קבלת מידע על היסטוריית רכישות בחנות:
    //next versions// 5 .קבלת מידע על התנהלות המערכת:

}
