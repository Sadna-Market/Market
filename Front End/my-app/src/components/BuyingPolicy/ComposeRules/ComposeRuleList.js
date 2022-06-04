import React, { useState, useEffect } from "react";
import ComposeRuleID from "./ComposeRuleID";

const ComposeRuleList = (props) => {
  let rules = [
    { id: 1, kind: "AND" },
    { id: 2, kind: "OR" },
    { id: 3, kind: "Simple" },
    { id: 4, kind: "Simple" },
    { id: 5, kind: "AND" },
    { id: 6, kind: "Simple" },
    { id: 7, kind: "Condition" },
  ];
  if (rules.length === 0) {
    return <h2 className="stores-list__fallback">This store without Rules</h2>;
  }

  const changeListHandler = (toAdd, ruleID) => {
    props.onChangeList(toAdd, ruleID);
  };

  const infoHandler = (ruleID) => {
    props.onEnterToStore(ruleID);
  };

  let expensesContent = rules.map((expense) => (
    <ComposeRuleID
      id={expense.id}
      kind={expense.kind}
      onInfo={infoHandler}
      onChangeList={changeListHandler}
      checkbox={props.checkbox}
    />
  ));

  //const [searchStore, setStore] = useState("");

  return (
    <div>
      <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default ComposeRuleList;
