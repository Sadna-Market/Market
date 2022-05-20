import React, {useState, useEffect} from "react";
//import logo from "./logo.svg";
import "./App.css";
import Bar from "./components/Bar/Bar.js";
import Market from "./components/Market/Market";
import SignUp from "./components/Bar/SignUp";
import InitMarket from "./components/Bar/InitMarket";
import SignUpManager from "./components/Manager/SignUpManager";
//import {createApiClientHttp} from "../../my-app/src/client/clientHttp";

//let apiClientHttp = createApiClientHttp();

function App(props) {

  //const [UUID, setUUID] = useState(6);
  let UUID = 6;
  const [command, setCommand] = useState(<Market uuid="6" />);

  const [updateMarket, b] = useState("");

  
  const loginHandler = (newUUID) => {
    UUID = newUUID;
    setCommand(<Market uuid={UUID} />);
  };

   //todo: generate UUId of GUEST
  const logoutHandler = () => {
    UUID = 6;
    setCommand(<Market uuid={UUID} />);
  };

  //todo: generate UUId of GUEST
  const newMarketHandler = () => {
    UUID =6;
    setCommand(<Market uuid={UUID} />);
  };


  //todo: after init the market you won't login!
  const InitMarketHendler = () => {
    setCommand((pre) => {
      return (
        <InitMarket
          onInit={() =>
            setCommand(<SignUpManager onSaveData={newMarketHandler} />)
          }
          onCencel={() => setCommand(<Market uuid={UUID} />)}
        />
      );
    });
  };

  const loginAfterRegisterHolder = () =>{
    setCommand(<Market uuid={UUID} />);
  };

  const signUpHandler = () => {
    setCommand((pre) => {
      return (
        <SignUp
          onSaveExpenseData={loginAfterRegisterHolder}
        />
      );
    });
  };

  const [barCommand,setBarcommand] = useState(
    <Bar
    uuid={UUID}
    onLogin={loginHandler}
    onLogout={logoutHandler}
    onSignUp={signUpHandler}
    onInitMarket={InitMarketHendler}
  />
  );

  return (
    <div>
      <div>
        {barCommand}
      </div>
      {command}
    </div>
  );
}

export default App;
