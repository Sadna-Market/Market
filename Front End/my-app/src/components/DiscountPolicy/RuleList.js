import React, { useState, useEffect } from "react";
import RuleID from "./RuleID";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const RuleList = (props) => {
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  console.log("RuleList")
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [enteredrules, Setrules] = useState([]);

  async function getAllRules() {
    let allRules = [];

    const getBuyPolicyResponse = await apiClientHttp.getDiscountPolicy(UUID,storeID);
    console.log("start func  getAllRules getDiscountPolicy")

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
  // let rules = [
  //   { id: 1, kind: "Add" },
  //   { id: 2, kind: "Simple" },
  //   { id: 3, kind: "XOR" },
  //   { id: 4, kind: "Simple" },
  //   { id: 5, kind: "XOR" },
  //   { id: 6, kind: "Or" },
  //   { id: 7, kind: "Simple" },
  // ];
  if (enteredrules.length === 0) {
    return <h2 className="stores-list__fallback">Found No Rule</h2>;
  }

  // if (props.search != "") {
  //   rules = rules.filter((rule) => rule.id === parseInt(props.search));
  // }

  //todo: update the list
  async function removeRuleHandler(ruleId){
    console.log("ruleId "+ruleId)

    const removeNewDiscountRuleResponse = await apiClientHttp.removeNewDiscountRule(UUID,storeID,ruleId);


    if (removeNewDiscountRuleResponse.errorMsg !== -1) {
      SetError(errorCode.get(removeNewDiscountRuleResponse.errorMsg))
    } else {
      SetError("")
      getAllRules()
    }
  }

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
