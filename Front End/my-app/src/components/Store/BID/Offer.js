import React, { useState } from "react";
import Card from "../../UI/Card";
import AllOffers from "./AllOffers";
import "./BIDItem.css";
import Options from "./Options";

const Offer = (props) => {
  let UUID = props.uuid;
  let productID = props.id;
  let storeID = props.storeID;
  const [offersCommand, setOffersCommand] = useState("");

  const [offer, setoffer] = useState("");
  const offerChangeHandler = (event) => {
    setoffer(event.target.value);
  };

  //get the price and the amount
  let offers = props.offers;
  let name = props.name;

  //todo:open offers
  const clickHandler = () => {
    //open offers
    if (offersCommand == "")
      setOffersCommand(
        <Options uuid={UUID} storeID={storeID} offers={offers} />
      );
    else setOffersCommand("");
  };
  return (
    <li>
      <Card className="product-item">
        <div className="product-item__price">#{productID}</div>
        <div className="product-item__description">
          <h2>{name}</h2>
          <h2>Number of Offers: {Math.ceil(offers)} </h2>
          <h2>{props.managers} /5</h2>
          <div className="BIDitem__control">
            <button onClick={clickHandler}>Offers</button>
          </div>
        </div>
      </Card>
      <Card>{offersCommand}</Card>
    </li>
  );
};

export default Offer;
