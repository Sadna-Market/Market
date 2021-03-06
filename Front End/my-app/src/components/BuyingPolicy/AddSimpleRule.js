import React, { useState } from "react";
import CategoryRule from "./simpleBuyingRules/CategoryRule";
import ProductRule from "./simpleBuyingRules/ProductRule";
import ShoppingBagRule from "./simpleBuyingRules/ShoppingBagRules";
import UserRule from "./simpleBuyingRules/UserRule";

const AddSimpleRule = (props) => {
    console.log("buying policy " + "AddSimpleRule ")

   let UUID = props.uuid;
  let storeID = props.storeID;
  let basicPage = (
    <>
      {props.simplePage ? (
        <div>
          <h3>Simple Rules</h3>
        </div>
      ) : (
        <></>
      )}
      <div>
        <button
          onClick={() => {
            setCommand(
              <ShoppingBagRule
                 compose={props.compose}
                 uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage = {true}
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
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage = {true}
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
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage = {true}
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
                compose={props.compose}
                uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage = {true}
              />
            );
          }}
        >
          User
        </button>
      </div>
    </>
  );

  const [command, setCommand] = useState(basicPage);
  return <div>{command}</div>;
};

export default AddSimpleRule;
