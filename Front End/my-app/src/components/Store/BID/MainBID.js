import React, { useState } from "react";
import BIDItem from "./BIDItem";

const MainBID = (props) => {
  let UUID = props.uuid;
  //todo: get all my products
  let products = [
    { id: 1, name: "Sony 5", price: Math.random() * 100 },
    { id: 2, name: "TV", price: Math.random() * 100 },
    { id: 3, name: "Car KIA RIO", price: Math.random() * 100 },
    { id: 4, name: "AC/DC", price: Math.random() * 100, amount: 7 },
    { id: 5, name: "MAMA Yokero", price: Math.random() * 100 },
    { id: 6, name: "Iphone 13", price: Math.random() * 100 },
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
