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
        event.preventDefault();
        console.log(4444)
        const initResponse = await apiClientHttp.initMarket(enteredEmail, enteredPassword, enteredPhone);
        if (initResponse.get('errorMsg') !== -1) {
            SetError(initResponse.get('errorMsg'))
        } else {
            const loginResponse = await apiClientHttp.login("5db620f6-16c4-4f67-bf24-1f54b60cdd04 ", "liel@gmail.com", "12334abcA!");
            if (loginResponse.get('errorMsg') !== -1) {
                SetError(loginResponse.get('errorMsg'))
            } else {
                props.onSaveData(loginResponse.get('value'));
                console.log(222222222222)

            }
        }
    }

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
