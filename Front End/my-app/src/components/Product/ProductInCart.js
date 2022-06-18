import React, {useState} from "react";
import Card from "../UI/Card";

const ProductInCart = props=>{
    console.log("ProductInCart")

    let UUID=props.uuid;
    let productID = props.id;
    let storeID = props.storeID;
    const [amount,setAmount] = useState(props.amount);

    //get the price and the amount
    let price=props.price;
    let name= "name";

    //todo:cencel this product from list
    const clickHandler = () => {
        props.onCancel(price*amount,storeID,productID)
        setAmount(0);

      };
      return (
        <li>
          <Card className="product-item">
            <div className="product-item__price">#{productID}</div>
            <div className="product-item__description">
              <h2>{name}</h2>
              <h2>price: {price}</h2>
              <h2>amount: {amount}</h2>
              <button onClick={clickHandler}>cencel</button>
            </div>
          </Card>
        </li>
      );
};

export default ProductInCart;