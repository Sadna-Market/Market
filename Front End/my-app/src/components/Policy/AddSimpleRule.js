import React, { useState } from "react";
import CategoryRule from "./simpleBuyingRules/CategoryRule";
import ProductRule from "./simpleBuyingRules/ProductRule";
import ShoppingBagRule from "./simpleBuyingRules/ShoppingBagRules";
import UserRule from "./simpleBuyingRules/UserRule";

const AddSimpleRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;
  const [command, setCommand] = useState("");
  return (
    <div>
      <h3>Simple Rules</h3>
      <div>
        <button
          onClick={() => {
            setCommand(
              <ShoppingBagRule
                uuid={UUID}
                storeID={storeID}
                onRule={() => props.onRule()}
              />
            );
          }}
        >
          Shopping Bug
        </button>
        <button
          onClick={() => {
            setCommand(
              <ProductRule
                uuid={UUID}
                storeID={storeID}
                onRule={() => props.onRule()}
              />
            );
          }}
        >
          Product
        </button>
        <button
          onClick={() => {
            setCommand(
              <CategoryRule
                uuid={UUID}
                storeID={storeID}
                onRule={() => props.onRule()}
              />
            );
          }}
        >
          Category
        </button>
        <button
          onClick={() => {
            setCommand(
              <UserRule
                uuid={UUID}
                storeID={storeID}
                onRule={() => props.onRule()}
              />
            );
          }}
        >
          User
        </button>
      </div>
      <div>{command}</div>
    </div>
  );
};

export default AddSimpleRule;
