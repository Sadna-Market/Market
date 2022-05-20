import React, { useState } from "react";

const AddManager = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [email, setEmail] = useState("");
  const changeEmailHandler = (event) => {
    setEmail(event.target.value);
  };

  const cleanHandler = () => {
    setEmail("");
  };

  //add to be manager
  const ManHandler = () => {
    cleanHandler();
    props.onStore();
  };

  //add to be owner
  const OwnHandler = () => {
    cleanHandler();
    props.onStore();
  };

  return (
    <div>
      <h3>Add Owner \ Manager</h3>
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
