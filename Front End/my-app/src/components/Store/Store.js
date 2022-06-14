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
import {createApiClientHttp} from "../../client/clientHttp";
import {errorCode} from "../../ErrorCodeGui"
import BID from "./BID/BID";


const Store = (props) => {
    let apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    console.log("Store" )

    let storeID = props.storeID;
  let UUID = props.uuid;
    console.log("UUID " +UUID)

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

  //todo: check that hasParmission!!!! to like in the storlist eith refresh ..
    // check if uuid is pwner
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
  //check ig uuid is manager in this store
  if (UUID != 7) {
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
        <button onClick={policyHandler}> Discount Policy </button>
        <button onClick={buyingHandler}> Buy Policy </button>
      </>
    );
  }

  //todo: get info
    // call to func that take store id and return all this
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

    async function rulesHandler(){
        console.log(":UUID " + UUID + "storeID " +storeID)

        const getStoreRolesResponse = await apiClientHttp.getStoreRoles(UUID,storeID);
        if (getStoreRolesResponse.errorMsg!== -1) {
            SetError(errorCode.get(getStoreRolesResponse.errorMsg))
        } else {
            // console.log(":getStoreRolesResponse. "+ getStoreRolesResponse[0].type)
            
            console.log(":getStoreRolesResponse.value22 " + getStoreRolesResponse)
            let str = JSON.stringify(getStoreRolesResponse);
            //:getStoreRolesResponse.value {"errorMsg":-1,"value":{"owner":["sysManager@gmail.com"],"manager":[],"founder":["1dfssdf"]}}
            console.log(":getStoreRolesResponse.value "+ str)
            // getStoreRolesResponse.value
            setCommand(<Rules uuid={UUID} storeID={storeID} rules={getStoreRolesResponse.value}/>);

        }

  }

  const BIDHandler = () => {
    setCommand(<BID uuid={UUID} storeID={storeID} />);
  };

  return (
    <div className="store">
      <h3>{name}</h3>
      <h3>{isOpen}</h3>
      <button onClick={rulesHandler}>Rules</button>
      <button onClick={BIDHandler}>BID</button>
      {permission}
      <div>{command}</div>
    </div>
  );
};

export default Store;
