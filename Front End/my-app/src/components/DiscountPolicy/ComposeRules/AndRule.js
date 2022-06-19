import React, { useState } from "react";
import AddSimpleRule from "../SimpleRules/AddSimpleRule";
import CategoryDiscount from "./CategoryDiscount";
import MoreRule from "./MoreRule";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/DiscountRules"

const AndRule = (props) => {
  console.log("AndRule")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;
  let list = [];

  const newRule = (num) => {
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} compose={true}/>
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
    <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} compose={true}/>
  );

  //todo: AddRule
  const confirmHandler = () => {
    //do..... with the list
    // props.onRule();
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={newRule} compose={true}/>
    );
  };

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>AND Rule1</h3>
      <div>{command}</div>
    </div>
  );
};

export default AndRule;
