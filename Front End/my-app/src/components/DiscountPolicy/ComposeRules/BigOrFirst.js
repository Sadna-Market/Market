import React, { useEffect, useState } from "react";

const BigOrFirst = (props) => {
  let listOfRules = props.rules;
  let UUID = props.uuid;
  let storeID = props.storeID;
  let kind = props.kind; // AND/OR

  const [discount, setDiscount] = useState(true);

  const cleanHandler = () => {
    props.onRule();
  };

  //todo: add new Category Discout rule with all the list
  const addHandler = () => {
    cleanHandler();
    props.onRule(11);
  };

  const changeValueHandler1 = (event) => {
    setDiscount(false);
  };

  const changeValueHandler2 = (event) => {
    setDiscount(false);
  };

  return (
    <div>
      <h2>Select the category on which the discount will be made</h2>
      <div className="products__controls">
        <div className="products__control">
          <input
            type="radio"
            value={discount}
            onChange={changeValueHandler1}
            name="discount"
          />
          <label>Big Discount</label>
        </div>
        <div className="products__control">
          <input
            type="radio"
            value={!discount}
            onChange={changeValueHandler2}
            name="discount"
          />        
          <label>First Discount</label>
        </div>
        <button onClick={cleanHandler}>Cancel</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default BigOrFirst;
