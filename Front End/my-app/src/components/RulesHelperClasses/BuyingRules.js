class Rule{
    constructor(uuid, storeId){
        this.uuid=uuid;
        this.storeId=storeId;
    }
}
class productRule extends Rule{
    constructor(uuid, storeId,productID,minQuantity,maxQuantity){
        super(uuid,storeId);
        this.productID=productID;
        this.minQuantity=minQuantity;
        this.maxQuantity=maxQuantity;
    }
}

class CategoryRule extends Rule{
    constructor(uuid, storeId,categoryID,minAge,minHour,maxHour){
        super(uuid,storeId);
        this.categoryID=categoryID;
        this.minAge=minAge;
        this.minHour=minHour;
        this.maxHour=maxHour;
    }
}


class UserRule extends Rule{
    constructor(uuid, storeId,userEmail){
        super(uuid,storeId);
        this.userEmail=userEmail;
    }
}

class ShoppingRule extends Rule{
    constructor(uuid, storeId,quantity,productTypes){
        super(uuid,storeId);
        this.quantity=quantity;
        this.productTypes=productTypes;
    }
}


// // i need "getAllRules()" return : Rule Number: 1  Kind: Add  Amount: 4
//  apiClientHttp.sendRules(combineMap) return : value == rule id!!

// // i need "getAllDRules()" return : Rule Number: 1  Kind: Add  Amount: 4
//  apiClientHttp.sendDRules(combineMap) return : value == rule id!!


/// *************************** simple bying rull   ******************************************
let rule1 = new CategoryRule("UUID","storeID","category","minAge","minHour","maxHour")
let rule2 = new productRule("UUID","storeID","productID","minQuantity","maxQuantity")
let rule3 = new ShoppingRule("UUID","storeID","minQuantity","minProductTypes")
let rule4 = new UserRule("UUID","storeID","email")
// const sendRulesResponse = await apiClientHttp.sendRules(rule);
console.log(rule1)
console.log(rule2)
console.log(rule3)
console.log(rule4)
// CategoryRule {
//     uuid: 'UUID',
//         storeId: 'storeID',
//         categoryID: 'category',
//         minAge: 'minAge',
//         minHour: 'minHour',
//         maxHour: 'maxHour'
// }
// productRule {
//     uuid: 'UUID',
//         storeId: 'storeID',
//         productID: 'productID',
//         minQuantity: 'minQuantity',
//         maxQuantity: 'maxQuantity'
// }
// ShoppingRule {
//     uuid: 'UUID',
//         storeId: 'storeID',
//         quantity: 'minQuantity',
//         productTypes: 'minProductTypes'
// }
// UserRule { uuid: 'UUID', storeId: 'storeID', userEmail: 'email' }
// const list= [];
// list.push(rule1)
// list.push(rule2)
// list.push(rule3)
// list.push(rule4)
//
// let andMap ={"and":list}
// let orMap ={"or":list}
// let conditionMap ={"condition":list}
// console.log(andMap)
// console.log(orMap)
// console.log(conditionMap)
//
// combinelist=[1,2]
// let combineAndMap ={"combineAnd":combinelist}
// let combineOrMap ={"combineOr":combinelist}
// let combineConditionMap ={"combineCondition":combinelist}

// console.log(combineMap)


//לסדר קומבין ברולים של הקנייה