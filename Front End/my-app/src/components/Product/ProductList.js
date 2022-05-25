import React, {useState} from "react";
import ProductID from "./ProductID";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const ProductList = props=>{
    console.log("ProductList")
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    const [UUID, setUUID] = useState(props.uuid);
    // let products = [
    //   { id: 1, name: "a product" },
    //   { id: 2, name: "b product" },
    //   { id: 3, name: "c product" },
    //   { id: 4, name: "d product" },
    //   { id: 5, name: "e product" },
    //   { id: 6, name: "f product" },
    //   { id: 7, name: "g product" },
    // ];
    let products = []
    async function getAllProducts() {
        const getAllProductsResponse = await apiClientHttp.getAllProducts();
        console.log("start func  getAllProducts")
        console.log(getAllProductsResponse)

        if (getAllProductsResponse.errorMsg !== -1) {
            SetError(errorCode.get(getAllProductsResponse.errorMsg))
        } else {
           // products.
            for (let i = 0; i < getAllProductsResponse.value.length; i++) {
                products.push({ id: getAllProductsResponse.value[i].productID, name:  getAllProductsResponse.value[i].productName })
            }
        }
    }
    getAllProducts();
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