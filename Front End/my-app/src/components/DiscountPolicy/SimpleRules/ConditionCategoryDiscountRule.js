import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/DiscountRules"
const ConditionCategoryDiscountRule = (props) => {
  console.log("ConditionCategoryDiscountRule")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [discount, setDiscount] = useState("1");
  const changeDiscountHandler = (event) => {
    setDiscount(event.target.value);
  };

  const [categoryID, setCategoryID] = useState("");
  const changeCategoryIDHandler = (event) => {
    setCategoryID(event.target.value);
  };

  const [minAge, setMinAge] = useState("");
  const changeMinAgeHandler = (event) => {
    setMinAge(event.target.value);
  };

  const [minHour, setMinHour] = useState("");
  const changeMinHourHandler = (event) => {
    setMinHour(event.target.value);
  };

  const [maxHour, setmaxHour] = useState("");
  const changeMaxHourHandler = (event) => {
    setmaxHour(event.target.value);
  };

  const cleanHandler = () => {
    SetError("")
    // setDiscount("");
    setCategoryID("");
    setMinAge("");
    setMinHour("");
    setmaxHour("");
  };

  //todo: add new Category Discout rule
  async function addHandler(){
    let rule = new RulesClass.conditionOnCategoryDiscount(UUID, storeID, discount, categoryID, minAge, minHour, maxHour)
    if (props.compose === undefined) { //false case - no comopse - realy simple
      const sendRulesResponse = await apiClientHttp.addNewDiscountRule(UUID,storeID, {'conditionOnCategoryDiscount':rule});

      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        cleanHandler();
        // props.onRule(sendRulesResponse.value);
        props.onRule(-1);

      }
    } else {
      cleanHandler();
      props.onRule({'conditionOnCategoryDiscount':rule});
    }
  };
  // const [DiscountCommand, SetDiscountCommand] = useState("");
let DiscountCommand=""
  if (props.compose === undefined) {
    DiscountCommand=
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

  }
  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <h3>Category Discount For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Category ID</label>
          <input
            type="number"
            min="0"
            value={categoryID}
            placeholder="Write Category ID"
            onChange={changeCategoryIDHandler}
          />
        </div>
        {DiscountCommand}
        <div className="products__control">
          <label>Minimum Age</label>
          <input
            type="number"
            min="0"
            value={minAge}
            placeholder="write Minimum Age"
            onChange={changeMinAgeHandler}
          />
        </div>
        <div className="products__control">
          <label>Minimum Hour</label>
          <input
            type="number"
            min="0"
            value={minHour}
            placeholder="write Minimum Hour 0-23"
            onChange={changeMinHourHandler}
          />
        </div>
        <div className="products__control">
          <label>Maximum Hour</label>
          <input
            type="number"
            min="0"
            value={maxHour}
            placeholder="write Maximum Hour 0-23"
            onChange={changeMaxHourHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default ConditionCategoryDiscountRule;
