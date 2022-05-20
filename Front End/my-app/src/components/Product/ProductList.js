import React, {useState} from "react";
import ProductID from "./ProductID";


const ProductList = props=>{
    const [UUID, setUUID] = useState(props.uuid);
    let products = [
      { id: 1, name: "a product" },
      { id: 2, name: "b product" },
      { id: 3, name: "c product" },
      { id: 4, name: "d product" },
      { id: 5, name: "e product" },
      { id: 6, name: "f product" },
      { id: 7, name: "g product" },
    ];
    if (products.length === 0) {
      return <h2 className="stores-list__fallback">Found No expenses</h2>;
    }

    if (props.search != "") {
      products = products.filter((product) => product.id === parseInt(props.search));
    }

    const readMoreHandler = productID =>{
      props.onReadMore(productID);
    };
  
    let expensesContent = products.map((expense) => (
      <ProductID id={expense.id} name={expense.name} onReadMore={readMoreHandler} />
    ));
  
   // const [searchStore, setStore] = useState("");
  
  
    return (
      <div>
        <ul className="products-list">{expensesContent}</ul>
      </div>
    );
};

export default ProductList;