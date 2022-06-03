import React, { useState } from "react";
import ProductInStoreList from "../Product/ProductInStoreList";
import NewProductInStore from "./NewProductInStore";
import RemoveProduct from "./RemoveProduct";
import EditProduct from "./EditProduct";
import AddManager from "./AddManager";
import EditPermission from "./EditPermission";
import CloseStore from "./closeStore";
import Rules from "./Rules";
import HistoryInStore from "./HistoryInStore";
import RemoveManager from "./RemoveManager";
import PolicyStore from "./DiscountPolicy";
import BuyingPolicy from "./BuyingPolicy";
import DiscountPolicy from "./DiscountPolicy";
import "./Store.css";

const Store = (props) => {
  let storeID = props.storeID;
  let UUID = props.uuid;

  const addProductHandler = () => {
    setCommand(
      <NewProductInStore
        uuid={UUID}
        storeID={storeID}
        onStore={returnToStore}
      />
    );
  };

  const returnToStore = () => {
    setCommand(
      <>
        <h2>Founder: {founder}</h2>
        <h2>Rate: {rate}</h2>
        <div>
          <ProductInStoreList uuid={UUID} storeID={storeID} />
        </div>
      </>
    );
  };

  const removeProductHandler = () => {
    setCommand(
      <RemoveProduct uuid={UUID} storeID={storeID} onStore={returnToStore} />
    );
  };

  const editProductHandler = () => {
    setCommand(
      <EditProduct uuid={UUID} storeID={storeID} onStore={returnToStore} />
    );
  };
  const addManagerHandler = () => {
    setCommand(
      <AddManager uuid={UUID} storeID={storeID} onStore={returnToStore} />
    );
  };

  const removeManagerHandler = () => {
    setCommand(
      <RemoveManager uuid={UUID} storeID={storeID} onStore={returnToStore} />
    );
  };

  const editPermissionHandler = () => {
    setCommand(
      <EditPermission uuid={UUID} storeID={storeID} onStore={returnToStore} />
    );
  };

  //todo: check that hasParmission!!!!!
  const closeStoreHandler = () => {
    setCommand(
      <CloseStore
        uuid={UUID}
        storeID={storeID}
        onMarket={() => props.onMarket()}
      />
    );
  };

  const historyHandler = () => {
    setCommand(<HistoryInStore uuid={UUID} storeID={storeID} />);
  };

  const policyHandler = () => {
    setCommand(<DiscountPolicy uuid={UUID} storeID={storeID} />);
  };

  const buyingHandler = () => {
    setCommand(<BuyingPolicy uuid={UUID} storeID={storeID} />);
  };

  // const [permission, setPermission] = useState("");
  let permission = "";
  if (UUID == 7) {
    permission = (
      <>
        <button onClick={addProductHandler}> Add Product</button>
        <button onClick={removeProductHandler}> Remove Product</button>
        <button onClick={editProductHandler}> Edit Product</button>
        <button onClick={addManagerHandler}> Add Manager</button>
        <button onClick={removeManagerHandler}> Remove Manager</button>
        <button onClick={editPermissionHandler}> Edit Permission</button>
        <h2></h2>
        <button onClick={closeStoreHandler}> Close Store</button>
        <button onClick={historyHandler}> History </button>
        <button onClick={policyHandler}> Policy </button>
        <button onClick={buyingHandler}> Buying Strategy </button>
      </>
    );
  }

  //todo: get info
  let name = "Amazing Store";
  let founder = "Alvis Presly";
  let isOpen = "Open";
  let rate = "10";

  const [command, setCommand] = useState(
    <>
      <h2>Founder: {founder}</h2>
      <h2>Rate: {rate}</h2>
      <div>
        <ProductInStoreList uuid={UUID} storeID={storeID} />
      </div>
    </>
  );

  const rulesHandler = () => {
    setCommand(<Rules uuid={UUID} storeID={storeID} />);
  };

  return (
    <div className="store">
      <h3>{name}</h3>
      <h3>{isOpen}</h3>
      <button onClick={rulesHandler}>Rules</button>
      {permission}
      <div>{command}</div>
    </div>
  );
};

export default Store;
