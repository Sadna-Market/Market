import React from "react";
import Card from "../UI/Card";

const Rules = (props) => {
    console.log("Rules")

    let UUID = props.uuid;
    let storeID = props.storeID;
    let Rules = props.rules;
    let owner=Rules.owner;
    let manager=Rules.manager;
    let founder=Rules.founder;


    //            //:getStoreRolesResponse.value {"errorMsg":-1,"value":{"owner":["sysManager@gmail.com"],"manager":[],"founder":["1dfssdf"]}}
    let owners = owner.map((owner) => (
    <h3>
        owner: {owner}
    </h3>
    ));
    let managers = manager.map((manager) => (
        <h3>
            manager: {manager}
        </h3>
    ));
    let founders = founder.map((founder) => (
        <h3>
            founder: {founder}
        </h3>
));
    //todo: return what return from this function
    return (
        <div>
            <h3>
                storeID: {storeID}
            </h3>
            {founders}

            {managers}

            {owners}

        </div>
    );
};
export default Rules;
