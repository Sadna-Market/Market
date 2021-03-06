import React, { useState } from "react";
import AddSimpleRule from "../SimpleRules/AddSimpleRule";
import MoreRule from "./MoreRule";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"

const AddRule = (props) => {
  console.log("AddRule")
  const [enteredError, SetError] = useState("");
  const apiClientHttp = createApiClientHttp();
  let UUID = props.uuid;
  let storeID = props.storeID;
  let list = [];

  const newRule = (num) => {
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler}  compose={true}/>
    );
  };

  //todo: create Add with list
  async function finishHandler(){
    let AddMap ={"add":{'list': list}}

    let str = JSON.stringify(AddMap);
    console.log("AddMap    "+str)
    const sendRulesResponse = await apiClientHttp.addNewDiscountRule(UUID,storeID,AddMap);
    let str2 = JSON.stringify(sendRulesResponse);
    console.log("sendRulesResponse    "+str2)

    if (sendRulesResponse.errorMsg !== -1) {
      SetError(errorCode.get(sendRulesResponse.errorMsg))
    } else {
      props.onRule();
    }
  }

  const moreHandler = (ruleID) => {
    list.push(ruleID);
    console.log(list);
    setComaand(<MoreRule onMore={newRule} onFinish={finishHandler} />);
  };

  const [command, setComaand] = useState(
    <AddSimpleRule uuid={UUID} storeID={storeID} onRule={moreHandler}  compose={true}/>
  );

  //todo: AddRule
  const confirmHandler = () => {
    //do..... with the list
    // props.onRule();
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={newRule}  compose={true}/>
    );
  };

  //const [command, setCommand] = useState();
  return (
    <div>
      <h3>ADD Rule</h3>
      <div>{command}</div>
    </div>
  );
};

export default AddRule;
