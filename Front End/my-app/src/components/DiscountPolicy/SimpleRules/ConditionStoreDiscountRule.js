import React, {useState} from "react";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"
import * as RulesClass from "../../RulesHelperClasses/DiscountRules"

const ConditionStoreDiscountRule = (props) => {
    console.log("ConditionStoreDiscountRule")
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    let UUID = props.uuid;
    let storeID = props.storeID;

    const [discount, setDiscount] = useState("");
    const changeDiscountHandler = (event) => {
        setDiscount(event.target.value);
    };

    const [totalPrice, setTotalPrice] = useState("");
    const changeTotalPriceHandler = (event) => {
        setTotalPrice(event.target.value);
    };

    const [minQuantity, setminQuantity] = useState("");
    const changeMinQuantityHandler = (event) => {
        setminQuantity(event.target.value);
    };

    const [minProductTypes, setminProductTypes] = useState("");
    const changeMaxQuanHandler = (event) => {
        setminProductTypes(event.target.value);
    };

    const cleanHandler = () => {
        SetError("")
        setDiscount("");
        setTotalPrice("");
        setminProductTypes("");
        setminQuantity("");
    };

    //todo: add new Category Discout rule
    async function addHandler() {
        let rule = new RulesClass.conditionOnStoreDiscount(UUID, storeID, discount, minQuantity, minProductTypes, totalPrice)
        let str = JSON.stringify(rule);
        console.log("sendRulesResponse    "+str)

        if (props.compose === undefined) { //false case - no comopse - realy simple
            const sendRulesResponse = await apiClientHttp.addNewDiscountRule(UUID,storeID, {'conditionOnStoreDiscount':rule});

            if (sendRulesResponse.errorMsg !== -1) {
                SetError(errorCode.get(sendRulesResponse.errorMsg))
            } else {
                cleanHandler();
                // props.onRule(sendRulesResponse.value);
                props.onRule(-1);

            }
        } else {
            cleanHandler();
            props.onRule({'conditionOnStoreDiscount':rule});
        }
    }

    return (
        <div>
            <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
            <h3>Category Discount For store #{storeID}</h3>
            <div className="products__controls">
                <div className="products__control">
                    <label>Discount</label>
                    <input
                        type="number"
                        min="0"
                        value={discount}
                        placeholder="write number between 1-100%"
                        onChange={changeDiscountHandler}
                    />
                </div>
                <div className="products__control">
                    <label>Min Quantity</label>
                    <input
                        type="number"
                        min="0"
                        value={minQuantity}
                        placeholder="write Minimum Quantity"
                        onChange={changeMinQuantityHandler}
                    />
                </div>
                <div className="products__control">
                    <label>Min Product Types</label>
                    <input
                        type="number"
                        min="0"
                        value={minProductTypes}
                        placeholder="write Min Product Types"
                        onChange={changeMaxQuanHandler}
                    />
                </div>
                <div className="products__control">
                    <label>Total Price</label>
                    <input
                        type="number"
                        min="0"
                        value={totalPrice}
                        placeholder="Write Total Price"
                        onChange={changeTotalPriceHandler}
                    />
                </div>
                <button onClick={cleanHandler}>Clean</button>
                <button onClick={addHandler}>Add Rule</button>
            </div>
        </div>
    );
};

export default ConditionStoreDiscountRule;
