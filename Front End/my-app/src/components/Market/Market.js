import React, { useState, useEffect } from "react";
import Card from "../UI/Card";
import "./Market.css";
import StoreList from "../Store/StoreList";
import MarketButton from "./MarketButton";
import ProductButton from "../Product/ProductButton";
import Store from "../Store/Store";
import MyCart from "../Cart/MyCart";
import ManagerButton from "../Manager/ManagerButton";

const Market = (props) => {
  let UUID = props.uuid;

  const showStoreHandler = (storeID) => {
    console.log(storeID);
    setCommand(<Store storeID={storeID} uuid={UUID} />);
  };

  const [command, setCommand] = useState(
    <MarketButton onShowStore={showStoreHandler} />
  );
  let permissonCommand = "";

  const managerHandler = () => {
    setCommand(<ManagerButton uuid={UUID} />);
  };

  //todo: check permission if login
  if (props.uuid === 7) {
    permissonCommand = (
      <>
        <button> Open New Store</button>
        <button> My Stores</button>
      </>
    );
  }

  //todo: check if manager
  if (props.uuid === 7) {
    permissonCommand = (
      <>
        {permissonCommand}
        <button onClick={managerHandler}> Manager</button>
      </>
    );
  }

  const MarketHandler = () => {
    setCommand(<MarketButton onShowStore={showStoreHandler} />);
  };

  const productHandler = () => {
    setCommand(<ProductButton />);
  };

  const myCartHandler = () => {
    setCommand(
      <MyCart
        uuid={UUID}
        onMarket={() =>
          setCommand(
            <MarketButton uuid={UUID} onShowStore={showStoreHandler} />
          )
        }
      />
    );
  };

  const [searchStore, setStore] = useState("");

  return (
    <Card className="market">
      <div>
        <button onClick={MarketHandler}> Market</button>
        <button onClick={myCartHandler}> My Cart</button>
        <button onClick={productHandler}> Products</button>
        {permissonCommand}
      </div>
      <div>{command}</div>
    </Card>
  );
};

export default Market;
