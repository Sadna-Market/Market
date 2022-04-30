package main.Service;

import main.System.Server.Domain.Market.Market;
import main.System.Server.Domain.UserModel.UserManager;

public class TestableFacade extends Facade {

//-------------------------------for tests only !---------------------------------------------

    public void reset() {
        /**
         * @requirement
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": void
         *        }
         *
         * @documentation:

         * */
        this.userManager = new UserManager();
        this.market = new Market(this.userManager);
    }

    /**
     * Checks if the system has a system manager
     *
     * @return true if there is, else false
     */
    public boolean hasSystemManager() {
        //TODO: add for tests in iMarket
        return false;
    }

    /**
     * checks is the system has an External Service Payment connected
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean> hasPaymentService() {
        return new SLResponsOBJ (market.isHavePaymentInstane());
    }

    /**
     * checks is the system has an External Service Supply connected
     *
     * @return true if there is, else false
     */
    public SLResponsOBJ<Boolean>  hasSupplierService() {
            return new SLResponsOBJ (market.isHaveSupplyService());
        }

    /**
     * deletes all system managers( just for the testing)
     */
    public void deleteSystemManagers() {
        //TODO: add for tests in iMarket
    }

    /**
     * disconnects the service from system
     *
     * @param service the service to disconnect
     */
    public void disconnectExternalService(String service) {
    }

    /**
     * checks wether the service is connected to the system
     *
     * @param service the service to check
     * @return true if the system has connection else false
     */
    public boolean serviceIsAlive(String service) {
        //TODO: add for tests in iMarket
        return false;
    }

    public SLResponsOBJ<Boolean> supply() {
        /**
         * @requirement @requirement II. 3
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean -
         *        }
         *
         * @documentation: not for the user!

         * */

        return new SLResponsOBJ<>();
    }


    public SLResponsOBJ<Boolean> payment() {
        /**
         * @requirement II. 4
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean
         *        }
         *
         * @documentation: not for the user!

         * */

        return new SLResponsOBJ<>();
    }

    public SLResponsOBJ<Boolean> connectService(String serviceName) {
        /**
         * @requirement
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean -
         *        }
         *
         * @documentation: not for the user!

         * */

        return new SLResponsOBJ<>();
    }

    public SLResponsOBJ<Boolean> disconnectService(String serviceName) {
        /**
         * @requirement
         *
         *
         * @return DResponseObj
         *        {
         *        "errorOccurred(): boolean
         *        "errorMsg": String
         *        "value": Boolean -
         *        }
         *
         * @documentation: not for the user!

         * */

        return new SLResponsOBJ<>();
    }
}
