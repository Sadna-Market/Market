package main.System.Server.Domain.Response;

public class DResponse {
    public String errorMsg;
    private boolean errorOccurred;
    public DResponse(){}
    public DResponse(String msg){
        errorMsg = msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean errorOccurred() {
        return errorMsg != null;
    }

    public void setErrorMsg(String s){
        errorMsg=s;
    }
}
