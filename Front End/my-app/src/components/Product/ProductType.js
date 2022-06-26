import React, {useEffect, useState} from "react";
import Card from "../UI/Card";
import "./ProductType.css";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const ProductType = (props) => {
    console.log("ProductType")
    console.log("asdasd ",props.uuid)
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    let productID = props.productID;
    const [rateMe, setRate] = useState();
    const [tempRateMe, settempRateMe] = useState();

    const [enteredProductInfo, SetProductInfo] = useState([{name:"",description:"",category:"",rate:""}]);
    const [name, setname] = useState();
    const [description, setdescription] = useState();
    const [category, setcategory] = useState();
    const [rate, setrate] = useState();

    const rateHandler = event => {
        settempRateMe(event.target.value);
    };

    async function rateClickHandler() {

        const setRateResponse = await apiClientHttp.setRate(props.uuid,productID,tempRateMe);
        let str = JSON.stringify(setRateResponse);

        if (setRateResponse.errorMsg !== -1) {
            SetError(errorCode.get(setRateResponse.errorMsg))
        } else {
            getProductInfo();

        }
    };


    console.log("StoreList")

    async function getProductInfo() {
        let INFO = [];

        const getProductTypeInfoResponse = await apiClientHttp.getProductTypeInfo(productID);
        console.log("start func  getProductInfo")
        let str = JSON.stringify(getProductTypeInfoResponse);
        console.log("sendRulesResponse    " + str)
        if (getProductTypeInfoResponse.errorMsg !== -1) {
            SetError(errorCode.get(getProductTypeInfoResponse.errorMsg))
        } else {

            setname(getProductTypeInfoResponse.value.productName)
            setdescription(getProductTypeInfoResponse.value.description)
            setcategory(getProductTypeInfoResponse.value.category)
            setrate(getProductTypeInfoResponse.value.rate)
            SetError("")
            const getRateResponse = await apiClientHttp.getRate(props.uuid,productID);
            console.log('getRateResponse',getRateResponse)
            if (getRateResponse.errorMsg !== -1) {
                SetError(errorCode.get(getRateResponse.errorMsg))
            } else {
                setRate(getRateResponse.value)
            }
        }


    }
    //
    useEffect(() => {
        getProductInfo();
    }, [enteredProductInfo.refresh]);
     console.log('enteredProductInfo',enteredProductInfo[0].rate)

    return (
        <Card>
            <div className="productType">
                <div>
                    <h3>Product Number: {productID}</h3>
                </div>
                <div>
                    <h3>Name: {name}</h3>
                </div>
                <div>
                    <h3>Description: {description}</h3>
                </div>
                <div>
                    <h3>Category: {category}</h3>
                </div>
                <div>
                    <h3>Rate: {rate}</h3>
                </div>
                <div className="bar__control">
                    <label>Rate Product</label>
                    <input
                        type="number"
                        min="0"
                        max="10"
                        value={tempRateMe}
                        onChange={rateHandler}
                        placeholder="1-10"
                    />
                    <button onClick={rateClickHandler}>Rate</button>
                </div>
            </div>
        </Card>
    );
};

export default ProductType;
