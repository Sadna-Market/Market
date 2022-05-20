import React, { useState } from "react";

const NewProductType = (props) => {
  let UUID = props.uuid;

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
    setName("");
    setDesc("");
    setCategory("");
  };

  //todo: create new ProductType
  const createHandler = () => {
    cleanHandler();
  };

  return (
    <div>
      <h3>New ProductType</h3>
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
