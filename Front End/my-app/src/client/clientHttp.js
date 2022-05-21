import axios from "axios";

export const createApiClientHttp = () => {
    const UrlBase = "http://localhost:8090/";
    const apiUrl = UrlBase.concat("api/");
    return {
        initMarket : async (email,password,phoneNumber,datofbirth) =>
        {
            
            let path = apiUrl.concat("initMarket");
            const body = {
                    datofbirth:datofbirth,
                    email: email,
                    Password: password,
                    phoneNumber:phoneNumber

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },



        guestVisit:async ()=>
        {
            let path = apiUrl.concat(`guestVisit`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        },



        guestLeave :async (uuid)=>
        {
            let path = apiUrl.concat(`guestLeave/${uuid}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        },


        addNewMember:async(uuid,email,password,phonenumber,datofbirth)=>{
            let path = apiUrl.concat(`addNewMember/${uuid}`);
            console.log(path,"kkkkk")
            console.log(path,'datofbirth  '+datofbirth)
            const body = {
                "datofbirth":datofbirth,
                "email": email,
                "Password": password,
                "phoneNumber":phonenumber

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },


        login:async(uuid,email,password)=>
        {
            let path = apiUrl.concat(`login/${uuid}`);
            const body = {
                email: email,
                Password: password,
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },



        getStore : async (storeid)=>{
            let path = apiUrl.concat(`getStore/${storeid}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },

        getInfoProductInStore :async (storid,productid)=>{
            let path = apiUrl.concat(`getInfoProductInStore/${storid}/${productid}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },


        searchProductByName : async(productName)=>{
            let path = apiUrl.concat(`searchProductByName/${productName}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        searchProductByNamelist:async(l,productname)=>{
            let path = apiUrl.concat(`searchProductByName/List/${productname}`);
            let body= {data:{
                    lst : l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },

        filterByName :async (l , productName) =>{
            let path = apiUrl.concat(`filterByName/${productName}`);
            let body= {data:{
                    lst : l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },

        searchProductByDesc :async (desc)=>{
            let path = apiUrl.concat(`searchProductByDesc/${desc}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        searchProductByDescList :async (l,desc)=>{
            let path = apiUrl.concat(`searchProductByDesc/list/${desc}`);
            let body= {data:{
                    lst : l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },



        filterByDesc : async(l,desc)=>{
            let path = apiUrl.concat(`filterByDesc/${desc}`);
            let body= {data:{
                    lst : l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },


        searchProductByRate :async (rate) =>{
            let path = apiUrl.concat(`searchProductByRate/${rate}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },


        searchProductByRateList :async (l,rate)=>{
            let path = apiUrl.concat(`searchProductByRate/list/${rate}`);
            let body= {data:{
                    lst : l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },

        filterByStoreRate : async(l,minRate)=>{
            let path = apiUrl.concat(`filterByStoreRate/${minRate}`);
            let body= {data:{
                    lst : l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },


        searchProductByRangePrices:async (productId,min,max)=>{
            let path = apiUrl.concat(`searchProductByRangePrices/${productId}/${max}/${max}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },

        searchProductByRangePricesList:async (l,productId,min,max)=>{
            let path = apiUrl.concat(`searchProductByRangePrices/list/${productId}/${max}/${max}`);
            let body = {data:{
                    lst :l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },


        filterByRangePrices: async(l,min,max)=>{
            let path = apiUrl.concat(`filterByRangePrices/${min}/${max}`);
            let  body = {data:{
                    lst :l
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },


        addNewProductType :async (uuid,namep,descriptionp,categoryp)=>{
            let path = apiUrl.concat(`addNewProductType/${uuid}`);
            let body = {
                data : {
                    name:namep,
                    description:descriptionp,
                    category:categoryp
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },

        addProductToShoppingBag :async (uuid,storeid,productid,quantity)=>{
            let path = apiUrl.concat(`addProductToShoppingBag/${uuid}/${storeid}/${productid}/${quantity}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },


        getShoppingCart :async (uuid)=>{
            let path = apiUrl.concat(`getShoppingCart/${uuid}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },


        removeProductFromShoppingBag :async (uuid,storeid,productid)=>{
            let path = apiUrl.concat(`removeProductFromShoppingBag/${uuid}/${storeid}/${productid}`);
            return await axios.delete(path).then((res)=>{
                return res.data;
            })
        },


        setProductQuantityShoppingBag :async (uuid,productId,storeid,quantity)=>{
            let path = apiUrl.concat(`setProductQuantityShoppingBag/${uuid}/${productId}/${storeid}/${quantity}`);

            return await axios.post(path).then((res)=>{
                return res.data;
            })
        },




        orderShoppingCart: async (uuid,city,adress,apartment,CreditCard,CreditDate,pin)=>{
            let path = apiUrl.concat(`orderShoppingCart/${uuid}`);
            let body = {
                data : {
                    city:city,
                    adress:adress,
                    apartment:apartment,
                    CreditCard:CreditCard,
                    CreditDate:CreditDate,
                    pin:pin
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },

        logout:async (uuid)=>{
            let path = apiUrl.concat(`logout/${uuid}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },

        changePassword:async (uuid,email,Password,newPassword)=>{
            let path = apiUrl.concat(`changePassword/${uuid}`);
            let  body = {
                data : {
                    email:email,
                    Password,Password,
                    newPassword:newPassword
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },


        openNewStore: async (uuid,name,founder)=>{
            console.log("openNewStore "+uuid);
            console.log(name);
            console.log(founder);

            let path = apiUrl.concat(`openNewStore/${uuid}`);
            let body = {
                    "name":name,
                    "founder":founder,
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },


        addNewProductToStore: async(uuid,storeid,productId,price,quantity)=>{
            let path = apiUrl.concat(`addNewProductToStore/${uuid}/${storeid}/${productId}`);
            let body = {

                "price":price,
                "quantity":quantity,

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },


        deleteProductFromStore: async(uuid,storeId,productId)=>{
            let path = apiUrl.concat(`deleteProductFromStore/${uuid}/${storeId}/${productId}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },


        setProductPriceInStore: async(uuid,storeid,productId,price)=>{
            let path = apiUrl.concat(`setProductPriceInStore/${uuid}/${storeid}/${productId}`);
            let body = {

                "price":price

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },


        setProductQuantityInStore: async(uuid,storeid,productId,quantity)=>{
            let path = apiUrl.concat(`setProductQuantityInStore/${uuid}/${storeid}/${productId}`);
            let body = {

                "quantity" :quantity,
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },



        addNewStoreOwner: async(uuid, storeId,OwnerEmail)=>{
            let path = apiUrl.concat(`addNewStoreOwner/${uuid}/${storeId}`);
            let body = {

                OwnerEmail:OwnerEmail,

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },



        addNewStoreManger: async(uuid, storeId,mangerEmail)=>{
            let path = apiUrl.concat(`addNewStoreManger/${uuid}/${storeId}`);
            let body = {

                mangerEmail:mangerEmail,

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },



        setManagerPermissions: async(uuid, storeId,mangerEmail,per,onof)=>{
            let path = apiUrl.concat(`setManagerPermissions/${uuid}/${storeId}`);
            let body = {

                mangerEmail:mangerEmail,
                per:per,
                onof:onof

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },



        closeStore: async(uuid, storeId)=>{
            let path = apiUrl.concat(`closeStore/${uuid}/${storeId}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },

        getStoreRoles:async(uuid, storeId)=>{
            let path = apiUrl.concat(`getStoreRoles/${uuid}/${storeId}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        getStoreOrderHistory: async(uuid, storeId)=>{
            let path = apiUrl.concat(`getStoreOrderHistory/${uuid}/${storeId}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },


        getUserInfo: async(uuid,email)=>{
            let path = apiUrl.concat(`getUserInfo/${uuid}`);
            let body = {
                data:{
                    email :email
                }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        }
        ,

        filterByRate :async(l,minRate)=>
        {
            let path = apiUrl.concat(`filterByRate/${minRate}`);
            let body={
                data:{
                    lst:l
                }
            }
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })

        },

        searchProductByCategory:async(category)=>
        {
            let path = apiUrl.concat(`searchProductByCategory/${category}`);

            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },
        searchProductByCategoryList:async(l,category)=>
        {
            let path = apiUrl.concat(`searchProductByCategory/list/${category}`);
            let body={
                data:{
                    lst:l
                }
            }
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })

        },

        filterByCategory:async(l,category)=>
        {
            let path = apiUrl.concat(`filterByCategory/${category}`);
            let body={
                data:{
                    lst:l
                }
            }
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })

        },

        searchProductByStoreRate:async(rate)=>
        {
            let path = apiUrl.concat(`searchProductByStoreRate/${rate}`);

            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },

        searchProductByStoreRateList:async(l,rate)=>
        {
            let path = apiUrl.concat(`searchProductByStoreRate/list/${rate}`);
            let body={
                data:{
                    lst:l
                }
            }
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })

        },


        getloggedInMembers:async(uuid)=>{
            let path = apiUrl.concat(`getloggedInMembers/${uuid}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        getloggedOutMembers:async(uuid)=>{
            let path = apiUrl.concat(`getloggedOutMembers/${uuid}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        getAllStores:async()=>{
            console.log(" client getAllStores");

            let path = apiUrl.concat(`getAllStores`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        getAllProducts:async()=>{
            let path = apiUrl.concat(`getAllProducts`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        getAllProductsInStore:async(storeID)=>{
            let path = apiUrl.concat(`getAllProductsInStore/${storeID}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },





    }
}


async function test (){
    console.log("00000000000000000000000000000")
    let res = createApiClientHttp();
    let uuid =await res.guestVisit()
    let json;
    // console.log(uuid.value)
    // await res.guestLeave(uuid.value);
    // let json =await res.initMarket();
    // console.log(json)
    //  json =await res.addNewMember(uuid.value,"dfdf","Fdfd","fdfd");
    // console.log(json)
    // json =await res.login(uuid.value,"Dsds","Dsdsd");
    // console.log(json)
    // json =await res.getStore(2);
    // console.log(json)
    // json =await res.getInfoProductInStore(33,33);
    // console.log(json)
    // json =await res.searchProductByName("DSd");
    // console.log(json)
    // json =await res.searchProductByNamelist([2,3],"DSd");
    // console.log(json)
    // json =await res.filterByName([2,3],"Dsd");
    // console.log(json)
    // json =await res.searchProductByDesc("Dds");
    // console.log(json)
    // json =await res.searchProductByDescList([1,2],"Fdf");
    // console.log(json)
    // json =await res.filterByDesc([1,2],"Dsds");
    // console.log(json)
    // json =await res.searchProductByRate(33);
    // console.log(json)
    // json =await res.searchProductByRateList([1,2],33);
    // console.log(json)
    // json =await res.filterByRate([2,3],33);
    // console.log(json)
    // json =await res.searchProductByCategory(3);
    // console.log(json)
    //  json =await res.searchProductByCategoryList([1],3);
    // console.log(json)
//  json =await res.filterByCategory([2],3);
//     console.log(json)
//  json =await res.searchProductByStoreRate(3);
//     console.log(json)
//  json =await res.searchProductByStoreRateList([3],3);
//     console.log(json)
//  json =await res.filterByStoreRate([3],2);
//     console.log(json)
    json =await res.searchProductByRangePrices(3,3,2);
    console.log(json)
//  json =await res.searchProductByRangePricesList([2],2,2,2);
//     console.log(json)
//  json =await res.filterByRangePrices([2],2,2);
//     console.log(json)
//  json =await res.addNewProductType(uuid.value,"dsd","Dsd",2);
//     console.log(json)
//  json =await res.addProductToShoppingBag(uuid.value,3,3,3);
//     console.log(json)
//     console.log(uuid.value)
// //  json =await res.getShoppingCart(uuid.value); ///not working change in domain concurent hash
// //     console.log(json)
// json =await res.removeProductFromShoppingBag(uuid.value,3,3,3);
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

//     json =await res.addNewProductToStore(uuid.value,1,1,1.1,1);
//     console.log(json)

//     json =await res.deleteProductFromStore(uuid.value,1,2);
//     console.log(json)

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

//     json =await res.closeStore(uuid.value,1);
//     console.log(json)

//     json =await res.getStoreRoles(uuid.value,1);
//     console.log(json)


//     json =await res.getStoreOrderHistory(uuid.value,1);
//     console.log(json)


//     json =await res.getUserInfo(uuid.value,"ff");
//     console.log(json)


}
export default createApiClientHttp;
