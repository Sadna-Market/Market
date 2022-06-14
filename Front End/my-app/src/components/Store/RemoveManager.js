import React, { useState } from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const RemoveManager = (props) => {
  console.log("RemoveManager" )
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  let UUID = props.uuid;
  let storeID = props.storeID;

  const [email, setEmail] = useState("");
  const changeEmailHandler = (event) => {
    setEmail(event.target.value);
  };

  const cleanHandler = () => {
    SetError("")
    setEmail("");
  };

  //todo: remove to be manager
  async function ManHandler(){
    // const addNewStoreMangerResponse = await apiClientHttp.removeStoreOwner(UUID, storeID, email);
    // if (addNewStoreMangerResponse.errorMsg !== -1) {
    //   SetError(errorCode.get(addNewStoreMangerResponse.errorMsg))
    // } else {
      cleanHandler();
      props.onStore();
    // }
  }

  //todo: remove to be owner
  async function OwnHandler(){
    const removeStoreOwnerResponse = await apiClientHttp.removeStoreOwner(UUID, storeID, email);
    if (removeStoreOwnerResponse.errorMsg !== -1) {
      SetError(errorCode.get(removeStoreOwnerResponse.errorMsg))
    } else {
      cleanHandler();
      props.onStore();
    }
  }

  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

      <h3>Remove Owner \ Manager</h3>
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
        <button onClick={ManHandler}>Remove Manager</button>
        <button onClick={OwnHandler}>Remove Owner</button>
      </div>
    </div>
  );
};

export default RemoveManager;
