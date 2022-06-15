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
    setEmail("");
  };

  //todo: remove to be manager
  const ManHandler = () => {
    cleanHandler();
    props.onStore();
  };

  //todo: remove to be owner
  const OwnHandler = () => {
    cleanHandler();
    props.onStore();
  };

  return (
    <div>
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
