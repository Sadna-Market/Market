import React, { useState } from "react";
import OrRule from "./OrRule";
import AndRule from "./AndRule";
import AddRule from "./AddRule";
import XorRule from "./XorRule";
import CategoryDiscount from "./CategoryDiscout";
import BigOrFirst from "./BigOrFirst";

const AndCombineRule = (props) => {
  console.log("AndCombineRule")

  let UUID = props.uuid;
  let storeID = props.storeID;

  const categoryHandler = (rules, kind) => {
    setCommand(
      <CategoryDiscount
        uuid={UUID}
        storeID={storeID}
        onRule={() => props.onRule()}
        rules={rules}
        kind={kind}
      />
    );
  };

  const BigHandler = (rules) => {
    setCommand(
      <BigOrFirst
        uuid={UUID}
        storeID={storeID}
        onRule={() => props.onRule()}
        rules={rules}
      />
    );
  };
  const andHandler = () => {
    setCommand(
      <AndRule uuid={UUID} storeID={storeID} onCategory={categoryHandler} />
    );
  };
  const addHandler = () => {
    setCommand(
      <AddRule uuid={UUID} storeID={storeID} onRule={() => props.onRule()} />
    );
  };
  const orHandler = () => {
    setCommand(
      <OrRule uuid={UUID} storeID={storeID} onCategory={categoryHandler} />
    );
  };
  const xorHandler = () => {
    setCommand(
      <XorRule uuid={UUID} storeID={storeID} onBigOrFirst={BigHandler} />
    );
  };
  let besicPage = (
    <>
      <h3>Combine Rules</h3>
      <div>
        <button onClick={andHandler}>AND</button>
        <button onClick={orHandler}>OR</button>
        <button onClick={xorHandler}>XOR</button>
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

export default AndCombineRule;
