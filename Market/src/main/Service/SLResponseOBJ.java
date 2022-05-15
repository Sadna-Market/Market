package main.Service;

import main.System.Server.Domain.Response.DResponseObj;

public class SLResponseOBJ<T> extends SLRespons {
    public T value;

    public SLResponseOBJ(int msg) {
        super(msg);
    }

    public SLResponseOBJ(T value) {
        super();
        this.value = value;
    }



    public SLResponseOBJ(T value, int msg) {
        super(msg);
        this.value = value;
    }

    public SLResponseOBJ() {
        super();
    }

    public SLResponseOBJ(DResponseObj<T> drResponse) {
        this.value = drResponse.getValue();
        this.errorMsg = drResponse.errorMsg;
    }

    public T getValue() {
        return value;
    }
}
