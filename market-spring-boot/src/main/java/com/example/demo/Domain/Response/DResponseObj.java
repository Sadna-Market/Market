package com.example.demo.Domain.Response;

public class DResponseObj<T> extends DResponse {
    public T value;
    public DResponseObj(int msg){
        super(msg);
    }
    public DResponseObj(T value){
        super();
        this.value = value;
    }
    public DResponseObj(T value, int msg){
        super(msg);
        this.value = value;
    }
    public DResponseObj(){
        super();
    }

    public T getValue() {
        return value;
    }
}
