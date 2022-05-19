import React, { useState } from "react";
import ProductInStoreList from "../Product/ProductInStoreList";

const Store = (props) => {
  let storeID = props.storeID;
  let UUID = props.uuid;

 // const [permission, setPermission] = useState("");
  let permission=""
  if (UUID == 7) {
      permission=
      <>
        <button> Add Product</button>
        <button> Remove Product</button>
        <button> Add Manager\Owner</button>
        <button> remove Manager\Owner</button>
      </>
    ;
  }

  //todo: get info
  let name = "Amazing Store";
  let founder = "Alvis Presly";
  let isOpen = "Open";
  let rate = "10";

  return (
    <div>
      <h3>{name}</h3>
      <h3>{isOpen}</h3>
      {permission}
      <h2>Founder: {founder}</h2>
      <h2>Rate: {rate}</h2>
      <div>
          <ProductInStoreList uuid={UUID} storeID={storeID}/>
      </div>
    </div>
  );
};

export default Store;
