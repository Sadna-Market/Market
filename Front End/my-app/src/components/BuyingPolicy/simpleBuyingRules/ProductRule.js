import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"

const ProductRule = (props) => {
  console.log("ProductRule")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [minQuantity, setminQuantity] = useState("");

  const changeMinQuantityHandler = (event) => {
    setminQuantity(event.target.value);
  };

  const [maxQuantity, setmaxQuantity] = useState("");
  const changeMaxQuanHandler = (event) => {
    setmaxQuantity(event.target.value);
  };

  const [productID, setproductID] = useState("");
  const changeProductHandler = (event) => {
    setproductID(event.target.value);
  };

  const cleanHandler = () => {
    setminQuantity("");
    setmaxQuantity("");
    setproductID("");
  };

  //todo: add new product Rule to store
  async function addHandler (){
    let rule = new RulesClass.productRule(UUID,storeID,productID,minQuantity,maxQuantity)
    if (props.compose===undefined) { //false case - no comopse - realy simple

      const sendRulesResponse = await apiClientHttp.sendRules(rule);

      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        cleanHandler();
        // props.onRule(sendRulesResponse.value);
        props.onRule(-1);

      }
    }
    
    cleanHandler();
    props.onRule(rule);
  }

  return (
    <div>
      <h3>Add Product Rule For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Product ID</label>
          <input
            type="number"
            min="0"
            value={productID}
            placeholder="Write PRoduct ID"
            onChange={changeProductHandler}
          />
        </div>
        <div className="products__control">
          <label>Min Quantity</label>
          <input
            type="number"
            min="0"
            value={minQuantity}
            placeholder="Write Minmum Quantity"
            onChange={changeMinQuantityHandler}
          />
        </div>
        <div className="products__control">
          <label>Max Quantity</label>
          <input
            type="number"
            min="0"
            value={maxQuantity}
            placeholder="Write Minmum Product Types"
            onChange={changeMaxQuanHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default ProductRule;
