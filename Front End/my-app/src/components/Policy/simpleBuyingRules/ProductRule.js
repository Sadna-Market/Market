import React, { useState } from "react";

const ProductRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [minQuantity, setminQuantity] = useState("");

  const changeQuantityHandler = (event) => {
    setminQuantity(event.target.value);
  };

  const [maxQuantity, setmaxQuantity] = useState("");
  const changeMaxQuanHandler = (event) => {
    setmaxQuantity(event.target.value);
  };

  const [productID, setproductID] = useState("");
  const changeProductHandler = (event) => {
    setproductID(event.target.value);
  };

  const cleanHandler = () => {
    setminQuantity("");
    setmaxQuantity("");
    setproductID("");
  };

  //todo: add new ShoppingBag Rule to store
  const addHandler = () => {
    cleanHandler();
    props.onRule();
  };

  return (
    <div>
      <h3>Add Product Rule For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Product ID</label>
          <input
            type="number"
            min="0"
            value={minQuantity}
            placeholder="Write PRoduct ID"
            onChange={changeProductHandler}
          />
        </div>
        <div className="products__control">
          <label>Min Quantity</label>
          <input
            type="number"
            min="0"
            value={minQuantity}
            placeholder="Write Minmum Quantity"
            onChange={changeQuantityHandler}
          />
        </div>
        <div className="products__control">
          <label>Max Quantity</label>
          <input
            type="number"
            min="0"
            value={maxQuantity}
            placeholder="Write Minmum Product Types"
            onChange={changeMaxQuanHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default ProductRule;
