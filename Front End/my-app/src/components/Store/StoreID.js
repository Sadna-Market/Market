import React, { useState } from "react";
import "./StoreID.css";
import Card from "../UI/Card";

const StoreID = (props) => {
  console.log("StoreID" +props.open+"1")
  // const [enteredOpenCloseStore,SetpenCloseStore ] = useState(props.open);
  let openOrClose=''
  if (props.open==false){
    openOrClose="close"
  }
  else {
    openOrClose="open"

  }


  const clickHandler = () => {
    props.onEnterToStore(props.id);
  };
  return (
    <li>
      <Card className="store-item">
        <div className="store-item__price">#{props.id}</div>
        <div className="store-item__description">
          <h2>{props.name}</h2>
          <h2>store is {openOrClose}</h2>
          <button onClick={clickHandler}>inter to the store</button>
        </div>
      </Card>
    </li>
  );
};

export default StoreID;
