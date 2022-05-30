import React, { useState } from "react";

const UserRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [email, setEmail] = useState("");
  const changeEmailHandler = (event) => {
    setEmail(event.target.value);
  };

  const cleanHandler = () => {
    setEmail("");
  };

  //todo: add new user Rule to store
  const addHandler = () => {
    cleanHandler();
    props.onRule();
  };

  return (
    <div>
      <h3>Add User Rule For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>User Email</label>
          <input
            type="text"
            value={email}
            placeholder="Write User Email"
            onChange={changeEmailHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default UserRule;
