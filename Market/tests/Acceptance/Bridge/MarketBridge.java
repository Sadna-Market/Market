package Acceptance.Bridge;

import Acceptance.Obj.ATResponseObj;
import Acceptance.Obj.CreditCard;
import Acceptance.Obj.ItemDetail;
import Acceptance.Obj.User;

import java.util.List;

public interface MarketBridge {
    /**
     * init all resources of system (External Services, cart..)
     * @return true if success else false
     */
    boolean initSystem();

    /**
     * Discards all resources from init (doesn't change the memory)
     */
    void exitSystem();

    /**
     * Checks if the system has a system manager
     * @return true if there is, else false
     */
    boolean hasSystemManager();

    /**
     * checks is the system has an External Service Payment connected
     * @return true if there is, else false
     */
    boolean hasPaymentService();

    /**
     * checks is the system has an External Service Supply connected
     * @return true if there is, else false
     */
    boolean hasSupplierService();

    /**
     * deletes all system managers( just for the testing)
     */
    void deleteSystemManagers();

    /**
     * disconnects the service from system
     * @param service the service to disconnect
     */
    void disconnectExternalService(String service);

    /**
     * checks wether the service is connected to the system
     * @param service the service to check
     * @return true if the system has connection else false
     */
    boolean serviceIsAlive(String service);

    /**
     * contacts the Payment service to pay with creditCard amount of dollars
     * @param creditCard the credit card to take information for paying
     * @param dollars the amount to pay
     * @return response object with the recipe certification
     */
    ATResponseObj<String> pay(CreditCard creditCard, int dollars);

    /**
     * contacts the Supply service to supply derliver items to user
     * @param deliver the list of items to deliver
     * @param user the use to deliver
     * @return certificate for success supply
     */
    ATResponseObj<String> supply(List<ItemDetail> deliver, User user);
}
