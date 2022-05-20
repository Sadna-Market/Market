import React, {useState} from "react";
import NewProductType from "./NewProductType";

const ManagerButton = (props) => {
  let UUID = props.uuid;
  const [command,setCommand] = useState("");


  const addProductHandler = () => {
      setCommand(<NewProductType />);
  };

  return (
    <div>
      <div>
        <h3>Manager</h3>
        <button onClick={addProductHandler}>Add Product to Market</button>
      </div>
      <div>
          {command}
      </div>
    </div>
  );
};

export default ManagerButton;
