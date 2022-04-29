package main.Service;

import main.System.Server.Domain.Response.DResponse;

public class SLRespons {
    public String errorMsg;
    private boolean errorOccurred;

    public SLRespons() {
    }

    public SLRespons(String msg) {
        errorMsg = msg;
    }

    public SLRespons(DResponse drResponse) {
        this.errorMsg = drResponse.errorMsg;
        this.errorOccurred = drResponse.errorOccurred();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean errorOccurred() {
        return errorMsg != null;
    }

    public void setErrorMsg(String s) {
        errorMsg = s;
    }
}
