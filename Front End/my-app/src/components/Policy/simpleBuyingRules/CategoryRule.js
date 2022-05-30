import React, { useState } from "react";

const CategoryRule = (props) => {
  let UUID = props.uuid;
  let storeID = props.storeID;

  const [category, setCategory] = useState("");
  const changeCategoryHandler = (event) => {
    setCategory(event.target.value);
  };

  const [minAge, setminAge] = useState("");
  const changeMinAgeHandler = (event) => {
    setminAge(event.target.value);
  };

  const [minHour, setminHour] = useState("");
  const changeMinHourHandler = (event) => {
    setminHour(event.target.value);
  };

  const [maxHour, setmaxHour] = useState("");
  const changeMaxHourHandler = (event) => {
    setmaxHour(event.target.value);
  };

  const cleanHandler = () => {
    setCategory("");
    setminAge("");
    setminHour("");
    setmaxHour("");
  };

  //todo: add new ShoppingBag Rule to store
  const addHandler = () => {
    cleanHandler();
    props.onRule();
  };

  return (
    <div>
      <h3>Add Category Rule For store #{storeID}</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Category ID</label>
          <input
            type="number"
            min="0"
            value={category}
            placeholder="Write Category ID"
            onChange={changeCategoryHandler}
          />
        </div>
        <div className="products__control">
          <label>Min Age</label>
          <input
            type="number"
            min="0"
            value={minAge}
            placeholder="Write Minmum Quantity"
            onChange={changeMinAgeHandler}
          />
        </div>
        <div className="products__control">
          <label>minHour</label>
          <input
            type="number"
            min="0"
            max="23"
            value={minHour}
            placeholder="Write Hour 0-23"
            onChange={changeMinHourHandler}
          />
        </div>
        <div className="products__control">
          <label>maxHour</label>
          <input
            type="number"
            min="0"
            max="23"
            value={maxHour}
            placeholder="Write Hour 0-23"
            onChange={changeMaxHourHandler}
          />
        </div>
        <button onClick={cleanHandler}>Clean</button>
        <button onClick={addHandler}>Add Rule</button>
      </div>
    </div>
  );
};

export default CategoryRule;
