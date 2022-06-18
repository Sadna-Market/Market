import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/DiscountRules"
const ConditionProductDiscountRule = (props) => {
  console.log("ConditionCategoryDiscountRule")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [discount, setDiscount] = useState("");
  const changeDiscountHandler = (event) => {
    setDiscount(event.target.value);
  };

  const [productID, setPRoductID] = useState("");
  const changeProductIDHandler = (event) => {
    setPRoductID(event.target.value);
  };

  const [minQuantity, setminQuantity] = useState("");
  const changeMinQuantityHandler = (event) => {
    setminQuantity(event.target.value);
  };

  const [maxQuantity, setmaxQuantity] = useState("");
  const changeMaxQuanHandler = (event) => {
    setmaxQuantity(event.target.value);
  };

  const cleanHandler = () => {
    SetError("")
    setDiscount("");
    setPRoductID("");
    setmaxQuantity("");
    setminQuantity("");
  };

  //todo: add new Category Discout rule
  async function  addHandler(){
    let rule = new RulesClass.conditionOnProductDiscount(UUID, storeID, discount, productID, minQuantity, maxQuantity)
    if (props.compose === undefined) { //false case - no comopse - realy simple

      const sendRulesResponse = await apiClientHttp.addNewDiscountRule(UUID,storeID, {'conditionOnProductDiscount':rule});

      let str = JSON.stringify(sendRulesResponse);
      console.log("sendRulesResponse    "+str)


      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        cleanHandler();
        // props.onRule(sendRulesResponse.value);
        props.onRule(-1);

      }
    } else {
      cleanHandler();
      props.onRule({'conditionOnProductDiscount':rule});
    }
  }

  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <h3>Category Discount For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Product ID</label>
          <input
            type="number"
            min="0"
            value={productID}
            placeholder="Write Product ID"
            onChange={changeProductIDHandler}
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
        <div className="products__control">
          <label>Min Quantity</label>
          <input
            type="number"
            min="0"
            value={minQuantity}
            placeholder="write Minimum Quantity"
            onChange={changeMinQuantityHandler}
          />
        </div>
        <div className="products__control">
          <label>Max Quantity</label>
          <input
            type="number"
            min="0"
            value={maxQuantity}
            placeholder="write Maximum Quantity"
            onChange={changeMaxQuanHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default ConditionProductDiscountRule;
