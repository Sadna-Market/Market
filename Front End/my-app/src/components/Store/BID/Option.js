import React, { useState } from "react";
import Card from "../../UI/Card";
import AllOffers from "./AllOffers";
import "./BIDItem.css";
import Options from "./Options";

const Option = (props) => {
  console.log("Option")

  let UUID = props.uuid;
  let productID = props.id;
  let storeID = props.storeID;
  let name = props.name;
  let offer = props.offer;
  const [otherOffer, setOffer] = useState("");
  const offerChangeHandler = (event) => {
    setOffer(event.target.value);
  };

  //todo:accept offer
  const acceptHandler = () => {
    //open offers
  };
  //todo: cancel Offer
  const cancelHandler = () => {
    //open offers
  };
  return (
    <li>
      <Card className="product-item">
        <div className="product-item__description">
          <h2>{name}</h2>
          <h2>Offer: ${offer} </h2>
          <div className="BIDitem__control">
            <label>Your Offer</label>
            $
            <input
              className="BIDitem__control"
              type="number"
              value={otherOffer}
              onChange={offerChangeHandler}
              min={offer}
            />
          </div>
          <div className="BIDitem__control">
            <button onClick={acceptHandler}>send Your ofer</button>
            <button onClick={acceptHandler}>Accept</button>
            <button onClick={cancelHandler}>Cancel</button>
          </div>
        </div>
      </Card>
    </li>
  );
};

export default Option;
