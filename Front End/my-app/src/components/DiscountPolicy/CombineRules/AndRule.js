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
    async function confirmHandler(){
      let combineAndMap ={"combineAnd":list}
        const sendRulesResponse = await apiClientHttp.sendDRules(combineAndMap);

        if (sendRulesResponse.errorMsg !== -1) {
            SetError(errorCode.get(sendRulesResponse.errorMsg))
        } else {
            // props.onRule(sendRulesResponse.value);
            props.onRule();
        }
  }

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>AND Rule</h3>
      <h2>Sign the rules to Combile with AND, when you finish press</h2>
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

export default AndRule;
