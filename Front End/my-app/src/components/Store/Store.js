import React, {useEffect, useState} from "react";
import ProductInStoreList from "../Product/ProductInStoreList";
import NewProductInStore from "./NewProductInStore";
import RemoveProduct from "./RemoveProduct";
import EditProduct from "./EditProduct";
import AddManager from "./AddManager";
import EditPermission from "./EditPermission";
import CloseStore from "./closeStore";
import Rules from "./Rules";
import HistoryInStore from "./HistoryInStore";
import RemoveManager from "./RemoveManager";
import PolicyStore from "./DiscountPolicy";
import BuyingPolicy from "./BuyingPolicy";
import DiscountPolicy from "./DiscountPolicy";
import "./Store.css";
import {createApiClientHttp} from "../../client/clientHttp";
import {errorCode} from "../../ErrorCodeGui"
import BID from "./BID/BID";


const Store = (props) => {

    let apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");
    console.log("Store")

    let storeID = props.storeID;
    let UUID = props.uuid;
    const [name, Setname] = useState("");
    const [founder, Setfounder] = useState("");
    const [isOpen, SetisOpen] = useState("");
    const [rate, Setrate] = useState("");
    const [isOwner, SetisOwner] = useState(false);
    const [isManager, SetisManager] = useState(false);

    const [tempRateMe, settempRateMe] = useState();

    const rateHandler = event => {
        settempRateMe(event.target.value);
    };
    console.log("UUID " + UUID)

    async function getStoreInfo() {
        let stores = [];

        const getStoreInfoResponse = await apiClientHttp.getStoreInfo(storeID);
        let str = JSON.stringify(getStoreInfoResponse);
        console.log("getStoreInfoResponse    " + str)

        if (getStoreInfoResponse.errorMsg !== -1) {
            SetError(errorCode.get(getStoreInfoResponse.errorMsg))
        } else {
            console.log(getStoreInfoResponse.value.name, getStoreInfoResponse.value.founder, getStoreInfoResponse.value.rate, getStoreInfoResponse.value.isOpen)
            Setname(getStoreInfoResponse.value.name)
            Setfounder(getStoreInfoResponse.value.founder)
            Setrate(getStoreInfoResponse.value.rate)
            if (getStoreInfoResponse.value.isOpen)
                SetisOpen("the store is open")
            else {
                SetisOpen("the store is close")

            }

            console.log("storesSSSS " + stores)
            SetError("")
            const isOwnerUUIDResponse = await apiClientHttp.isOwnerUUID(UUID, storeID);
            let str = JSON.stringify(isOwnerUUIDResponse);
            console.log("isOwnerUUIDResponse    " + str)

            if (isOwnerUUIDResponse.errorMsg !== -1) {
                SetError(errorCode.get(isOwnerUUIDResponse.errorMsg))
            } else {
                SetisOwner(isOwnerUUIDResponse.value)
            }
            const isManagerUUIDResponse = await apiClientHttp.isManagerUUID(UUID, storeID);
            let str2 = JSON.stringify(isManagerUUIDResponse);
            console.log("isManagerUUIDResponse    " + str2)

            if (isManagerUUIDResponse.errorMsg !== -1) {
                SetError(errorCode.get(isManagerUUIDResponse.errorMsg))
            } else {
                SetisManager(isManagerUUIDResponse.value)
            }


        }

    }

    useEffect(() => {
        getStoreInfo();
    }, [name.refresh]);
    //todo: get info
    // call to func that take store id and return all this
    async function rateClickHandler() {

        const setRateResponse = await apiClientHttp.newStoreRate(props.uuid, storeID, tempRateMe);
        let str = JSON.stringify(setRateResponse);

        if (setRateResponse.errorMsg !== -1) {
            SetError(errorCode.get(setRateResponse.errorMsg))
        } else {
            const getRateResponse = await apiClientHttp.getStoreRate(props.uuid, storeID);
            console.log('getRateResponse', getRateResponse)
            if (getRateResponse.errorMsg !== -1) {
                SetError(errorCode.get(getRateResponse.errorMsg))
            } else {
                Setrate(getRateResponse.value)
            }
        }

    };

    const addProductHandler = () => {
        setCommand(
            <NewProductInStore
                uuid={UUID}
                storeID={storeID}
                onStore={returnToStore}
            />
        );
    };

    const returnToStore = () => {
        setCommand(
            <>
                <h2>Founder: {founder}</h2>
                <h2>Rate: {rate}</h2>
                <div>
                    <ProductInStoreList uuid={UUID} storeID={storeID}/>
                </div>
            </>
        );
    };

    const removeProductHandler = () => {
        setCommand(
            <RemoveProduct uuid={UUID} storeID={storeID} onStore={returnToStore}/>
        );
    };

    const editProductHandler = () => {
        setCommand(
            <EditProduct uuid={UUID} storeID={storeID} onStore={returnToStore}/>
        );
    };
    const addManagerHandler = () => {
        setCommand(
            <AddManager uuid={UUID} storeID={storeID} onStore={returnToStore}/>
        );
    };

    const removeManagerHandler = () => {
        setCommand(
            <RemoveManager uuid={UUID} storeID={storeID} onStore={returnToStore}/>
        );
    };

    const editPermissionHandler = () => {
        setCommand(
            <EditPermission uuid={UUID} storeID={storeID} onStore={returnToStore}/>
        );
    };

    //todo: check that hasParmission!!!! to like in the storlist eith refresh ..
    // check if uuid is pwner
    // if (isOwner){
    const closeStoreHandler = () => {
        setCommand(
            <CloseStore
                uuid={UUID}
                storeID={storeID}
                onMarket={() => props.onMarket()}
            />
        );
    };
    // }
    const historyHandler = () => {
        setCommand(<HistoryInStore uuid={UUID} storeID={storeID}/>);
    };

    const policyHandler = () => {
        setCommand(<DiscountPolicy uuid={UUID} storeID={storeID}/>);
    };

    const buyingHandler = () => {
        setCommand(<BuyingPolicy uuid={UUID} storeID={storeID}/>);
    };

    // const [permission, setPermission] = useState("");
    let permission = "";
    //check ig uuid is manager in this store
    if (isManager) {
        permission = (
            <>
                <button onClick={rulesHandler}>Rules</button>
                <button onClick={addProductHandler}> Add Product</button>
                <button onClick={removeProductHandler}> Remove Product</button>
                <button onClick={editProductHandler}> Edit Product</button>
                <button onClick={addManagerHandler}> Add Manager</button>
                <button onClick={removeManagerHandler}> Remove Manager</button>
                <button onClick={editPermissionHandler}> Edit Permission</button>
                <h2></h2>
                <button onClick={historyHandler}> History</button>
                <button onClick={policyHandler}> Discount Policy</button>
                <button onClick={buyingHandler}> Buy Policy</button>
            </>
        );
    } else if (isOwner) {
        permission = (
            <>
                <button onClick={rulesHandler}>Rules</button>
                <button onClick={addProductHandler}> Add Product</button>
                <button onClick={removeProductHandler}> Remove Product</button>
                <button onClick={editProductHandler}> Edit Product</button>
                <button onClick={addManagerHandler}> Add Manager</button>
                <button onClick={removeManagerHandler}> Remove Manager</button>
                <button onClick={editPermissionHandler}> Edit Permission</button>
                <h2></h2>
                <button onClick={closeStoreHandler}> Close Store</button>
                <button onClick={historyHandler}> History</button>
                <button onClick={policyHandler}> Discount Policy</button>
                <button onClick={buyingHandler}> Buy Policy</button>
            </>
        );
    }


    const [command, setCommand] = useState(
        <>
            {/*<h2>Founder: {founder}</h2>*/}
            {/*<h2>Rate: {rate}</h2>*/}
            <div>
                <ProductInStoreList uuid={UUID} storeID={storeID}/>
            </div>
        </>
    );

    async function rulesHandler() {
        console.log(":UUID " + UUID + "storeID " + storeID)

        const getStoreRolesResponse = await apiClientHttp.getStoreRoles(UUID, storeID);
        if (getStoreRolesResponse.errorMsg !== -1) {
            SetError(errorCode.get(getStoreRolesResponse.errorMsg))
        } else {
            // console.log(":getStoreRolesResponse. "+ getStoreRolesResponse[0].type)

            console.log(":getStoreRolesResponse.value22 " + getStoreRolesResponse)
            let str = JSON.stringify(getStoreRolesResponse);
            //:getStoreRolesResponse.value {"errorMsg":-1,"value":{"owner":["sysManager@gmail.com"],"manager":[],"founder":["1dfssdf"]}}
            console.log(":getStoreRolesResponse.value " + str)
            // getStoreRolesResponse.value
            setCommand(<Rules uuid={UUID} storeID={storeID} rules={getStoreRolesResponse.value}/>);

        }

    }

    const BIDHandler = () => {
        setCommand(<BID uuid={UUID} storeID={storeID}/>);
    };

    return (
        <div className="store">
            <h3>{name}</h3>
            <h3>{isOpen}</h3>
            <h3>
                <h2>Founder: {founder}</h2>
                <h2>Rate: {rate}</h2>
            </h3>
            <button onClick={BIDHandler}>BID</button>
            {permission}
            <div>{command}</div>
            <div className="bar__control">
                <label>Rate Store</label>
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
    );
};

export default Store;
