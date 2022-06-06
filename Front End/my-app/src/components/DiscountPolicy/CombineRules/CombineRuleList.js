import React, { useState, useEffect } from "react";
import CombineRuleID from "./CombineRuleID";

const CombineRuleList = (props) => {
  console.log("CombineRuleList")

  //get all ruls
  let rules = [
    { id: 1, kind: "ADD" },
    { id: 2, kind: "XOR" },
    { id: 3, kind: "Simple" },
    { id: 4, kind: "AND" },
    { id: 5, kind: "Add" },
    { id: 6, kind: "Simple" },
    { id: 7, kind: "Simple" },
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
    <CombineRuleID
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

export default CombineRuleList;
