import React, {useState, useEffect} from "react";
//import logo from "./logo.svg";
import "./App.css";
import Bar from "./components/Bar/Bar.js";
import Market from "./components/Market/Market";
import SignUp from "./components/Bar/SignUp";
import InitMarket from "./components/Bar/InitMarket";
import SignUpManager from "./components/Manager/SignUpManager";
import {createApiClientHttp} from "../../my-app/src/client/clientHttp";


function App(props) {
    // constructor()
    // {
    //
    // let ui='-1'
    //     console.log(ui)
    //
    // };
    let apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    let UUID='-1'
    //const [UUID, setUUID] = useState("-2");


    async function guestVisit(event) {
        const guestVisitResponse = await apiClientHttp.guestVisit();
        if (guestVisitResponse.errorMsg !== -1) {
            SetError(guestVisitResponse.errorMsg)
        } else {
            console.log(guestVisitResponse)
            UUID=guestVisitResponse.value
        }
    }
   // console.log(UUID)

    const [command, setCommand] = useState(<Market uuid="6"/>);

    const [updateMarket, b] = useState("");


    const loginHandler = (newUUID) => {
        UUID=newUUID
       setBarcommand(
            <Bar
                uuid={newUUID}
                onLogin={loginHandler}
                onLogout={logoutHandler}
                onSignUp={signUpHandler}
                onInitMarket={InitMarketHendler}
            />
        );
        setCommand(<Market uuid={newUUID}/>);
    };

    //todo: generate UUId of GUEST
    const logoutHandler = (uuid1) => {
        UUID=uuid1
        setBarcommand(
            <Bar
                uuid={uuid1}
                onLogin={loginHandler}
                onLogout={logoutHandler}
                onSignUp={signUpHandler}
                onInitMarket={InitMarketHendler}
            />
        );
        setCommand(<Market uuid={uuid1}/>);
    };

    //todo: generate UUId of GUEST
    async function newMarketHandler(uuid1) {
       // uuid.preventDefault();

        //const initResponse = await setUUID(uuid);
        UUID=uuid1
        //props.UUID
        console.log("uuid: " +uuid1)
        console.log("UUID: " +UUID)

        setCommand(<Market uuid={uuid1}/>);
    }


    //todo: after init the market you won't login!
    const InitMarketHendler = () => {
        setCommand((pre) => {
            return (
                <InitMarket
                    onInit={() =>
                        setCommand(<SignUpManager onSaveData={newMarketHandler}/>)
                    }
                    onCencel={
                    () => setCommand(<Market uuid={UUID}/>)
                }
                />
            );
        });
    };

    const loginAfterRegisterHolder = (uuid1) => {
        setCommand(<Market uuid={uuid1}/>);
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

    const [barCommand, setBarcommand] = useState(
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
