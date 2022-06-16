import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/DiscountRules"
const CategoryDiscount = (props) => {
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  console.log("CategoryDiscount")
    let listOfRules= props.rules;
  let UUID = props.uuid;
  let storeID = props.storeID;
  let kind= props.kind; // AND/OR

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
  // "and": {
  //   "categoryID": "1",
  //       "discount": "1",
  //       "list": [
  //todo: add new Category Discout rule with all the list
  async function  addHandler(){
    let map=[]
    if (kind==='AND') {
      map = {"and": {'categoryID': categoryID, 'discount': discount, 'list': props.rules}}
    }
    else if (kind==='OR'){
      map = {"or": {'categoryID': categoryID, 'discount': discount, 'list': props.rules}}
    }
    else if (kind==='XOR'){
      map = {"xor": {'categoryID': categoryID, 'discount': discount, 'list': props.rules}}
    }

      if (props.compose === undefined) { //false case - no comopse - realy simple

        const sendRulesResponse = await apiClientHttp.addNewDiscountRule(UUID,storeID,map);
        let str = JSON.stringify(sendRulesResponse);
        console.log("sendRulesResponse    "+str)

        if (sendRulesResponse.errorMsg !== -1) {
          SetError(errorCode.get(sendRulesResponse.errorMsg))
        } else {
          cleanHandler();
          // props.onRule(sendRulesResponse.value);
          props.onRule(-1);

        }
      }

      cleanHandler();
    props.onRule(11);
  };

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
