import React, { useState } from "react";
import AddRule from "./AddRule";
import ConditionRule from "./ConditionRule";
import OrRule from "./OrRule";

const SimpleComposeRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
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
  const conditionHandler = () => {
    setCommand(
      <ConditionRule
        uuid={UUID}
        storeID={storeID}
        onRule={() => props.onRule()}
      />
    );
  };
  let besicPage = (
    <>
      <h3>Compose Rules</h3>
      <div>
        <button onClick={addHandler}>AND</button>
        <button onClick={orHandler}>OR</button>
        <button onClick={conditionHandler}>Condition</button>
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

export default SimpleComposeRule;
