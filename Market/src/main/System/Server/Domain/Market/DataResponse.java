package main.System.Server.Domain.Market;

public class DataResponse<T> extends Response{
    T data;

    public T getData() {
        return data;
    }

    public DataResponse(T data) {
        this.data=data;
    }

    public DataResponse(String err) {
        super(err);
    }
}
