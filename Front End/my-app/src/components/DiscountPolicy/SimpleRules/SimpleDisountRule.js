import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/DiscountRules"

const SimpleDiscountRule = (props) => {
  console.log("SimpleDiscountRule")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [discount, setDiscount] = useState("");
  const changeDiscountHandler = (event) => {
    setDiscount(event.target.value);
  };

  const cleanHandler = () => {
    setDiscount("");
  };

  //todo: add new Discount simple rule
  async function addHandler(){
    let rule = new RulesClass.FullDiscount(UUID, storeID, discount)
    if (props.compose === undefined) { //false case - no comopse - realy simple

      const sendRulesResponse = await apiClientHttp.sendDRules(rule);

      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        cleanHandler();
        // props.onRule(sendRulesResponse.value);
        props.onRule(-1);

      }
    } else {
      cleanHandler();
      props.onRule(rule);
    }
  }

  return (
    <div>
      <h3>store #{storeID} Discount</h3>
      <div className="products__controls">
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
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default SimpleDiscountRule;
