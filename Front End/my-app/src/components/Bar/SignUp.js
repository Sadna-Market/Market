import React, { useState } from "react";
import "./SignUp.css";
import Card from "../UI/Card";

const SignUp = (props) => {
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
    props.onSaveExpenseData(userData);
  };

  const cancelHandler = () => {
    SetName("");
    SetEmail("");
    setPassword("");
    SetPhone("");
  };

  return (
    <div className="signUp">
      <form onSubmit={submitHandler}>
        <div className="signUp__controls">
          <div className="signUp__control">
            <label>Name</label>
            <input
              type="text"
              value={enteredName}
              onChange={nameChangeHandler}
            />
          </div>
          <div className="signUp__control">
            <label>Email</label>
            <input
              type="text"
              value={enteredEmail}
              onChange={emailChangeHandler}
            />
          </div>
          <div className="signUp__control">
            <label>Password</label>
            <input
              type="text"
              value={enteredPassword}
              onChange={passChangeHandler}
            />
          </div>
          <div className="signUp__control">
            <label>Phone</label>
            <input
              type="number"
              value={enteredPhone}
              onChange={phoneChangeHandler}
            />
          </div>
        </div>
        <div className="signUp__actions">
          <button type="button" onClick={cancelHandler}>
            Cancel
          </button>
          <button type="submit"> confirm</button>
        </div>
      </form>
    </div>
  );
};

export default SignUp;
