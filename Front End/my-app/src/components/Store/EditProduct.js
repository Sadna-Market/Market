import React, { useState } from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const EditProduct = (props) => {
  console.log("EditProduct")

  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  const [enteredError2, SetError2] = useState("");

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
//        setProductPriceInStore: async(uuid,storeid,productId,price)=>{
  //        setProductQuantityInStore: async(uuid,storeid,productId,quantity)=>{

//      <EditProduct uuid={UUID} storeID={storeID} onStore={returnToStore} />
  //Edit product from store
  //if quantity === "" so is only about price , same to price.
  async function editHandler(){

    let pass = true;
    if (price!==""){
      const setProductPriceInStoreResponse = await apiClientHttp.setProductPriceInStore(UUID, storeID, productID,price);
      if (setProductPriceInStoreResponse.errorMsg !== -1) {
        pass=false;
        SetError(errorCode.get(setProductPriceInStoreResponse.errorMsg))
      }
      console.log(setProductPriceInStoreResponse)

    }
    console.log('before quantity'+quantity)

    if (quantity!==""){
      console.log('quantity'+quantity)

      const setProductQuantityInStoreResponse = await apiClientHttp.setProductQuantityInStore(UUID, storeID, productID,quantity);
      if (setProductQuantityInStoreResponse.errorMsg !== -1) {
        pass=false;
        SetError2(errorCode.get(setProductQuantityInStoreResponse.errorMsg))
      }
      console.log(setProductQuantityInStoreResponse)

    }

    if (pass===true){
      cleanHandler();
      props.onStore();
    }

  }

  return (
    <div>
      <h3>Edit Product From Store {storeID}</h3>
      <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
      <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError2}</div>
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
      <button onClick={editHandler}>Edit Product</button>
    </div>
  );
};

export default EditProduct;
