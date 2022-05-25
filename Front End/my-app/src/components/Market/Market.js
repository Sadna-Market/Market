import React, { useState, useEffect } from "react";
import Card from "../UI/Card";
import "./Market.css";
import StoreList from "../Store/StoreList";
import MarketButton from "./MarketButton";
import ProductButton from "../Product/ProductButton";
import Store from "../Store/Store";
import MyCart from "../Cart/MyCart";
import ManagerButton from "../Manager/ManagerButton";
import NewStore from "../Store/newStore";
import Profile from "./Profile";
import Help from "./Help";

const Market = (props) => {
  let UUID = props.uuid;
  console.log("UUID: " +UUID)

  const showStoreHandler = (storeID) => {
    console.log(storeID);
    setCommand(
      <Store storeID={storeID} uuid={UUID} onMarket={onMarketHandler} />
    );
  };

  const [command, setCommand] = useState(
    <MarketButton onShowStore={showStoreHandler} />
  );

  const [notification, setNotification] = useState("Notification");
  let permissonCommand = "";

  const managerHandler = () => {
    setCommand(<ManagerButton uuid={UUID} />);
  };

  const openStoreHandler = () => {
    console.log("openStoreHandler UUID: " +UUID)
    setCommand(
      <NewStore
        uuid={UUID}
        useremail={props.useremail}
        onMarket={() => {
          setCommand(
            <MarketButton uuid={UUID} onShowStore={showStoreHandler} />
          );
        }}
      />
    );
  };

  const profileHandler = () => {
    setCommand(<Profile uuid={UUID} />);
  };

  //todo: check permission if login
  if (props.uuid != -1) {
    permissonCommand = (
      <>
        <button onClick={openStoreHandler}> Open New Store</button>
        <button onClick={profileHandler}>Profile</button>
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

  const onMarketHandler = () => {
    setCommand(<MarketButton uuid={UUID} onShowStore={showStoreHandler} />);
  };

  const MarketHandler = () => {
    setCommand(<MarketButton uuid={UUID} onShowStore={showStoreHandler} />);
  };

  const productHandler = () => {
    setCommand(<ProductButton />);
  };

  const helpHandler = () => {
    setCommand(<Help uuid={UUID} onHelp={(text) => setNotification(text)} />);
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

  return (
    <Card className="market">
      <h2>Notification:</h2>
      {notification}
      <div>
        <button onClick={MarketHandler}> Market</button>
        <button onClick={myCartHandler}> My Cart</button>
        <button onClick={productHandler}> Products</button>
        {permissonCommand}
        <button onClick={helpHandler}> Help!</button>
      </div>
      <div>{command}</div>
    </Card>
  );
};

export default Market;
