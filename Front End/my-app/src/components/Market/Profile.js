import React, {useEffect, useState} from "react";
import Password from "./Password";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const Profile = (props) => {
    console.log("Profile")
    console.log("userEmail"+ props.useremail)
    const [enteredHistory, SetHistory] = useState([]);

    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    let UUID = props.uuid;
    let userEmail = props.useremail;
    const [isOSystemManager, SetSystemManager] = useState(false);
    const [email, Setemail] = useState("");

    const [command, setCommand] = useState("");
    const [AllUsercommand, setAllUsercommand] = useState("");
    const changeEmailHandler = (event) => {
        Setemail(event.target.value);
    };

    const changePassHandler = () => {
        setCommand(<Password uuid={UUID} onProfile={() => setCommand("")}/>);
    };
    //isSystemManagerUUID
    //todo History of User
    async function isSystemManagerUUIDHander() {
        // they need to add userHistory for user in all the store, the function getUserInfo its for sys manager and it return alll users history

        const isSystemManagerUUIDResponse = await apiClientHttp.isSystemManagerUUID(UUID); //
        if (isSystemManagerUUIDResponse.errorMsg !== -1) {
            SetError(errorCode.get(isSystemManagerUUIDResponse.errorMsg))
        } else {
            SetSystemManager(isSystemManagerUUIDResponse.value)
            SetError("")

        }
        let str = JSON.stringify(isSystemManagerUUIDResponse);
        console.log(" isSystemManagerUUIDResponse  "+str)
    }
    useEffect(() => {
        isSystemManagerUUIDHander();
    }, [isOSystemManager.refresh]);
    async function AllUsersHistoryHander() {
        let History = [];
        // setCommand("No History");
        console.log("historyHander UUID  "+UUID +"email "+ email+"end ")
        const getUserInfoResponse = await apiClientHttp.getUserInfo(UUID,email); //it return  user x history!!!!
        if (getUserInfoResponse.errorMsg !== -1) {
            SetError(errorCode.get(getUserInfoResponse.errorMsg))
        } else {
            let str = JSON.stringify(getUserInfoResponse);
            console.log("vhistoryHander getUserInfoResponse  "+getUserInfoResponse.value)
            for (let i = 0; i < getUserInfoResponse.value.length; i++) {
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
    async function historyHander() {
        // they need to add userHistory func for user in all the store, the function getUserInfo its for sys manager and it return  user x history

        // setCommand("No History");
        // console.log("historyHander UUID  "+UUID +"userEmail "+ userEmail+"end ")
        // const getUserInfoResponse = await apiClientHttp.getUserInfo(UUID,userEmail); //
        // if (getUserInfoResponse.errorMsg !== -1) {
        //     SetError(errorCode.get(getUserInfoResponse.errorMsg))
        // } else {
        //     setCommand(getUserInfoResponse.value);
        //     SetError("")
        // }
        // let str = JSON.stringify(getUserInfoResponse);
        // console.log("vhistoryHander getUserInfoResponse  "+str)
    }
    //isSystemManagerUUID
    let expensesContent = enteredHistory.map((expense) => (
    <h2>TID: {expense.TID} ,TID final Price:  {expense.finalPrice} ,item ID: {expense.itemID} ,Product price: {expense.price} ,quantity: {expense.quantity}</h2>

    ));

    let permission = "";
    if (isOSystemManager){
        permission = (
            <>
                {/*<h2>All Users history in the system</h2>*/}
                <h3> </h3>
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
            </>
        );
    }
    return (
        <div>
            <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
            <h3> My Profile </h3>
            <button onClick={changePassHandler}> Change Password</button>
            <button onClick={historyHander}> My History in the system</button>
            {permission}
            <div>
                <h2>{command}</h2>
            </div>
            <div>
                <h2>{AllUsercommand}</h2>
                {expensesContent}
            </div>
        </div>
    );
};

export default Profile;
