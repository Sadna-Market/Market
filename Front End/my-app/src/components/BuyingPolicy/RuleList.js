import React, { useState, useEffect } from "react";
import RuleID from "./RuleID";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"
const RuleList = (props) => {
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  console.log("buying policy " + "RuleList ")

  let UUID = props.uuid;
  let storeID = props.storeID;

  const [enteredrules, Setrules] = useState([]);
  
  async function getAllRules() {
    let allRules = [];

    const getBuyPolicyResponse = await apiClientHttp.getBuyPolicy(UUID,storeID);
    console.log("start func  getAllStores")

    let str = JSON.stringify(getBuyPolicyResponse);
    console.log("getBuyPolicyResponse    "+str)

    if (getBuyPolicyResponse.errorMsg !== -1) {
      SetError(errorCode.get(getBuyPolicyResponse.errorMsg))
    } else {
      for (let i = 0; i < getBuyPolicyResponse.value.length; i++) {
        allRules.push({
          id: getBuyPolicyResponse.value[i].id,
          kind: getBuyPolicyResponse.value[i].kind,
        })

      }
      console.log("enteredrules "+allRules)
      SetError("")
      Setrules(allRules);
    }
  }

  useEffect(() => {
    getAllRules();
  }, [enteredrules.refresh]);
  console.log(enteredrules)
  console.log(enteredrules.length)
  let rules = [
    { id: 1, kind: "Add" },
    { id: 2, kind: "Simple" },
    { id: 3, kind: "condition" },
    { id: 4, kind: "Simple" },
    { id: 5, kind: "Or" },
    { id: 6, kind: "Or" },
    { id: 7, kind: "Simple" },
  ];
  if (enteredrules.length === 0) {
    return <h2 className="stores-list__fallback">Found No Rule</h2>;
  }
//enteredrules
  if (props.search != "") {
    // Setrules(enteredrules.filter((rule) => rule.id === parseInt(props.search)));
  }

  //todo: update the list
  async function removeRuleHandler(ruleId){
    console.log("ruleId "+ruleId)
    const removeBuyRuleResponse = await apiClientHttp.removeBuyRule(UUID,storeID,ruleId);


    if (removeBuyRuleResponse.errorMsg !== -1) {
      SetError(errorCode.get(removeBuyRuleResponse.errorMsg))
    } else {
      SetError("")
      getAllRules()
    }
  };

  const infoHandler = (ruleID) => {
    props.onInfo(ruleID);
  };

  let expensesContent = enteredrules.map((expense) => (
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
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default RuleList;
