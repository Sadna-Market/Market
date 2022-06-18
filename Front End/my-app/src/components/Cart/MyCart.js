import React, {useEffect, useState} from "react";
import BuyCart from "./BuyCart";

import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

import ProductInCart from "../Product/ProductInCart";

const MyCart = (props) => {
    console.log("MyCart")
    const [Cartproducts, SetProducts] = useState([]);
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    const [total, setTatal] = useState(0);

    let UUID = props.uuid;
    //todo: get all my products
    //getShoppingCart
    async function getMyCart() {
        let products = [];
        const getShoppingCartResponse = await apiClientHttp.getShoppingCart(UUID);
        console.log("start func getMyCart getShoppingCart")
        let str = JSON.stringify(getShoppingCartResponse);

        console.log("sendRulesResponse    " + str)

        if (getShoppingCartResponse.errorMsg !== -1) {
            SetError(errorCode.get(getShoppingCartResponse.errorMsg))
        } else {
            let shoppingBagHash = getShoppingCartResponse.value.shoppingBagHash
            for (const key in shoppingBagHash) { //key = storeID
                let store = (shoppingBagHash[key].store);//{"storeId":1,"name":"sys","founder":"sysManager@gmail.com","isOpen":true,"rate":5}
                let productQuantity = (shoppingBagHash[key].productQuantity); //{1:20, 2:50} product id and quantity
                for (const key in productQuantity) {
                    products.push({
                        id: key,
                        storeID:store.storeId,
                        // price: store.price,
                        // productName: store.productName,
                        price: 1,
                        amount: productQuantity[key],
                    })
                }
            }
            let sum = 0;
            for (let i = 0; i < products.length; i++) {
                sum += products[i].price * products[i].amount;
            }
            console.log(products)
            setTatal(sum)
            SetError("")
            SetProducts(products);
        }
    }

    useEffect(() => {
        getMyCart();
    }, [Cartproducts.refresh]);

    async function cancelHandler(price, storeid, productid) {
        console.log("removeProductFromShoppingBagResponse   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ")
        console.log("uuid,storeid,productid ",UUID,storeid,productid)

        // removeProductFromShoppingBag :async (uuid, storeid, productid) => {
        const removeProductFromShoppingBagResponse = await apiClientHttp.removeProductFromShoppingBag(UUID, storeid, productid);
        let str = JSON.stringify(removeProductFromShoppingBagResponse);
        console.log("removeProductFromShoppingBagResponse    " + str)
        if (removeProductFromShoppingBagResponse.errorMsg !== -1) {
            SetError(errorCode.get(removeProductFromShoppingBagResponse.errorMsg))
        } else {
            setTatal(total - price);
        }
    }

    let productIDs = Cartproducts.map((expense) => (
        <ProductInCart
            id={expense.id}
            // name={expense.name}
            storeID={expense.storeID}
            amount={expense.amount}
            price={expense.price}
            onCancel={cancelHandler}
            uuid={UUID}
        />
    ));
    const [command, setCommand] = useState(
        <div>
            {/*<ul className="products-list">{productIDs}</ul>*/}

            {/*<h3>Total Price: ${total}</h3>*/}
        </div>
    );

    const buyHandler = () => {
        setCommand(<BuyCart uuid={UUID} onMarket={() => props.onMarket()}/>);
    };

    return (
        <div>
            <div style={{
                color: 'black',
                position: 'relative',
                background: '#c51244',
                fontSize: 15
            }}>{enteredError}</div>
            <h3>My Cart</h3>
            <h3>Total Price: ${total}</h3>
            <button onClick={buyHandler}> Buy All The Cart</button>
            {command}
            <ul className="products-list">{productIDs}</ul>

        </div>
    );
};

export default MyCart;
