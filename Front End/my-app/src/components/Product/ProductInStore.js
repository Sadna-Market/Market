import React, { useState } from "react";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const ProductInStore = (props) => {
  console.log("ProductInStore")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  const [add, setAdd] = useState("");
  let UUID = props.UUID;
  let productID = props.id;
  let storeID = props.storeID;
  let productName = props.name;

//      <ProductInStore id={expense.id} UUID={props.uuid} storeID={props.storeID} />
  //      <ProductInStore id={expense.id} UUID={props.uuid} storeID={props.storeID} name={expense.name} />
  //    //str. {"errorMsg":-1,"value":[{"quantity":10,"price":10,"itemID":1,"name":null}]}
  //todo get the info
  let name = "productID: "+props.id;
  let price =props.price;
  let quantity=props.quantity;
  // let max = "100";

  const changeAmountHandler = (event) => {
    setAdd(event.target.value);
  };

  //todo: add to cart!
  //        addProductToShoppingBag :async (uuid,storeid,productid,quantity)=>{
  async function clickHandler() {
    console.log("UUID    "+UUID,'storeID',storeID,'productID',productID,'quantity',add)

    const addProductToShoppingBagResponse = await apiClientHttp.addProductToShoppingBag(UUID, storeID, productID,add);
    if (addProductToShoppingBagResponse.errorMsg !== -1) {
      SetError(errorCode.get(addProductToShoppingBagResponse.errorMsg))
    } else {
      let str = JSON.stringify(addProductToShoppingBagResponse);
      console.log("addProductToShoppingBagResponse    "+str)
      setAdd("");
      SetError("")
    }
  }
  return (
    <li>
      <Card className="product-item">
        <div className="product-item__description">
          <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

          <h2>{name}</h2>
          <h2>Price: ${price}</h2>
          <h2>quantity: {quantity}</h2>
          <div className="bar__control">
            <input
              type="number"
              value={add}
              placeholder="amount"
              onChange={changeAmountHandler}
              min="0"
              // max={max}
            />
          </div>
          <button onClick={clickHandler}>Add</button>
        </div>
      </Card>
    </li>
  );
};

export default ProductInStore;
