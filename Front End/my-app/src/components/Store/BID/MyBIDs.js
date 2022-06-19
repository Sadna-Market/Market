import React, { useState } from "react";
import BIDItem from "./BIDItem";
import Offer from "./Offer";
import Status from "./Status";

const MyBIDs = (props) => {
  console.log("MyBIDs")

  let UUID = props.uuid;
  let storeID = props.storeID;
  //todo: get all my products
  let products = [
    { id: 1, name: "Sony 5", amount: 10, status: "Waiting" },
    { id: 2, name: "TV", amount: 10, status: "Rejected" },
    { id: 3, name: "Car KIA RIO", amount: 10, status: "Counter" },
    { id: 4, name: "AC/DC", amount: 10, status: "Approved" },
    { id: 5, name: "MAMA Yokero", amount: 10, status: "Waiting" },
    { id: 6, name: "Iphone 13", amount: 10, status: "bought" },
  ];

  let productIDs = products.map((expense) => (
    <Status
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
        <ul className="products-list">{productIDs}</ul>
      </div>
    </div>
  );
};

export default MyBIDs;
