import React, { useState } from "react";

const CategoryDiscountRule = (props) => {
  console.log("CategoryDiscountRule")

  let UUID = props.uuid;
  let storeID = props.storeID;

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
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default CategoryDiscountRule;
