import React, { useEffect, useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/DiscountRules"

const BigOrFirst = (props) => {
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  console.log("BigOrFirst")
  let listOfRules = props.rules;
  let UUID = props.uuid;
  let storeID = props.storeID;
  let kind = props.kind; // AND/OR

  const [discount, setDiscount] = useState(true);

  const cleanHandler = () => {
    props.onRule();
  };

  //todo: add new Category Discout rule with all the list
  async function addHandler(){
    let sendRulesResponse="";
    if (discount){//        combineXorDiscountRules:async(uuid,storeid,desicion,obj)=>{
       sendRulesResponse = await apiClientHttp.combineXorDiscountRules(UUID,storeID,'Big Discount',{'combineXor':listOfRules});
    }
    else {
       sendRulesResponse = await apiClientHttp.combineXorDiscountRules(UUID,storeID,'First',{'combineXor':listOfRules});
    }
    let str = JSON.stringify(sendRulesResponse);
    console.log("sendRulesResponse    "+str)

    if (sendRulesResponse.errorMsg !== -1) {
      SetError(errorCode.get(sendRulesResponse.errorMsg))
    } else {
      cleanHandler();
      props.onRule(-1);
    }

  }

  const changeValueHandler1 = (event) => {
    setDiscount(true);
  };

  const changeValueHandler2 = (event) => {
    setDiscount(false);
  };

  return (
    <div>
      <h2>Select the category on which the discount will be made</h2>
      <div className="products__controls">
        <div className="products__control">
          <input
            type="radio"
            value={discount}
            onChange={changeValueHandler1}
            name="discount"
          />
          <label>Big Discount</label>
        </div>
        <div className="products__control">
          <input
            type="radio"
            value={!discount}
            onChange={changeValueHandler2}
            name="discount"
          />        
          <label>First Discount</label>
        </div>
        <button onClick={cleanHandler}>Cancel</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default BigOrFirst;
