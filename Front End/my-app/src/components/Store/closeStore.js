import React, {useState, useEffect} from "react";
import "./CloseStore.css";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const CloseStore = (props) => {
    console.log("CloseStore")
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    let UUID = props.uuid;
    let storeID = props.storeID;

    //close store
    async function yesHolder() {
        const closeStoreResponse = await apiClientHttp.closeStore(UUID, storeID);
        if (closeStoreResponse.errorMsg !== -1) {
            SetError(errorCode.get(closeStoreResponse.errorMsg))
        } else {
            props.onMarket();
        }
        console.log("closeStoreResponse")
        console.log(closeStoreResponse)

    }

    const cencelHolder = () => {
        props.onMarket();
    };

    return (
        <Card className="close">
            <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
            <h3>Are you Sure?</h3>
            <button onClick={yesHolder}> Yes</button>
            <button onClick={cencelHolder}> Cancel</button>
        </Card>
    );
};

export default CloseStore;
