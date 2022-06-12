import React from "react";
import "./MoreRule.css";

const MoreRule = (props) => {
    console.log("MoreRule")

    return (
    <div className="moreRule">
      <button onClick={() => props.onMore()} >Add more Rule</button>
      <button onClick={() => props.onFinish()}>Finish</button>
    </div>
  );
};

export default MoreRule;
