import React, {useEffect, useState} from "react";
import AddProduct from "./CreateBID";
import AllOffers from "./AllOffers";
import MainBID from "./MainBID";
import CeateBID from "./CreateBID";
import MyBIDs from "./MyBIDs";
import BuyBID from "./BuyBID";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
const BID = (props) => {
  console.log("BID")
  const [isOwner, SetisOwner] = useState(false);
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;
  let permission = <></>;

  async function checkIsOwner() {
    const isOwnerUUIDResponse = await apiClientHttp.isOwnerUUID(UUID, storeID);

    if (isOwnerUUIDResponse.errorMsg !== -1) {
      // SetError(errorCode.get(isOwnerUUIDResponse.errorMsg))
    } else {
      SetisOwner(isOwnerUUIDResponse.value)
    }
  }
  useEffect(() => {
    checkIsOwner();
  }, [isOwner.refresh]);
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
  if (isOwner) {
    permission = (
      <>
        {/* <button onClick={BIDHandler}> All BID</button> */}
        <button onClick={allOffers}> All Offers</button>
      </>
    );
  }
  else {
    permission = (
        <>
          {/* <button onClick={BIDHandler}> All BID</button> */}
          <button onClick={MyBIDHandler}> My BIDs</button>
        </>
    );
  }

  //todo: return what return from this function
  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <h3>BID Store #{storeID}</h3>
      <div>
        <button onClick={createBIDHandler}> Create BID</button>
        {permission}
      </div>
      <div>{command}</div>
    </div>
  );
};
export default BID;
