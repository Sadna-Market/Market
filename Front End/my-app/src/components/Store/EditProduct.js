import React, { useState } from "react";

const EditProduct = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [productID, setProductID] = useState("");
  const changeProductHandler = (event) => {
    setProductID(event.target.value);
  };

  const [price, setPrice] = useState("");
  const changePriceHandler = (event) => {
    setPrice(event.target.value);
  };

  const [quantity, setQuantity] = useState("");
  const changeQuanHandler = (event) => {
    setQuantity(event.target.value);
  };

  const cleanHandler = () => {
    setProductID("");
    setPrice("");
    setQuantity("");
  };

  //Edit product from store
  //if quantity === "" so is only about price , same to price.
  const editHandler = () => {
    cleanHandler();
    props.onStore();
  };

  return (
    <div>
      <h3>Edit Product From Store {storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>ProductID</label>
          <input
            type="number"
            min="0"
            value={productID}
            placeholder="Write ProductID to Edit"
            onChange={changeProductHandler}
          />
        </div>
        <div className="products__control">
          <label>Price</label>
          <input
            type="number"
            min="0"
            value={price}
            placeholder="Write Price (can stay empty)"
            onChange={changePriceHandler}
          />
        </div>
        <div className="products__control">
          <label>Quantity</label>
          <input
            type="number"
            min="0"
            value={quantity}
            placeholder="Write Quantity (can stay empty)"
            onChange={changeQuanHandler}
          />
        </div>
      </div>
      <button onClick={cleanHandler}>Clean</button>
      <button onClick={editHandler}>Remove Product</button>
    </div>
  );
};

export default EditProduct;
