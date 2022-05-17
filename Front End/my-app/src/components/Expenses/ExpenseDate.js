import React from "react";
import './ExpenseDate.css';
function ExpenseDate(props) {
  var monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  const month = monthNames[props.date.getMonth()];
  const day = props.date.getDate();
  const year = props.date.getFullYear();

  return (
    <div className="expense-date">
      <div className="expense-date__month"> {month} </div>
      <div className="expense-date__year"> {year} </div>
      <div className="expense-date__day"> {day} </div>
    </div>
  );
}

export default ExpenseDate;
