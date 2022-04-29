package main.System.Server.Domain.Response;

public class DResponseObj<T> extends DResponse {
    public T value;
    public DResponseObj(String msg){
        super(msg);
    }
    public DResponseObj(T value){
        super();
        this.value = value;
    }
    public DResponseObj(T value, String msg){
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
