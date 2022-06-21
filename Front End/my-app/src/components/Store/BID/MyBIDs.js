import React, {useEffect, useState} from "react";
import BIDItem from "./BIDItem";
import Offer from "./Offer";
import Status from "./Status";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"

const MyBIDs = (props) => {
    console.log("MyBIDs")
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    let UUID = props.uuid;
    let storeID = props.storeID;
    //todo: get all my products
    //getMyBIDs
    const [enteredBIDs, SetBIDs] = useState([]);


    async function getAllBIDS() {
        let Bids = [];

        const getMyBIDsResponse = await apiClientHttp.getMyBIDs(UUID, storeID);

        let str = JSON.stringify(getMyBIDsResponse);
        console.log("getMyBIDsResponse    " + str)

        if (getMyBIDsResponse.errorMsg !== -1) {
            SetError(errorCode.get(getMyBIDsResponse.errorMsg))
        } else {
            for (let i = 0; i < getMyBIDsResponse.value.length; i++) {
                Bids.push({
                    id: getMyBIDsResponse.value[i].productID,
                    name: getMyBIDsResponse.value[i].productName,
                    amount: getMyBIDsResponse.value[i].quantity,
                    originPrice: getMyBIDsResponse.value[i].originPrice,
                    counterPrice: getMyBIDsResponse.value[i].counterPrice,
                    status: getMyBIDsResponse.value[i].status,
                })

            }
            console.log("Bids " + Bids)
            SetError("")
            SetBIDs(Bids);
        }
    }

    useEffect(() => {
        getAllBIDS();
    }, [enteredBIDs.refresh]);
    // let products = [
    //   { id: 1, name: "Sony 5", amount: 10, status: "Waiting" },
    //   { id: 2, name: "TV", amount: 10, status: "Rejected" },
    //   { id: 3, name: "Car KIA RIO", amount: 10, status: "Counter" },
    //   { id: 4, name: "AC/DC", amount: 10, status: "Approved" },
    //   { id: 5, name: "MAMA Yokero", amount: 10, status: "Waiting" },
    //   { id: 6, name: "Iphone 13", amount: 10, status: "bought" },
    // ];

    let enteredBIDss = enteredBIDs.map((expense) => (
        <Status
            originPrice={expense.originPrice}
            counterPrice={expense.counterPrice}
            id={expense.id}
            name={expense.name}
            status={expense.status}
            uuid={UUID}
            storeID={storeID}
            amount={expense.amount}
            onConfirm={() => props.onConfirm(expense.id, expense.amount)}
        />
    ));

    return (
        <div>
            <h3>Status</h3>
            <div>
                <ul className="products-list">{enteredBIDss}</ul>
            </div>
        </div>
    );
};

export default MyBIDs;
