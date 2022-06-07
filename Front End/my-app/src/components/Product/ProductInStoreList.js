import React, {useEffect, useState} from "react";
import ProductInStore from "./ProductInStore";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"


const ProductInStoreList = props =>{
    console.log("ProductInStoreList")

    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    const [products, Setproducts] = useState([]);

    async function getAllProducts() {
        let allProducts = [];

        const getAllProductsInStoreResponse = await apiClientHttp.getAllProductsInStore(props.storeID);
        console.log("start func  getAllProducts")
        console.log(getAllProductsInStoreResponse)

        if (getAllProductsInStoreResponse.errorMsg !== -1) {
            SetError(errorCode.get(getAllProductsInStoreResponse.errorMsg))
        } else {
            for (let i = 0; i < getAllProductsInStoreResponse.value.length; i++) {
                allProducts.push({
                    id: getAllProductsInStoreResponse.value[i].storeId,
                    name: getAllProductsInStoreResponse.value[i].name,
                    open: getAllProductsInStoreResponse.value[i].isOpen
                })
            }
            Setproducts(allProducts);
        }
    }

    useEffect(() => {
        getAllProducts();
    }, [products.refresh]);

    // let products = [
    //   { id: 1, name: "a product" },
    //   { id: 2, name: "b product" },
    //   { id: 3, name: "c product" },
    //   { id: 4, name: "d product" },
    //   { id: 5, name: "e product" },
    //   { id: 6, name: "f product" },
    //   { id: 7, name: "g product" },
    // ];

//getAllProductsInStore

    let expensesContent = products.map((expense) => (
      <ProductInStore id={expense.id} UUID={props.uuid} storeID={props.storeID} name={expense.name} />
    ));
  
   // const [searchStore, setStore] = useState("");
  
  
    return (
      <div>
        <ul className="products-list">{expensesContent}</ul>
      </div>
    );
};

export default ProductInStoreList;