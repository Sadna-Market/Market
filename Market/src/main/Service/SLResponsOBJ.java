package main.Service;

import main.System.Server.Domain.Response.DResponseObj;

public class SLResponsOBJ<T> extends SLRespons {
    public T value;

    public SLResponsOBJ(int msg) {
        super(msg);
    }

    public SLResponsOBJ(T value) {
        super();
        this.value = value;
    }

    public SLResponsOBJ(T value, int msg) {
        super(msg);
        this.value = value;
    }

    public SLResponsOBJ() {
        super();
    }

    public SLResponsOBJ(DResponseObj drResponse) {
        this.value = (T) drResponse.getValue();
        this.errorMsg = drResponse.errorMsg;
    }

    public T getValue() {
        return value;
    }
}
