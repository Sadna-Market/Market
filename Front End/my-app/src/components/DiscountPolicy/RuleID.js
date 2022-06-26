import React, { useState } from "react";
import Card from "../UI/Card";
import RuleInfo from "./RuleInfo";

const RuleID = (props) => {
  console.log("RuleID")

  let UUID = props.uuid;
  let storeID = props.storeID;
  let ruleID = props.ruleID;
  let kind = props.kind;

  const [info, setInfo] = useState("");

  //todo: remove Rule
  const removeHandler = () => {
    props.onRemove(ruleID);
  };

  const infoHandler = () => {
    if (info == "") {
      setInfo(<RuleInfo ruleID={ruleID} uuid={UUID} storeID={storeID}/>);
    } else {
      setInfo("");
    }
    //props.onInfo(ruleID);
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
      <div>{info}</div>
    </li>
  );
};

export default RuleID;
