import React, { useState } from "react";
import CombineRuleList from "./CombineRuleList";

const XorRule = (props) => {
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

  //todo: XOR rule
  const confirmHandler = () => {
    //do..... with the list
    props.onBigOrFirst(list);
  };

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>XOR Rule</h3>
      <h2>Sign the rules to Combile with XOR, when you finish press</h2>
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

export default XorRule;
