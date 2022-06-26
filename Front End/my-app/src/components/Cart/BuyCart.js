import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const BuyCart = (props) => {
  console.log("BuyCart")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  const [enteredConfirmation, SetConfirmation] = useState("");
  let UUID = props.uuid;
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
    SetError("")
    setCardDate("");
    setCardDate("");
    setPin("");
    setCity("");
    setAdress("");
    setApart("");
    setCardNumber("");
  };

  async function buyHandler(){
    console.log("UUID, city, adress,apartment,cardNumber,cardDate, cardPin",UUID, city, adress,apartment,cardNumber,cardDate, cardPin)
    const orderShoppingCartResponse = await apiClientHttp.orderShoppingCart(UUID, city, adress,apartment,cardNumber,cardDate, cardPin);
    let str = JSON.stringify(orderShoppingCartResponse);
    console.log("orderShoppingCartResponset",str)

    if (orderShoppingCartResponse.errorMsg !== -1) {
      SetError(errorCode.get(orderShoppingCartResponse.errorMsg))
    } else {
      console.log("orderShoppingCartResponse")

      console.log(orderShoppingCartResponse.value)
      SetConfirmation("The Order completed successfully! \n TID :" +orderShoppingCartResponse.value.TID+ " Final Price :" + orderShoppingCartResponse.value.finalPrice)
      cleanHandler();
    }
  }
  async function finishBuyHandler() {
    props.onMarket();

  }

  // useEffect(() => {
  //   getAllStores();
  // }, [enteredConfirmation.refresh]);
  // let permission = "";

  return (
      <div className="products">
        <div style={{ color: 'red',backgroundColor: "green",fontSize: 30 }}>{enteredConfirmation}</div>
        <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
        <button onClick={finishBuyHandler}>Return to the Market</button>

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

export default BuyCart;
