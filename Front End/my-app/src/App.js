import React, {useState, useEffect} from "react";
//import logo from "./logo.svg";
import "./App.css";
import Bar from "./components/Bar/Bar.js";
import Expenses from "./components/Expenses/Expenses";
import NewExpense from "./components/NewExpense/NewExpense";
import Market from "./components/Market/Market";
import SignUp from "./components/Bar/SignUp";
import InitMarket from "./components/Bar/InitMarket";
import SignUpManager from "./components/Manager/SignUpManager";
import {createApiClientHttp} from "../../my-app/src/client/clientHttp";

let apiClientHttp = createApiClientHttp();

function App(props) {
    const [enteredError, SetError] = useState("");
    const items = [
        {
            id: "e1",
            title: "Toilet Paper",
            amount: 94.12,
            date: new Date(2020, 7, 14),
        },
        {id: "e2", title: "New TV", amount: 799.49, date: new Date(2021, 2, 12)},
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

    // need to understand when client open the web and then give him the uuid with guest visit
    // figure it out way app called twice and when
    let UUID = -1;
    guestGetInToTheWeb();

    async function guestGetInToTheWeb() {
        const initResponse = await apiClientHttp.guestVisit();
        UUID = initResponse.get('value')
        console.log(initResponse)
    }

    const [command, setCommand] = useState(<Market uuid={UUID}/>);

    const [updateMarket, b] = useState("");

    const loginHandler = () => {
        setCommand(<Market uuid={UUID}/>);
    };

    async function logoutHandler() {
        const logoutResponse = await apiClientHttp.logout(UUID);
        if (logoutResponse.get('errorMsg') !== -1) {
            SetError(logoutResponse.get('errorMsg'))
        } else {
            setCommand(<Market uuid={UUID}/>);//todo if user logout, then the uuid not need to be sent no?
        }
    }

    const newMarketHandler = data => {
        setCommand(<Market uuid={data}/>);
    };

    const InitMarketHendler = () => {
        setCommand((pre) => {
            return (
                <InitMarket
                    onInit={() =>
                        setCommand(<SignUpManager onSaveData={newMarketHandler}/>)
                    }
                    onCencel={() => setCommand(<Market uuid={UUID}/>)}
                />
            );
        });
    };

    const signUpHandler = () => {

        setCommand((pre) => {
            return (
                <SignUp
                    UUID
                    onSaveExpenseData={(data) => setCommand(<Market uuid={UUID}/>)}
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
                    UUID
                />
            </div>
            {command}
        </div>
    );
}

export default App;
