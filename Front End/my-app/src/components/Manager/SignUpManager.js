import React, {useState} from "react";
import "./SignUpManager.css";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";


const SignUpManager = (props) => {
    const apiClientHttp = createApiClientHttp();

    const [enteredName, SetName] = useState("");
    const [enteredError, SetError] = useState("");

    const nameChangeHandler = (event) => {
        SetName(event.target.value);
    };

    const [enteredEmail, SetEmail] = useState("");
    const emailChangeHandler = (event) => {
        SetEmail(event.target.value);
    };

    const [enteredPassword, setPassword] = useState("");
    const passChangeHandler = (event) => {
        setPassword(event.target.value);
    };

    const [enteredPhone, SetPhone] = useState("");
    const phoneChangeHandler = (event) => {
        SetPhone(event.target.value);
    };

    async function submitHandler(event) {
        // props.preventDefault();
        event.preventDefault();

        console.log(66666666666)
         const initResponse = await apiClientHttp.initMarket("liel@gmail.com", "123456", "0526565201");

        // const initResponse = await apiClientHttp.initMarket(enteredEmail, enteredPassword, enteredPhone);
        // const initResponse = await apiClientHttp.guestVisit();

        console.log(initResponse)

        props.onSaveData(8888);

        //5
        // if (initResponse.errorMsg===1) {
        //     props.onSaveData(22222222222);
        // } else {
        //     props.onSaveData(33333333333);
        //     props.onSaveData(initResponse.errorMsg);
        //
        // }

        // if (json.errorMsg === -1) {
        //     const json2 = await apiClientHttp.login(json.value, enteredEmail, enteredPassword)
        //     if (json2.errorMsg === -1) {
        //         props.onSaveData(1);
        //         // props.onSaveData(json2.value);
        //
        //     } else {
        //         props.onSaveData(3);
        //
        //         SetError(json2.value);
        //         // cancelHandler();
        //     }
        // } else {
        //     SetError(json.value);
        //     props.onSaveData(2);
        //
        // }
            // const userData = {
            //     name: enteredName,
            //     email: enteredEmail,
            //     password: enteredPassword,
            //     Phone: enteredPhone,
            // };

            //return uuid
            // let json = await apiClientHttp.initMarket(userData.email, userData.password, userData.Phone);

            // props.onSaveData(7);

    }
    // async function submitHandler(event) {
    //     event.preventDefault();
    //     const userData = {
    //         name: enteredName,
    //         email: enteredEmail,
    //         password: enteredPassword,
    //         Phone: enteredPhone,
    //     };
    //
    //     let json = await apiClientHttp.initMarket(userData.email, userData.password, userData.Phone);
    //     if (json.errorMsg === -1) {
    //         json = await apiClientHttp.login(json.value, userData.email, userData.password)
    //         if (json.errorMsg === -1) {
    //             props.onSaveData(json.value);
    //         } else {
    //             console.log(json.value)
    //             SetError(json.value);
    //             cancelHandler();
    //         }
    //     }
    //     else {
    //         console.log(json.value)
    //         SetError(json.value);
    //         cancelHandler();
    //     }
    // }
    // console.log(uuid.value)
    // console.log(uuid.errorMsg)

    const cancelHandler = () => {
        SetName("");
        SetEmail("");
        setPassword("");
        SetPhone("");
    };

    return (
        <div className="signUpManager">
            <h3>Manager System</h3>
            <h3>{enteredError}</h3>

            <form onSubmit={submitHandler}>
                <div className="signUpManager__controls">
                    <div className="signUpManager__control">
                        <label>Name</label>
                        <input
                            type="text"
                            value={enteredName}
                            onChange={nameChangeHandler}
                        />
                    </div>
                    <div className="signUpManager__control">
                        <label>Email</label>
                        <input
                            type="text"
                            value={enteredEmail}
                            onChange={emailChangeHandler}
                        />
                    </div>
                    <div className="signUpManager__control">
                        <label>Password</label>
                        <input
                            type="text"
                            value={enteredPassword}
                            onChange={passChangeHandler}
                        />
                    </div>
                    <div className="signUpManager__control">
                        <label>Phone</label>
                        <input
                            type="number"
                            value={enteredPhone}
                            onChange={phoneChangeHandler}
                        />
                    </div>
                </div>
                <div className="signUpManager__actions">
                    <button type="button" onClick={cancelHandler}>
                        Cancel
                    </button>
                    <button type="submit"> confirm</button>
                </div>
            </form>
        </div>
    );
};

export default SignUpManager;
