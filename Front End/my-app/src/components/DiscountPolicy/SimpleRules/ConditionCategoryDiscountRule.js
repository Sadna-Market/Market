import React, { useState } from "react";

const ConditionCategoryDiscountRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [discount, setDiscount] = useState("");
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
    setDiscount("");
    setCategoryID("");
    setMinAge("");
    setMinHour("");
    setmaxHour("");
  };

  //todo: add new Category Discout rule
  const addHandler = () => {
    cleanHandler();
    //return the ruleId in onRule insead of 10/11/12/13
    props.onRule(11);
  };

  return (
    <div>
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
