import React, { useState } from "react";
import Card from "../../UI/Card";
import AllOffers from "./AllOffers";
import "./BIDItem.css";
import Options from "./Options";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
const Status = (props) => {
  console.log("Status")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let productID = props.id;
  let storeID = props.storeID;
  //todo:open offers
  const clickHandler = () => {};
  const [offer, setOffer] = useState("");

  const offerHandler = (event) => {
    setOffer(event.target.value);
  };
  const confirmHandler = (event) => {//buy
    // BuyBID:async(userId,storeID,productID,city,adress,apartment,creditcard,creditDate,pin)=>{
    setStatus("Cancel")
  };
  async function cancelHandler(){
    const removeBIDResponse = await apiClientHttp.removeBID(UUID,storeID,productID,);
    console.log("start func  getAllProducts")
    console.log(removeBIDResponse)

    if (removeBIDResponse.errorMsg !== -1) {
      SetError(errorCode.get(removeBIDResponse.errorMsg))
    } else {
      SetError("")

    }
  }
  //confirmhander/ /
  //after this all the manager need to confired and then pay
  //responseCounterBID  aprove true or fale if he confirm or not   only ehne counter
  // confirm -responseCounterBID  true
  //reject- responseCounterBID  false
  // cancel - removebid
  // without the $
  //to present price (הצעת הנגד)


  //todo: send offer

  const sendHandler = () => {
    setOffer("");
  };
  //get the price and the amount
  const [status, setStatus] = useState(props.status);

  let name = props.name;

  return (
    <li>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

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
              <button onClick={() => props.onConfirm()}>Confirm</button>

            </>
          ) : (
            <></>
          )}
        </div>
        <div>
          {status === "Waiting" ||
          status === "Approved" ||
          status === "Counter" ? (
            <button onClick={cancelHandler}>Cancel</button>
          ) : (
            <></>
          )}
        </div>
      </Card>
    </li>
  );
};

export default Status;
