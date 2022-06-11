import React, { useState } from "react";
import Card from "../UI/Card";

const ProductInStore = (props) => {
  console.log("ProductInStore")

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
  const clickHandler = () => {
    setAdd("");
  };
  return (
    <li>
      <Card className="product-item">
        <div className="product-item__description">
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
