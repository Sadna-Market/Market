import React, {useState} from "react";
import "./ExpenseItem.css";
import ExpenseDate from "./ExpenseDate";
import Card from "../UI/Card";

function ExpenseItem(props) {
  console.log("ExpenseItem")

  const [amount, setTAmount] = useState(0);
  const clickHandler = () => setTAmount(amount+1);
  if (amount ===0){}
  const cancelHandler = () => setTAmount(0);
  return (
    <li>
    <Card className="expense-item">
      <ExpenseDate date={props.date}/>
      <div className="expense-item__description">
        <h2>{props.title}</h2>
        <h2>amount: {amount}</h2>
        <button onClick={clickHandler}>buy</button>
      <div className="expense-item__price">${props.amount}</div>
      <button onClick={cancelHandler}>cancel</button>
      </div>


    </Card>
    </li>
  );
}

export default ExpenseItem;
