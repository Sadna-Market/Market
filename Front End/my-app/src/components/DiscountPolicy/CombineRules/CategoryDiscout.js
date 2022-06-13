import React, { useState } from "react";

const CategoryDiscount = (props) => {
  let listOfRules = props.rules;
  let UUID = props.uuid;
  let storeID = props.storeID;
  let kind = props.kind; // AND/OR

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

  //todo: add new Category Discout rule with all the list
  const addHandler = () => {
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
