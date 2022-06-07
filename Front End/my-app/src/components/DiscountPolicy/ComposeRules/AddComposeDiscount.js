import React, { useState } from "react";
import AddRule from "./AddRule";
import AndRule from "./AndRule";
import OrRule from "./OrRule";
import XorRule from "./XorRule";

const AddComposeDiscount = (props) => {
  console.log("AddComposeDiscount")

  let UUID = props.uuid;
  let storeID = props.storeID;
  const andHandler = () => {
    setCommand(
      <AndRule uuid={UUID} storeID={storeID} onRule={() => props.onRule()} />
    );
  };
  const addHandler = () => {
    setCommand(
      <AddRule uuid={UUID} storeID={storeID} onRule={() => props.onRule()} />
    );
  };
  const orHandler = () => {
    setCommand(
      <OrRule uuid={UUID} storeID={storeID} onRule={() => props.onRule()} />
    );
  };
  const xorHandler = () => {
    setCommand(
      <XorRule uuid={UUID} storeID={storeID} onRule={() => props.onRule()} />
    );
  };
  let besicPage = (
    <>
      <h3>Compose Rules</h3>
      <div>
        <button onClick={andHandler}>AND</button>
        <button onClick={orHandler}>OR</button>
        <button onClick={orHandler}>XOR</button>
        <button onClick={addHandler}>ADD</button>
      </div>
    </>
  );
  const [command, setCommand] = useState(besicPage);
  return (
    <div>
      <div>{command}</div>
    </div>
  );
};

export default AddComposeDiscount;
