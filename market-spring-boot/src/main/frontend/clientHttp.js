import axios from "axios";

export const createApiClientHttp = () => {
    const UrlBase = "http://localhost:8090/";
    const apiUrl = UrlBase.concat("api/");
    return {
        initMarket :async (email,password,phoneNumber) =>{
            let path = apiUrl.concat("initMarket");
            console.log(path)
            const body = {data:{
                email: email,
                Password: password,
                phoneNumber:phoneNumber
            }
              };
             return await axios.get(path,body).then((res)=>{
                 return res.data;
             })
        },



        guestVisit:async ()=>
        {
            let path = apiUrl.concat("guestVisit");
             return await axios.get(path).then((res)=>{
                 return res.data;
             })
        },



        guestLeave:async(uuid)=>{
            let path = apiUrl.concat("guestLeave");
            path = apiUrl.concat("/"+uuid)
             return await axios.get(path).then((res)=>{
                 return res.data;
             })
        },


        addNewMember:async(uuid,email,password,phonenumber)=>{

            let path = apiUrl.concat("addNewMember");
            path = apiUrl.concat("/"+uuid)
            const body = {data:{
                email: email,
                Password: password,
                phoneNumber:phonenumber
            }
        }
             return await axios.get(path,body).then((res)=>{
                 return res.data;
             })
        },


        login:async(uuid,email,password)=>
        {   
            let path = apiUrl.concat("login");
            path = apiUrl.concat("/"+uuid)
            const body = {data:{
                email: email,
                Password: password,
            }
        }
        return await axios.get(path,body).then((res)=>{
            return res.data;
        })

        },



        getStore : async(storeid)=>{
            let path = apiUrl.concat("getStore");
            path = apiUrl.concat("/"+storeid)
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },

        getInfoProductInStore :async (storid,productid)=>{
            let path = apiUrl.concat("getInfoProductInStore");
            path = apiUrl.concat("/"+storid)
            path = apiUrl.concat("/"+productid)
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },


        searchProductByName : async(productName)=>{
            let path = apiUrl.concat("searchProductByName");
            path = apiUrl.concat("/"+productName)
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        searchProductByNamelist:async(l,productname)=>{
            let path = apiUrl.concat("searchProductByName/List");
            path = apiUrl.concat("/"+productName)
            body= {data:{
                lst : l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },

        filterByName :async (l , productName) =>{
            let path = apiUrl.concat("filterByName");
            path = apiUrl.concat("/"+productName)
            body= {data:{
                lst : l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },

        searchProductByDesc :async (desc)=>{
            let path = apiUrl.concat("searchProductByDesc");
            path = apiUrl.concat("/"+desc)
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        searchProductByDescList :async (l,desc)=>{
            let path = apiUrl.concat("searchProductByDesc/list");
            path = apiUrl.concat("/"+desc)
            body= {data:{
                lst : l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },



        filterByDesc : async(l,desc)=>{
            let path = apiUrl.concat("filterByDesc");
            path = apiUrl.concat("/"+desc)
            body= {data:{
                lst : l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },


        searchProductByRate :async (rate) =>{
            let path = apiUrl.concat("searchProductByRate");
            path = apiUrl.concat("/"+rate)
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },


        searchProductByRateList :async (l,rate)=>{
            let path = apiUrl.concat("searchProductByRate/list");
            path = apiUrl.concat("/"+rate)
            body= {data:{
                lst : l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },

        filterByStoreRate : async(l,minRate)=>{
            let path = apiUrl.concat("filterByStoreRate");
            path = apiUrl.concat("/"+minRate)
            body= {data:{
                lst : l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },


        searchProductByRangePrices:async (productId,min,max)=>{
            let path = apiUrl.concat("searchProductByRangePrices");
            path = apiUrl.concat("/"+productId)
            path = apiUrl.concat("/"+min)
            path = apiUrl.concat("/"+max)
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },

        searchProductByRangePricesList:async (l,productId,min,max)=>{
            let path = apiUrl.concat("searchProductByRangePrices/list");
            path = apiUrl.concat("/"+productId)
            path = apiUrl.concat("/"+min)
            path = apiUrl.concat("/"+max)
            body = {data:{
                lst :l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },

        
        filterByRangePrices: async(l,min,max)=>{
            let path = apiUrl.concat("filterByRangePrices");
            path = apiUrl.concat("/"+min)
            path = apiUrl.concat("/"+max)
            body = {data:{
                lst :l
            }}
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })
        },


        addNewProductType :async (uuid,namep,descriptionp,categoryp)=>{
            let path = apiUrl.concat("addNewProductType");
            path = apiUrl.concat("/"+uuid)
            body = { 
                data : {
                    name:namep,
                    description:descriptionp,
                    category:categoryp
                }
            }
            return await axios.get(path,body).then((res)=>{
                return res.data;
            })

        },

        addProductToShoppingBag : ()=>{

        },
 

        getShoppingCart : ()=>{

        },


        removeProductFromShoppingBag : ()=>{

        },


        setProductQuantityShoppingBag : ()=>{

        },


        orderShoppingCart: ()=>{

        },

        logout: ()=>{


        },

        changePassword:()=>{

        },


        openNewStore: ()=>{


        },


        addNewProductToStore:()=>{

        },
    

        deleteProductFromStore:()=>{

        },


        setProductPriceInStore:()=>{

        },


        setProductQuantityInStore:()=>{

        },



        addNewStoreOwner:()=>{

        },



        addNewStoreManger:()=>{

        },



        setManagerPermissions:()=>{

        },



        closeStore:()=>{

        },

        getStoreRoles:()=>{

        },

        getStoreOrderHistory:()=>{

        },


        getUserInfo:()=>{

        }

     


    }
}

createApiClientHttp().initMarket("aaa","bbb","xssssxx")
createApiClientHttp().guestVisit()