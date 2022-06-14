import React,{useState} from "react";
import StoreList from "../Store/StoreList";
import UserList from "./UserList";



const Users = props =>{
    console.log("Users")

    let UUID= props.uuid;

    const [searchUser, setUser] = useState("");

    const enterToStoreHandler = storeID => {
      props.onShowStore(storeID);
    };
    const [command, setCommand] = useState(<UserList search={searchUser} onEnterToStore={enterToStoreHandler}/>);
  
    const searchButtonHolder = () => {
      setCommand(<UserList search={searchUser} onEnterToStore={enterToStoreHandler} />);
      setUser(() => "");
    };
  
  
  
    const findStoreHandler = (event) => {
        setUser(() => event.target.value);
    };
  
    return (
      <div className="marketButton">
        <h3>Users</h3>
        <div className="bar__controls">
          <div className="bar__control">
            <label>Search User</label>
            <input
              type="text"
              value={searchUser}
              onChange={findStoreHandler}
              placeholder="user Email"
            />
            <button onClick={searchButtonHolder}>Search</button>
          </div>
        </div>
        {command}
      </div>
    );
};

export default Users;