import React, {useState, useEffect} from "react";
//import logo from "./logo.svg";
import "./App.css";
import Bar from "./components/Bar/Bar.js";
import Market from "./components/Market/Market";
import SignUp from "./components/Bar/SignUp";
import InitMarket from "./components/Bar/InitMarket";
import SignUpManager from "./components/Manager/SignUpManager";
import {createApiClientHttp} from "../../my-app/src/client/clientHttp";
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

function App(props) {
    console.log("App")
    let stompClient = null; //save this instance
    let serverChannelURI = "http://localhost:8090/notifications";
    //connect()
    const [Appnotification, setAppNotification] = useState("");

    const connect = () => {
        var socket = new SockJS(serverChannelURI);
        stompClient = Stomp.over(socket);
        stompClient.debug = () => {
        }; //to remove the debug of sockjs with stomp.
        stompClient.connect({}, onConnected, onError);

    }

    let onConnected = () => {

        // Subscribe to the notifications from server
        stompClient.subscribe("/user/notification/item", onMessageReceived);
        console.log("********************* onConnected UUID " + UUID);

        // Tell your 'uuid' to the server - start the session through webSocket
        stompClient.send("/ws/start/" + UUID)
    };

//Call this from logout() or guestLeave()
    const onDisconnect = () => {
        console.log("********************* onDisconnect UUID " + UUID);
        setAppNotification("")

        // Tell your 'uuid' to the server - end the session through webSocket
        stompClient.send("/ws/stop/" + UUID)

        console.log("onDisconnect end");
    };

    let onMessageReceived = (payload) => {
        var message = payload.body;
        console.log("Server message: [" + message + "]"); //for testing
        setAppNotification(message)
        //do what ever we want to do with the message (string)
    }

    let onError = () => {
        console.log("Error on attemp to connect with web-socket [is server on?]");
        //show an error on to the user
    }
    let apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    // const [UUID, SetUUID] = useState("-1");
    let UUID='-1'
    //
    // async function guestVisit() {
    //     const guestVisitResponse = await apiClientHttp.guestVisit();
    //     if (guestVisitResponse.errorMsg !== -1) {
    //         SetError(guestVisitResponse.errorMsg)
    //     } else {
    //         console.log("guestVisitResponse "+guestVisitResponse)
    //         console.log("guestVisitResponse val "+guestVisitResponse.value)
    //         SetUUID(guestVisitResponse.value)
    //         UUID=guestVisitResponse.value
    //
    //     }
    // }
    // // guestVisit();
    // useEffect(() => {
    //     guestVisit();
    // }, [UUID.refresh]);
    console.log("App UUID "+UUID)


    const [command, setCommand] = useState(<Market uuid={UUID} isLogin={false}/>);

    const [updateMarket, b] = useState("");


    const loginHandler = (newUUID,email,systemManager) => {
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
        console.log("loginHandler newUUID "+newUUID)

        console.log("loginHandler UUID "+UUID)
        connect();
        console.log("after login")
        console.log("email"+email)

        setCommand(<Market uuid={newUUID} useremail={email} isLogin={true} isSystemManager={systemManager}/>);
    };

    //todo: generate UUId of GUEST
    const logoutHandler = (uuid1) => {
        // UUID=uuid1
        // guestVisit()//??!
        // UUID='-1';
        setBarcommand(
            <Bar
                uuid={uuid1}
                onLogin={loginHandler}
                onLogout={logoutHandler}
                onSignUp={signUpHandler}
                onInitMarket={InitMarketHendler}
            />
        );
        console.log("logoutHandler UUID "+UUID)
        onDisconnect();
        console.log("after logout")
        setCommand(<Market uuid={UUID} isLogin={false}/>);
    };

    //todo: generate UUId of GUEST
    async function newMarketHandler(uuid1) {
        // UUID=uuid1
        //props.UUID
        console.log("uuid: " +uuid1)
        UUID='-1;'
        setCommand(<Market uuid='-1'/>);
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

    const loginAfterRegisterHolder = () => {
        setCommand(<Market uuid={UUID}/>);
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
            <div className="market ">
               <h2>{Appnotification}</h2>

            </div>
            {command}
        </div>
    );
}

export default App;
