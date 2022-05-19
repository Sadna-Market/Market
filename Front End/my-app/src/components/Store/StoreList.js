import React, { useState, useEffect } from "react";
import StoreID from "./StoreID";

const StoreList = (props) => {
  let stores = [
    { id: 1, name: "a store", open: "Open" },
    { id: 2, name: "b store", open: "Open" },
    { id: 3, name: "c store", open: "Close" },
    { id: 4, name: "d store", open: "Open" },
    { id: 5, name: "e store", open: "Close" },
    { id: 6, name: "f store", open: "Open" },
    { id: 7, name: "g store", open: "Open" },
  ];
  if (stores.length === 0) {
    return <h2 className="stores-list__fallback">Found No Stores</h2>;
  }

  if (props.search != "") {
    stores = stores.filter((store) => store.id === parseInt(props.search));
  }

  const enterToStoreHandler = storeID => {
    props.onEnterToStore(storeID);
  };

  let expensesContent = stores.map((expense) => (
    <StoreID id={expense.id} name={expense.name} open={expense.open} onEnterToStore={enterToStoreHandler}/>
  ));

  //const [searchStore, setStore] = useState("");

  return (
    <div>
      <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default StoreList;
