import React, {useState} from "react";
import Users from "./Users";
import NewProductType from "./NewProductType";

import UserList from "./UserList";

const ManagerButton = (props) => {
  let UUID = props.uuid;
  const [command,setCommand] = useState("");


  const addProductHandler = () => {
      setCommand(<NewProductType />);
  };

  const connectUsersHandler = () => {
    setCommand(<Users uuid={UUID} />);
};


  return (
    <div>
      <div>
        <h3>Manager</h3>
        <button onClick={addProductHandler}>Add Product to Market</button>
        <button onClick={connectUsersHandler}>Users</button>
      </div>
      <div>
          {command}
      </div>
    </div>
  );
};

export default ManagerButton;
