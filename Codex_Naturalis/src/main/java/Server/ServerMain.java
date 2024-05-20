package Server;

import Server.Controller.ServerController;
import Server.Model.ServerModel;
import Server.Network.Listener.ListenerRMI;
import Server.Network.Listener.ListenerSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The main class to start the server application.<br>
 * This class initializes the server model, controller, and starts the server socket listener.
 */
public class ServerMain {
    /**
     * The main method to start the server application.<br>
     * It initializes the server model, controller, initializes the local RMI registry, and starts the server socket and RMI listeners.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        //Setting up the logger
        try(InputStream inputStream = ServerMain.class.getClassLoader().getResourceAsStream("config/loggingServer.properties"))
        {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger(ServerMain.class.getName());
        logger.info("Server started");




        // Creating and initializing the server model
        logger.fine("Creating Server Model in ServerMain");
        System.out.println("Creating server Model");
        ServerModel serverModel = new ServerModel();

        // Creating the server controller and passing the server model
        logger.fine("Creating Server Controller in ServerMain");
        ServerController serverController = new ServerController(serverModel);

        // Creating the server socket listener and passing the server controller
        logger.fine("Creating Server socket Listener in ServerMain");
        ListenerSocket listenerSocket = new ListenerSocket(serverController);

        //Creating the server RMI listener and passing the server controller
        logger.fine("Creating Server RMI Listener in ServerMain");
        ListenerRMI listenerRMI = new ListenerRMI(serverController);

        // Starting the server listener threads
        Thread socketThread = new Thread(listenerSocket);
        Thread RMIThread = new Thread(listenerRMI);
        logger.fine("Starting socket listener Thread in ServerMain");
        socketThread.start();
        logger.fine("Starting RMI listener Thread in ServerMain");
        RMIThread.start();
    }
}
