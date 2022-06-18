import React, { useState } from "react";
//import "./StoreID.css";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const UserID = (props) => {
  //todo: remove user
  console.log("UserID")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  async function clickHandler(){

    //   const removeUserResponse = await apiClientHttp.removeUser(props.uuid,props.email);
    //
    //   if (removeUserResponse.errorMsg !== -1) {
    //     SetError(errorCode.get(removeUserResponse.errorMsg))
    //   } else {
    //    SetError("")
    //    props.onEnterToStore(props.id);
    //   }


  }
  return (
      <li>
        <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
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
