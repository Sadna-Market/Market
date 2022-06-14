import React, { useState } from "react";
import AddSimpleRule from "../AddSimpleRule";
import ComposeRuleList from "../ComposeRules/ComposeRuleList";
import MoreRule from "./MoreRule";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"
import createApiClientHttp from "../../../client/clientHttp.js";

const AddRule = (props) => {
  console.log("buying policy " + "AddRule ")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;
  let list = [];//check if it not apeend twhene its doesnt need to 

  const newRule = (num) => {

    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} compose={true}/>
    );
  };

  //todo: create Add with list
  async function  finishHandler(){
    let andMap ={"and":list}
      const sendRulesResponse = await apiClientHttp.sendRules(andMap);

      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        // props.onRule(sendRulesResponse.value);
        props.onRule();
      }

  }

  const moreHandler = (ruleID) => {
    list.push(ruleID);
    console.log(list);
    setComaand(<MoreRule onMore={newRule} onFinish={finishHandler} compose={true}/>);

  };

  const [command, setComaand] = useState(
    <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} compose={true}/>
  );

  //todo: AddRule
  const confirmHandler = () => {
    //do..... with the list
    // props.onRule();
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={newRule} compose={true} />
    );
  };

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>Add Rule</h3>
      <div>{command}</div>
    </div>
  );
};

export default AddRule;
