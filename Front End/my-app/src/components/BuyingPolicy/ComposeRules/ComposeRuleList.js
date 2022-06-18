import React, { useState, useEffect } from "react";
import ComposeRuleID from "./ComposeRuleID";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"

const ComposeRuleList = (props) => {
  console.log("ComposeRuleList")
  const [enteredError, SetError] = useState("");
  const apiClientHttp = createApiClientHttp();
  const [enteredRules, SetRules] = useState([]);

  //remove // after daniel imp

  // async function getAllRules() {
  //   let rules = [];
  //
  //   const getAllRulesResponse = await apiClientHttp.getAllRules();
  //
  //   if (getAllRulesResponse.errorMsg !== -1) {
  //     SetError(errorCode.get(getAllRulesResponse.errorMsg))
  //   } else {
  //SetError("")
  //     for (let i = 0; i < getAllRulesResponse.value.length; i++) {
  //       rules.push({
  //         id: getAllRulesResponse.value[i].id,
  //         kind: getAllRulesResponse.value[i].kind,
  //       })
  //     }
  //     SetRules(rules);
  //   }
  // }
  //
  // useEffect(() => {
  //   getAllRules();
  // }, [enteredRules.refresh]);
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
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

      <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default ComposeRuleList;
