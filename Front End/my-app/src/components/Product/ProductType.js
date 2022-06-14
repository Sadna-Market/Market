import React,{useState} from "react";
import Card from "../UI/Card";
import "./ProductType.css";

const ProductType = (props) => {
    console.log("ProductType")

    let productID = props.productID;
  const [rateMe,setRate] = useState("");

  const rateHandler = event=>{
      setRate(event.target.value);
  };

  //todo: rate product
  const rateClickHandler = ()=>{
      setRate("");
  }

  //todo:get the info of the product
  let product = {
    name: "Hary Potter",
    description: "amazing",
    category: "Books",
    rate: 5,
  };
  return (
    <Card>
      <div className="productType">
        <div>
          <h3>Product Number: {productID}</h3>
        </div>
        <div>
          <h3>Name: {product.name}</h3>
        </div>
        <div>
          <h3>Description: {product.description}</h3>
        </div>
        <div>
          <h3>Category: {product.category}</h3>
        </div>
        <div>
          <h3>Rate: {product.rate}</h3>
        </div>
        <div className="bar__control">
            <label>Rate Product</label>
            <input
              type="number"
              min="0"
              max="10"
              value={rateMe}
              onChange={rateHandler}
              placeholder="1-10"
            />
            <button onClick={rateClickHandler}>Rate</button>
      </div>
      </div>
    </Card>
  );
};

export default ProductType;
