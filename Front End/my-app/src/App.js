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
// import createApiClientHttp from "../src/client/clientHttp.js";
import {errorCode} from "../src/ErrorCodeGui"
function App(props) {
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
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
        // setAppNotification("dasss1123213123sssssssssssssssssssssssssssf\ndasss1123213123sssssssssssssssssssssssssssf\ndasss1123213123sssssssssssssssssssssssssssf\ndasss1123213123sssssssssssssssssssssssssssf\ndasss1123213123sssssssssssssssssssssssssssf")
        //do what ever we want to do with the message (string)
    }

    let onError = () => {
        console.log("Error on attemp to connect with web-socket [is server on?]");
        //show an error on to the user
    }


    // const [UUID, SetUUID] = useState("-1");
    let UUID='-5'
    //
    async function guestVisitHandler(newUUID) {//args?????
        UUID=newUUID
        setBarcommand(
            <Bar
                onGuestVisit={guestVisitHandler}
                uuid={newUUID}
                onLogin={loginHandler}
                onLogout={logoutHandler}
                onSignUp={signUpHandler}
                onInitMarket={InitMarketHendler}
            />
        );
        console.log("guestVisitHandler newUUID "+newUUID)

        console.log("guestVisitHandler UUID "+UUID)
        connect();
        setCommand(<Market uuid={newUUID} useremail={"guest"} isLogin={false} isSystemManager={false}/>);

    }

    console.log("App UUID "+UUID)


    const [command, setCommand] = useState("");

    const [updateMarket, b] = useState("");


    async function loginHandler(newUUID,email,systemManager){
        UUID=newUUID
       setBarcommand(
            <Bar
                onGuestVisit={guestVisitHandler}
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
        await new Promise(r => setTimeout(r, 2000));
        const modifyDelayMessagesResponse = await apiClientHttp.modifyDelayMessages(UUID);
        console.log("modifyDelayMessagesResponse",modifyDelayMessagesResponse)
        if (modifyDelayMessagesResponse.errorMsg !== -1) {
            SetError(errorCode.get(modifyDelayMessagesResponse.errorMsg))
        } else {

        }

        setCommand(<Market uuid={newUUID} useremail={email} isLogin={true} isSystemManager={systemManager}/>);
    }

    //todo: generate UUId of GUEST
    const logoutHandler = (uuid1) => {
        // UUID=uuid1
        // guestVisit()//??!
        // UUID='-1';
        setBarcommand(
            <Bar
                onGuestVisit={guestVisitHandler}
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
        setCommand(
            <div className="market ">
                <h2> sign up successfully </h2>

            </div>);


        // setCommand(<Market uuid={UUID}/>);
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
            onGuestVisit={guestVisitHandler}
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
