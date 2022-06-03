import React, { useState } from "react";
import RuleList from "../RuleList";
import ComposeRuleList from "./ComposeRuleList";
import "./ConditionRule.css";

const ConditionRule = (props) => {
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

  //todo: create rule
  const confirmHandler = () => {
    //do..... with the list
    props.onRule();
  };

  const [ifRule, SetIfRule] = useState("");
  const ifRuleChangeHandler = (event) => {
    SetIfRule(event.target.value);
  };
  const [thenRule, SetThenRule] = useState("");
  const thenRuleChangeHandler = (event) => {
    SetThenRule(event.target.value);
  };


  //const [command, setCommand] = useState();
  return (
    <div className="condition">
      <h3>Condition Rule</h3>
      <h2>if Rule #</h2>
      <input
              type="number"
              value={ifRule}
              onChange={ifRuleChangeHandler}
            />
      <h2>happened ,then Rule #</h2>
      <input
              type="number"
              value={thenRule}
              onChange={thenRuleChangeHandler}
            />
       <h2> happens, when you finish press</h2>
      <button onClick={confirmHandler}>Confirm</button>
      <div>
        <ComposeRuleList
          uuid={UUID}
          storeID={storeID}
          onChangeList={changeList}
          checkbox={false}
        />
      </div>
    </div>
  );
};

export default ConditionRule;
