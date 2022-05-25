import React, {useState} from "react";
import Users from "./Users";
import ConnectUsers from "./Users";
import DisConnectUsers from "./DisConnectUsers";
import NewProductType from "./NewProductType";
import RemoveUser from "./RemoveUser";
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
