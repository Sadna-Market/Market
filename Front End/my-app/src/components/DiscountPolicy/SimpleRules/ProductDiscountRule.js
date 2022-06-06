import React, { useState } from "react";

const ProductDiscountRule = (props) => {
  console.log("ProductDiscountRule")

  let UUID = props.uuid;
  let storeID = props.storeID;

  const [discount, setDiscount] = useState("");
  const changeDiscountHandler = (event) => {
    setDiscount(event.target.value);
  };

  const [productID, setproductID] = useState("");
  const changeProductHandler = (event) => {
    setproductID(event.target.value);
  };

  const cleanHandler = () => {
    setDiscount("");
    setproductID("");
  };

  //todo: add new Product Discount rule
  const addHandler = () => {
    cleanHandler();
    //return the ruleId in onRule insead of 10/11/12/13
    props.onRule(11);
  };

  return (
    <div>
      <h3>Product Discount For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Product ID</label>
          <input
            type="number"
            min="0"
            value={productID}
            placeholder="Write PRoduct ID"
            onChange={changeProductHandler}
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

export default ProductDiscountRule;
