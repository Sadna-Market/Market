import React from "react";
import Card from "../UI/Card";
import './InitMarket.css';

const InitMarket= props => {
    console.log("InitMarket")

    //todo: init
    const yesHolder =()=>{
        props.onInit();
    }

    const cencelHolder =()=>{
        props.onCencel();
    }

    return(
        <Card className="initMarket">
            <h3>Are you Sure?</h3>
            <button onClick={yesHolder}> Yes</button>
            <button onClick={cencelHolder}> Cancel</button>
        </Card>
    )
};

export default InitMarket;