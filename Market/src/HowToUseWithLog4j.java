import main.Main;
import org.apache.log4j.Logger;

public class HowToUseWithLog4j {
    //when we use with logger, we want to recognize from where the log.
    // so for each class we will write:
    //static Logger logger=Logger.getLogger(<the name of the class>.class);
    static Logger logger=Logger.getLogger(HowToUseWithLog4j.class);


    public static void main(String[] args) {
        //we will use with debug only for actions without Side effect - the condition of the system doesn't change.
        //on our logger, we won't see a debug massages (we will change that when we need to fix a bugs).
        logger.debug("----debug-----");

        //we will use with info only for actions with Side effect - the condition of the system change after the act.
        logger.info("----info-----");

        //we will use with Error only for actions that don't get us to continue in our Usecase. example: get in params null or invalid value, or because condition that not pass.
        logger.error("----Error-----");

        //we will use with Fatal only for Exceptions (in the catch).example: try{...} catch(Exception e){log.fatal(e.massage);}
        logger.fatal("----Fatal-----");
    }
}
