
import "./Expenses.css";
import Card from "../UI/Card";
import React, { useState } from "react";
import ExpenseFilter from "./ExpenseFilter";
import ExpensesList from "./ExpensesList";
import ExpensesChart from "./ExpensesChart";

function Expenses(props) {
  console.log("Expenses")

  const [year, setYear] = useState("2020");
  const filterYearHandler = (selectedYear) => {
    setYear(selectedYear);
    console.log(selectedYear);
  };
  const filterExpenses = props.items.filter((expnse) => {
    return expnse.date.getFullYear().toString() === year;
  });
  return (
    <Card className="expenses">
      <h3> store 1</h3>
      <ExpenseFilter
        selected={year}
        onSelactedYear={filterYearHandler}
        />
        <ExpensesChart expenses={filterExpenses} />
      <ExpensesList items={filterExpenses} />
    </Card>
  );
}

export default Expenses;
