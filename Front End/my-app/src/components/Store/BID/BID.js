import React, { useState } from "react";
import AddProduct from "./CreateBID";
import AllOffers from "./AllOffers";
import MainBID from "./MainBID";
import CeateBID from "./CreateBID";
import MyBIDs from "./MyBIDs";
import BuyBID from "./BuyBID";

const BID = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  let permission = <></>;
  const confirmHandler = (productID, amount) => {
    setComand(
      <BuyBID
        uuid={UUID}
        storeID={storeID}
        productID={productID}
        amount={amount}
        onBID={BIDHandler}
      />
    );
  };
  const [command, setComand] = useState(
    <MyBIDs uuid={UUID} storeID={storeID} onConfirm={confirmHandler} />
  );

  const BIDHandler = () => {
    setComand(
      <MyBIDs uuid={UUID} storeID={storeID} onConfirm={confirmHandler} />
    );
  };

  const MyBIDHandler = () => {
    setComand(
      <MyBIDs uuid={UUID} storeID={storeID} onConfirm={confirmHandler} />
    );
  };

  const createBIDHandler = () => {
    setComand(<CeateBID uuid={UUID} storeID={storeID} onBID={BIDHandler} />);
  };

  const allOffers = () => {
    setComand(<AllOffers uuid={UUID} storeID={storeID} />);
  };

  //check permission
  if (UUID == 7) {
    permission = (
      <>
        {/* <button onClick={BIDHandler}> All BID</button> */}
        <button onClick={allOffers}> All Offers</button>
      </>
    );
  }

  //todo: return what return from this function
  return (
    <div>
      <h3>BID Store #{storeID}</h3>
      <div>
        <button onClick={createBIDHandler}> Create BID</button>
        <button onClick={MyBIDHandler}> My BIDs</button>
        {permission}
      </div>
      <div>{command}</div>
    </div>
  );
};
export default BID;
