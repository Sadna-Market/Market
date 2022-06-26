import createApiClientHttp from "../../client/clientHttp.js";

console.log("aaaaaaaaaaa")

async function test () {
    let res = createApiClientHttp();
    console.log("aaaaaaaaaaa")

    let uuid = await res.guestVisit()
    // let json = await res.initMarket();\
    console.log(uuid)

    console.log("initMarket")
}

test()