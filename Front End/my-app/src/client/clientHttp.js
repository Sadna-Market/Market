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
            let body= {
                lst : l
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },

        filterByName :async (l , productName) =>{
            console.log("*************filterByName******************** "+l+" "+productName)
            let path = apiUrl.concat(`filterByName/${productName}`);
            let body= {
                lst : l
            }
            return await axios.post(path,body).then((res)=>{
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
            let body= {
                lst : l
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },



        filterByDesc : async(l,desc)=>{
            console.log("*************filterByDesc******************** "+l+" "+desc)

            let path = apiUrl.concat(`filterByDesc/${desc}`);
            let body= {
                lst : l
            }
            return await axios.post(path,body).then((res)=>{
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
            let body= {
                lst : l
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },

        filterByStoreRate : async(l,minRate)=>{
            console.log("*************filterByStoreRate******************** "+l+" "+minRate)

            let path = apiUrl.concat(`filterByStoreRate/${minRate}`);
            let body= {
                lst : l
            }
            return await axios.post(path,body).then((res)=>{
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
            let path = apiUrl.concat(`searchProductByRangePrices/list/${productId}/${min}/${max}`);
            let body = {
                lst :l
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },


        filterByRangePrices: async(l,min,max)=>{
            console.log("*************filterByRangePrices******************** "+l+"  min "+min+" max "+max)

            let path = apiUrl.concat(`filterByRangePrices/${min}/${max}`);
            let  body = {

                lst :l
            }
            console.log(body)
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },


        filterByRangePrices2: async(l,min,max)=>{
            console.log("*************filterByRangePrices2********************   min "+min+" max "+max)

            let path = apiUrl.concat(`filterByRangePrices2/${min}/${max}`);
            let  body = {

                "lst" :l
            }

            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        },


        addNewProductType :async (uuid,namep,descriptionp,categoryp)=>{
            console.log("addNewProductType client");
            console.log(uuid);
            console.log(namep);
            console.log(descriptionp);
            console.log(categoryp);
            let path = apiUrl.concat(`addNewProductType/${uuid}`);
            let body = {

                name:namep,
                description:descriptionp,
                category:categoryp

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
            console.log("uuid,storeid,productid ",uuid,storeid,productid)
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
            console.log("client http UUID, city, adress,apartment,cardNumber,cardDate, cardPin",uuid,city,adress,apartment,CreditCard,CreditDate,pin)

            let path = apiUrl.concat(`orderShoppingCart/${uuid}`);
            let body = {

                city:city,
                adress:adress,
                apartment:apartment,
                CreditCard:CreditCard,
                CreditDate:CreditDate,
                pin:pin

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
            console.log("uuid");
            console.log(uuid);
            console.log(email);
            console.log(Password);
            console.log(newPassword);

            let path = apiUrl.concat(`changePassword/${uuid}`);
            let  body = {
                email:email,
                Password,Password,
                newPassword:newPassword

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
            console.log("addNewProductToStore  client "+storeid+" "+productId+" "+(Number(price).toFixed(2))+" "+quantity )

            let path = apiUrl.concat(`addNewProductToStore/${uuid}/${storeid}/${productId}`)
            let body = {

                "price":Number(price).toFixed(2),
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
            console.log("setProductPriceInStore uuid"+uuid+"storeid "+storeid+" productId "+ productId+"+ price"+ price)
            let path = apiUrl.concat(`setProductPriceInStore/${uuid}/${storeid}/${productId}`);
            let body = {

                "price":Number(price).toFixed(2)

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },


        setProductQuantityInStore: async(uuid,storeid,productId,quantity)=>{
            console.log("setProductQuantityInStore uuid,storeid,productId,quantity"+uuid+" "+storeid+" "+productId+" "+quantity)
            let path = apiUrl.concat(`setProductQuantityInStore/${uuid}/${storeid}/${productId}`);
            let body = {

                "quantity" :quantity,
            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },



        addNewStoreOwner: async(uuid, storeId,OwnerEmail)=>{
            console.log("addNewStoreOwnerEmail uuid, storeId,mangerEmail"+ uuid+ storeId+OwnerEmail)

            let path = apiUrl.concat(`addNewStoreOwner/${uuid}/${storeId}`);
            let body = {

                OwnerEmail:OwnerEmail,

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },



        addNewStoreManger: async(uuid, storeId,mangerEmail)=>{
            console.log("addNewStoreManger uuid, storeId,mangerEmail"+ uuid+ storeId+mangerEmail)
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
            console.log("getStoreOrderHistory client http  "+uuid +""+ storeId)

            let path = apiUrl.concat(`getStoreOrderHistory/${uuid}/${storeId}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },


        getUserInfo: async(uuid,email)=>{
            console.log("getUserInfo client http  "+uuid +""+ email)

            let path = apiUrl.concat(`getUserInfo/${uuid}`);
            let body = {

                "email" :email

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })
        }
        ,

        filterByRate :async(l,minRate)=>
        {
            console.log("*************filterByRate******************** "+l+"  minRate "+minRate)

            let path = apiUrl.concat(`filterByRate/${minRate}`);
            let body={

                lst:l

            }
            return await axios.post(path,body).then((res)=>{
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

                lst:l

            }
            return await axios.post(path,body).then((res)=>{
                return res.data;
            })

        },

        filterByCategory:async(l,category)=>
        {
            console.log("*************filterByCategory******************** "+l+"  category "+category)

            let path = apiUrl.concat(`filterByCategory/${category}`);
            let body={

                lst:l

            }
            return await axios.post(path,body).then((res)=>{
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

                lst:l

            }
            return await axios.post(path,body).then((res)=>{
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
                console.log(" res.data "+res.data);

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
        isOwnerUUID:async(uuid,storeID)=>{
            let path = apiUrl.concat(`isOwnerUUID/${uuid}/${storeID}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },



        isManagerUUID:async(uuid,storeID)=>{
            let path = apiUrl.concat(`isManagerUUID/${uuid}/${storeID}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },


        isSystemManagerUUID:async(uuid)=>{
            let path = apiUrl.concat(`isSystemManagerUUID/${uuid}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        addNewBuyRule:async(uuid,storeid,obj)=>{
            let path = apiUrl.concat(`addNewBuyRule/${uuid}/${storeid}`);


            return await axios.post(path,obj).then((res)=>{
                return res.data;
            })
        },
        removeBuyRule:async(uuid,storeid,buyRuleID)=>{
            console.log(uuid,storeid)
            let path = apiUrl.concat(`removeBuyRule/${uuid}/${storeid}/${buyRuleID}`);
            return await axios.get(path).then((res)=>{

                return res.data;
            })
        },
        addNewDiscountRule:async(uuid,storeid,obj)=>{
            console.log("uuid,storeid,obj",uuid,storeid,obj)
            let path = apiUrl.concat(`addNewDiscountRule/${uuid}/${storeid}`);
            console.log(obj)
            return await axios.post(path,obj).then((res)=>{
                return res.data;
            })
        },
        removeNewDiscountRule:async(uuid,storeid,buyRuleID)=>{
            console.log(uuid,storeid)
            let path = apiUrl.concat(`removeNewDiscountRule/${uuid}/${storeid}/${buyRuleID}`);
            return await axios.get(path).then((res)=>{

                return res.data;
            })
        },
        getBuyPolicy:async(uuid,storeid)=>{
            let path = apiUrl.concat(`getBuyPolicy/${uuid}/${storeid}`);
            return await axios.get(path).then((res)=>{

                return res.data;
            })
        },
        getDiscountPolicy:async(uuid,storeid)=>{
            let path = apiUrl.concat(`getDiscountPolicy/${uuid}/${storeid}`);
            return await axios.get(path).then((res)=>{

                return res.data;
            })
        },
        getBuyRuleByID:async(uuid,storeid,buyRuleID)=>{
            let path = apiUrl.concat(`getBuyRuleByID/${uuid}/${storeid}/${buyRuleID}`);
            return await axios.get(path).then((res)=>{

                return res.data;
            })
        },
        getDiscountRuleByID:async(uuid,storeid,discountRuleID)=>{
            let path = apiUrl.concat(`getDiscountRuleByID/${uuid}/${storeid}/${discountRuleID}`);
            return await axios.get(path).then((res)=>{

                return res.data;
            })
        },
        removeStoreOwner:async(uuid,storeid,OwnerEmail)=>{
            let path = apiUrl.concat(`removeStoreOwner/${uuid}/${storeid}/${OwnerEmail}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })
        },

        getAllMembers:async(uuid)=>{ //need to be a admin
            let path = apiUrl.concat(`getAllMembers/${uuid}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },

        removeMember:async(uuid,email)=>{
            let path = apiUrl.concat(`removeMember/${uuid}/${email}`);
            return await axios.get(path).then((res)=>{
                return res.data;
            })

        },
        ///testssss
        removeStoreMenager:async(uuid,storeid,manageremail)=>{
            let path = apiUrl.concat(`removeStoreMenager/${uuid}/${storeid}/${manageremail}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },

        getRate:async(uuid,productTypeID)=>{
            let path = apiUrl.concat(`getRate/${uuid}/${productTypeID}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })

        },

        setRate:async(uuid,productTypeID,rate)=>{
            let path = apiUrl.concat(`setRate/${uuid}/${productTypeID}/${rate}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        },
        getProductTypeInfo:async(productTypeId)=>{
            let path = apiUrl.concat(`getProductTypeInfo/${productTypeId}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        combineANDDiscountRules:async(uuid,storeid,obj)=>{
            let path = apiUrl.concat(`combineANDDiscountRules/${uuid}/${storeid}`);
            return await axios.post(path,obj).then((res)=>{
                return res.data;
            })
        } ,
        combineORDiscountRules:async(uuid,storeid,obj)=>{
            let path = apiUrl.concat(`combineORDiscountRules/${uuid}/${storeid}`);
            return await axios.post(path,obj).then((res)=>{
                return res.data;
            })
        } ,
        combineXorDiscountRules:async(uuid,storeid,desicion,obj)=>{
            console.log("client  combineXorDiscountRules  uuid,storeid,desicion,obj ",uuid,storeid,desicion,obj)
            let path = apiUrl.concat(`combineXorDiscountRules/${uuid}/${storeid}/${desicion}`);
            return await axios.post(path,obj).then((res)=>{
                return res.data;
            })
        } ,
        getBIDStatus:async(uuid,userEmail,storeID,productID)=>{
            let path = apiUrl.concat(`getBIDStatus/${uuid}/${userEmail}/${storeID}/${productID}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        BuyBID:async(userId,storeID,productID,city,adress,apartment,creditcard,creditDate,pin)=>{
            let path = apiUrl.concat(`BuyBID/${userId}/${storeID}/${productID}/${city}/${adress}/${apartment}`);
            return await axios.post(path,{"creditCard":creditcard,"creditDate":creditDate,"pin":pin}).then((res)=>{
                return res.data;
            })
        } ,
        responseCounterBID:async(uuid,storeID,productID,approve)=>{
            let path = apiUrl.concat(`responseCounterBID/${uuid}/${storeID}/${productID}/${approve}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,

        counterBID:async(uuid,userEmail,storeID,productID,newTotalPrice)=>{
            let path = apiUrl.concat(`counterBID/${uuid}/${userEmail}/${storeID}/${productID}/${newTotalPrice}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,

        rejectBID:async(uuid,userEmail,storeID,productID)=>{
            let path = apiUrl.concat(`rejectBID/${uuid}/${userEmail}/${storeID}/${productID}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,

        approveBID:async(uuid,userEmail,storeID,productID)=>{
            let path = apiUrl.concat(`approveBID/${uuid}/${userEmail}/${storeID}/${productID}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        removeBID:async(uuid,storeID,productID)=>{
            let path = apiUrl.concat(`removeBID/${uuid}/${storeID}/${productID}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        createBID:async(uuid,storeID,productID,quantity,totalPrice)=>{
            let path = apiUrl.concat(`createBID/${uuid}/${storeID}/${productID}/${quantity}/${totalPrice}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        getAllusers:async()=>{
            let path = apiUrl.concat(`getAllusers`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        cancelMembership:async(uuid,cancelMemberUsername)=>{
            let path = apiUrl.concat(`cancelMembership/${uuid}/${cancelMemberUsername}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        getStoreRate:async(uuid,Store)=>{
            let path = apiUrl.concat(`getStoreRate/${uuid}/${Store}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        newStoreRate:async(uuid,Store,rate)=>{
            let path = apiUrl.concat(`newStoreRate/${uuid}/${Store}/${rate}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,

        reopenStore:async(uuid,Store)=>{
            let path = apiUrl.concat(`reopenStore/${uuid}/${Store}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,



        getMyBIDs:async(uuid,Store)=>{
            let path = apiUrl.concat(`getMyBIDs/${uuid}/${Store}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,

        getAllOffersBIDS:async(uuid,Store)=>{
            let path = apiUrl.concat(`getAllOffersBIDS/${uuid}/${Store}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,

        getStoreInfo:async(Store)=>{
            let path = apiUrl.concat(`getStoreInfo/${Store}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        } ,
        modifyDelayMessages:async(uuid)=>{
            let path = apiUrl.concat(`modifyDelayMessages/${uuid}`);
            return await axios.post(path).then((res)=>{
                return res.data;
            })
        }
    }
}
/*
CategoryRule {
  uuid: 'UUID',11
  storeId: 'storeID',
  categoryID: 'category',
  minAge: 'minAge',
  minHour: 'minHour',
  maxHour: 'maxHour'
}
productRule {
  uuid: 'UUID',
  storeId: 'storeID',
  productID: 'productID',
  minQuantity: 'minQuantity',
  maxQuantity: 'maxQuantity'
}
ShoppingRule {
  uuid: 'UUID',
  storeId: 'storeID',
  quantity: 'minQuantity',
  productTypes: 'minProductTypes'
}
UserRule { uuid: 'UUID', storeId: 'storeID', userEmail: 'email' }
{
  and: [
    CategoryRule {
      uuid: 'UUID',
      storeId: 'storeID',
      categoryID: 'category',
      minAge: 'minAge',
      minHour: 'minHour',
      maxHour: 'maxHour'
    },
    productRule {
      uuid: 'UUID',
      storeId: 'storeID',
      productID: 'productID',
      minQuantity: 'minQuantity',
      maxQuantity: 'maxQuantity'
    },
    ShoppingRule {
      uuid: 'UUID',
      storeId: 'storeID',
      quantity: 'minQuantity',
      productTypes: 'minProductTypes'
    },
    UserRule { uuid: 'UUID', storeId: 'storeID', userEmail: 'email' }
  ]
}
{
  or: [
    CategoryRule {
      uuid: 'UUID',
      storeId: 'storeID',
      categoryID: 'category',
      minAge: 'minAge',
      minHour: 'minHour',
      maxHour: 'maxHour'
    },
    productRule {
      uuid: 'UUID',
      storeId: 'storeID',
      productID: 'productID',
      minQuantity: 'minQuantity',
      maxQuantity: 'maxQuantity'
    },
    ShoppingRule {
      uuid: 'UUID',
      storeId: 'storeID',
      quantity: 'minQuantity',
      productTypes: 'minProductTypes'
    },
    UserRule { uuid: 'UUID', storeId: 'storeID', userEmail: 'email' }
  ]
}
{
  condition: [
    CategoryRule {
      uuid: 'UUID',
      storeId: 'storeID',
      categoryID: 'category',
      minAge: 'minAge',
      minHour: 'minHour',
      maxHour: 'maxHour'
    },
    productRule {
      uuid: 'UUID',
      storeId: 'storeID',
      productID: 'productID',
      minQuantity: 'minQuantity',
      maxQuantity: 'maxQuantity'
    },
    ShoppingRule {
      uuid: 'UUID',
      storeId: 'storeID',
      quantity: 'minQuantity',
      productTypes: 'minProductTypes'
    },
    UserRule { uuid: 'UUID', storeId: 'storeID', userEmail: 'email' }
  ]
}
{ combine: [ 1, 2, 3, 4, 5 ] }
*/

// test()
async function test (){

//     console.log("00000000000000000000000000000")
    let res = createApiClientHttp();
//     let json;
// //     // json =await res.initMarket("adminedanielkheyfets@gmail.com","admin123asdS!","0538265477","20/01/2001");
//     json =await res.guestVisit();
//     json =await res.login(json.value,"sysManager@gmail.com","Shalom123$")
//     console.log(json)
// //     let aaaa= await res.removeMember(json.value,"syfffsManager@gmail.com")
// //     console.log(aaaa)
// //    aaaa= await res.getUserInfo(json.value,"sysManager@gmail.com")
// //     console.log(aaaa)
//     let aaaa= await res.getBuyPolicy(json.value,"1")
//     console.log(aaaa)

//     aaaa= await res.getUserInfo(json.value,"sysManager@gmail.com")
//     console.log(aaaa)

//     aaaa= await res.addNewProductToStore(json.value,"1","1","1","1")
//     console.log(aaaa)

//     aaaa= await res.setProductPriceInStore(json.value,"1","1","1")
//     console.log(aaaa)


//     aaaa= await res.setProductQuantityInStore(json.value,"1","1","1")
//     console.log(aaaa)



//     aaaa= await res.getDiscountPolicy(json.value,"1")
//     console.log(aaaa)

//     json= await res.getAllMembers(json.value)
//     console.log(json);


    // console.log(json,"Admin")
    // let uuid=json.value
    // console.log(uuid,"00000")
    // let obj ={CategoryRule: {
    //         uuid: 'UUID',
    //         storeId: 'storeID',
    //         categoryID: "1",
    //         minAge: "1",
    //         minHour: "1",
    //         maxHour: "1"
    //     }}

    // json=await res.addNewBuyRule(uuid,1,obj)

    // console.log(json,"00000")
    // json =await res.addNewProductType(uuid,"d;;;;sd","Dsd",1);
    // console.log(json,"00000")
    // json =await res.logout(uuid)
    // console.log(json,"00000")





//     let uuid =await res.guestVisit()
//    let json =await res.addNewMember(uuid.value,"edanielkheyfets@gmail.com","123asdS!","0538265477","20/01/2001");
//     json =await res.login(uuid.value,"edanielkheyfets@gmail.com","123asdS!");
//     uuid =json.value
//     json=await res.openNewStore(uuid,"lechem","edanielkheyfets@gmail.com")
//     console.log(json,"add new store")
//     json=await res.addNewProductToStore(uuid,1,1,80,30)
//     console.log(json,"d")

//     json = await res.setProductPriceInStore(uuid,1,1,60)
//     console.log(json,"d")

//     json= await res.setProductQuantityInStore(uuid,1,1,100)
//     console.log(json,"d")
//     json =await res.changePassword(uuid,"edanielkheyfets@gmail.com","123asdS!","123as111dS!")
//     json =await res.setManagerPermissions(uuid,1,"edanielkheyfets@gmail.com","edanielkhddeyfets@gmail.com","Ds",true)
//     json =await res.getStoreOrderHistory(uuid,1);
//     json =await res.addProductToShoppingBag(uuid,1,1,10);
//     json =await res.getShoppingCart(uuid);

//     console.log(json.value.shoppingBagHash['1'],"d")
//     json = await res.getAllStores();
//     console.log(json)
//     json = await res.getAllProducts();
//     console.log(json)
//     json = await res.getAllProductsInStore(1);
//     console.log(json)

//     json = await res.isOwnerUUID(uuid,1);
//     console.log(json)
//     json = await res.isManagerUUID(uuid,1);
//     console.log(json)
//     json = await res.isSystemManagerUUID(uuid);
//     console.log(json)

//     json =await res.closeStore(uuid,1);

//     console.log(json,"d")

//     console.log(uuid.value)
//     await res.guestLeave(uuid.value);
//     //  json =await res.initMarket();
//     console.log(json)
//      json =await res.addNewMember(uuid.value,"dfdf","Fdfd","fdfd");
//     console.log(json)
//     json =await res.login(uuid.value,"Dsds","Dsdsd");
//     console.log(json)
//     json =await res.getStore(2);
//     console.log(json)
//     json =await res.getInfoProductInStore(33,33);
//     console.log(json)
    let json =await res.searchProductByName("DSd");
    console.log(json)
    json =await res.searchProductByNamelist([2,3],"DSd");
    console.log(json)
    json =await res.filterByName([2,3],"Dsd");
    console.log(json)
    json =await res.searchProductByDesc("Dds");
    console.log(json)
    json =await res.searchProductByDescList([1,2],"Fdf");
    console.log(json)
    json =await res.filterByDesc([1,2],"Dsds");
    console.log(json)
    json =await res.searchProductByRate(33);
    console.log(json)
    json =await res.searchProductByRateList([1,2],33);
    console.log(json)
    json =await res.filterByRate([2,3],33);
    console.log(json)
    json =await res.searchProductByCategory(3);
    console.log(json)
    json =await res.searchProductByCategoryList([1],3);
    console.log(json)
    json =await res.filterByCategory([2],3);
    console.log(json)
    json =await res.searchProductByStoreRate(3);
    console.log(json)
    json =await res.searchProductByStoreRateList([3],3);
    console.log(json)
    json =await res.filterByStoreRate([3],2);
    console.log(json)
    let k =await res.searchProductByRangePrices(3,3,2);
    console.log(k,"Ddd")
    k =await res.searchProductByRangePricesList([2],2,2,2);
    console.log(k)
    k =await res.filterByRangePrices([2],2,2);
    console.log(k)
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
