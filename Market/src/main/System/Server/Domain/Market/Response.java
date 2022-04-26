package main.System.Server.Domain.Market;

public class Response {
    String err=null;

    public Response(String err) {
        this.err = err;
    }

    public Response() {
    }

    public String getErr() {
        return err;
    }
    public boolean isErr(){
        return err==null;
    }
}
