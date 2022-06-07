import React, { useState } from "react";
//import "./StoreID.css";
import Card from "../UI/Card";

const UserID = (props) => {
  //todo: remove user
  console.log("UserID")

  const clickHandler = () => {

    props.onEnterToStore(props.id);
    
  };
  return (
    <li>
      <Card className="store-item">
        <div className="store-item__description">
          <h2>{props.email}</h2>
          <h2>{props.state}</h2>
          <button onClick={clickHandler}>Remove User</button>
        </div>
      </Card>
    </li>
  );
};

export default UserID;
