import React, {useState} from "react";
import "./SignUp.css";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";

const SignUp = (props) => {
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    const [enteredName, SetName] = useState("");
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
        const userData = {
            name: enteredName,
            email: enteredEmail,
            password: enteredPassword,
            Phone: enteredPhone,
        };

        const registerResponse = await apiClientHttp.addNewMember(props.UUID, enteredEmail, enteredPassword, enteredPhone);
        if (registerResponse.get('errorMsg') !== -1) {
            SetError(registerResponse.get('errorMsg'))
        } else {
            const loginResponse = await apiClientHttp.login(props.UUID, enteredEmail, enteredPassword);
            if (loginResponse.get('errorMsg') !== -1) {
                SetError(loginResponse.get('errorMsg'))
            } else {
                props.onSaveExpenseData(userData);
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
        <div className="signUp">
            <form onSubmit={submitHandler}>
                <div className="signUp__controls">
                    <div className="signUp__control">
                        <label>Name</label>
                        <input
                            type="text"
                            value={enteredName}
                            onChange={nameChangeHandler}
                        />
                    </div>
                    <div className="signUp__control">
                        <label>Email</label>
                        <input
                            type="text"
                            value={enteredEmail}
                            onChange={emailChangeHandler}
                        />
                    </div>
                    <div className="signUp__control">
                        <label>Password</label>
                        <input
                            type="text"
                            value={enteredPassword}
                            onChange={passChangeHandler}
                        />
                    </div>
                    <div className="signUp__control">
                        <label>Phone</label>
                        <input
                            type="number"
                            value={enteredPhone}
                            onChange={phoneChangeHandler}
                        />
                    </div>
                </div>
                <div className="signUp__actions">
                    <button type="button" onClick={cancelHandler}>
                        Cancel
                    </button>
                    <button type="submit"> confirm</button>
                </div>
            </form>
        </div>
    );
};

export default SignUp;
