import React, { useState } from "react";

const AddProduct = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [minPrice, setMinPrice] = useState("");

  const changeMinPriceHandler = (event) => {
    setMinPrice(event.target.value);
  };

  const [productID, setproductID] = useState("");
  const changeProductHandler = (event) => {
    setproductID(event.target.value);
  };

  const cleanHandler = () => {
    setMinPrice("");
    setproductID("");
  };

  //todo: add BID
  const addHandler = () => {
    cleanHandler();
    //add BID
    props.onBID();
  };

  return (
    <div>
      <h3>Add Product to BID #{storeID}</h3>
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
          <label>Min Quantity</label>
          <input
            type="number"
            min="0"
            value={minPrice}
            placeholder="Write Minmum Quantity"
            onChange={changeMinPriceHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add BID</button>
      </div>
    </div>
  );
};

export default AddProduct;
