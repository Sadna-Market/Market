import React, { useState } from "react";
import createApiClientHttp from "../../client/clientHttp.js";
import {errorCode} from "../../ErrorCodeGui"

const NewProductType = (props) => {
  console.log("NewProductType")

  let UUID = props.uuid;
  const apiClientHttp = createApiClientHttp();
  const [enteredError, SetError] = useState("");

  const [name, setName] = useState("");
  const changeNameHandler = (event) => {
    setName(event.target.value);
  };
  const [desc, setDesc] = useState("");
  const changeDescHandler = (event) => {
    setDesc(event.target.value);
  };
  const [category, setCategory] = useState("");
  const changeCatHandler = (event) => {
    setCategory(event.target.value);
  };

  const cleanHandler = () => {
    SetError("")
    setName("");
    setDesc("");
    setCategory("");
  };

  //todo: create new ProductType
  //        addNewProductType :async (uuid,namep,descriptionp,categoryp)=>{
  async function createHandler() {

    const addNewProductTypeResponse = await apiClientHttp.addNewProductType(UUID, name, desc, category);
    if (addNewProductTypeResponse.errorMsg !== -1) {
      SetError(errorCode.get(addNewProductTypeResponse.errorMsg))
    } else {
      cleanHandler();
    }
  }

  return (
    <div>
      <div style={{ color: 'black',position: 'relative',background: '#c51244',fontSize: 15 }}>{enteredError}</div>
      <h3>New ProductType</h3>
      <h3>{enteredError}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Name</label>
          <input
            type="text"
            value={name}
            placeholder="Write Product Name"
            onChange={changeNameHandler}
          />
        </div>
        <div className="products__control">
          <label>Description</label>
          <input
            type="text"
            value={desc}
            placeholder="Write Product Description"
            onChange={changeDescHandler}
          />
        </div>
        <div className="products__control">
          <label>Category</label>
          <input
            type="number"
            value={category}
            min="0"
            max="10"
            placeholder="Write Product Category"
            onChange={changeCatHandler}
          />
        </div>
      </div>
      <button onClick={cleanHandler}>Clean</button>
      <button onClick={createHandler}>Create</button>
    </div>
  );
};

export default NewProductType;
