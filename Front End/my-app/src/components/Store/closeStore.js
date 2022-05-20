import React from "react";
import "./CloseStore.css";
import Card from "../UI/Card";

const CloseStore = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  //close store
  const yesHolder = () => {
    props.onMarket();
  };

  const cencelHolder = () => {
    props.onMarket();
  };

  return (
    <Card className="close">
      <h3>Are you Sure?</h3>
      <button onClick={yesHolder}> Yes</button>
      <button onClick={cencelHolder}> Cancel</button>
    </Card>
  );
};

export default CloseStore;
