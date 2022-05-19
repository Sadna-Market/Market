import React, { useState } from "react";
import ProductList from "./ProductList";
import ProductType from "./ProductType";

const ProductButton = (props) => {
  
  // const [command, setCommand] = useState(<StoreList search={searchStore} />);
  // const searchButtonHolder = () => {
  //   setCommand(<StoreList search={searchStore} />);
  //   setStore(() => "");
  // };

  // if (props.clean === true) {
  //   setCommand(<StoreList search="" />);
  // }

  // const findStoreHandler = (event) => {
  //   setStore(() => event.target.value);
  // };
  const [searchProduct, setProduct] = useState("");
  const readMoreHandler = (productID) => {
    setBackCommand(command);
    setCommand(<ProductType productID={productID} />);
  };

  const [command, setCommand] = useState(
    <ProductList search="" onReadMore={readMoreHandler} />
  );
  const [backCommand, setBackCommand] = useState(command);

  const backHandler = () => {
    setCommand(backCommand);
  };

  const findProductHandler = (event) => {
    setProduct(() => event.target.value);
  };

  const searchHandler = () => {
    setBackCommand(command);
    setCommand(<ProductList search={searchProduct} onReadMore={readMoreHandler}/>);
    setProduct("");
  };

  return (
    <div className="marketButton">
      <h3>Products</h3>
      <div className="bar__controls">
        <div className="bar__control">
          <label>Search By ID</label>
          <input
            value={searchProduct}
            type="text"
            placeholder="Product ID"
            onChange={findProductHandler}
          />
          <button onClick={searchHandler}>Search</button>
          <button onClick={backHandler}>back</button>
        </div>
      </div>
      {command}
    </div>
  );
};

export default ProductButton;
