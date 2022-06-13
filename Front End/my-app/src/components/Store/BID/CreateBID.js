import React, { useState } from "react";

const CeateBID = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");

  const changePriceHandler = (event) => {
    setPrice(event.target.value);
  };

  const changeQuantityHandler = (event) => {
    setQuantity(event.target.value);
  };

  const [productID, setproductID] = useState("");
  const changeProductHandler = (event) => {
    setproductID(event.target.value);
  };

  const cleanHandler = () => {
    setQuantity("");
    setPrice("");
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
          <label>Quantity</label>
          <input
            type="number"
            min="0"
            value={quantity}
            placeholder="Write Minmum Quantity"
            onChange={changeQuantityHandler}
          />
        </div>
        <div className="products__control">
          <label>Price</label>
          <input
            type="number"
            min="0"
            value={price}
            placeholder="Write Minmum Quantity"
            onChange={changePriceHandler}
          />
        </div>
        <div>
          <button onClick={cleanHandler}>Clean</button>
          <button onClick={addHandler}>Add BID</button>
        </div>
      </div>
    </div>
  );
};

export default CeateBID;
