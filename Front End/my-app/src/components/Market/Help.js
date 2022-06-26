import React, { useState } from "react";

const Help = (props) => {
    console.log("Help")

    let UUID = props.uuid;

  const [help, setHelp] = useState("");
  const changeHelpHandler = (event) => {
    setHelp(event.target.value);
  };

  const cleanHandler = () => {
    setHelp("");
  };

  //todo: help
  const sendHandler = () => {
    cleanHandler();
    props.onHelp("I need somebody!");
  };

  return (
    <div>
      <h3>Help</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>What your problem?</label>
          <input
            type="text"
            value={help}
            placeholder="Tell us!"
            onChange={changeHelpHandler}
          />
        </div>
      </div>

      <div>
      <button onClick={cleanHandler}>Clean</button>
        <button onClick={sendHandler}>Send</button>
      </div>
    </div>
  );
};

export default Help;
