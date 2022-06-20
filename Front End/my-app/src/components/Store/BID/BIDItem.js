import React, { useState } from "react";
import Card from "../../UI/Card";
import "./BIDItem.css";

const BIDItem = (props) => {
  console.log("BIDItem")

  let UUID = props.uuid;
  let productID = props.id;
  let storeID = props.storeID;

  const [offer, setoffer] = useState("");
  const offerChangeHandler = (event) => {
    setoffer(event.target.value);
  };

  //get the price and the amount
  let price = props.price;
  let name = props.name;

  //todo:send offer
  const clickHandler = () => {
    
    //send offer
  };
  return (
    <li>
      <Card className="product-item">
        <div className="product-item__price">#{productID}</div>
        <div className="product-item__description">
          <h2>{name}</h2>
          <h2>min price: ${Math.ceil(price)} </h2>
          <div className="BIDitem__control">
            <label>price</label>
            $
            <input
              className="BIDitem__control"
              type="number"
              value={offer}
              onChange={offerChangeHandler}
              min={Math.ceil(price)}
            />
            <button onClick={clickHandler}>send Offer</button>
          </div>
        </div>
      </Card>
    </li>
  );
};

export default BIDItem;
