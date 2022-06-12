import React, { useState } from "react";
import AddProduct from "./AddProduct";
import AllOffers from "./AllOffers";
import MainBID from "./MainBID";

const BID = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  let permission = <></>;
  const [command, setComand] = useState(
    <MainBID uuid={UUID} storeID={storeID} />
  );

  const BIDHandler = () => {
    setComand(<MainBID uuid={UUID} storeID={storeID} />);
  };

  const addProductHandler = () => {
    setComand(<AddProduct uuid={UUID} storeID={storeID} onBID={BIDHandler} />);
  };

  const allOffers = () => {
    setComand(<AllOffers uuid={UUID} storeID={storeID} />);
  };

  //check permission
  if (UUID == 7) {
    permission = (
      <>
        <button onClick={BIDHandler}> All BID</button>
        <button onClick={addProductHandler}> Add Product</button>
        <button onClick={allOffers}> All Offers</button>
      </>
    );
  }

  //todo: return what return from this function
  return (
    <div>
      <h3>BID Store #{storeID}</h3>
      <div>{permission}</div>
      <div>{command}</div>
    </div>
  );
};
export default BID;
