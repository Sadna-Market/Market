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
            SetError("")
            console.log(products)
            setallProducts(products)
        }
    }

    useEffect(() => {
        getAllProducts();
    }, [allProducts.refresh]);


    //
    // async function setSearch(){
    //     console.log("setSearch")
    //
    //     let allpro = allProducts.filter((product) => product.id === parseInt(props.search));
    //     setallProducts(allpro);
    //
    // }
    // if (props.search != ""){
    //     useEffect(() => {
    //         if (props.search != ""){
    //         setSearch();
    //         }
    //     }, [allProducts.refresh]);
    //
    // }

    if (allProducts.length === 0) {
        return <h2 className="stores-list__fallback">Found No expenses</h2>;
    }
    // let allpro='-1'
    // if (props.search != "") {
    //      allpro = allProducts.filter((product) => product.id === parseInt(props.search));
    //     // setallProducts(allpro)
    // }


    const readMoreHandler = productID => {
        props.onReadMore(productID);
    };

    let expensesContent = allProducts.map((expense) => (
        <ProductID id={expense.id} name={expense.name} onReadMore={readMoreHandler}/>
    ));

    // const [searchStore, setStore] = useState("");


    return (
        <div>
            <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>

            <ul className="products-list">{expensesContent}</ul>
        </div>
    );
};

export default ProductList;