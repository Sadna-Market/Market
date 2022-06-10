import React, { useState } from "react";
import Card from "../UI/Card";
import "./Bar.css";
import SignUp from "./SignUp";
import SignIn from "./Login";
import {createApiClientHttp} from "../../client/clientHttp";
import {errorCode} from "../../ErrorCodeGui"
const Bar = (props) => {
  console.log("Bar")

  let apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  const [isLogin, setIsLogin] = useState(false);

  const [userName, setUserName] = useState("");


  const [email, setEmail] = useState("");
  const emailChangeHandler = (event) => {
    setEmail(event.target.value);
  };

  const [password, setPassword] = useState("");
  const passChangeHandler = (event) => {
    setPassword(event.target.value);
  };


  //todo: login after that the UUID put on onLogin(<UUID>)
  async function loginHandler(event) {
    event.preventDefault();
    // const Response = await apiClientHttp.getAllStores();

    const guestVisitResponse = await apiClientHttp.guestVisit();
    // const loginResponse = await apiClientHttp.login(guestVisitResponse.value, email, password);
    const loginResponse = await apiClientHttp.login(guestVisitResponse.value, "sysManager@gmail.com", "Shalom123$2");

    let systemManager=false;
    if (loginResponse.errorMsg!== -1) {
      SetError(errorCode.get(loginResponse.errorMsg))
    } else {
      const isSystemManagerUUIDResponse = await apiClientHttp.isSystemManagerUUID(loginResponse.value);

      if (isSystemManagerUUIDResponse.errorMsg=== -1){
        systemManager=true
      }
      setUserName(email);
      setIsLogin(true);
      props.onLogin(loginResponse.value,email,systemManager);
    }
    console.log(loginResponse)

  }

  async function logoutHandler(event) {
    event.preventDefault();
    const logoutResponse = await apiClientHttp.logout(props.uuid);

    if (logoutResponse.errorMsg!== -1) {
      SetError(errorCode.get(logoutResponse.errorMsg))
    } else {
      setEmail("");
      setPassword("");
      props.onLogout(logoutResponse.value);
      setIsLogin(false);
    }
    console.log(logoutResponse)

  }


  const initHandler = () => {
    props.onInitMarket();
  };

  const singUpHandler = () => {
    props.onSignUp();
  };

  const newUserHolder = (userData) => {
    console.log(userData);
  };

  let command = "";
  if (isLogin === false) {
    command = (
      <div className="bar__controls">
        <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
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
          <button
            className="signUp__button"
            onClick={singUpHandler}
            // onUserData={newUserHolder}
          >
            Sign-Up
          </button>
          {/*<button className="bar__button" onClick={initHandler}>*/}
          {/*  {" "}*/}
          {/*  Init-Market*/}
          {/*</button>*/}

        </div>
      </div>
    );
  } else {
    command = (
      <div>
        <label>welcome {userName}</label>
        <div className="bar__actions">
          <button
            className="signUp__button"
            onClick={logoutHandler}
            // onUserData={newUserHolder}
          >
            LogOut
          </button>
        </div>
      </div>
    );
  }

  return <Card className="bar">{command}</Card>;
};

export default Bar;
