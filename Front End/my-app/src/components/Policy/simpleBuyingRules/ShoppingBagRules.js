import React, { useState } from "react";

const ShoppingBagRule = (props) => {
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
    setminQuantity("");
    setminProductTypes("");
  };

  //todo: add new ShoppingBag Rule to store
  const addHandler = () => {
    cleanHandler();
    //return the ruleId in onRule insead of 10/11/12/13
    props.onRule(12);
  };

  return (
    <div>
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