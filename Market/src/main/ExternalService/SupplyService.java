package main.ExternalService;


//threadsafe
public class SupplyService {
    private static class SupplyServiceWrapper{
        static  SupplyService INSTANSE = new SupplyService();
    }
    private SupplyService(){}

    public static SupplyService getInstance(){
        return SupplyService.SupplyServiceWrapper.INSTANSE;
    }
}
