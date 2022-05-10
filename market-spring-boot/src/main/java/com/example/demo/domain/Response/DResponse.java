package com.example.demo.domain.Response;


public class DResponse {
    public int errorMsg = -1;
    public DResponse() {
    }

    public DResponse(int errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorMsg() {
        return errorMsg;
    }

    public boolean errorOccurred() {
        return errorMsg != -1;
    }

    public void setErrorMsg(int s) {
        errorMsg = s;
    }
}
