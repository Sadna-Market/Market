import React, { useState } from "react";
import "./SignUpManager.css";
import Card from "../UI/Card";

const SignUpManager = (props) => {
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

  const submitHandler = (event) => {
    event.preventDefault();
    //register

    const userData = {
      name: enteredName,
      email: enteredEmail,
      password: enteredPassword,
      //phone
    };

    props.onSaveData();
  };

  const cancelHandler = () => {
    SetName("");
    SetEmail("");
    setPassword("");
    SetPhone("");
  };

  return (
    <div className="signUpManager">
      <h3>Manager System</h3>
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
        </div>
        <div className="signUpManager__actions">
          <button type="button" onClick={cancelHandler}>
            Cancel
          </button>
          <button type="submit"> confirm</button>
        </div>
      </form>
    </div>
  );
};

export default SignUpManager;
