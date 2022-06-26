import React, { useState } from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const AddManager = (props) => {
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  let UUID = props.uuid;
  let storeID = props.storeID;

  const [email, setEmail] = useState("");
  const changeEmailHandler = (event) => {
    setEmail(event.target.value);
  };

  const cleanHandler = () => {
    setEmail("");
  };

  async function ManHandler(){
    console.log("addNewStoreMange UUID r "+UUID)

    const addNewStoreMangerResponse = await apiClientHttp.addNewStoreManger(UUID, storeID, email);
    if (addNewStoreMangerResponse.errorMsg !== -1) {
      SetError(errorCode.get(addNewStoreMangerResponse.errorMsg))
    } else {
      cleanHandler();
      props.onStore();
    }
    console.log("addNewStoreMangerResponse")

    console.log(addNewStoreMangerResponse)

  }

  //add to be owner
  async function OwnHandler(){
    const addNewStoreOwnerResponse = await apiClientHttp.addNewStoreOwner(UUID, storeID, email);
    if (addNewStoreOwnerResponse.errorMsg !== -1) {
      SetError(errorCode.get(addNewStoreOwnerResponse.errorMsg))
    } else {
      cleanHandler();
      props.onStore();
    }
    console.log("addNewStoreOwnerResponse")

    console.log(addNewStoreOwnerResponse)

  }

  return (
    <div>
      <h3>Add Owner \ Manager</h3>
      <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
      <div className="products__controls">
        <div className="products__control">
          <label>The Email To Add</label>
          <input
            type="text"
            value={email}
            placeholder="write the Email"
            onChange={changeEmailHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={ManHandler}>Add Manager</button>
        <button onClick={OwnHandler}>Add Owner</button>
      </div>
    </div>
  );
};

export default AddManager;
