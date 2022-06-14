import React, { useState } from "react";
import CategoryDiscountRule from "./CategoryDiscountRule";
import ConditionCategoryDiscountRule from "./ConditionCategoryDiscountRule";
import ConditionProductDiscountRule from "./ConditionProductDiscountRule";
import ConditionStoreDiscountRule from "./ConditionStoreDiscountRule";
import ProductDiscountRule from "./ProductDiscountRule";
import SimpleDiscountRule from "./SimpleDisountRule";

const AddSimpleRule = (props) => {
    console.log("AddSimpleRule")

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
              <SimpleDiscountRule
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage={true}
              />
            );
          }}
        >
          Store
        </button>
        <button
          onClick={() => {
            setCommand(
              <ProductDiscountRule
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage={true}
              />
            );
          }}
        >
          Product
        </button>
        <button
          onClick={() => {
            setCommand(
              <CategoryDiscountRule
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage={true}
              />
            );
          }}
        >
          Category
        </button>
        <button
          onClick={() => {
            setCommand(
              <ConditionStoreDiscountRule
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage={true}
              />
            );
          }}
        >
          Condition on Store
        </button>
        <button
          onClick={() => {
            setCommand(
              <ConditionProductDiscountRule
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage={true}
              />
            );
          }}
        >
          Condition on Product
        </button>
        <button
          onClick={() => {
            setCommand(
              <ConditionCategoryDiscountRule
                  compose={props.compose}
                  uuid={UUID}
                storeID={storeID}
                onRule={(res) => props.onRule(res)}
                simplePage={true}
              />
            );
          }}
        >
          Condition on Category
        </button>
      </div>
    </>
  );

  const [command, setCommand] = useState(basicPage);
  return <div>{command}</div>;
};

export default AddSimpleRule;
