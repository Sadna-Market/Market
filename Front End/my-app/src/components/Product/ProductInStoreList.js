import React, {useEffect, useState} from "react";
import ProductInStore from "./ProductInStore";
import Card from "../UI/Card";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"


const ProductInStoreList = props =>{
    console.log("ProductInStoreList")
    console.log("props.uuid "+props.uuid)

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
                    id: getAllProductsInStoreResponse.value[i].itemID,
                    name: getAllProductsInStoreResponse.value[i].name,
                    price: getAllProductsInStoreResponse.value[i].price,
                    quantity: getAllProductsInStoreResponse.value[i].quantity

                })
            }

            console.log(":allProducts. " +allProducts)
            let str = JSON.stringify(getAllProductsInStoreResponse);
            console.log(":str. " + str)

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
    //    //str. {"errorMsg":-1,"value":[{"quantity":10,"price":10,"itemID":1,"name":null}]}

    let expensesContent = products.map((expense) => (
      <ProductInStore id={expense.id} UUID={props.uuid} storeID={props.storeID} name={expense.name}  quantity={expense.quantity} price={expense.price}/>
    ));
  
   // const [searchStore, setStore] = useState("");
  
  
    return (
      <div>
        <ul className="products-list">{expensesContent}</ul>
      </div>
    );
};

export default ProductInStoreList;