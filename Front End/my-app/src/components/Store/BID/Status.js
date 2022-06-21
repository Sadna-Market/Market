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
  async function reject(){
    //        responseCounterBID:async(uuid,storeID,productID,approve)=>{
    const responseCounterBIDResponse = await apiClientHttp.responseCounterBID(UUID,storeID,productID,false);
    console.log(responseCounterBIDResponse)

    if (responseCounterBIDResponse.errorMsg !== -1) {
      SetError(errorCode.get(responseCounterBIDResponse.errorMsg))
    } else {
      SetError("")

    }
  }
  async function Confirm(){
    const responseCounterBIDResponse = await apiClientHttp.responseCounterBID(UUID,storeID,productID,true);
    console.log(responseCounterBIDResponse)

    if (responseCounterBIDResponse.errorMsg !== -1) {
      SetError(errorCode.get(responseCounterBIDResponse.errorMsg))
    } else {
      SetError("")

    }
  }
  //todo: send offer

  const sendHandler = () => {
    setOffer("");
  };
  //get the price and the amount
  const [status, setStatus] = useState(props.status);
  const [originPrice, setoriginPrice] = useState(props.originPrice);
  const [counterPrice, setcounterPrice] = useState(props.counterPrice);

  let name = props.name;
//            originPrice={expense.originPrice}
//             counterPrice={expense.counterPrice}
//             id={expense.id}
//             name={expense.name}
//             status={expense.status}
//             uuid={UUID}
//             storeID={storeID}
//             amount={expense.amount}
//             onConfirm={() => props.onConfirm(expense.id, expense.amount)}
  return (
    <li>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

      <Card className="product-item">
        <div className="product-item__price">#{productID}</div>
        <div className="product-item__description">
          <h2>{name}</h2>
          <h2>amount: {props.amount}</h2>
          <h2>Status: {status} </h2>
          <h2>OriginPrice: {originPrice} </h2>

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
          {/*{status === "Counter" ? (*/}
          {/*  <>*/}
          {/*    <h2>Your Offer</h2>$*/}
          {/*    <input*/}
          {/*      type="number"*/}
          {/*      value={offer}*/}
          {/*      onChange={offerHandler}*/}
          {/*    ></input>*/}
          {/*    <button onClick={sendHandler}>Send</button>*/}
          {/*  </>*/}
          {/*) : */}

              {status === "Approved" ? (
            <>
              <button onClick={() => props.onConfirm()}>Buy</button>
              <button onClick={cancelHandler}>Cancel</button>

            </>
          ) :

                  (
            <></>
          )}
        </div>
        <div>
          {status === "Waiting" ? (
              <>
            <button onClick={cancelHandler}>Cancel</button>
            </>
            ) : (
            <></>
          )}
        </div>
        <div>
          {
          status === "Counter" ? (
              <>
                <h2>CounterPrice: {counterPrice} </h2>
                <button onClick={Confirm}>Confirm</button>
                <button onClick={reject}>Reject</button>
                <button onClick={cancelHandler}>Cancel</button>

              </>
          ) : (
              <></>
          )}
        </div>
      </Card>
    </li>
  );
};

export default Status;
