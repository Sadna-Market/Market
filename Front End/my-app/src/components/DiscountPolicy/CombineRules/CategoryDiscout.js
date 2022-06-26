import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass from "../../RulesHelperClasses/BuyingRules"

const CategoryDiscount = (props) => {
  let listOfRules = props.rules;
  let UUID = props.uuid;
  let storeID = props.storeID;
  let kind = props.kind; // AND/OR
  const [enteredError, SetError] = useState("");
  const apiClientHttp = createApiClientHttp();

  const [discount, setDiscount] = useState("");
  const changeDiscountHandler = (event) => {
    setDiscount(event.target.value);
  };

  const [categoryID, setCategoryID] = useState("");
  const changeCategoryHandler = (event) => {
    setCategoryID(event.target.value);
  };

  const cleanHandler = () => {
    setDiscount("");
    setCategoryID("");
  };

  async function addHandler(){
    let map=[]
    if (kind==='AND') {
      map = {"combineAnd":props.rules,'category': categoryID,'discount':discount}
      const sendRulesResponse = await apiClientHttp.combineANDDiscountRules(UUID,storeID,map);
      let str = JSON.stringify(sendRulesResponse);
       console.log("sendRulesResponse    "+str)

       if (sendRulesResponse.errorMsg !== -1) {
         SetError(errorCode.get(sendRulesResponse.errorMsg))
       } else {
         cleanHandler();
         props.onRule(-1);

       }
    }
    else if (kind==='OR'){
      map = {"combineOr":props.rules,'category': categoryID,'discount':discount}
      const sendRulesResponse = await apiClientHttp.combineORDiscountRules(UUID,storeID,map);
      let str = JSON.stringify(sendRulesResponse);
      console.log("sendRulesResponse    "+str)

      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        cleanHandler();
        props.onRule(-1);

      }
    }
    else if (kind==='XOR'){
      map = {"combineXor":props.rules,'category': categoryID,'discount':discount}
      const sendRulesResponse = await apiClientHttp.combineXorDiscountRules(UUID,storeID,map);
      let str = JSON.stringify(sendRulesResponse);
      console.log("sendRulesResponse    "+str)

      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        cleanHandler();
        props.onRule(-1);

      }
    }


  }

  return (
    <div>
      <h2>Select the category on which the discount will be made</h2>
      <div className="products__controls">
        <div className="products__control">
          <label>Category ID</label>
          <input
            type="number"
            min="0"
            value={categoryID}
            placeholder="Write Category ID"
            onChange={changeCategoryHandler}
          />
        </div>
        <div className="products__control">
          <label>Discount</label>
          <input
            type="number"
            min="0"
            value={discount}
            placeholder="write number between 1-100%"
            onChange={changeDiscountHandler}
          />
        </div>
        <button onClick={cleanHandler}>Cancel</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default CategoryDiscount;
