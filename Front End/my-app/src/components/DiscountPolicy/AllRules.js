import React, { useState } from "react";
import RuleInfo from "./RuleInfo";
import RuleList from "./RuleList";

const AllRules = (props) => {
  console.log("AllRules")

  let UUID = props.uuid;
  let storeID = props.storeID;
  const [searchRule, setRule] = useState("");

  const infoRuleHandler = (ruleID,UUID,storeID) => {
    setCommand(<RuleInfo ruleID={ruleID} uuid={UUID} storeID={storeID}/>);
  };
//    const getRuleInfoResponse = await apiClientHttp.getBuyRuleByID( props.uuid,props.storeID,props.ruleID); check
  const [command, setCommand] = useState(
    <RuleList
      search={searchRule}
      uuid={UUID}
      storeID={storeID}
      onInfo={infoRuleHandler}
    />
  );

  const searchButtonHolder = () => {
    setBack(command);
    setCommand(
      <RuleList
        search={searchRule}
        uuid={UUID}
        storeID={storeID}
        onInfo={infoRuleHandler}
      />
    );
    setRule(() => "");
  };

  const [back, setBack] = useState(command);

  const findStoreHandler = (event) => {
    setRule(() => event.target.value);
  };

  const backHandler = () => {
    setCommand(back);
  };

  return (
    <div className="marketButton">
      <h3>All Rules</h3>
      <div className="bar__controls">
        <div className="bar__control">
          <label>Search Rule</label>
          <input
            type="text"
            value={searchRule}
            onChange={findStoreHandler}
            placeholder="Rule ID"
          />
          <button onClick={searchButtonHolder}>Search</button>
          <button onClick={backHandler}>Back</button>
        </div>
      </div>
      {command}
    </div>
  );
};

export default AllRules;
