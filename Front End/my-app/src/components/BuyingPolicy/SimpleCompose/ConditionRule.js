import React, { useState } from "react";
import AddSimpleRule from "../AddSimpleRule";
import ComposeRuleList from "../ComposeRules/ComposeRuleList";
import RuleID from "../RuleID";
import RuleInfo from "../RuleInfo";
import MoreRule from "./MoreRule";

const ConditionRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [ifRule, setIfRule] = useState("");
  const [ThenRule, setThenRule] = useState("");
  const [confirm, setconfirm] = useState("");

  const newRule = (num) => {
    // setComaand(
    //   <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} />
    // );
  };

  //todo: create Add with list
  const finishHandler = () => {
    //work with list
    props.onRule();
  };

  const showHandler1 = (ruleID) => {
    setCommand1(<RuleInfo ruleID={ruleID} />);
    setIfRule(ruleID);
  };

  const showHandler2 = (ruleID) => {
    setCommand2(<RuleInfo ruleID={ruleID} />);
    setThenRule(ruleID);
  };

  //todo conditionRule
  const confirmHandler = () => {
    //return onRule(<ruleID>)
    props.onRule(15);
  };

  const [command1, setCommand1] = useState(
    <AddSimpleRule uuid={UUID} storeID={storeID} onRule={showHandler1} />
  );

  const [command2, setCommand2] = useState(
    <AddSimpleRule uuid={UUID} storeID={storeID} onRule={showHandler2} />
  );

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>Condition Rule</h3>
      <div>
        <h2>when you finish, Press</h2>
        <button onClick={confirmHandler}>Confirm</button>
      </div>
      <h2>create Simple Rule for if:</h2>
      <div>{command1}</div>
      <h2>create Simple Rule for then:</h2>
      <div>{command2}</div>
    </div>
  );
};

export default ConditionRule;
