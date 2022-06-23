import React, {useState} from "react";
import Card from "../../UI/Card";
import AllOffers from "./AllOffers";
import "./BIDItem.css";
import Options from "./Options";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"

const Offer = (props) => {
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    console.log("Offer")
    const [counter, setcounter] = useState("");

    const counterHandler = (event) => {
        setcounter(event.target.value);
    };

    async function sendHandler() {
        //        counterBID:async(uuid,userEmail,storeID,productID,newTotalPrice)=>{
        setcounter("");
        const counterBIDResponse = await apiClientHttp.counterBID(UUID, props.username, props.storeID, props.id, counter);
        console.log(counterBIDResponse)

        if (counterBIDResponse.errorMsg !== -1) {
            SetError(errorCode.get(counterBIDResponse.errorMsg))
        } else {
            SetError("")
            props.onChange()
        }
    }

    //        approveBID:async(uuid,userEmail,storeID,productID)=>{
    async function confirmHandler() {
        const approveBIDResponse = await apiClientHttp.approveBID(UUID, props.username, props.storeID, props.id);
        console.log(approveBIDResponse)

        if (approveBIDResponse.errorMsg !== -1) {
            SetError(errorCode.get(approveBIDResponse.errorMsg))
        } else {
            SetError("")
            props.onChange()
        }
    }

    async function rejectHandler() {
        //        rejectBID:async(uuid,userEmail,storeID,productID)=>{
        const rejectBIDResponse = await apiClientHttp.rejectBID(UUID, props.username, props.storeID, props.id);
        console.log(rejectBIDResponse)

        if (rejectBIDResponse.errorMsg !== -1) {
            SetError(errorCode.get(rejectBIDResponse.errorMsg))
        } else {
            SetError("")
            props.onChange()
        }
    }

    let UUID = props.uuid;
    let productID = props.id;
    let storeID = props.storeID;
    const [offersCommand, setOffersCommand] = useState("");

    const [offer, setoffer] = useState("");
    const offerChangeHandler = (event) => {
        setoffer(event.target.value);
    };

    //get the price and the amount
    let offers = props.offers;
    let name = props.name;

    //todo:open offers
    const clickHandler = () => {
        //open offers
        if (offersCommand == "")
            setOffersCommand(
                <Options uuid={UUID} storeID={storeID} offers={offers}/>
            );
        else setOffersCommand("");
    };
    return (
        <li>
            <Card className="product-item">
                <div className="product-item__price">#{productID}</div>
                <div className="product-item__description">
                    <h2>{name}</h2>
                    <h2>counter Price: {props.counterPrice} </h2>
                    <h2>quantity : {props.quantity} </h2>
                    {props.status === "Waiting" ? (
                        <>
                            <button onClick={confirmHandler}>Confirm</button>
                            <button onClick={rejectHandler}>Reject</button>
                            <h2>Send qounter</h2>
                            <h2>
                                <input
                                    type="number"
                                    value={counter}
                                    onChange={counterHandler}
                                ></input>
                                <button onClick={sendHandler}>Send</button>
                            </h2>
                        </>
                    ) : (
                        <></>
                    )}
                    <div>
                        {props.status === "Counter" ? (
                            <>
                                <h2> status is Counter, waiting to client response</h2>

                            </>
                        ) : (
                            <></>
                        )}
                    </div>
                    <div>
                        {props.status === "Approved" ? (
                            <>

                                <h2>  status is Approved!</h2>

                            </>
                        ) : (
                            <></>
                        )}
                    </div>
                    <div>
                        {props.status === "Rejected" ? (
                            <>

                                <h2>  status is rejected!</h2>

                            </>
                        ) : (
                            <></>
                        )}
                    </div>


                    {/*<div className="BIDitem__control">*/}
                    {/*  <button onClick={clickHandler}>Offers</button>*/}
                    {/*</div>*/}
                </div>
            </Card>
            <Card>{offersCommand}</Card>
        </li>
    );
};

export default Offer;
