import React, {useState} from "react";
import ProductInStore from "./ProductInStore";
import Card from "../UI/Card";


const ProductInStoreList = props =>{
    let products = [
      { id: 1, name: "a product" },
      { id: 2, name: "b product" },
      { id: 3, name: "c product" },
      { id: 4, name: "d product" },
      { id: 5, name: "e product" },
      { id: 6, name: "f product" },
      { id: 7, name: "g product" },
    ];


  
    let expensesContent = products.map((expense) => (
      <ProductInStore id={expense.id} UUID={props.uuid} storeID={props.storeID} />
    ));
  
   // const [searchStore, setStore] = useState("");
  
  
    return (
      <div>
        <ul className="products-list">{expensesContent}</ul>
      </div>
    );
};

export default ProductInStoreList;