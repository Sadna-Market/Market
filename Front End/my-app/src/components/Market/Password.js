import React, { useState } from "react";

const Password = (props) => {
  let UUID = props.uuid;

  const [email, setEmail] = useState("");
  const changeEmailHandler = (event) => {
    setEmail(event.target.value);
  };
  const [pass1, setpass1] = useState("");
  const changePass1Handler = (event) => {
    setpass1(event.target.value);
  };
  const [pass2, setpass2] = useState("");
  const changePsss2Handler = (event) => {
    setpass2(event.target.value);
  };

  const cleanHandler=()=>{
      setEmail("");
      setpass1("");
      setpass2("");
  }

  //todo: change thr password
  const confirmHandler= ()=>{
      cleanHandler();
      props.onProfile();
  };
  return (
    <div>
      <h3>Change Password</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Email</label>
          <input
            type="text"
            value={email}
            placeholder="Write Email"
            onChange={changeEmailHandler}
          />
        </div>
        <div className="products__control">
          <label>Old Password </label>
          <input
            type="text"
            value={pass1}
            placeholder="Write Old Password"
            onChange={changePass1Handler}
          />
        </div>
        <div className="products__control">
          <label>New Password </label>
          <input
            type="text"
            value={pass2}
            placeholder="Write NEw Password"
            onChange={changePsss2Handler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
      <button onClick={confirmHandler}>Confirm</button>
      </div>
    </div>
  );
};

export default Password;