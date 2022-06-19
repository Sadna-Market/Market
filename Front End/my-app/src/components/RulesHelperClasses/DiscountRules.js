class DRule{
    constructor(uuid, storeId){
        this.uuid=uuid;
        this.storeId=storeId;
    }
}
class FullDiscount extends DRule{
    constructor(uuid, storeId,discount){
        super(uuid,storeId);
        this.discount=discount;
    }
}

class ProductDiscount extends DRule{
    constructor(uuid, storeId,discount,productID){
        super(uuid,storeId);
        this.discount=discount;
        this.productID=productID;
    }
}


class categoryDiscount extends DRule{
    constructor(uuid, storeId,discount,categoryID){
        super(uuid,storeId);
        this.discount=discount;
        this.categoryID=categoryID;

    }
}

class conditionOnStoreDiscount extends DRule{
    constructor(uuid, storeId,discount,minQuantity,maxQuantity,totalPrice){
        super(uuid,storeId);
        this.discount=discount;
        this.minQuantity=minQuantity;
        this.maxQuantity=maxQuantity;
        this.totalPrice=totalPrice;

    }

}
class conditionOnProductDiscount extends DRule{
    constructor(uuid, storeId,discount,productID,minQuantity,maxQuantity){
        super(uuid,storeId);
        this.discount=discount;
        this.productID=productID;
        this.minQuantity=minQuantity;
        this.maxQuantity=maxQuantity;

    }

}
class conditionOnCategoryDiscount extends DRule {
    constructor(uuid, storeId, discount,categoryID,minimumAge, minimumHour, maximumHour) {
        super(uuid, storeId);
        this.discount = discount;
        this.minimumAge = minimumAge;
        this.categoryID = categoryID;
        this.minimumHour = minimumHour;
        this.maximumHour = maximumHour;

    }
}
// let rule1 = new conditionOnCategoryDiscount("uuid","storeId","discount","minimumAge","categoryID","minimumHour","maximumHour")
// let rule2 = new conditionOnProductDiscount("uuid", "storeId","discount","productID","minQuantity","maxQuantity")
// let rule3 = new conditionOnStoreDiscount("uuid", "storeId","discount","minQuantity","maxQuantity","totalPrice")
// let rule4 = new categoryDiscount("uuid", "storeId","discount","categoryID")
// let rule5 = new ProductDiscount("uuid", "storeId","discount","productID")
// let rule6 = new FullDiscount("uuid", "storeId","discount")
// console.log(rule1)
// console.log(rule2)
// console.log(rule3)
// console.log(rule4)
// console.log(rule5)
// console.log(rule6)
// const list= [];
// list.push(rule1)
// list.push(rule2)
// list.push(rule3)
// list.push(rule4)
// list.push(rule5)
// list.push(rule6)
//
// let andMap ={"and":list}
// let orMap ={"or":list}
// let xorMap ={"xor":list}
//
// combinelist=[1,2]
// let conditionMap ={"conditionAdd":combinelist}
// let combineAndMap ={"combineAnd":combinelist}
// let combineOrMap ={"combineOr":combinelist}
// let combineConditionMap ={"combineCondition":combinelist}
//
// console.log(andMap)
// console.log(orMap)
// console.log(xorMap)
// console.log(conditionMap)
// console.log(combineAndMap)
// console.log(combineOrMap)
// console.log(combineOrMap)
