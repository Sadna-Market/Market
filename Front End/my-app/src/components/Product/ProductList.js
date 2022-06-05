import React, {useState, useEffect} from "react";
import ProductID from "./ProductID";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const ProductList = props => {
    console.log("ProductList")
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    const [UUID, setUUID] = useState(props.uuid);
    const [allProducts, setallProducts] = useState([]);

    async function getAllProducts() {
        let products = []
        const getAllProductsResponse = await apiClientHttp.getAllProducts();
        console.log("start func  getAllProducts")
        console.log(getAllProductsResponse)

        if (getAllProductsResponse.errorMsg !== -1) {
            SetError(errorCode.get(getAllProductsResponse.errorMsg))
        } else {
            console.log("elseelseelseelseelseelseelseelseucts")

            // products.
            for (let i = 0; i < getAllProductsResponse.value.length; i++) {
                products.push({id: getAllProductsResponse.value[i].productID, name: getAllProductsResponse.value[i].productName})
            }
            console.log(products)
            setallProducts(products)
        }
    }

    useEffect(() => {
        getAllProducts();
    }, [allProducts.refresh]);

    // const [enteredstores, Setstores] = useState([]);
    //
    //
    // console.log("StoreList")
    //
    // async function getAllStores() {
    //     let stores = [];
    //
    //     const getAllStoresResponse = await apiClientHttp.getAllStores();
    //     console.log("start func  getAllStores")
    //
    //     if (getAllStoresResponse.errorMsg !== -1) {
    //         SetError(errorCode.get(getAllStoresResponse.errorMsg))
    //     } else {
    //         for (let i = 0; i < getAllStoresResponse.value.length; i++) {
    //             stores.push({
    //                 id: getAllStoresResponse.value[i].storeId,
    //                 name: getAllStoresResponse.value[i].name,
    //                 open: getAllStoresResponse.value[i].isOpen
    //             })
    //         }
    //         Setstores(stores);
    //     }
    // }
    //
    // useEffect(() => {
    //     getAllStores();
    // }, [enteredstores.refresh]);
    // console.log(enteredstores)
    // console.log(enteredstores.length)


    if (allProducts.length === 0) {
        return <h2 className="stores-list__fallback">Found No expenses</h2>;
    }

    if (props.search != "") {
        allProducts = allProducts.filter((product) => product.id === parseInt(props.search));
    }

    const readMoreHandler = productID => {
        props.onReadMore(productID);
    };

    let expensesContent = allProducts.map((expense) => (
        <ProductID id={expense.id} name={expense.name} onReadMore={readMoreHandler}/>
    ));

    // const [searchStore, setStore] = useState("");


    return (
        <div>
            <ul className="products-list">{expensesContent}</ul>
        </div>
    );
};

export default ProductList;