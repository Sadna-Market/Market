import React, {useState, useEffect} from "react";
import Card from "../UI/Card";
import "./Market.css";
import StoreList from "../Store/StoreList";
import MarketButton from "./MarketButton";
import ProductButton from "../Product/ProductButton";
import Store from "../Store/Store";
import MyCart from "../Cart/MyCart";
import ManagerButton from "../Manager/ManagerButton";
import NewStore from "../Store/newStore";
import Profile from "./Profile";
import Help from "./Help";


const Market = (props) => {
    console.log("Market")

    let UUID = props.uuid
    let OldUUID=props.olduuid


    console.log("Market UUID: " + UUID)
    const [notification, setNotification] = useState("Notification");
    // setNotification(props.noty)
    console.log("props.log " + props.log);
    console.log("props.uuid " + props.uuid);
    console.log("props.olduuid " + props.olduuid);
    console.log("props.email " + props.emal);


    const showStoreHandler = (storeID) => {
        console.log("showStoreHandler  storeID:" + storeID);
        setCommand(
            <Store storeID={storeID} uuid={UUID} onMarket={onMarketHandler}/>
        );
    };

    const [command, setCommand] = useState(
        <MarketButton onShowStore={showStoreHandler}/>
    );

    let permissonCommand = "";

    const managerHandler = () => {
        setCommand(<ManagerButton uuid={UUID}/>);
    };

    const openStoreHandler = () => {
        console.log("openStoreHandler UUID: " + UUID)
        setCommand(
            <NewStore
                uuid={UUID}
                useremail={props.useremail}
                onMarket={() => {
                    setCommand(
                        <MarketButton uuid={UUID} onShowStore={showStoreHandler}/>
                    );
                }}
            />
        );
    };

    const profileHandler = () => {
        setCommand(<Profile uuid={UUID} useremail={props.useremail} email={props.email}/>);
    };

    if (props.isLogin) {
        permissonCommand = (
            <>
                <button onClick={openStoreHandler}> Open New Store</button>
                <button onClick={profileHandler}>Profile</button>
            </>
        );
    }

    //todo: check if manager
    if (props.isSystemManager) {
        permissonCommand = (
            <>
                {permissonCommand}
                <button onClick={managerHandler}> Manager</button>
            </>
        );
    }

    const onMarketHandler = () => {
        setCommand(<MarketButton uuid={UUID} onShowStore={showStoreHandler}/>);
    };

    const MarketHandler = () => {
        setCommand(<MarketButton uuid={UUID} onShowStore={showStoreHandler}/>);
    };

    const productHandler = () => {
        setCommand(<ProductButton uuid={UUID}/>);
    };

    const helpHandler = () => {
        setCommand(<Help uuid={UUID} onHelp={(text) => setNotification(text)}/>);
    };

    const myCartHandler = () => {
        setCommand(
            <MyCart
                uuid={UUID}
                onMarket={() =>
                    setCommand(
                        <MarketButton uuid={UUID} onShowStore={showStoreHandler}/>
                    )
                }
            />
        );
    };

    return (
        <Card className="market">
            {/*{notification}*/}
            <div>

                <button onClick={MarketHandler}> Market</button>
                <button onClick={myCartHandler}> My Cart</button>
                <button onClick={productHandler}> Products</button>
                {permissonCommand}
                <button onClick={helpHandler}> Help!</button>
            </div>
            <div>{command}</div>
        </Card>
    );
};

export default Market;
