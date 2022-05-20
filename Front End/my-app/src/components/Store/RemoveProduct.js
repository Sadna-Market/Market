import React, { useState } from "react";

const RemoveProduct = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [productID, setProductID] = useState("");
  const changeProductHandler = (event) => {
    setProductID(event.target.value);
  };

  const cleanHandler = () => {
    setProductID("");
  };

  //remove product from store
  const removeHandler = () => {
    cleanHandler();
    props.onStore();
  };

  return (
    <div>
      <h3>Remove Product From Store {storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>ProductID</label>
          <input
            type="number"
            min="0"
            value={productID}
            placeholder="Write ProductID to remove"
            onChange={changeProductHandler}
          />
        </div>
      </div>
      <button onClick={cleanHandler}>Clean</button>
      <button onClick={removeHandler}>Remove Product</button>
    </div>
  );
};

export default RemoveProduct;
