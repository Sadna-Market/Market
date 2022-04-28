package Acceptance.Bridge;

public class ProxyMarket implements MarketBridge{
    private final MarketBridge realMarket;
    public ProxyMarket(){
        realMarket = new RealMarket();
    }
}
