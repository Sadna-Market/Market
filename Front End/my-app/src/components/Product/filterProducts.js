import React,{useState} from "react";
import './FilterProducts.css';

const FilterProducts = (props) => {
    const [name,setName] = useState("");
    const changeNameHandler = event=>{
        setName(event.target.value);
    }
    const [desc,setDesc] = useState("");
    const changeDescHandler = event=>{
        setDesc(event.target.value);
    }
    const [category, setCategory] = useState("");
    const changeCatHandler = event=>{
        setCategory(event.target.value);
    }
    const [rate,setRate] = useState("");
    const changeRateHandler = event=>{
        setRate(event.target.value);
    }
    const [storeRate, setStoreRate] = useState("");
    const changeStoreRateHandler = event=>{
        setStoreRate(event.target.value);
    }
    const [minPrice, setMinPrice]= useState("");
    const changeMinHandler = event=>{
        setMinPrice(event.target.value);
    }
    const [maxPrice, setMaxPrice] = useState("");
    const changeMaxHandler = event=>{
        setMaxPrice(event.target.value);
    }

    const cleanHandler = ()=>{
        setName("");
        setDesc("");
        setCategory("");
        setRate("");
        setStoreRate("");
        setMinPrice("");
        setMaxPrice("");
    }

    const searchHandler = ()=>{
        let filter = {
            Name: name,
            Desc: desc,
            Category: category,
            Rate: rate,
            StoreRate: storeRate,
            Min: minPrice,
            max: maxPrice,
        }
        props.onFilter(filter);
        cleanHandler()
    }


  return (
    <div className="products">
      <h3>Filters</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Filter by Name</label>
          <input type="text"
          value={name}
          placeholder="Write Product Name"
          onChange={changeNameHandler}
           />
        </div>
        <div className="products__control">
          <label>Filter by Description</label>
          <input type="text"
          value={desc}
          placeholder="Write Product Description"
          onChange={changeDescHandler}
           />
        </div>
        <div className="products__control">
            <label>Filter by Category</label>
            <input
              type="number"
              value={category}
              min="0"
              max="10"
              placeholder="Write Product Category"
              onChange={changeCatHandler}
            />
          </div>
        <div className="products__control">
            <label>Filter by Rate</label>
            <input
              type="number"
              value={rate}
              min="0"
              max="10"
              placeholder="Write Product Rate (min)"
              onChange={changeRateHandler}
            />
          </div>
          <div className="products__control">
            <label>Filter by Store Rate</label>
            <input
              type="number"
              value={storeRate}
              min="0"
              max="10"
              placeholder="Write Store Rate (min)"
              onChange={changeStoreRateHandler}
            />
          </div>
          <div className="products__control">
            <label>Minimum Price</label>
            <input
              type="number"
              value={minPrice}
              min="0"
              placeholder="Write Minimum Price"
              onChange={changeMinHandler}
            />
          </div>
          <div className="products__control">
            <label>Maximum Price</label>
            <input
              type="number"
              value={maxPrice}
              min="0"
              placeholder="Write Maximum Price"
              onChange={changeMaxHandler}
            />
          </div>
          <button onClick={cleanHandler}>Clean</button>
          <button onClick={searchHandler}>Search</button>
      </div>
    </div>
  );
};

export default FilterProducts;
