import React, {useState} from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const RemoveProduct = (props) => {
    console.log("RemoveProduct" )

    let UUID = props.uuid;
    let storeID = props.storeID;
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    const [productID, setProductID] = useState("");
    const changeProductHandler = (event) => {
        setProductID(event.target.value);
    };

    const cleanHandler = () => {
        SetError("")
        setProductID("");
    };


    async function removeHandler() {
        const deleteProductFromStoreResponse = await apiClientHttp.deleteProductFromStore(UUID, storeID, productID);
        if (deleteProductFromStoreResponse.errorMsg !== -1) {
            SetError(errorCode.get(deleteProductFromStoreResponse.errorMsg))
        } else {
            cleanHandler();
            props.onStore();
        }

    }

    return (
        <div>
            <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
            <h3>Remove Product From Store {storeID}</h3>
            <div style={{ color: 'red',backgroundColor: "black",fontSize: 30 }}>{enteredError}</div>
            <div className="products__controls">
                <div className="products__control">
                    <label>ProductID</label>
                    <input
                        type="number"
                        min="0"
                        value={productID}
                        placeholder="Write ProductID to remove"
                        onChange={changeProductHandler}
                    />
                </div>
            </div>
            <button onClick={cleanHandler}>Clean</button>
            <button onClick={removeHandler}>Remove Product</button>
        </div>
    );
};

export default RemoveProduct;
