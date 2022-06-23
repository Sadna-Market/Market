import React, {useState} from "react";
import Users from "./Users";
import ConnectUsers from "./Users";
// import DisConnectUsers from "./DisConnectUsers";
// import NewProductType from "./NewProductType";
// import RemoveUser from "./RemoveUser";
import NewProductType from "./NewProductType";

import UserList from "./UserList";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const ManagerButton = (props) => {
    console.log("ManagerButton")
    const [email, Setemail] = useState("");
    const [emptyHistory, SetemptyHistory] = useState("");


    const changeEmailHandler = (event) => {
        Setemail(event.target.value);
    };
    const [enteredHistory, SetHistory] = useState([]);

    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    let UUID = props.uuid;
    const [command, setCommand] = useState("");


    const addProductHandler = () => {
        setCommand(<NewProductType uuid={UUID}/>);
    };

    const connectUsersHandler = () => {
        setCommand(<Users uuid={UUID}/>);
    };
    async function AllUsersHistoryHander() {
        let History = [];
        // setCommand("No History");
        console.log("historyHander UUID  " + UUID + "email " + email + "end ")
        const getUserInfoResponse = await apiClientHttp.getUserInfo(UUID, email); //it return  user x history!!!!
        if (getUserInfoResponse.errorMsg !== -1) {
            SetError(errorCode.get(getUserInfoResponse.errorMsg))
        } else {
            let str = JSON.stringify(getUserInfoResponse);
            console.log("vhistoryHander getUserInfoResponse 1 " + getUserInfoResponse.value)
            if (getUserInfoResponse.value.length > 0) {
                SetemptyHistory("")

            }
            else{
                SetemptyHistory("No history yet")
            }
            for (let i = 0; i < getUserInfoResponse.value.length; i++)
            {
                for (let k = 0; k < getUserInfoResponse.value[i].length; k++) {

                    console.log(getUserInfoResponse.value[i][k])
                    console.log(getUserInfoResponse.value[i][k].finalPrice)
                    console.log(getUserInfoResponse.value[i][k].products)
                    let products = getUserInfoResponse.value[i][k].products
                    for (let j = 0; j < products.length; j++) {
                        console.log(products[j].itemID)
                        console.log(products[j].name)
                        console.log(products[j].price)
                        console.log(products[j].quantity)
                        History.push({
                            TID: getUserInfoResponse.value[i][k].TID,
                            finalPrice: getUserInfoResponse.value[i][k].finalPrice,
                            itemID: products[j].itemID,
                            // name: products[j].name,
                            price: products[j].price,
                            quantity: products[j].quantity,
                        })
                    }
                }
            }
            SetHistory(History)
            SetError("")
        }

    }
    let expensesContent = enteredHistory.map((expense) => (
        <h2>TID: {expense.TID} ,TID final Price: {expense.finalPrice} ,item ID: {expense.itemID} ,Product
            price: {expense.price} ,quantity: {expense.quantity}</h2>

    ));
    return (
        <div>
            <div>
                <h3>Manager</h3>
                <button onClick={addProductHandler}>Add Product to Market</button>
                <button onClick={connectUsersHandler}>Users</button>
                <h3></h3>
                <div className="products__control">
                    <label>Email</label>
                    <input
                        type="text"
                        value={email}
                        placeholder="Write Email"
                        onChange={changeEmailHandler}
                    />
                    <button onClick={AllUsersHistoryHander}> search history for user</button>

                </div>
            </div>
            <div>
                {command}
                {expensesContent}
                {emptyHistory}


            </div>
        </div>
    );
};

export default ManagerButton;
