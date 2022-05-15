package com.example.demo.Service.ServiceResponse;

import com.example.demo.Domain.Response.DResponse;

public class SLResponse {
    public int errorMsg=-1;

    public SLResponse() {
    }

    public SLResponse(int msg) {
        errorMsg = msg;
    }

    public SLResponse(DResponse drResponse) {
        this.errorMsg = drResponse.errorMsg;
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
