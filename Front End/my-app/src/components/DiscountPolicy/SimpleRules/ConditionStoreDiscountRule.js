import React, { useState } from "react";

const ConditionStoreDiscountRule = (props) => {
  console.log("ConditionStoreDiscountRule")

  let UUID = props.uuid;
  let storeID = props.storeID;

  const [discount, setDiscount] = useState("");
  const changeDiscountHandler = (event) => {
    setDiscount(event.target.value);
  };

  const [totalPrice, setTotalPrice] = useState("");
  const changeTotalPriceHandler = (event) => {
    setTotalPrice(event.target.value);
  };

  const [minQuantity, setminQuantity] = useState("");
  const changeMinQuantityHandler = (event) => {
    setminQuantity(event.target.value);
  };

  const [maxQuantity, setmaxQuantity] = useState("");
  const changeMaxQuanHandler = (event) => {
    setmaxQuantity(event.target.value);
  };

  const cleanHandler = () => {
    setDiscount("");
    setTotalPrice("");
    setmaxQuantity("");
    setminQuantity("");
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
          <label>Min Quantity</label>
          <input
            type="number"
            min="0"
            value={minQuantity}
            placeholder="write Minimum Quantity"
            onChange={changeMinQuantityHandler}
          />
        </div>
        <div className="products__control">
          <label>Max Quantity</label>
          <input
            type="number"
            min="0"
            value={maxQuantity}
            placeholder="write Maximum Quantity"
            onChange={changeMaxQuanHandler}
          />
        </div>
        <div className="products__control">
          <label>Total Price</label>
          <input
            type="number"
            min="0"
            value={totalPrice}
            placeholder="Write Total Price"
            onChange={changeTotalPriceHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default ConditionStoreDiscountRule;
