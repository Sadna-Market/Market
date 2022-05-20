import React, { useState } from "react";

const EditPermission = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [email, setEmail] = useState("");
  const changeEmailHandler = (event) => {
    setEmail(event.target.value);
  };

  const [permission, setPermission] = useState("");
  const changePermissionHandler = (event) => {
    setPermission(event.target.value);
  };

  const cleanHandler = () => {
    setEmail("");
    setPermission("");
  };

  //todo
  //2.4.7
  // public SLResponsOBJ<Boolean> setManagerPermissions(String userId, int storeId, String
  //     mangerEmil, String per ,boolean onof);
  const editHandler = () => {
    cleanHandler();
    props.onStore();
  };

  return (
    <div>
      <h3>Edit Permission Store {storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Email</label>
          <input
            type="text"
            value={email}
            placeholder="write the Email"
            onChange={changeEmailHandler}
          />
        </div>
        <div className="products__control">
          <label>Permission</label>
          <input
            type="text"
            value={permission}
            placeholder="write the Permission"
            onChange={changePermissionHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={editHandler}>Edit Permission</button>
      </div>
    </div>
  );
};

export default EditPermission;
