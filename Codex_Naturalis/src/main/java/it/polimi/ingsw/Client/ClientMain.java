package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.UserInputListener;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The ClientMain class is the entry point for the client application.
 * It initializes the client model, controller, and starts the user input listener.
 */
public class ClientMain {
    public static void main(String[] args) {

        // Generate a timestamp for the log file name
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String LogFileName = "Client:_"+timestamp+".log";

        // Setting up the logger
        // Give each client a different file to write to
        try (InputStream inputStream = ClientMain.class.getClassLoader().getResourceAsStream("config/loggingClient.properties")) {
            LogManager.getLogManager().readConfiguration(inputStream);

            LogManager.getLogManager().updateConfiguration((k) -> (o, n) -> {
                if (k.equals("java.util.logging.FileHandler.pattern"))
                    return LogFileName;
                else
                    return o;
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a logger instance for this class
        Logger logger = Logger.getLogger(ClientMain.class.getName());
        logger.info("Client started");

        // Initialize the client model
        ClientModel model = ClientModel.getInstance();

        // Initialize the refresh manager
        RefreshManager.getInstance();

        // Initialize the client controller
        ClientController controller = new ClientController(model);

        // Initialize and start the user input listener
        UserInputListener inputListener = new UserInputListener(controller);
        Thread inputListenerThread = new Thread(inputListener);
        inputListenerThread.start();
    }
}
