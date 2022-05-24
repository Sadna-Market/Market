import React, {useState} from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const EditPermission = (props) => {
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    let UUID = props.uuid;
    let storeID = props.storeID;

    const [email, setEmail] = useState("");

    const [onOffState, setonOffState] = useState("");

    const changeOnOffStateHandler = (event) => {
        setonOffState(event.target.value);
    };
    const changeEmailHandler = (event) => {
        setEmail(event.target.value);
    };

    const [permission, setPermission] = useState("");
    const changePermissionHandler = (event) => {
        setPermission(event.target.value);
    };

    const cleanHandler = () => {
        setEmail("");
        setPermission("");
    };

    //todo
    //2.4.7

    async function editHandler() {
        const setManagerPermissionsResponse = await apiClientHttp.setManagerPermissions(UUID, storeID, email, permission, onOffState);
        if (setManagerPermissionsResponse.errorMsg !== -1) {
            SetError(errorCode.get(setManagerPermissionsResponse.errorMsg))
        } else {
            cleanHandler();
            props.onStore();
        }

    }

    return (
        <div>
            <div style={{color: 'red', backgroundColor: "black", fontSize: 30}}>{enteredError}</div>
            <h3>Edit Manager Permission In Store {storeID}</h3>
            <div className="products__controls">
                <div className="products__control">
                    <label>Email</label>
                    <input
                        type="text"
                        value={email}
                        placeholder="write the Email"
                        onChange={changeEmailHandler}
                    />
                </div>
                <div className="products__control">
                    <label>Permission</label>
                    <input
                        type="text"
                        value={permission}
                        placeholder="write the Permission"
                        onChange={changePermissionHandler}
                    />
                </div>
                <div className="products__control">
                    <label>on / off</label>
                    <input
                        type="text"
                        value={onOffState}
                        placeholder="write on or off"
                        onChange={changeOnOffStateHandler}
                    />
                </div>
                <button onClick={cleanHandler}>Clean</button>
                <button onClick={editHandler}>Edit Permission</button>
            </div>
        </div>
    );
};

export default EditPermission;
