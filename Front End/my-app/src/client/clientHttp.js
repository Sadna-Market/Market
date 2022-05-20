import axios from "axios";

export const createApiClientHttp = () => {
    const UrlBase = "http://localhost:8090/";
    const apiUrl = UrlBase.concat("api/");
    const jsonForAll = new Map([
        ['errorMsg', -1],
        ['value', "absd324234bkb4kj234b2k3b423bn54b3jk6h3bjk54"],
        ['aaa', -1],
        ['ccc', -1],
    ])
    //{errorMsg: -1, value: '461182d2-0220-4d56-9aac-b880a5a86d06'}
    return {
        initMarket : async (email,password,phoneNumber) =>
        {
            return jsonForAll;
        },



        guestVisit:async ()=>
        {
            return jsonForAll;

        },



        guestLeave :async (uuid)=>
        {
            return jsonForAll;

        },


        addNewMember:async(uuid,email,password,phonenumber)=>{
            return jsonForAll;

        },


        login:async(uuid,email,password)=>
        {
            return jsonForAll;


        },



        getStore : async (storeid)=>{
            return jsonForAll;


        },

        getInfoProductInStore :async (storid,productid)=>{
            return jsonForAll;

        },


        searchProductByName : async(productName)=>{
            return jsonForAll;

        },

        searchProductByNamelist:async(l,productname)=>{
            return jsonForAll;

        },

        filterByName :async (l , productName) =>{
            return jsonForAll;

        },

        searchProductByDesc :async (desc)=>{
            return jsonForAll;

        },

        searchProductByDescList :async (l,desc)=>{
            return jsonForAll;

        },



        filterByDesc : async(l,desc)=>{
            return jsonForAll;

        },


        searchProductByRate :async (rate) =>{
            return jsonForAll;

        },


        searchProductByRateList :async (l,rate)=>{
            return jsonForAll;

        },

        filterByStoreRate : async(l,minRate)=>{
            return jsonForAll;

        },


        searchProductByRangePrices:async (productId,min,max)=>{
            return jsonForAll;


        },

        searchProductByRangePricesList:async (l,productId,min,max)=>{
            return jsonForAll;

        },


        filterByRangePrices: async(l,min,max)=>{
            return jsonForAll;

        },


        addNewProductType :async (uuid,namep,descriptionp,categoryp)=>{
            return jsonForAll;


        },

        addProductToShoppingBag :async (uuid,storeid,productid,quantity)=>{
            return jsonForAll;

        },


        getShoppingCart :async (uuid)=>{
            return jsonForAll;


        },


        removeProductFromShoppingBag :async (uuid,storeid,productid)=>{
            return jsonForAll;

        },


        setProductQuantityShoppingBag :async (uuid,productId,storeid,quantity)=>{
            return jsonForAll;

        },




        orderShoppingCart: async (uuid,city,adress,apartment,CreditCard,CreditDate,pin)=>{
            return jsonForAll;


        },

        logout:async (uuid)=>{
            return jsonForAll;


        },

        changePassword:async (uuid,email,Password,newPassword)=>{
            return jsonForAll;


        },


        openNewStore: async (uuid,name,founder)=>{
            return jsonForAll;

        },


        addNewProductToStore: async(uuid,storeid,productId,price,quantity)=>{
            return jsonForAll;

        },


        deleteProductFromStore: async(uuid,storeId,productId)=>{
            return jsonForAll;


        },


        setProductPriceInStore: async(uuid,storeid,productId,price)=>{
            return jsonForAll;


        },


        setProductQuantityInStore: async(uuid,storeid,productId,quantity)=>{
            return jsonForAll;


        },



        addNewStoreOwner: async(uuid, storeId,OwnerEmail)=>{
            return jsonForAll;


        },



        addNewStoreManger: async(uuid, storeId,mangerEmail)=>{
            return jsonForAll;

        },



        setManagerPermissions: async(uuid, storeId,mangerEmail,per,onof)=>{
            return jsonForAll;

        },



        closeStore: async(uuid, storeId)=>{
            return jsonForAll;


        },

        getStoreRoles:async(uuid, storeId)=>{
            return jsonForAll;

        },

        getStoreOrderHistory: async(uuid, storeId)=>{
            return jsonForAll;

        },


        getUserInfo: async(uuid,email)=>{
            return jsonForAll;

        }
        ,

        filterByRate :async(l,minRate)=>
        {
            return jsonForAll;

        },

        searchProductByCategory:async(category)=>
        {            return jsonForAll;


        },
        searchProductByCategoryList:async(l,category)=>
        {
            return jsonForAll;


        },

        filterByCategory:async(l,category)=>
        {
            return jsonForAll;


        },

        searchProductByStoreRate:async(rate)=>
        {
            return jsonForAll;


        },

        searchProductByStoreRateList:async(l,rate)=>
        {
            return jsonForAll;


        },






    }
}

