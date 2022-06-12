import React, { useState } from "react";

import Offer from "./Offer";
import Option from "./Option";

const Options = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  //todo: get all my products
  let offers = [
    { name: "Yaki@gail.com", offer: 20 },
    { name: "Niv@gail.com", offer: 550 },
    { name: "Liel@gail.com", offer: 320 },
    { name: "Dor@gail.com", offer: 220 },
    { name: "Daniel@gail.com", offer: 220 },
  ];

  offers = offers.slice(0, props.offers);

  let productIDs = offers.map((expense) => (
    <Option
      name={expense.name}
      offer={expense.offer}
      uuid={UUID}
      storeID={storeID}
    />
  ));

  return (
    <div>
      <ul className="products-list">{productIDs}</ul>
    </div>
  );
};

export default Options;
