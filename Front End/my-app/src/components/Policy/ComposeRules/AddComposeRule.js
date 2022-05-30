import React, { useState } from "react";

const ComposeRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  const [command, setCommand] = useState("");
  return (
    <div>
      <h3>Compose Rules</h3>
      <div>
        <button onClick={() => {}}>AND</button>
        <button onClick={() => {}}>OR</button>
        <button onClick={() => {}}>Condition</button>
      </div>
      <div>{command}</div>
    </div>
  );
};

export default ComposeRule;
