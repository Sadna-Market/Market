import React, {useState} from "react";
import './Product.css';



const ProductID = props=>{
    const [amount, setTAmount] = useState(0);
    const clickHandler = () => setTAmount(amount + 1);
    if (amount === 0) {
    }
    const cancelHandler = () => setTAmount(0);
    return (
      <li>
        <Card className="product-item">
          <div className="product-item__price">#{props.id}</div>
          <div className="product-item__description">
            <h2>{props.name}</h2>
            <button onClick={clickHandler}>read more</button>
          </div>
        </Card>
      </li>
    );
};

export default ProductID;