async function test (){
    console.log("00000000000000000000000000000")
    let res = createApiClientHttp();
    // let uuid =await res.guestVisit()

    let json =await res.initMarket("daniel@gmail.com","123abcA!","0526464201");
    console.log("initMarket")
     console.log(json)
    // json =await res.login(uuid.value,"Dsds","Dsdsd");
    // console.log(json)
//     json =await res.addNewMember(uuid.value,"dfdf","Fdfd","fdfd");
//     console.log(json)

//     json =await res.getStore(2);
//     console.log(json)
//     json =await res.getInfoProductInStore(33,33);
//     console.log(json)
//     json =await res.searchProductByName("DSd");
//     console.log(json)
//     json =await res.searchProductByNamelist([2,3],"DSd");
//     console.log(json)
//     json =await res.filterByName([2,3],"Dsd");
//     console.log(json)
//     json =await res.searchProductByDesc("Dds");
//     console.log(json)
//     json =await res.searchProductByDescList([1,2],"Fdf");
//     console.log(json)
//     json =await res.filterByDesc([1,2],"Dsds");
//     console.log(json)
//     json =await res.searchProductByRate(33);
//     console.log(json)
//     json =await res.searchProductByRateList([1,2],33);
//     console.log(json)
//     json =await res.filterByRate([2,3],33);
//     console.log(json)
//     json =await res.searchProductByCategory(3);
//     console.log(json)
//     json =await res.searchProductByCategoryList([1],3);
//     console.log(json)
//     json =await res.filterByCategory([2],3);
//     console.log(json)
//     json =await res.searchProductByStoreRate(3);
//     console.log(json)
//     json =await res.searchProductByStoreRateList([3],3);
//     console.log(json)
//     json =await res.filterByStoreRate([3],2);
//     console.log(json)
//     json =await res.searchProductByRangePrices(3,3,2);
//     console.log(json)
//     json =await res.searchProductByRangePricesList([2],2,2,2);
//     console.log(json)
//     json =await res.filterByRangePrices([2],2,2);
//     console.log(json)
//     json =await res.addNewProductType(uuid.value,"dsd","Dsd",2);
//     console.log(json)
//     json =await res.addProductToShoppingBag(uuid.value,3,3,3);
//     console.log(json)
//     console.log(uuid.value)
// //  json =await res.getShoppingCart(uuid.value); ///not working change in domain concurent hash
// //     console.log(json)
//     json =await res.removeProductFromShoppingBag(uuid.value,3,3,3);
//     console.log(json)
//     json =await res.setProductQuantityShoppingBag(uuid.value,3,3,3);
//     console.log(json)
//     json =await res.orderShoppingCart(uuid.value);
//     console.log(json)
//     json =await res.logout(uuid.value);
//     console.log(json)
//     json =await res.changePassword(uuid.value,"Dsd","dsds","Dsd");
//     console.log(json)
//     json =await res.openNewStore(uuid.value,"name","ds");
//     console.log(json)
//
//     json =await res.addNewProductToStore(uuid.value,1,1,1.1,1);
//     console.log(json)
//
//     json =await res.deleteProductFromStore(uuid.value,1,2);
//     console.log(json)
//
//     json =await res.setProductPriceInStore(uuid.value,1,1,2.2);
//     console.log(json)
//     json =await res.setProductQuantityInStore(uuid.value,1,1,1);
//     console.log(json)
//     json =await res.addNewStoreOwner(uuid.value,1,"sdsdds");
//     console.log(json)
//     json =await res.addNewStoreManger(uuid.value,1,"ds");
//     console.log(json)
//     json =await res.setManagerPermissions(uuid.value,1,"name","ds",true);
//     console.log(json)
//
//     json =await res.closeStore(uuid.value,1);
//     console.log(json)
//
//     json =await res.getStoreRoles(uuid.value,1);
//     console.log(json)
//
//
//     json =await res.getStoreOrderHistory(uuid.value,1);
//     console.log(json)
//
//
//     json =await res.getUserInfo(uuid.value,"ff");
//     console.log(json)


}
//test()

export default createApiClientHttp;

// test()
// async function test (){
//     let res = createApiClientHttp();
//
//     let json =await res.guestVisit();
//     if(json.errorMsg==-1){
//         console.log(json.value)
//     }
//
//
// }
