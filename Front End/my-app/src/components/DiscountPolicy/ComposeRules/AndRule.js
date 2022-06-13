import React, { useState } from "react";
import AddSimpleRule from "../SimpleRules/AddSimpleRule";
import CategoryDiscount from "./CategoryDiscount";
import MoreRule from "./MoreRule";

const AndRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  let list = [];

  const newRule = (num) => {
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} />
    );
  };

  //todo: create AND with list
  const finishHandler = () => {
    //work with list
    setComaand(
      <CategoryDiscount
        uuid={UUID}
        storeID={storeID}
        rules={list}
        kind="AND"
        onRule={() => props.onRule()}
      />
    );
  };

  const moreHandler = (ruleID) => {
    list.push(ruleID);
    console.log(list);
    setComaand(
      <MoreRule onMore={newRule} onFinish={finishHandler} continue={true} />
    );
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
      <h3>AND Rule</h3>
      <div>{command}</div>
    </div>
  );
};

export default AndRule;
