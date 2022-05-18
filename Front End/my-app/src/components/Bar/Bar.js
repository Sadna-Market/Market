import React, { useState } from "react";
import Card from "../UI/Card";
import "./Bar.css";
import SignUp from "./SignUp";
import SignIn from "./Login";
import {createApiClientHttp} from "../../client/clientHttp";
const Bar = (props) => {
  const [isLogin, setIsLogin] = useState(false);
  let apiClientHttp = createApiClientHttp();

  const [userName, setUserName] = useState("");

  const [email, setEmail] = useState("");
  const emailChangeHandler = (event) => {
    setEmail(event.target.value);
  };

  const [password, setPassword] = useState("");
  const passChangeHandler = (event) => {
    setPassword(event.target.value);
  };

  const loginHandler = (event) => {
    event.preventDefault();

    //login from service
    setUserName("Yaki");

    setIsLogin(true);
    props.onLogin(7);
  };

  const logoutHandler = (event) => {
    event.preventDefault();
    setEmail("");
    setPassword("");
    props.onLogout();
    setIsLogin(false);
  };

  const initHandler= ()=>{
      props.onInitMarket();
  };

  const singUpHandler = () => {
    props.onSignUp();
  };
  
  const newUserHolder = userData=>{
      console.log(userData);
  };

  let command = "";
  if (isLogin === false) {
    command = (
      <div className="bar__controls">
        <div className="bar__control">
          <label>User's Email</label>
          <input type="text" value={email} onChange={emailChangeHandler} />
        </div>
        <div className="bar__control">
          <label>Password</label>
          <input type="text" value={password} onChange={passChangeHandler} />
        </div>
        <div className="bar__actions">
          <button className="bar__button" onClick={loginHandler} type="submit">
            Login
          </button>
          <button className="signUp__button" onClick={singUpHandler} onUserData={newUserHolder}>
            Sign-Up
          </button>
          <button className="bar__button" onClick={initHandler}> Init-Market</button>
        </div>
      </div>
    );
  } else {
    command = (
      <div>
        <label>welcome {userName}</label>
        <div className="bar__actions">
          <button className="signUp__button" onClick={logoutHandler} onUserData={newUserHolder}>
            LogOut
          </button>
        </div>
      </div>
    );
  }

  return <Card className="bar">{command}</Card>;
};

export default Bar;
