import React, { useState } from "react";
import CombineRuleList from "./CombineRuleList";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/DiscountRules"
const AndRule = (props) => {
    console.log("AndRule")
    const [enteredError, SetError] = useState("");
    const apiClientHttp = createApiClientHttp();
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

  //todo: AndRULE
  const confirmHandler = () => {

    //do..... with the list
    props.onCategory(list, "AND");
  };


  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>AND Rule1</h3>
      <h2>Sign the rules to Combile with AND, when you continue press</h2>
      <button onClick={confirmHandler}>continue</button>
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

export default AndRule;
