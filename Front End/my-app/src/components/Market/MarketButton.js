import React, { useEffect, useState } from "react";
import StoreList from "../Store/StoreList";


const MarketButton = (props) => {
  const [searchStore, setStore] = useState("");
  console.log("MarketButton")

  const enterToStoreHandler = storeID => {
    props.onShowStore(storeID);
  };
  const [command, setCommand] = useState(<StoreList search={searchStore} onEnterToStore={enterToStoreHandler}/>);
  const [backCommand, setBackCommand] = useState(command);

  const searchButtonHolder = () => {
    setBackCommand(command);
    setCommand(<StoreList search={searchStore} onEnterToStore={enterToStoreHandler} />);
    setStore(() => "");
  };

  const backHandler= () =>{
    setCommand(backCommand);
  };


  const findStoreHandler = (event) => {
    setStore(() => event.target.value);
  };

  return (
    <div className="marketButton">
      <h3>Market</h3>
      <div className="bar__controls">
        <div className="bar__control">
          <label>Search Store</label>
          <input
            type="text"
            value={searchStore}
            onChange={findStoreHandler}
            placeholder="store ID"
          />
          <button onClick={searchButtonHolder}>Search</button>
          <button onClick={backHandler}>Back</button>
        </div>
      </div>
      {command}
    </div>
  );
};

export default MarketButton;
