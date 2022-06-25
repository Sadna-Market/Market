import React, {useEffect, useState} from "react";
import Card from "../UI/Card";
import "./RuleInfo.css";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"
const RuleInfo = (props) => {
  console.log("buying policy " + "RuleInfo ")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  
  // const [kindRule, SetkindRule] = useState("");
  //
  // async function getRuleInfo() {
  //   let allRules = [];
  //
  //   const getRuleInfoResponse = await apiClientHttp.getRuleInfo(UUID,storeID);
  //   console.log("start func  getAllStores")
  //
  //   let str = JSON.stringify(getRuleInfoResponse);
  //   console.log("getBuyPolicyResponse    "+str)
  //
  //   if (getRuleInfoResponse.errorMsg !== -1) {
  //     SetError(errorCode.get(getRuleInfoResponse.errorMsg))
  //   } else {
  //     for (let i = 0; i < getRuleInfoResponse.value.length; i++) {
  //       allRules.push({
  //         id: getBuyPolicyResponse.value[i].id,
  //         kind: getBuyPolicyResponse.value[i].kind,
  //       })
  //
  //     }
  //     SetError("")
  //   }
  // }
  //
  // useEffect(() => {
  //   getRuleInfo();
  // }, [kindRule.refresh]);
  //
  let UUID = props.uuid;
  let storeID = props.storeID;
  let ruleID = props.ruleID;
  let kind=props.kind;
  //todo:get the info of the Rule
  // let info = {
  //   kind: "Add",
  //   amountOfPridecate: 4,
  // };
  return (
    <Card>
      <div className="productType">
        <div>
          <h3>Rule Number: {ruleID}</h3>
        </div>
        <div>
          <h3>Kind: {kind}</h3>
        </div>
        {/*<div>*/}
        {/*  <h3>Amount: {info.amountOfPridecate}</h3>*/}
        {/*</div>*/}
      </div>
    </Card>
  );
};

export default RuleInfo;
