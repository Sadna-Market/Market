import React, {useState} from "react";



const ProductList = ()=>{
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
    if (product.length === 0) {
      return <h2 className="stores-list__fallback">Found No expenses</h2>;
    }
  
    // if (props.search != null && props.search != "") {
    //   stores = stores.filter((x) => x.id === props.search);
    // }
  
    let expensesContent = products.map((expense) => (
      <ProductID id={expense.id} name={expense.name} />
    ));
  
    const [searchStore, setStore] = useState("");
  
  
    return (
      <div>
        <ul className="products-list">{expensesContent}</ul>
      </div>
    );
};

export default ProductList;