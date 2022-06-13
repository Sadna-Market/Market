import React, { useState } from "react";
import AddProduct from "./CreateBID";
import AllOffers from "./AllOffers";
import MainBID from "./MainBID";
import CeateBID from "./CreateBID";
import MyBIDs from "./MyBIDs";

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

  const MyBIDHandler = () => {
    setComand(<MyBIDs uuid={UUID} storeID={storeID} />);
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
