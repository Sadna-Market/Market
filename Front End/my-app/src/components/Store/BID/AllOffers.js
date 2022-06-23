import React, {useEffect, useState} from "react";
import BIDItem from "./BIDItem";
import Offer from "./Offer";
import createApiClientHttp from "../../../client/clientHttp.js";
import {errorCode} from "../../../ErrorCodeGui"

const AllOffers = (props) => {
    console.log("AllOffers")
    let UUID = props.uuid;
    //todo: get all my products
    const [enteredOffers, SetOffers] = useState([]);
    const apiClientHttp = createApiClientHttp();
    const [enteredError, SetError] = useState("");

    async function getAllOffers() {
        let Offers = [];

        const getAllOffersBIDSResponse = await apiClientHttp.getAllOffersBIDS(UUID, props.storeID);

        let str = JSON.stringify(getAllOffersBIDSResponse);
        console.log("getAllOffersBIDSResponse    " + str)

        if (getAllOffersBIDSResponse.errorMsg !== -1) {
            SetError(errorCode.get(getAllOffersBIDSResponse.errorMsg))
        } else {
            for (const key in getAllOffersBIDSResponse.value) { //key = storeID
                let offer = (getAllOffersBIDSResponse.value[key]);//{"storeId":1,"name":"sys","founder":"sysManager@gmail.com","isOpen":true,"rate":5}
                for (let i = 0; i < offer.length; i++) {
                    console.log(offer[i].username)
                    console.log(offer[i].productID)
                    Offers.push({
                        username: offer[i].username,
                        id: offer[i].productID,
                        name: offer[i].productName,
                        quantity: offer[i].quantity,
                        originPrice: offer[i].originPrice,
                        counterPrice: offer[i].counterPrice,
                        status: offer[i].status,


                    })

                }


            }


            // console.log("Bids " + Bids)
            SetError("")
            SetOffers(Offers);
        }
    }


    useEffect(() => {
        getAllOffers();
    }, [enteredOffers.refresh]);
    // let products = [
    //   { id: 1, name: "Sony 5", offers: 2, managers:2},
    //   { id: 2, name: "TV", offers: 3 , managers:1},
    //   { id: 3, name: "Car KIA RIO", offers: 1, managers:3},
    //   { id: 4, name: "AC/DC", offers: 2 , managers:3},
    //   { id: 5, name: "MAMA Yokero", offers: 3 , managers:0},
    //   { id: 6, name: "Iphone 13", offers: 2 , managers:1},
    // ];
    let sum = 0;
    for (let i = 0; i < enteredOffers.length; i++) {
        sum += enteredOffers[i].price * enteredOffers[i].amount;
    }

    const [total, setTatal] = useState(sum);

    const cancelHandler = (price) => {
        setTatal(total - price);
    };

    let productIDs = enteredOffers.map((expense) => (
        <Offer
            storeID={props.storeID}
            username={expense.username}
            id={expense.id}
            name={expense.name}
            originPrice={expense.originPrice}
            counterPrice={expense.counterPrice}
            quantity={expense.quantity}
            onCancel={cancelHandler}
            status={expense.status}
            uuid={UUID}
            managers={0}
            onChange={getAllOffers}

        />
    ));

    return (
        <div>
            <h3>Offers</h3>
            <div>
                <ul className="products-list">{productIDs}</ul>
            </div>
        </div>
    );
};

export default AllOffers;
