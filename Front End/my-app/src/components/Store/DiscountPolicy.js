import React, { useState } from "react";
import AddSimpleRule from "../DiscountPolicy/SimpleRules/AddSimpleRule";
import AllRules from "../DiscountPolicy/AllRules";
import SimpleComposeRule from "../BuyingPolicy/SimpleCompose/AddSimpleCompose";
import AndCombineRule from "../DiscountPolicy/CombineRules/AddCombineRule";
import AddComposeDiscount from "../DiscountPolicy/ComposeRules/AddComposeDiscount";

const DiscountPolicy = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [command, setComaand] = useState(
    <AllRules uuid={UUID} storeID={storeID} />
  );

  const simpleRuleHandler = () => {
    setComaand(
      <AddSimpleRule
        uuid={UUID}
        storeID={storeID}
        onRule={allRulesHandler}
        simplePage={true}
      />
    );
  };

  const composeRuleHandler = () => {
    setComaand(
      <AndCombineRule uuid={UUID} storeID={storeID} onRule={allRulesHandler} />
    );
  };

  const simpleComposeRuleHandler = () => {
    setComaand(
      <AddComposeDiscount
        uuid={UUID}
        storeID={storeID}
        onRule={allRulesHandler}
      />
    );
  };

  const allRulesHandler = () => {
    setComaand(<AllRules uuid={UUID} storeID={storeID} />);
  };

  return (
    <div>
      <h3>Discount</h3>
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

export default DiscountPolicy;
