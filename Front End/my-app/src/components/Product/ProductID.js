import React, {useState} from "react";
import './ProductID.css';
import Card from "../UI/Card";

const ProductID = props=>{
    const clickHandler = () => {
      props.onReadMore(props.id);
    };
    return (
      <li>
        <Card className="product-item">
          <div className="product-item__price">#{props.id}</div>
          <div className="product-item__description">
            <h2></h2>
            <h2>{props.name}</h2>
            <button onClick={clickHandler}>read more</button>
          </div>
        </Card>
      </li>
    );
};

export default ProductID;