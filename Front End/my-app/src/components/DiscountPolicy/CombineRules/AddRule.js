import React, { useState } from "react";
import CombineRuleList from "./CombineRuleList";

const AddRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  let list = [];

  const changeList = (toAdd, ruleID) => {
    if (toAdd) {
      //console.log("add rule #" + ruleID);
      list.push(ruleID);
    } else {
      //console.log("remove rule #" + ruleID);
      list = list.filter((rule) => rule != ruleID);
    }
  };

  //todo: AddRule
  const confirmHandler = () => {
    //do..... with the list
    props.onRule();
  };

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>ADD Rule</h3>
      <h2>Sign the rules to Combile with ADD, when you finish press</h2>
      <button onClick={confirmHandler}>Confirm</button>
      <div>
        <CombineRuleList
          uuid={UUID}
          storeID={storeID}
          onChangeList={changeList}
          checkbox={true}
        />
      </div>
    </div>
  );
};

export default AddRule;
