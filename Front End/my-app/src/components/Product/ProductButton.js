import React, { useState } from "react";
import ProductList from "./ProductList";
import ProductType from "./ProductType";
import FilterProducts from "./filterProducts";
import FilterProductList from "./FilterProductList";

const ProductButton = (props) => {
  console.log("ProductButton")

  console.log("asdasddasasddasda ",props.uuid)

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
    setCommand(<ProductType productID={productID} uuid={props.uuid} />);
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

  const afterFilterHandler= data =>{
      setCommand(<FilterProductList filter={data} onReadMore={readMoreHandler} />)
  };

  const filterHandler = ()=>{
      setCommand(<FilterProducts onFilter={afterFilterHandler}/>);
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
          <button onClick={filterHandler}>Filter</button>
          <button onClick={backHandler}>Back</button>
        </div>
      </div>
      {command}
    </div>
  );
};

export default ProductButton;
