import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"

const BuyBID = (props) => {
  let UUID = props.uuid;
  let productID = props.productID;
  let storeID = props.storeID;
  let amount = props.amount;
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  const [cardNumber, setCardNumber] = useState("");
  const changeCardHandler = (event) => {
    setCardNumber(event.target.value);
  };
  const [cardDate, setCardDate] = useState("");
  const changeDateHandler = (event) => {
    setCardDate(event.target.value);
  };
  const [cardPin, setPin] = useState("");
  const changePinHandler = (event) => {
    setPin(event.target.value);
  };

  const [city, setCity] = useState("");
  const changeCityHandler = (event) => {
    setCity(event.target.value);
  };
  const [adress, setAdress] = useState("");
  const changeAdressHandler = (event) => {
    setAdress(event.target.value);
  };
  const [apartment, setApart] = useState("");
  const changeApartHandler = (event) => {
    setApart(event.target.value);
  };

  const cleanHandler = () => {
    setCardDate("");
    setCardDate("");
    setPin("");
    setCity("");
    setAdress("");
    setApart("");
  };

  //todo: orderto BID!
  //        BuyBID:async(userId,storeID,productID,city,adress,apartment,creditcard,creditDate,pin)=>{
  async function  buyHandler(){
    const BuyBIDResponse = await apiClientHttp.BuyBID(UUID,storeID,productID,city,adress,apartment,cardNumber,cardDate,cardPin);
    console.log(BuyBIDResponse)

    if (BuyBIDResponse.errorMsg !== -1) {
      SetError(errorCode.get(BuyBIDResponse.errorMsg))
    } else {
      SetError("")
      props.onBID();
      cleanHandler();
    }


  };

  return (
    <div className="products">
      <h3>Credit Card</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>CreditCard</label>
          <input
            type="number"
            value={cardNumber}
            placeholder="Write CraditCard"
            onChange={changeCardHandler}
          />
        </div>
        <div className="products__control">
          <label>Date of Card</label>
          <input
            type="text"
            value={cardDate}
            placeholder="Write Date"
            onChange={changeDateHandler}
          />
        </div>
        <div className="products__control">
          <label>Pin</label>
          <input
            type="number"
            value={cardPin}
            placeholder="Write pin"
            onChange={changePinHandler}
          />
        </div>
      </div>
      <h3>Address</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>City</label>
          <input
            type="text"
            value={city}
            placeholder="Write City"
            onChange={changeCityHandler}
          />
        </div>
        <div className="products__control">
          <label>Adress</label>
          <input
            type="text"
            value={adress}
            placeholder="Write Adress"
            onChange={changeAdressHandler}
          />
        </div>
        <div className="products__control">
          <label>Apartment</label>
          <input
            type="number"
            value={apartment}
            placeholder="Write number of Apartment"
            onChange={changeApartHandler}
          />
        </div>
      </div>
      <button onClick={cleanHandler}>Clean</button>
      <button onClick={buyHandler}>Buy</button>
    </div>
  );
};

export default BuyBID;
