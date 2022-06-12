import React, { useState } from "react";
import "./CombineRuleID.css";
import Card from "../../UI/Card";
import RuleInfo from "../RuleInfo";

const CombineRuleID = (props) => {
  console.log("CombineRuleID")

  let UUID = props.uuid;
  let storeID = props.storeID;
  let ruleID = props.id;
  const [checkbox, setCheckbox] = useState(false);
  const [info, setInfo] = useState("");
  const clickHandler = () => {
    if (info == "") {
      setInfo(<RuleInfo uuid={UUID} storeID={storeID} ruleID={ruleID} />);
    } else {
      setInfo("");
    }
    // props.onInfo(ruleID);
  };
  const changeListHandler = (toAdd) => {
    props.onChangeList(!toAdd, ruleID);
  };
  if (props.checkbox) {
    return (
      <li>
        <Card className="compose-item">
          <input
            type="checkbox"
            checked={checkbox}
            onChange={() => {
              changeListHandler(checkbox);
              setCheckbox(!checkbox);
            }}
          />
          <div className="compose-item__price">#{props.id}</div>
          <div className="compose-item__description">
            <h2></h2>
            <h2>{props.kind}</h2>
            <button onClick={clickHandler}>info</button>
          </div>
        </Card>
        <div>
          <div>{info}</div>
        </div>
      </li>
    );
  } else {
    return (
      <li>
        <Card className="compose-item">
          <div className="compose-item__price">#{props.id}</div>
          <div className="compose-item__description">
            <h2></h2>
            <h2>{props.kind}</h2>
            <button onClick={clickHandler}>info</button>
          </div>
        </Card>
        <div>
          <div>{info}</div>
        </div>
      </li>
    );
  }
};

export default CombineRuleID;
