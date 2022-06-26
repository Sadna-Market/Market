import React, { useState } from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const NewProductInStore = (props) => {
  console.log("NewProductInStore")

  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [productName, setproductName] = useState("");
  const changeProductNameHandler = (event) => {
    setproductName(event.target.value);
  };

  const [productID, setProductID] = useState("");
  const changeProductHandler = (event) => {
    setProductID(event.target.value);
  };
  const [price, setPrice] = useState("");
  const changePriceHandler = (event) => {
    setPrice(event.target.value);
  };
  const [quantity, setquan] = useState("");
  const changequanHandler = (event) => {
    setquan(event.target.value);
  };

  const cleanHandler = () => {
    SetError("")
    setProductID("");
    setPrice("");
    setquan("");
  };

  //todo: add new product type to store
  async function addHandler() {
    console.log("start func addHandler addNewProductToStore")

    const addNewProductToStoreResponse = await apiClientHttp.addNewProductToStore(UUID,storeID,productID,price,quantity);
    console.log(addNewProductToStoreResponse)

    if (addNewProductToStoreResponse.errorMsg !== -1) {
      SetError(errorCode.get(addNewProductToStoreResponse.errorMsg))
    } else {
      cleanHandler();
      props.onStore();

    }

  }
//text   const [productName, setproductName] = useState("");
  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <h3>Add new ProductType to the Store {storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Product Name</label>
          <input
              type="text"
              min="0"
              value={productName}
              placeholder="Write product Name"
              onChange={changeProductNameHandler}
          />
        </div>
        <div className="products__control">
          <label>ProductID</label>
          <input
            type="number"
            min="0"
            value={productID}
            placeholder="Write ProductID"
            onChange={changeProductHandler}
          />
        </div>
        <div className="products__control">
          <label>Price</label>
          <input
            type="number"
            min="0"
            value={price}
            placeholder="Write ProductID"
            onChange={changePriceHandler}
          />
        </div>
        <div className="products__control">
          <label>Quantity</label>
          <input
            type="number"
            min="0"
            value={quantity}
            placeholder="Write ProductID"
            onChange={changequanHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Product</button>
      </div>
    </div>
  );
};

export default NewProductInStore;
