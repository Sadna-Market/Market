import React, {useEffect, useState} from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const HistoryInStore = (props) => {
  console.log("HistoryInStore")
  const [enteredHistory, SetHistory] = useState([]);

  let UUID = props.uuid;
  let storeID = props.storeID;
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  const [email, setemail] = useState("");
  const changeEmailHandler = (event) => {
    setemail(event.target.value);
  };

  const [command, setCommand] = useState("");

  const cleanHandler = () => {
    setemail("");
    setCommand("");
  };
//          for (const key in shoppingBagHash) { //key = storeID
//                 let store = (shoppingBagHash[key].store);//{"storeId":1,"name":"sys","founder":"sysManager@gmail.com","isOpen":true,"rate":5}
//                 let productQuantity = (shoppingBagHash[key].productQuantity); //{1:20, 2:50} product id and quantity
//                 for (const key in productQuantity) {
//                     products.push({
//                         id: key,
//                         storeID:store.storeId,
//                         // price: store.price,
//                         // productName: store.productName,
//                         price: 1,
//                         amount: productQuantity[key],
//                     })
//                 }
//             }
  //todo: search the history
  async function searchHandler(){
    let History = [];

    console.log("searchHandler getStoreOrderHistory storeID" +storeID )

    const getStoreOrderHistoryResponse = await apiClientHttp.getStoreOrderHistory(UUID, storeID);//all //just for manager and ouwner and sys

    if (getStoreOrderHistoryResponse.errorMsg !== -1) {
      SetError(errorCode.get(getStoreOrderHistoryResponse.errorMsg))
    } else {

      for (let i = 0; i < getStoreOrderHistoryResponse.value.length; i++) {
        console.log(getStoreOrderHistoryResponse.value[i].TID)
        console.log(getStoreOrderHistoryResponse.value[i].finalPrice)
        console.log(getStoreOrderHistoryResponse.value[i].products)
        let products=getStoreOrderHistoryResponse.value[i].products
        for (let j = 0; j < products.length; j++) {
          console.log(products[j].itemID)
          console.log(products[j].name)
          console.log(products[j].price)
          console.log(products[j].quantity)
          History.push({
            TID: getStoreOrderHistoryResponse.value[i].TID,
            finalPrice: getStoreOrderHistoryResponse.value[i].finalPrice,
            itemID: products[j].itemID,
            // name: products[j].name,
            price: products[j].price,
            quantity: products[j].quantity,
          })
        }
      }
      // setCommand(getStoreOrderHistoryResponse.value);
      SetHistory(History)
      setemail("");
      SetError("")
    }
    console.log("getStoreOrderHistoryResponse")
    console.log(getStoreOrderHistoryResponse)
  }
  useEffect(() => {
    searchHandler();
  }, [enteredHistory.refresh]);
  
  let expensesContent = enteredHistory.map((expense) => (
      <h3>
      <h2>TID: {expense.TID} ,TID final Price:  {expense.finalPrice} ,item ID: {expense.itemID} ,Product price: {expense.price} ,quantity: {expense.quantity}</h2>
      </h3>
));
  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

      <h3>History of All Users in Store {storeID}</h3>
      <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
      <div className="products__controls">

        {/*<div className="products__control">*/}
        {/*  <label>Email</label>*/}
        {/*  <input*/}
        {/*    type="text"*/}
        {/*    value={email}*/}
        {/*    placeholder="Write Email"*/}
        {/*    onChange={changeEmailHandler}*/}
        {/*  />*/}
        {/*</div>*/}
        {/*<button onClick={cleanHandler}>Clean</button>*/}
        {/*<button onClick={searchHandler}>Search</button>*/}
        {/*<h3></h3>*/}

        {expensesContent}
      </div>
      <div><h2>{command}</h2></div>
    </div>
  );
};

export default HistoryInStore;
