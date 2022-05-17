package com.example.Acceptance.Obj;

public class ATResponseObj<T> extends  ATResponse {
    public T value;

    public ATResponseObj(T value){
        super();
        this.value = value;
    }

    public ATResponseObj(String msg){
        super(msg);
    }

    public ATResponseObj(T value, String msg){
        super(msg);
        this.value = value;
    }

}
