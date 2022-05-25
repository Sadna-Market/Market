import React, { useState } from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const HistoryInStore = (props) => {
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

  //todo: search the history
  async function searchHandler(){
    console.log("searchHandler getStoreOrderHistory")

    const getStoreOrderHistoryResponse = await apiClientHttp.getStoreOrderHistory(UUID, storeID);

    if (getStoreOrderHistoryResponse.errorMsg !== -1) {
      SetError(errorCode.get(getStoreOrderHistoryResponse.errorMsg))
    } else {
      setCommand(getStoreOrderHistoryResponse.value);
      setemail("");

    }
    console.log("getStoreOrderHistoryResponse")
    console.log(getStoreOrderHistoryResponse)

  }

  return (
    <div>
      <h3>History of User in Store {storeID}</h3>
      <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
      <div className="products__controls">
        <div className="products__control">
          <label>Email</label>
          <input
            type="text"
            value={email}
            placeholder="Write Email"
            onChange={changeEmailHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={searchHandler}>Search</button>
      </div>
      <div><h2>{command}</h2></div>
    </div>
  );
};

export default HistoryInStore;
