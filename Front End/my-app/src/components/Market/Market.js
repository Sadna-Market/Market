import React, { useState, useEffect } from "react";
import Card from "../UI/Card";
import "./Market.css";
import StoreList from "../Store/StoreList";

const Market = (props) => {
  let permissonCommand = "";

  //check permission
  if (props.uuid === 7) {
    permissonCommand = (
      <>
        <button> Open New Store</button>
        <button> My Stores</button>
        <button> Manager</button>
      </>
    );
  }
  let mainCommand = "";

  const MarketHandler = () => {
    mainCommand = "";
  };

  const [searchStore,setStore] = useState('');


  const findStoreHandler = (event) => {
    setStore(event.target.value);
  };

  const [command,setCommand] =useState(<StoreList />);

  const searchButtonHolder = () =>{

      //console.log(searchStore);
      setCommand(<StoreList search={searchStore} />);
      setStore('');
  };

  return (
    <Card className="market">
      <div>
        <button onClick={MarketHandler}> Market</button>
        <button> My Cart</button>
        <button> Products</button>
        {permissonCommand}
      </div>
      <h3>Market</h3>
      <div className="bar__controls">
          <div className="bar__control">
            <label>Search Store</label>
            <input type="text" value={searchStore} onChange={findStoreHandler} placeholder="store ID" />
            <button onClick={searchButtonHolder}>Search</button>
          </div>
        </div>
        {command}
    </Card>
  );
};

export default Market;
