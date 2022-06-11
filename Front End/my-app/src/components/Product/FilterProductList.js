import React, {useEffect, useState} from "react";
import ProductID from "./ProductID";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"
const FilterProductList = (props) => {
  console.log("FilterProductList")
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");
  const [allProducts, setallProducts] = useState([]);
  let getAllFilterProductsResponse=null;

  //todo: filter : props with Name,Desc,Category,Rate,StoreRate,Min,Max
  async function getAllProducts() {
    let products = []
    const getAllProductsResponse = await apiClientHttp.getAllProducts();

    if (getAllProductsResponse.errorMsg !== -1) {
      SetError(errorCode.get(getAllProductsResponse.errorMsg))
    } else {

      // products.
      for (let i = 0; i < getAllProductsResponse.value.length; i++) {
        products.push(getAllProductsResponse.value[i].productID)
      }
    }

    if (props.filter.Name!==''){
      const getAllFilterProductsResponse = await apiClientHttp.filterByName(products,props.filter.Name);
      console.log("props.filter.Name "+props.filter.Name)

    }
    else if (props.filter.Desc!==''){
      const getAllFilterProductsResponse = await apiClientHttp.filterByDesc(products,props.filter.Desc);
      console.log("props.filter.Desc "+props.filter.Desc)

    }
    else if (props.filter.Category!==''){
      console.log("props.filter.category "+props.filter.Category)
      const getAllFilterProductsResponse = await apiClientHttp.filterByCategory(products,props.filter.Category);

    }
    else if (props.filter.Rate!==''){
      console.log("props.filter.rate "+props.filter.Rate)

      const getAllFilterProductsResponse = await apiClientHttp.filterByRate(products,props.filter.Rate);

    }
    else if (props.filter.StoreRate!==''){
      const getAllFilterProductsResponse = await apiClientHttp.filterByStoreRate(products,props.filter.StoreRate);
      console.log("props.filter.StoreRate "+props.filter.StoreRate)

    }
    else if (props.filter.Min!=='' && props.filter.max!==''){
      const getAllFilterProductsResponse = await apiClientHttp.filterByRangePrices(products,props.filter.Min,props.filter.max);
      console.log("props.filter.Min "+props.filter.Min+ 'MAX' + props.filter.max)

    }
    let filterProducts = []


    if (getAllFilterProductsResponse!==null&& getAllFilterProductsResponse.errorMsg !== -1) {
      SetError(errorCode.get(getAllFilterProductsResponse.errorMsg))
    } else {
      // products.
      for (let i = 0; i < getAllFilterProductsResponse.value.length; i++) {
        filterProducts.push({id: getAllFilterProductsResponse.value[i].productID, name: getAllFilterProductsResponse.value[i].productName})
      }
      setallProducts(filterProducts)
    }
  }

  useEffect(() => {
    getAllProducts();
  }, [allProducts.refresh]);

  // let products = [
  //   { id: 13, name: "a product" },
  //   { id: 2322, name: "b product" },
  //   { id: 34, name: "c product" },
  //   { id: 42, name: "d product" },
  //   { id: 533, name: "e product" },
  //   { id: 62, name: "f product" },
  //   { id: 73, name: "g product" },
  // ];

  const readMoreHandler = productID =>{
    props.onReadMore(productID);
  };

  let productIDs = allProducts.map((expense) => (
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
