import React, { useState } from "react";

const HistoryInStore = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [email, setemail] = useState("");
  const changeEmailHandler = (event) => {
    setemail(event.target.value);
  };

  const [command, setCommand] = useState("");

  const cleanHandler = () => {
    setemail("");
    setCommand("");
  };

  //todo: search the history
  const searchHandler = () => {
    setCommand("bla bla bla bla");
    setemail("");
  };

  return (
    <div>
      <h3>History of User in Store {storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Email</label>
          <input
            type="text"
            value={email}
            placeholder="Write Email"
            onChange={changeEmailHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={searchHandler}>Search</button>
      </div>
      <div><h2>{command}</h2></div>
    </div>
  );
};

export default HistoryInStore;
