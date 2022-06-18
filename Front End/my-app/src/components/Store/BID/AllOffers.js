import React, { useState } from "react";
import BIDItem from "./BIDItem";
import Offer from "./Offer";

const AllOffers = (props) => {
  console.log("AllOffers")
  let UUID = props.uuid;
  //todo: get all my products
  let products = [
    { id: 1, name: "Sony 5", offers: 2, managers:2},
    { id: 2, name: "TV", offers: 3 , managers:1},
    { id: 3, name: "Car KIA RIO", offers: 1, managers:3},
    { id: 4, name: "AC/DC", offers: 2 , managers:3},
    { id: 5, name: "MAMA Yokero", offers: 3 , managers:0},
    { id: 6, name: "Iphone 13", offers: 2 , managers:1},
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
    <Offer
      id={expense.id}
      name={expense.name}
      offers={expense.offers}
      onCancel={cancelHandler}
      uuid={UUID}
      managers={expense.managers}
    />
  ));

  return (
    <div>
      <h3>Offers</h3>
      <div>
        <ul className="products-list">{productIDs}</ul>
      </div>
    </div>
  );
};

export default AllOffers;
