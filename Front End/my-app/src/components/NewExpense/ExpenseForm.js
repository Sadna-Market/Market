import React, { useState } from "react";
import "./ExpenseForm.css";



const ExpenseForm = (props) => {
  console.log("ExpenseForm")


  const [enteredName, SetName] = useState("");
  const nameChangeHandler = (event) => {
    SetName(event.target.value);
  };

  const [enteredEmail, SetEmail] = useState("");
  const emailChangeHandler = (event) => {
    SetEmail(event.target.value);
  };

  const [enteredAge, SetAge] = useState("");
  const ageChangeHandler = (event) => {
    SetAge(event.target.value);
  };

  const [enteredDate, SetDate] = useState("");
  const dateChangeHandler = (event) => {
    SetDate(event.target.value);
  };

  const submitHandler = (event) => {
    event.preventDefault();

    const expenseData = {
      name: enteredName,
      email: enteredEmail,
      age: enteredAge,
      date: new Date(enteredDate)
    };

    props.onSaveExpenseData(expenseData);

    SetName('');
    SetEmail('');
    SetAge('');
    SetDate('');
  };

  return (
    <form onSubmit={submitHandler}>
      <div className="new-expense__controls">
        <div className="new-expense__control">
          <label>Name</label>
          <input type="text" value={enteredName} onChange={nameChangeHandler} />
        </div>
        <div className="new-expense__control">
          <label>email</label>
          <input
            type="text"
            value={enteredEmail}
            onChange={emailChangeHandler}
          />
        </div>
        <div className="new-expense__control">
          <label>age</label>
          <input
            type="number"
            value={enteredAge}
            min="18"
            step="1"
            onChange={ageChangeHandler}
          />
        </div>
        <div className="new-expense__control">
          <label>Your Day</label>
          <input
            type="date"
            value={enteredDate}
            min="1948-01-01"
            max="2023-01-01"
            onChange={dateChangeHandler}
          />
        </div>
      </div>
      <div className="new=expense__actions">
        <button type="button" onClick={props.onCancel}> Cancel</button>
        <button type="submit"> confirm</button>
      </div>
    </form>
  );
};

export default ExpenseForm;
