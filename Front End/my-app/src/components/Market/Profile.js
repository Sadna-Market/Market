import React, { useState } from "react";
import Password from "./Password";

const Profile = (props) => {
  let UUID = props.uuid;
  const [command, setCommand] = useState("");
  const changePassHandler = () => {
    setCommand(<Password uuid={UUID} onProfile={() => setCommand("")} />);
  };

  //todo History of User
  const historyHander = () => {

    setCommand("History Bla bla bla");
  };

  return (
    <div>
      <h3> My Profile </h3>
      <button onClick={changePassHandler}> Change Password</button>
      <button onClick={historyHander}> My History</button>
      <div>
        <h2>{command}</h2>
      </div>
    </div>
  );
};

export default Profile;
