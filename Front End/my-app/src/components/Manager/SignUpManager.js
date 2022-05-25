import React, {useState} from "react";
import "./SignUpManager.css";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"


const SignUpManager = (props) => {
  console.log("SignUpManager UUID:")

  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  const [enteredName, SetName] = useState("");
  const nameChangeHandler = (event) => {
    SetName(event.target.value);
  };

  const [enteredEmail, SetEmail] = useState("");
  const emailChangeHandler = (event) => {
    SetEmail(event.target.value);
  };

  const [enteredPassword, setPassword] = useState("");
  const passChangeHandler = (event) => {
    setPassword(event.target.value);
  };

  const [enteredPhone, SetPhone] = useState("");
  const phoneChangeHandler = (event) => {
    SetPhone(event.target.value);
  };

  const [enteredDate, SetDate] = useState("");// need to chang to 10/01/1996
  const dateChangeHandler = (event) => {
    SetDate(event.target.value);
  };

  //todo: init the market
  async function submitHandler (event) {
    event.preventDefault();

    const initResponse = await apiClientHttp.initMarket(enteredEmail, enteredPassword, enteredPhone, '10/01/1996');
    if (initResponse.errorMsg !== -1) {
      SetError(errorCode.get(initResponse.errorMsg))
    } else {
      props.onSaveData(initResponse.value);
    }
    console.log(initResponse)

  }

  const cancelHandler = () => {
    SetName("");
    SetEmail("");
    setPassword("");
    SetPhone("");
  };

  return (
      <div className="signUpManager">
        <h3>Manager System</h3>
        <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
        <form onSubmit={submitHandler}>
          <div className="signUpManager__controls">
            <div className="signUpManager__control">
              <label>Name</label>
              <input
                  type="text"
                  value={enteredName}
                  onChange={nameChangeHandler}
              />
            </div>
            <div className="signUpManager__control">
              <label>Email</label>
              <input
                  type="text"
                  value={enteredEmail}
                  onChange={emailChangeHandler}
              />
            </div>
            <div className="signUpManager__control">
              <label>Password</label>
              <input
                  type="text"
                  value={enteredPassword}
                  onChange={passChangeHandler}
              />
            </div>
            <div className="signUpManager__control">
              <label>Phone</label>
              <input
                  type="number"
                  value={enteredPhone}
                  onChange={phoneChangeHandler}
              />
            </div>
            <div className="new-expense__control">
              <label>BirthDay</label>
              <input
                  type="date"
                  value={enteredDate}
                  min="1879-03-14"
                  max="2004-14-04"
                  onChange={dateChangeHandler}
              />
            </div>
          </div>
          <div className="signUpManager__actions">
            <button type="button" onClick={cancelHandler}>
              Clean
            </button>
            <button type="submit"> confirm</button>
          </div>
        </form>
      </div>
  );
};

export default SignUpManager;
