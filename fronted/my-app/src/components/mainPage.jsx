import React, {Component, useState} from "react";

export default function MainPage(props) {
    const [enteredName, setNameState] = useState("");
    const [enteredUserId, setUserIdState] = useState("");
    const [enteredFounder, setFounderState] = useState("");
    let discountPolicy=null;
    let buyPolicy=null;
    let buyStrategy=null;
    const stores = [['store 1 ','1112'], ['store 2','222'], ['store 3','333']];
    const producCarttList = [['bamba ','1112' ,'10'], ['kola','222','10'], ['kifkef','333','20']];

    let storesListItem = stores.map((storesNames) =>
        <li>Store name   :{storesNames[0]}     Store id:   {storesNames[1]}</li>
    );
    let producCarttListItem = producCarttList.map((product) =>
        <li>Product name:   {product[0]}     Product id:   {product[1]}    Product price:   {product[2]}</li>
    );

    async function storeNameHandler(event) {
        event.preventDefault();
        setNameState(event);
        //enteredName

    }
    async function addNewStoreHandler(event) {

    }
    async function leaveMarketHandler(event) {

    }


    return (
        <form>
            <div className="col span-1-of-2 box">
                <label>hello</label>
                <li>{storesListItem} </li>

            </div>

            <h3>Add new store</h3>
            <div className="mb-3">
                <label>Store Name </label>
                <input
                    type="name"
                    className="form-control"
                    placeholder="Enter email"
                    onChange={storeNameHandler}
                />
                <button type="submit" onClick={addNewStoreHandler} className="btn btn-primary">
                    add
                </button>


                <h3>My cart</h3>
                <li>{producCarttListItem} </li>

                <h3>Total Price: {}</h3>
                <button type="submit" onClick={leaveMarketHandler} className="btn btn-primary">
                    pay
                </button>

                <h3>Leave Market</h3>
                <button type="submit" onClick={leaveMarketHandler} className="btn btn-primary">
                    leave
                </button>

            </div>
        </form>);

}
