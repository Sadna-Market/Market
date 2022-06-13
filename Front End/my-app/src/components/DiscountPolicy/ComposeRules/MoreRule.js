import React from "react";
import "./MoreRule.css";

const MoreRule = (props) => {
  return (
    <div className="moreRule">
      <button onClick={() => props.onMore()}>Add more Rule</button>
      {props.continue ? (
        <button onClick={() => props.onFinish()}>Continue</button>
      ) : (
        <button onClick={() => props.onFinish()}>Finish</button>
      )}
    </div>
  );
};

export default MoreRule;
