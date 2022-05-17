import React, { useState,useEffect } from "react";
import StoreID from "./StoreID";

const StoreList = (props) => {
  const [UUID, setUUID] = useState(props.uuid);
  let stores = [
    { id: 1, name: "a store" },
    { id: 2, name: "b store" },
    { id: 3, name: "c store" },
    { id: 4, name: "d store" },
    { id: 5, name: "e store" },
    { id: 6, name: "f store" },
    { id: 7, name: "g store" },
  ];
  if (stores.length === 0) {
    return <h2 className="stores-list__fallback">Found No Stores</h2>;
  }

  if (props.search != null && props.search != "") {
    stores = stores.filter((store) => store.id === props.search);
  }

  let expensesContent = stores.map((expense) => (
    <StoreID id={expense.id} name={expense.name} />
  ));

  const [searchStore, setStore] = useState("");


  return (
    <div>
      <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default StoreList;
