package Acceptance.Obj;

public class ATResponse {
    public String errorMsg;
    private boolean errorOccurred;
    public ATResponse(){}
    public ATResponse(String msg){
        errorMsg = msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean errorOccurred() {
        return errorMsg != null;
    }
}
