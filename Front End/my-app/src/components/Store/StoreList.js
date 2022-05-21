import React, { useState, useEffect } from "react";
import StoreID from "./StoreID";
import createApiClientHttp from "../../client/clientHttp.js";

const StoreList = (props) => {
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    let stores = [{ id: 1, name: "a store", open: "Open" }];
    console.log("check1")
    console.log("3")

    async function getAllStores() {
        console.log("check111111111111111")

        const getAllStoresResponse = await apiClientHttp.getAllStores();
        console.log(getAllStoresResponse)

        if (getAllStoresResponse.errorMsg !== -1) {
            SetError(SetError.errorMsg)
        } else {
            console.log("addNewMemberResponse.value  " + getAllStoresResponse.value)
            let stores = [
                { id: 1, name: "a store", open: "Open" },
                { id: 2, name: "b store", open: "Open" },
                { id: 3, name: "c store", open: "Close" },
                { id: 4, name: "d store", open: "Open" },
                { id: 5, name: "e store", open: "Close" },
                { id: 6, name: "f store", open: "Open" },
                { id: 7, name: "g store", open: "Open" },]
        }
    }
    getAllStores();
    console.log("check12223333")

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
        <h3>{enteredError}</h3>
        <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default StoreList;
