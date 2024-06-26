package it.polimi.ingsw.Client.Controller;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * The UserInputListener class is responsible for continuously listening to user input
 * from the console and passing it to the ClientController for handling.
 */
public class UserInputListener implements Runnable {
    private final ClientController clientController;
    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger;

    /**
     * Constructs a UserInputListener with the specified ClientController.
     *
     * @param clientController the ClientController to handle user input
     */
    public UserInputListener(ClientController clientController) {
        this.clientController = clientController;
        logger = Logger.getLogger(UserInputListener.class.getName());
    }

    /**
     * Continuously listens for user input from the console and passes it to the ClientController.
     */
    @Override
    public void run() {
        while (true) {
            // Read input from the user
            String input = scanner.nextLine();
            logger.info("User Input Listener received an input, executing the input.");

            // Handle the input using the ClientController
            clientController.handleInput(input);

            logger.info("User input execution completed.");
        }
    }
}
