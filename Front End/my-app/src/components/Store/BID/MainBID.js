import React, {useEffect, useState} from "react";
import BIDItem from "./BIDItem";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
const MainBID = (props) => {
  console.log("MainBID")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  const [products, Setproducts] = useState([]);

  let UUID = props.uuid;
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
      SetError("")
      Setproducts(allProducts);
    }
  }

  useEffect(() => {
    getAllProducts();
  }, [products.refresh]);

  let sum = 0;
  for (let i = 0; i < products.length; i++) {
    sum += products[i].price * products[i].amount;
  }

  const [total, setTatal] = useState(sum);

  const cancelHandler = (price) => {
    setTatal(total - price);
  };

  let productIDs = products.map((expense) => (
    <BIDItem
      id={expense.id}
      name={expense.name}
      price={expense.price}
      onCancel={cancelHandler}
      uuid={UUID}
    />
  ));

  return (
    <div>
      <ul className="products-list">{productIDs}</ul>
    </div>
  );
};

export default MainBID;
