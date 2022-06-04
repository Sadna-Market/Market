import React, { useState } from "react";
import AddSimpleRule from "../BuyingPolicy/AddSimpleRule";
import AllRules from "../BuyingPolicy/buyingPolicy";
import AddComposeRule from "../BuyingPolicy/ComposeRules/AddComposeRule";
import SimpleComposeRule from "../BuyingPolicy/SimpleCompose/AddSimpleCompose";

const BuyingPolicy = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [command, setComaand] = useState(
    <AllRules uuid={UUID} storeID={storeID} />
  );

  const simpleRuleHandler = () => {
    setComaand(
      <AddSimpleRule uuid={UUID} storeID={storeID} onRule={allRulesHandler} simplePage={true}/>
    );
  };

  const composeRuleHandler = () => {
    setComaand(
      <AddComposeRule uuid={UUID} storeID={storeID} onRule={allRulesHandler} />
    );
  };

  const simpleComposeRuleHandler = () => {
    setComaand(
      <SimpleComposeRule uuid={UUID} storeID={storeID} onRule={allRulesHandler} />
    );
  };

  const allRulesHandler = () => {
    setComaand(<AllRules uuid={UUID} storeID={storeID} />);
  };

  return (
    <div>
      <h3>Buying</h3>
      <div>
        <button onClick={allRulesHandler}>All Rules</button>
        <button onClick={simpleRuleHandler}>Add Simple Rule</button>
        <button onClick={simpleComposeRuleHandler}>Add Compose Rule</button>
        <button onClick={composeRuleHandler}>Combine Rules</button>
      </div>
      <div>{command}</div>
    </div>
  );
};

export default BuyingPolicy;
