package main.ExternalService;

import main.System.Server.Domain.Response.DResponseObj;

public interface IExternalService {
    public DResponseObj<Boolean> isConnect();
    public DResponseObj<Boolean> connect();
    public DResponseObj<Boolean> disConnect();

    //return the name of the service
    DResponseObj<String> ping();

}
