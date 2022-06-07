import React, {useState} from "react";
import Password from "./Password";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const Profile = (props) => {
    console.log("Profile")

    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    let UUID = props.uuid;
    let userEmail = props.useremail;

    const [command, setCommand] = useState("");
    const changePassHandler = () => {
        setCommand(<Password uuid={UUID} onProfile={() => setCommand("")}/>);
    };

    //todo History of User
    async function historyHander() {
        setCommand("History Bla bla bla");
        console.log("historyHander UUID  "+UUID +"userEmail "+ userEmail)
        const getUserInfoResponse = await apiClientHttp.getUserInfo(UUID,userEmail);
        if (getUserInfoResponse.errorMsg !== -1) {
            SetError(errorCode.get(getUserInfoResponse.errorMsg))
        } else {
            setCommand(getUserInfoResponse.value);

        }
    }

    return (
        <div>
            <h3> My Profile </h3>
            <button onClick={changePassHandler}> Change Password</button>
            <button onClick={historyHander}> My History</button>
            <div>
                <h2>{command}</h2>
            </div>
        </div>
    );
};

export default Profile;
