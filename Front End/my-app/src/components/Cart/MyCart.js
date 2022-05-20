import React, { useState } from "react";
import BuyCart from "./BuyCart";

import ProductInCart from "../Product/ProductInCart";

const MyCart = (props) => {
  let UUID = props.uuid;
  //todo: get all my products
  let products = [
    { id: 1, price: Math.random() * 100, amount: 3, name: "a product" },
    { id: 2, price: Math.random() * 100, amount: 4, name: "b product" },
    { id: 3, price: Math.random() * 100, amount: 3, name: "c product" },
    { id: 4, price: Math.random() * 100, amount: 7, name: "d product" },
    { id: 5, price: Math.random() * 100, amount: 1, name: "e product" },
    { id: 6, price: Math.random() * 100, amount: 12, name: "f product" },
  ];
  let sum = 0;
  for (let i = 0; i < products.length; i++) {
    sum += products[i].price * products[i].amount;
  }

  const [total, setTatal] = useState(sum);

  const cancelHandler = (price) => {
    setTatal(total - price);
  };

  let productIDs = products.map((expense) => (
    <ProductInCart
      id={expense.id}
      name={expense.name}
      amount={expense.amount}
      price={expense.price}
      onCancel={cancelHandler}
      uuid={UUID}
    />
  ));
  const [command, setCommand] = useState(
    <div>
      <ul className="products-list">{productIDs}</ul>

      <h3>Total Price: ${total}</h3>
    </div>
  );

  const buyHandler = () => {
    setCommand(<BuyCart uuid={UUID} onMarket={()=>props.onMarket()}/>);
  };

  return (
    <div>
      <h3>My Cart</h3>
      <button onClick={buyHandler}> Buy All The Cart</button>
      {command}
    </div>
  );
};

export default MyCart;
