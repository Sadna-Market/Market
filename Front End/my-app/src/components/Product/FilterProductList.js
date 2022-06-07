import React from "react";
import ProductID from "./ProductID";

const FilterProductList = (props) => {
  console.log("FilterProductList")

  //todo: filter : props with Name,Desc,Category,Rate,StoreRate,Min,Max
  let products = [
    { id: 13, name: "a product" },
    { id: 2322, name: "b product" },
    { id: 34, name: "c product" },
    { id: 42, name: "d product" },
    { id: 533, name: "e product" },
    { id: 62, name: "f product" },
    { id: 73, name: "g product" },
  ];

  const readMoreHandler = productID =>{
    props.onReadMore(productID);
  };

  let productIDs = products.map((expense) => (
    <ProductID id={expense.id} name={expense.name} onReadMore={readMoreHandler} />
  ));

  return (
    <div>
      <h3>After Filters</h3>
      <ul className="products-list">{productIDs}</ul>
    </div>
  );
};

export default FilterProductList;
