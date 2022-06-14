import React, { useState } from "react";
import Card from "../../UI/Card";
import AllOffers from "./AllOffers";
import "./BIDItem.css";
import Options from "./Options";

const Status = (props) => {
  let UUID = props.uuid;
  let productID = props.id;
  let storeID = props.storeID;
  //todo:open offers
  const clickHandler = () => {};
  const [offer, setOffer] = useState("");

  const offerHandler = (event) => {
    setOffer(event.target.value);
  };

  //todo: send offer
  const sendHandler = () => {
    setOffer("");
  };
  //get the price and the amount
  const [status, setStatus] = useState(props.status);

  let name = props.name;

  return (
    <li>
      <Card className="product-item">
        <div className="product-item__price">#{productID}</div>
        <div className="product-item__description">
          <h2>{name}</h2>
          <h2>amount: {props.amount}</h2>
          <h2>Status: {status} </h2>
          {/* {status === "Counter"?             
            <>
              <h2>Your Offer</h2>
              $<input type="number" value={offer} onChange={offerHandler}></input>
              <button onClick={sendHandler}>Send</button>
              <button onClick={()=>setStatus("Cancel")}>Cancel</button>
            </>:
            status === "Waiting"?
            <>
                <button onClick={()=>setStatus("Cancel")}>Cancel</button>
            </>:
            status === "Approved"?
            <>
                <button onClick={()=>setStatus("Cancel")}>Confirm</button>
            <button onClick={()=>setStatus("Cancel")}>Cancel</button>
        </>:
        <></>
             } */}
          {status === "Counter" ? (
            <>
              <h2>Your Offer</h2>$
              <input
                type="number"
                value={offer}
                onChange={offerHandler}
              ></input>
              <button onClick={sendHandler}>Send</button>
            </>
          ) : status === "Approved" ? (
            <>
              <button onClick={() => setStatus("Cancel")}>Confirm</button>
            </>
          ) : (
            <></>
          )}
        </div>
        <div>
          {status === "Waiting" ||
          status === "Approved" ||
          status === "Counter" ? (
            <button onClick={() => setStatus("Cancel")}>Cancel</button>
          ) : (
            <></>
          )}
        </div>
      </Card>
    </li>
  );
};

export default Status;
