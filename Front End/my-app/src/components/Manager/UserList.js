import React, { useState, useEffect } from "react";
import StoreID from "../Store/StoreID";
import UserID from "./UserID";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const UserList = (props) => {
  console.log("UserList")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  const [enteredusers, Setusers] = useState([]);

  console.log("StoreList")

  async function getAllUsers() {
    let users = [];

    const getAllUsersInTheSystemResponse = await apiClientHttp.getAllusers(props.uuid);

    let str = JSON.stringify(getAllUsersInTheSystemResponse);
    console.log("getAllUsersInTheSystemResponse    "+str)


    if (getAllUsersInTheSystemResponse.errorMsg !== -1) {
      SetError(errorCode.get(getAllUsersInTheSystemResponse.errorMsg))
    } else {
      for (let i = 0; i < getAllUsersInTheSystemResponse.value.length; i++) {
        if (getAllUsersInTheSystemResponse.value[i].isLogged){
          users.push({
            State: 'Connect',
            email: getAllUsersInTheSystemResponse.value[i].email,
          })
        }
        else {
          users.push({
            State: 'Disconnect',
            email: getAllUsersInTheSystemResponse.value[i].email,
          })
        }


      }
      SetError("")
      Setusers(users);
    }
  }

  useEffect(() => {
      getAllUsers();
  }, [enteredusers.refresh]);


  if (enteredusers.length === 0) {
    return <h2 className="stores-list__fallback">Found No Stores</h2>;
  }

  // if (props.search != "") {
  //   users = users.filter((user) => user.email === props.search);
  // }

  const enterToStoreHandler = (storeID) => {
    getAllUsers();
    // props.onEnterToStore(storeID);
  };

  let expensesContent = enteredusers.map((expense) => (
      <UserID
          email={expense.email}
          state={expense.State}
          onEnterToStore={enterToStoreHandler}
          uuid={props.uuid}
      />
  ));

  //const [searchStore, setStore] = useState("");

  return (
      <div>
        <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

        <ul className="stores-list">{expensesContent}</ul>
      </div>
  );
};

export default UserList;
