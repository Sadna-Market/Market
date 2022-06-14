import React, { useState } from "react";
import AddSimpleRule from "../AddSimpleRule";
import ComposeRuleList from "../ComposeRules/ComposeRuleList";
import MoreRule from "./MoreRule";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"
import createApiClientHttp from "../../../client/clientHttp.js";

const OrRule = (props) => {
  console.log("buying policy " + "OrRule ")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;
  let list = [];

  const newRule = (num) => {
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} compose={true} />
    );
  };

  //todo: create Add with list
  async function finishHandler (){
    let andMap ={"or":list}
    const sendRulesResponse = await apiClientHttp.addNewBuyRule(UUID,storeID,andMap);

    if (sendRulesResponse.errorMsg !== -1) {
      SetError(errorCode.get(sendRulesResponse.errorMsg))
    } else {
      SetError("")
      // props.onRule(sendRulesResponse.value);
      props.onRule();
    }
  }

  const moreHandler = (ruleID) => {
    SetError("")
    list.push(ruleID);
    console.log(list);
    setComaand(<MoreRule onMore={newRule} onFinish={finishHandler} />);
  };

  const [command, setComaand] = useState(
    <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler} compose={true}/>
  );

  //todo: AddRule
  const confirmHandler = () => {
    SetError("")
    //do..... with the list
    // props.onRule();
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={newRule} compose={true}/>
    );
  };

  //const [command, setCommand] = useState();
  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <h3>Or Rule</h3>
      <div>{command}</div>
    </div>
  );
};

export default OrRule;
