import React, {Component, useState} from "react";
import MainPage from "./mainPage";
import {Route, Routes, Switch} from "react-router-dom";
import ReactDOM from "react-dom";
import SignUp from "./signUp";
import InitMarket from "./initMarket";

function Login(props) {
    let ans =true;
    function errorMsgChangeHandler(event) {
        setErrorMsgState(event);
    }
    function try2(event) {
        setEmailState(event);
    }
    function FLogin(connID, name, pass) {
        return new Response();
    }

    const [enteredPassword, setPasswordState] = useState("");
    const [enteredEmail, setEmailState] = useState("");

    const [enteredErrorMsg, setErrorMsgState] = useState("");

    async function submitHandler(event) {

        event.preventDefault();

        //apiHttp.
        //connid= call guest visit
        const loginResponse = FLogin(
            enteredEmail,
            enteredPassword,
        );

        //boolean ans = loginResponse.value
        let ans = true;
        let errorMsg = "invalid blablala";
        if (!ans)
            errorMsgChangeHandler(errorMsg);
        else {
            errorMsgChangeHandler("good");

        }

    }

    return (
        <form method="post" className="contact-form" >
            <h3>Sign In</h3>
            <div className="mb-3">
                <label>email address {enteredEmail}</label>
                <input
                    type="email"
                    className="form-control"
                    placeholder="Enter email"
                    onChange={try2}

                />
            </div>
            <div className="mb-3">
                <label>Password</label>
                <input
                    type="password"
                    className="form-control"
                    placeholder="Enter password"
                    // onChange={nameChangeHandler}

                />
            </div>
            <div className="mb-3">
                <div className="custom-control custom-checkbox">
                    <input
                        type="checkbox"
                        className="custom-control-input"
                        id="customCheck1"
                    />
                    <label className="custom-control-label" htmlFor="customCheck1">
                        Remember me
                    </label>
                </div>
            </div>

            <div className="d-grid">
                <button type="submit" onClick={submitHandler} className="btn btn-primary">
                    Submit
                </button>
            </div>

            <h1 style={{color: "red"}}>{enteredErrorMsg}</h1>

            <p className="forgot-password text-right">
                Forgot <a href="#">password?</a>
            </p>
            {/*{ans?(*/}
            {/*    <MainPage*/}
            {/*    />*/}
            {/*    ):("")*/}
            {/*}*/}
        </form>


    )

}

export default Login;