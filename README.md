# Market 
#State file Doc

the structure of the file is:

```
{
systemItems:[]
login:[]
register:[]
stores:[]
add_item:{}
addManager:{}
addOwner:{}
LogoutandLeave:[]
}


the state file is in json format.
all the properties above need to be in the file, if not it will case an error.



systemItems - insert new items in the system with the admin credentials

the value need to be array of items with the properties:

"email": "u1@gmail.com",
"password": "abcA!123",
"phoneNumber": "0538265477",
"dateOfBirth" : "25/10/1984"

login -array of all the users to login :
"email": "u5@gmail.com",
"password": "abcA!123",

register - array of all the users to register :

"email": "u1@gmail.com",
"password": "abcA!123",
"phoneNumber": "0538265477",
"dateOfBirth" : "25/10/1984


stores - object , key - email of the users , value - store properties

{
 "u2@gmail.com": [
 {
 "name": "s1",
 "founder": "u2@gmail.com"
 }
 ]


add_item - object with the key :"email of the user" , and the value is object with the key : "store name" and the value is the array of items


{
 "u2@gmail.com": {
 "s1": [
 {
 "name": "bamba",
 "price": 30.00,
 "quantity": 20
 }
 ]
 }
 
addManager - 

"u2@gmail.com": object with key : "email of grantors" , value : object with key: "store name" and the value is array of permessions

"u2@gmail.com": {
 "s1": [
 {"u3@gmail.com": ["addNewProductToStore",
 "deleteProductFromStore",
 "setProductPriceInStore",
 "setProductQuantityInStore"]}
 ]
}
}

addOwner - object with key : "email of grantor" , value : object with key: "store name" ,value: arry of emails of new owners 


"u2@gmail.com": {
 "s1": [
 "u4@gmail.com","u5@gmail.com"
 ]
}

LogoutandLeave - array of users to logout
