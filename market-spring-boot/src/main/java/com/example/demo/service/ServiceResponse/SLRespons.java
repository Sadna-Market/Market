package com.example.demo.service.ServiceResponse;

import com.example.demo.domain.Response.DResponse;

public class SLRespons {
    public int errorMsg=-1;

    public SLRespons() {
    }

    public SLRespons(int msg) {
        errorMsg = msg;
    }

    public SLRespons(DResponse drResponse) {
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
