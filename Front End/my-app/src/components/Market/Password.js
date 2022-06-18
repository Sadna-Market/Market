import React, {useState} from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const Password = (props) => {
    console.log("Password")

    let UUID = props.uuid;
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    const [email, setEmail] = useState("");
    const changeEmailHandler = (event) => {
        setEmail(event.target.value);
    };
    const [pass1, setpass1] = useState("");
    const changePass1Handler = (event) => {
        setpass1(event.target.value);
    };
    const [pass2, setpass2] = useState("");
    const changePsss2Handler = (event) => {
        setpass2(event.target.value);
    };

    const cleanHandler = () => {
        SetError("")
        setEmail("");
        setpass1("");
        setpass2("");
    }

    //todo: change thr password
    async function confirmHandler() {
        const changePasswordResponse = await apiClientHttp.changePassword(UUID,email,pass1,pass2);
        console.log("start func  confirmHandler changePasswordResponse")
        console.log(changePasswordResponse)

        if (changePasswordResponse.errorMsg !== -1) {
            SetError(errorCode.get(changePasswordResponse.errorMsg))
        } else {
            props.onProfile();
        }
        cleanHandler();
    }

    return (
        <div>
            <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
            <h3>Change Password</h3>
            <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>

            <div className="products__controls">
                <div className="products__control">
                    <label>Email</label>
                    <input
                        type="text"
                        value={email}
                        placeholder="Write Email"
                        onChange={changeEmailHandler}
                    />
                </div>
                <div className="products__control">
                    <label>Old Password </label>
                    <input
                        type="text"
                        value={pass1}
                        placeholder="Write Old Password"
                        onChange={changePass1Handler}
                    />
                </div>
                <div className="products__control">
                    <label>New Password </label>
                    <input
                        type="text"
                        value={pass2}
                        placeholder="Write NEw Password"
                        onChange={changePsss2Handler}
                    />
                </div>
                <button onClick={cleanHandler}>Clean</button>
                <button onClick={confirmHandler}>Confirm</button>
            </div>
        </div>
    );
};

export default Password;
