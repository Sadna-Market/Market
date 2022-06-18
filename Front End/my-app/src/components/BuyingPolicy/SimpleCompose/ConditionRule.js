import React, { useState } from "react";
import AddSimpleRule from "../AddSimpleRule";
import ComposeRuleList from "../ComposeRules/ComposeRuleList";
import RuleID from "../RuleID";
import RuleInfo from "../RuleInfo";
import MoreRule from "./MoreRule";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"
import createApiClientHttp from "../../../client/clientHttp.js";

const ConditionRule = (props) => {
  console.log("buying policy " + "ConditionRule ")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
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
  async function finishHandler() {
    let list=[];
    list.push(ifRule)
    list.push(ThenRule)
    let andMap ={"condition":list}
    const sendRulesResponse = await apiClientHttp.addNewBuyRule(UUID,storeID,andMap);

    if (sendRulesResponse.errorMsg !== -1) {
      SetError(errorCode.get(sendRulesResponse.errorMsg))
    } else {
      SetError("")
      // props.onRule(sendRulesResponse.value);
      props.onRule();
    }
  }

  const showHandler1 = (ruleID) => {
    SetError("")
    setCommand1(<RuleInfo ruleID={ruleID} />);
    setIfRule(ruleID);
  };

  const showHandler2 = (ruleID) => {
    SetError("")
    setCommand2(<RuleInfo ruleID={ruleID} />);
    setThenRule(ruleID);
  };

  //todo conditionRule
  const confirmHandler = () => {

    SetError("")
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
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
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
