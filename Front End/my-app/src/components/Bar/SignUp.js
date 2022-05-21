import React, {useState} from "react";
import "./SignUp.css";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";

const SignUp = (props) => {
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

  async function submitHandler(event) {
    event.preventDefault();
    const guestVisitResponse = await apiClientHttp.guestVisit();
    const addNewMemberResponse = await apiClientHttp.addNewMember(guestVisitResponse.value,enteredEmail, enteredPassword, enteredPhone,'10/01/1996');

    if (addNewMemberResponse.errorMsg !== -1) {
      SetError(SetError.errorMsg)
    } else {
      props.onSaveExpenseData();
    }
    console.log(addNewMemberResponse)

  }

  const cancelHandler = () => {
    SetName("");
    SetEmail("");
    setPassword("");
    SetPhone("");
  };

  return (
    <div className="signUp">
      <h3>Registration</h3>
      <h3>{enteredError}</h3>

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
        <div className="signUp__actions">
          <button type="button" onClick={cancelHandler}>
            Clean
          </button>
          <button type="submit"> confirm</button>
        </div>
      </form>
    </div>
  );
};

export default SignUp;
