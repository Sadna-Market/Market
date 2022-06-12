import React, { useState } from "react";
import Card from "../UI/Card";
import "./RuleInfo.css";

const RuleInfo = (props) => {
  console.log("buying policy " + "RuleInfo ")

  let UUID = props.uuid;
  let storeID = props.storeID;
  let ruleID = props.ruleID;

  //todo:get the info of the Rule
  let info = {
    kind: "Add",
    amountOfPridecate: 4,
  };
  return (
    <Card>
      <div className="productType">
        <div>
          <h3>Rule Number: {ruleID}</h3>
        </div>
        <div>
          <h3>Kind: {info.kind}</h3>
        </div>
        <div>
          <h3>Amount: {info.amountOfPridecate}</h3>
        </div>
      </div>
    </Card>
  );
};

export default RuleInfo;
