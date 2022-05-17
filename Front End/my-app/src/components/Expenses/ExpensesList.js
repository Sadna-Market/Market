import React from "react";
import './ExpensesList.css';
import ExpenseItem from "./ExpenseItem";

const ExpensesList = props => {
    if (props.items.length === 0 ){
        return <h2 className='expenses-list__fallback'>
            Found No expenses</h2>
    }
    let expensesContent= props.items.map((expense) => (
        <ExpenseItem
          key={expense.id}
          title={expense.title}
          amount={expense.amount}
          date={expense.date}
        />
      ));
    
    return <ul className="expenses-list">
        {expensesContent}
    </ul>
};

export default ExpensesList;