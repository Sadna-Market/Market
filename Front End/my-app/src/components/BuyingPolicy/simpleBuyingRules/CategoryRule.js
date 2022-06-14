import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"

const CategoryRule = (props) => {
  console.log("CategoryRule")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  let UUID = props.uuid;
  let storeID = props.storeID;

  const [category, setCategory] = useState("");
  const changeCategoryHandler = (event) => {
    setCategory(event.target.value);
  };

  const [minAge, setminAge] = useState("");
  const changeMinAgeHandler = (event) => {
    setminAge(event.target.value);
  };

  const [minHour, setminHour] = useState("");
  const changeMinHourHandler = (event) => {
    setminHour(event.target.value);
  };

  const [maxHour, setmaxHour] = useState("");
  const changeMaxHourHandler = (event) => {
    setmaxHour(event.target.value);
  };

  const cleanHandler = () => {
    setCategory("");
    setminAge("");
    setminHour("");
    setmaxHour("");
  };

  //todo: add new ShoppingBag Rule to store
  async function  addHandler() {
    let rule = new RulesClass.CategoryRule(UUID,storeID,minAge,category,minHour,maxHour)
    if (props.compose===undefined) { //false case - no comopse - realy simple

      const sendRulesResponse = await apiClientHttp.addNewBuyRule(UUID,storeID, {'CategoryRule':rule});

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
    props.onRule({'CategoryRule':rule});
  }

  return (
    <div>
      <h3>Add Category Rule For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Category ID</label>
          <input
            type="number"
            min="0"
            value={category}
            placeholder="Write Category ID"
            onChange={changeCategoryHandler}
          />
        </div>
        <div className="products__control">
          <label>Min Age</label>
          <input
            type="number"
            min="0"
            value={minAge}
            placeholder="Write Minmum Quantity"
            onChange={changeMinAgeHandler}
          />
        </div>
        <div className="products__control">
          <label>minHour</label>
          <input
            type="number"
            min="0"
            max="23"
            value={minHour}
            placeholder="Write Hour 0-23"
            onChange={changeMinHourHandler}
          />
        </div>
        <div className="products__control">
          <label>maxHour</label>
          <input
            type="number"
            min="0"
            max="23"
            value={maxHour}
            placeholder="Write Hour 0-23"
            onChange={changeMaxHourHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default CategoryRule;
