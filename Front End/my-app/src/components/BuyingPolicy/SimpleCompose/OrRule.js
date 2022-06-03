import React, { useState } from "react";
import AddSimpleRule from "../AddSimpleRule";
import ComposeRuleList from "../ComposeRules/ComposeRuleList";
import MoreRule from "./MoreRule";

const OrRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  let list = [];

  const newRule = (num) => {
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} />
    );
  };

  //todo: create Add with list
  const finishHandler = ()=>{
    //work with list
    props.onRule();
  };

  const moreHandler = (ruleID) => {
    list.push(ruleID);
    console.log(list);
    setComaand(<MoreRule onMore={newRule} onFinish={finishHandler} />);
  };

  const [command, setComaand] = useState(
    <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} />
  );

  //todo: AddRule
  const confirmHandler = () => {
    //do..... with the list
    // props.onRule();
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={newRule} />
    );
  };

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>Or Rule</h3>
      <div>{command}</div>
    </div>
  );
};

export default OrRule;
