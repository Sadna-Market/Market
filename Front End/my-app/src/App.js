import React, { useState, useEffect } from "react";
//import logo from "./logo.svg";
import "./App.css";
import Bar from "./components/Bar/Bar";
import Expenses from "./components/Expenses/Expenses";
import NewExpense from "./components/NewExpense/NewExpense";
import Market from "./components/Market/Market";
import SignUp from "./components/Bar/SignUp";
import InitMarket from "./components/Bar/InitMarket";
import SignUpManager from "./components/Manager/SignUpManager";

function App(props) {
  const items = [
    {
      id: "e1",
      title: "Toilet Paper",
      amount: 94.12,
      date: new Date(2020, 7, 14),
    },
    { id: "e2", title: "New TV", amount: 799.49, date: new Date(2021, 2, 12) },
    {
      id: "e3",
      title: "Car Insurance",
      amount: 294.67,
      date: new Date(2021, 2, 28),
    },
    {
      id: "e4",
      title: "New Desk (Wooden)",
      amount: 450,
      date: new Date(2021, 5, 12),
    },
  ];
  const [expenses, setExpenses] = useState(items);
  let counter = 1;
  const addExpenseHandler = (expense) => {
    console.log("App");
    console.log(expense);
    //   //setExpenses([expense,{ title: expense.title, amount: expense.amount, date: expense.date }])
    setExpenses((prevExpense) => [
      {
        id: counter++,
        title: expense.name,
        amount: expense.age,
        date: expense.date,
      },
      ...prevExpense,
    ]);
  };

  //const [UUID, setUUID] = useState(6);
  let UUID = 6;
  const [command, setCommand] = useState(<Market uuid="6" />);

  const [updateMarket, b] = useState("");

  const loginHandler = (newUUID) => {
    UUID = newUUID;
    setCommand(<Market uuid={UUID} />);
  };

  const logoutHandler = () => {
    UUID = 6;
    setCommand(<Market uuid={UUID} />);
  };

  const newMarketHandler = data => {
    setCommand(<Market uuid={data} />);
  };

  const InitMarketHendler = () => {
    setCommand((pre) => {
      return (
        <InitMarket
          onInit={() =>
            setCommand(<SignUpManager onSaveData={newMarketHandler} />)
          }
          onCencel={() => setCommand(<Market uuid={UUID} />)}
        />
      );
    });
  };

  const signUpHandler = () => {
    setCommand((pre) => {
      return (
        <SignUp
          onSaveExpenseData={(data) => setCommand(<Market uuid={UUID} />)}
        />
      );
    });
  };

  return (
    <div>
      <div>
        <Bar
          onLogin={loginHandler}
          onLogout={logoutHandler}
          onSignUp={signUpHandler}
          onInitMarket={InitMarketHendler}
        />
      </div>
      {command}
    </div>
  );
}

export default App;
