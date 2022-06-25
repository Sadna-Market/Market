import React, {useEffect, useState} from "react";
import Card from "../UI/Card";
import "./RuleInfo.css";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"
const RuleInfo = (props) => {
  console.log("RuleInfo")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  const [kindRule, SetkindRule] = useState("");

  async function getRuleInfo() {
    let allRules = [];
    console.log("start func  get Discount RuleInfo", props.uuid, props.storeID, props.ruleID)

    const getRuleInfoResponse = await apiClientHttp.getDiscountRuleByID(props.uuid, props.storeID, props.ruleID);
    console.log("start22 func  get buy RuleInfo")

    let str = JSON.stringify(getRuleInfoResponse);
    console.log("getDiscountRuleByID    " + str)
    console.log("getDiscountRuleByID  not str 111 " + getRuleInfoResponse.value.discountRule.value)
    // console.log("getDiscountRuleByID  not str 22 " + getRuleInfoResponse.value.pred.predicateBuyRule)


//predicateDiscountRule
    if (getRuleInfoResponse.errorMsg !== -1) {
      SetError(errorCode.get(getRuleInfoResponse.errorMsg))
    } else {
      if (getRuleInfoResponse.value.discountRule.value !== null) {
        SetkindRule(getRuleInfoResponse.value.discountRule.value)
      }

      SetError("")

    }
  }

  useEffect(() => {
    getRuleInfo();
  }, [kindRule.refresh]);



  //todo:get the info of the Rule
  // let info = {
  //   kind: "Add",
  //   amountOfPridecate: 4,
  // };
  return (
    <Card>
      <div className="productType">
        {/*<div>*/}
        {/*  <h3>Rule Number: {ruleID}</h3>*/}
        {/*</div>*/}
        <div>
          <h2>{kindRule}</h2>
        </div>
        {/*<div>*/}
        {/*  <h3>Amount: {info.amountOfPridecate}</h3>*/}
        {/*</div>*/}
      </div>
    </Card>
  );
};

export default RuleInfo;
