import React, { useState, useEffect } from "react";
import StoreID from "./StoreID";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const StoreList = (props) => {
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    const [enteredstores, Setstores] = useState([]);
    const [enteredsearch, Setsearch] = useState(props.search);


    console.log("StoreList")

    async function getAllStores() {
        let stores = [];

        const getAllStoresResponse = await apiClientHttp.getAllStores();
        console.log("start func  getAllStores")

        if (getAllStoresResponse.errorMsg !== -1) {
            SetError(errorCode.get(getAllStoresResponse.errorMsg))
        } else {
            for (let i = 0; i < getAllStoresResponse.value.length; i++) {
                stores.push({
                    id: getAllStoresResponse.value[i].storeId,
                    name: getAllStoresResponse.value[i].name,
                    open: getAllStoresResponse.value[i].isOpen
                })

            }
            console.log("storesSSSS "+stores)

            Setstores(stores);
        }
    }

    useEffect(() => {
        getAllStores();
    }, [enteredstores.refresh]);
    console.log(enteredstores)
    console.log(enteredstores.length)

    console.log("check12223333")

    if (enteredstores.length === 0) {
    return <h2 className="stores-list__fallback">Found No Stores</h2>;
  }

  if (enteredsearch != "") {
      Setsearch("")
      console.log("props.search != \"\" "+props.search)
      console.log("enteredstores "+enteredstores)
      Setstores(enteredstores.filter((store) => store.id === parseInt(props.search)));
  }

  const enterToStoreHandler = storeID => {
    props.onEnterToStore(storeID);
  };

  let expensesContent = enteredstores.map((expense) => (
    <StoreID id={expense.id} name={expense.name} open={expense.open} onEnterToStore={enterToStoreHandler}/>
  ));

  //const [searchStore, setStore] = useState("");

  return (
    <div>
        <div style={{ color: 'red' , fontsize:'29' }}>{enteredError}</div>
        <ul className="stores-list">{expensesContent}</ul>
    </div>
  );
};

export default StoreList;
