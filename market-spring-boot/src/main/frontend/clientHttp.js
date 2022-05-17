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
             return await axios.post(path,body).then((res)=>{
                 return res.data;
             })
        },



        guestVisit:async ()=>
        {
            let path = apiUrl.concat("guestVisit");
             return await axios.post(path).then((res)=>{
                 return res.data;
             })
        },



        guestLeave:async(uuid)=>{
            let path = apiUrl.concat("guestLeave");
            path = apiUrl.concat("/"+uuid)
             return await axios.post(path).then((res)=>{
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
             return await axios.post(path,body).then((res)=>{
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
        return await axios.post(path,body).then((res)=>{
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
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },

        addProductToShoppingBag : (uuid,storeid,productid,quantity)=>{
            let path = apiUrl.concat("addProductToShoppingBag");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeid)
            path = apiUrl.concat("/"+productid)
            path = apiUrl.concat("/"+quantity)
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },
 

        getShoppingCart : (uuid)=>{
            let path = apiUrl.concat("getShoppingCart");
            path = apiUrl.concat("/"+uuid)
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },


        removeProductFromShoppingBag : (uuid,storeid,productid)=>{
            let path = apiUrl.concat("removeProductFromShoppingBag");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeid)
            path = apiUrl.concat("/"+productid)
            return await axios.delete(path).then((res)=>{
                return res.data;
            })
        },


        setProductQuantityShoppingBag : (uuid,productId,storeid,quantity)=>{
            let path = apiUrl.concat("setProductQuantityShoppingBag");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+productId)
            path = apiUrl.concat("/"+storeid)
            path = apiUrl.concat("/"+quantity)

            return await axios.post(path).then((res)=>{
                return res.data;
            })
        },

    


        orderShoppingCart: (uuid,city,adress,apartment,CreditCard,CreditDate,pin)=>{
            let path = apiUrl.concat("orderShoppingCart");
            path = apiUrl.concat("/"+uuid)
            body = { 
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

        logout: ()=>{
            let path = apiUrl.concat("logout");
            path = apiUrl.concat("/"+uuid)
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },

        changePassword:(uuid,email,Password,newPassword)=>{
            let path = apiUrl.concat("logout");
            path = apiUrl.concat("/"+uuid)
            body = { 
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


        openNewStore: (uuid,name,founder)=>{
            let path = apiUrl.concat("openNewStore");
            path = apiUrl.concat("/"+uuid)
            body = { 
                data : {
                  name:name,
                  founder,founder,
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },


        addNewProductToStore:(uuid,storeid,productId,price,quantity)=>{
            let path = apiUrl.concat("addNewProductToStore");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeid)
            path = apiUrl.concat("/"+productId)
            body = { 
                data : {
                    price:price,
                    quantity,quantity,
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },
    

        deleteProductFromStore:(uuid,storeid,productId)=>{
            let path = apiUrl.concat("deleteProductFromStore");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeid)
            path = apiUrl.concat("/"+productId)
            return await axios.delete(path).then((res)=>{
                return res.data;
            })

        },


        setProductPriceInStore:(uuid,storeid,productId,price)=>{
            let path = apiUrl.concat("setProductPriceInStore");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeid)
            path = apiUrl.concat("/"+productId)
            body = { 
                data : {
                    price:price,
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },


        setProductQuantityInStore:(uuid,storeid,productId,quantity)=>{
            let path = apiUrl.concat("setProductQuantityInStore");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeid)
            path = apiUrl.concat("/"+productId)
            body = { 
                data : {
                    quantity:quantity,
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },



        addNewStoreOwner:(uuid, storeId,OwnerEmail)=>{
            let path = apiUrl.concat("addNewStoreOwner");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeId)
            body = { 
                data : {
                    OwnerEmail:OwnerEmail,
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },



        addNewStoreManger:(uuid, storeId,mangerEmail)=>{
            let path = apiUrl.concat("addNewStoreManger");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeId)
            body = { 
                data : {
                    mangerEmail:mangerEmail,
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },



        setManagerPermissions:(uuid, storeId,mangerEmail,per,onof)=>{
            let path = apiUrl.concat("addNewStoreManger");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeId)
            body = { 
                data : {
                    mangerEmail:mangerEmail,
                    per:per,
                    onof:onof
                }
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },



        closeStore:(uuid, storeId)=>{
            let path = apiUrl.concat("closeStore");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeId)
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },

        getStoreRoles:(uuid, storeId)=>{
            let path = apiUrl.concat("getStoreRoles");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeId)
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        getStoreOrderHistory:(uuid, storeId)=>{
            let path = apiUrl.concat("getStoreOrderHistory");
            path = apiUrl.concat("/"+uuid)
            path = apiUrl.concat("/"+storeId)
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },


        getUserInfo:(uuid)=>{
            let path = apiUrl.concat("getUserInfo");
            path = apiUrl.concat("/"+uuid)
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        }

     


    }
}


async function test (){
    let res = createApiClientHttp();

    let json =await res.guestVisit();
    if(json.errorMsg==-1){
        console.log(json.value)
    }
    

}

test()