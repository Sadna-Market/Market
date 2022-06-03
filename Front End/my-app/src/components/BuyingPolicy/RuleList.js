import React, { useState, useEffect } from "react";
import RuleID from "./RuleID";

const RuleList = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  let rules = [
    { id: 1, kind: "Add" },
    { id: 2, kind: "Simple" },
    { id: 3, kind: "condition" },
    { id: 4, kind: "Simple" },
    { id: 5, kind: "Or" },
    { id: 6, kind: "Or" },
    { id: 7, kind: "Simple" },
  ];
  if (rules.length === 0) {
    return <h2 className="stores-list__fallback">Found No Rule</h2>;
  }

  if (props.search != "") {
    rules = rules.filter((rule) => rule.id === parseInt(props.search));
  }

  //todo: update the list
  const removeRuleHandler = () => {};

  const infoHandler = (ruleID) => {
    props.onInfo(ruleID);
  };

  let expensesContent = rules.map((expense) => (
    <RuleID
      ruleID={expense.id}
      uuid={UUID}
      storeID={storeID}
      kind={expense.kind}
      onRemove={removeRuleHandler}
      onInfo={infoHandler}
    />
  ));

  //const [searchStore, setStore] = useState("");

  return (
    <div>
      <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default RuleList;
