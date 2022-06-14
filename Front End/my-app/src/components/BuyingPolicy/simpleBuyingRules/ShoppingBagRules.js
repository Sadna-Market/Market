import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"

const ShoppingBagRule = (props) => {
  console.log("ShoppingBagRule")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  let UUID = props.uuid;
  let storeID = props.storeID;

  const [minQuantity, setminQuantity] = useState("");
  const changeQuantityHandler = (event) => {
    setminQuantity(event.target.value);
  };
  const [minProductTypes, setminProductTypes] = useState("");
  const changeProductTypeHandler = (event) => {
    setminProductTypes(event.target.value);
  };

  const cleanHandler = () => {
    SetError("")
    setminQuantity("");
    setminProductTypes("");
  };

  //todo: add new ShoppingBag Rule to store
  async function addHandler() {
    console.log("addHandler")
    let rule = new RulesClass.ShoppingRule(UUID,storeID,minQuantity,minProductTypes)
    if (props.compose===undefined) { //false case - no comopse - realy simple

      const sendRulesResponse = await apiClientHttp.addNewBuyRule(UUID,storeID, {'ShoppingRule':rule});
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
    else{
    cleanHandler();
    props.onRule({'ShoppingRule':rule});

    }
  }

  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <h3>Add Shopping Bag Rule For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Quantity</label>
          <input
            type="number"
            min="0"
            value={minQuantity}
            placeholder="Write Minmum Quantity"
            onChange={changeQuantityHandler}
          />
        </div>
        <div className="products__control">
          <label>Product Types</label>
          <input
            type="number"
            min="0"
            value={minProductTypes}
            placeholder="Write Minmum Product Types"
            onChange={changeProductTypeHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default ShoppingBagRule;
