package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Model.Server.ServerModel;
import it.polimi.ingsw.Server.Network.Listener.ListenerRMI;
import it.polimi.ingsw.Server.Network.Listener.ListenerSocket;

import java.util.logging.Logger;

/**
 * The central manager class responsible for initializing and managing the server components.
 * It creates and manages the server model, controller, socket listener, and RMI listener.
 */
public class ServerCentralManager {
    //ATTRIBUTES
    private static ServerModel serverModel;
    private static ServerController serverController;
    private static ListenerSocket listenerSocket;
    private static ListenerRMI listenerRMI;
    private final Logger logger;

    private boolean running;





    //CONSTRUCTOR
    /**
     * Constructs a ServerCentralManager object.
     * Initializes the server components including the server model, controller,
     * socket listener, and RMI listener.
     */
    public ServerCentralManager(){

        logger = Logger.getLogger(ServerMain.class.getName());
        logger.info("Server Manager started");
        logger.info("Initializing Server");

        //todo add configuration files to read from

        running = false;

        logger.fine("Creating Server Model");
        System.out.println("Creating server Model");
        serverModel = ServerModel.getInstance();

        // Creating the server socket listener and passing the server controller
        logger.fine("Creating Server socket Listener");
        listenerSocket = new ListenerSocket();

        //Creating the server RMI listener and passing the server controller
        logger.fine("Creating Server RMI Listener");
        listenerRMI = new ListenerRMI();
    }





    //SERVER MANAGER METHODS
    /**
     * Starts the server by opening it to incoming requests.
     * Starts the socket and RMI listeners in separate threads.
     */
    public void startServer(){
        logger.info("Opening the server to requests");

        running = true;

        Thread socketThread = new Thread(listenerSocket);
        Thread RMIThread = new Thread(listenerRMI);
        logger.fine("Starting socket listener Thread");
        socketThread.start();
        logger.fine("Starting RMI listener Thread");
        RMIThread.start();
    }

    /**
     * Stops the server.
     */
    public void stopServer(){
        //todo
    }





    //GETTERS
    /**
     * Checks if the server is currently running.
     *
     * @return true if the server is running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }
}
