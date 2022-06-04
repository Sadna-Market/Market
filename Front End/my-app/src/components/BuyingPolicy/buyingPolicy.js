import React, { useState } from "react";
import StoreList from "../Store/StoreList";
import RuleID from "./RuleID";
import RuleInfo from "./RuleInfo";
import RuleList from "./RuleList";

const AllRules = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  const [searchRule, setRule] = useState("");

  const infoRuleHandler = (ruleID) => {
    setCommand(<RuleInfo ruleID={ruleID} />);
  };

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
