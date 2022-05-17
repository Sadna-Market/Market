import React, { useState } from "react";
import "./NewExpense.css";
import ExpenseForm from "./ExpenseForm";

const NewExpense = (props) => {
  const [isEditing, setIsEditing] = useState(false);
  const saveExpenseDataHandler = (enteredExpenseData) => {
    const expenseData = {
      ...enteredExpenseData,
      id: Math.random().toString(),
    };
    props.onAddExpense(expenseData);
    setIsEditing(false);
    //console.log(expenseData);
  };
  const startEditingHandler = () => {
    setIsEditing(true);
  };
  const stoptHditingHandler = () => {
    setIsEditing(false);
  };

  let command = <button onClick={startEditingHandler}>Add new Expense</button>;
  if (isEditing) {
    command = (
      <ExpenseForm
        onSaveExpenseData={saveExpenseDataHandler}
        onCancel={stoptHditingHandler}
      />
    );
  }

  return <div className="new-expense">{command}</div>;
};

export default NewExpense;
