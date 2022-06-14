import React, { useState, useEffect } from "react";
import StoreID from "../Store/StoreID";
import UserID from "./UserID";

const UserList = (props) => {
  console.log("UserList")

  let users = [
    { email: "a@gmail.com", State: "Connect" },
    { email: "b@gmail.com", State: "Disconnect" },
    { email: "c@gmail.com", State: "Connect" },
    { email: "d@gmail.com", State: "Connect" },
    { email: "e@gmail.com", State: "Disconnect" },
    { email: "h@gmail.com", State: "Disconnect" },
    { email: "e@gmail.com", State: "Disconnect" },
  ];
  if (users.length === 0) {
    return <h2 className="stores-list__fallback">Found No Stores</h2>;
  }

  if (props.search != "") {
    users = users.filter((user) => user.email === props.search);
  }

  const enterToStoreHandler = (storeID) => {
    props.onEnterToStore(storeID);
  };

  let expensesContent = users.map((expense) => (
    <UserID
      email={expense.email}
      state={expense.State}
      onEnterToStore={enterToStoreHandler}
    />
  ));

  //const [searchStore, setStore] = useState("");

  return (
    <div>
      <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default UserList;
