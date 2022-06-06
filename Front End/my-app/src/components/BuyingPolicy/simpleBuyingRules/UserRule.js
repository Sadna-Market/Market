import React, { useState } from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass  from "../../RulesHelperClasses/BuyingRules"
const UserRule = (props) => {

  console.log("UserRule")
  console.log(props.compose)
  if (props.compose===undefined){
    console.log("compose===undefined "+props.compose)

  }
  else {
    console.log("compose!==undefined "+props.compose)

  }
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [email, setEmail] = useState("");
  const changeEmailHandler = (event) => {
    setEmail(event.target.value);
  };

  const cleanHandler = () => {
    setEmail("");
  };

  //todo: add new user Rule to store

  async function addHandler() {
    let rule = new RulesClass.UserRule(UUID,storeID,email)

    if (props.compose===undefined){ //false case - no comopse - realy simple
      const sendRulesResponse = await apiClientHttp.sendRules(rule);

      if (sendRulesResponse.errorMsg !== -1) {
        SetError(errorCode.get(sendRulesResponse.errorMsg))
      } else {
        cleanHandler();
        // props.onRule(sendRulesResponse.value);
        props.onRule(-1);

      }
    }
    else { //compose case
      cleanHandler();
      props.onRule(rule)

    }

  }

  return (
    <div>
      <h3>Add User Rule For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>User Email</label>
          <input
            type="text"
            value={email}
            placeholder="Write User Email"
            onChange={changeEmailHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default UserRule;
