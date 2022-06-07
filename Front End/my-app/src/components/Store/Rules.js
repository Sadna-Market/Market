import React from "react";

const Rules = (props) => {
    console.log("Rules" )

    let UUID = props.uuid;
  let storeID = props.storeID;

  //todo: return what return from this function
  return (
    <div>
      <h3>
        Hello World! {UUID} {storeID}
      </h3>
    </div>
  );
};
export default Rules;
