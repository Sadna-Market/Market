import React, { useState } from "react";
import Card from "../UI/Card";
const RuleID = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  let ruleID = props.ruleID;
  let kind = props.kind;

  //todo: remove Rule
  const removeHandler = () => {
    props.onRemove();
  };

  const infoHandler = () => {
    props.onInfo(ruleID);
  };

  return (
    <li>
      <Card className="store-item">
        <div className="store-item__price">#{ruleID}</div>
        <button onClick={infoHandler}>Info</button>
        <div className="store-item__description">
          <h2></h2>
          <h2>{kind}</h2>
          <button onClick={removeHandler}>remove Rule</button>
        </div>
      </Card>
    </li>
  );
};

export default RuleID;
