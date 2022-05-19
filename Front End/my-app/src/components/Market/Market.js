import React, { useState, useEffect } from "react";
import Card from "../UI/Card";
import "./Market.css";
import StoreList from "../Store/StoreList";
import MarketButton from "./MarketButton";
import ProductButton from "../Product/ProductButton";
import Store from "../Store/Store";

const Market = (props) => {
  let UUID = props.uuid;

  const showStoreHandler = storeID => {
    console.log(storeID);
    setCommand(<Store storeID={storeID} uuid={UUID}/>);
  };

  const [command, setCommand] = useState(<MarketButton onShowStore={showStoreHandler}/>);
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

  const MarketHandler = () => {
    setCommand(<MarketButton  onShowStore={showStoreHandler}/>);
  };

  const productHandler = ()=>{
    setCommand(<ProductButton />);
  }

  const [searchStore, setStore] = useState("");

  return (
    <Card className="market">
      <div>
        <button onClick={MarketHandler}> Market</button>
        <button> My Cart</button>
        <button onClick={productHandler}> Products</button>
        {permissonCommand}
      </div>
      <div>{command}</div>
    </Card>
  );
};

export default Market;
