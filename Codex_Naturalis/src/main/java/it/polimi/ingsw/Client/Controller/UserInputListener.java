package it.polimi.ingsw.Client.Controller;

import java.util.Scanner;
import java.util.logging.Logger;

public class UserInputListener implements Runnable{
    private final ClientController clientController;
    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger;

    public  UserInputListener(ClientController clientController){
        this.clientController = clientController;
        logger = Logger.getLogger(UserInputListener.class.getName());
    }

    @Override
    public void run() {
        while(true){
            String input = scanner.nextLine();
            logger.info("User Input Listener received an input, executing the input.");
            clientController.handleInput(input);
            logger.info("User input execution completed.");
        }
    }
}
