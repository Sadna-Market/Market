import React,{useState} from "react";

const NewStore = (props) => {
  let UUID = props.uuid;
  const [name, setName] = useState("");
  const changeNameHandler = (event) => {
    setName(event.target.value);
  };
  const [founder, setfounder] = useState("");
  const changeFounderHandler = (event) => {
    setfounder(event.target.value);
  };

  const cleanHandler = ()=>{
      setName("");
      setfounder("");
  }

  //todo: open Store
  const openHandler=()=>{
      cleanHandler();
      props.onMarket();
  };

  return (
    <div className="products">
      <h3>Open New Store</h3>
      <div className="products__controls">
        <div className="products__control">
          <label>Name</label>
          <input
            type="text"
            value={name}
            placeholder="Write Name"
            onChange={changeNameHandler}
          />
        </div>
        <div className="products__control">
          <label>Founder</label>
          <input
            type="text"
            value={founder}
            placeholder="Write Founder"
            onChange={changeFounderHandler}
          />
        </div>
      </div>
      <button onClick={cleanHandler}>Clean</button>
      <button onClick={openHandler}>Open Store</button>
    </div>
  );
};

export default NewStore;
