package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Model.Server.ServerModel;
import it.polimi.ingsw.Server.Network.Listener.ListenerRMI;
import it.polimi.ingsw.Server.Network.Listener.ListenerSocket;

import java.util.logging.Logger;

public class ServerCentralManager {
    //ATTRIBUTES
    private static ServerModel serverModel;
    private static ServerController serverController;
    private static ListenerSocket listenerSocket;
    private static ListenerRMI listenerRMI;
    private final Logger logger;

    private boolean running;





    //CONSTRUCTOR
    public ServerCentralManager(){

        logger = Logger.getLogger(ServerMain.class.getName());
        logger.info("Server Manager started");
        logger.info("Initializing Server");

        //todo add configuration files to read from

        running = false;

        logger.fine("Creating Server Model");
        System.out.println("Creating server Model");
        serverModel = new ServerModel();

        // Creating the server controller and passing the server model
        logger.fine("Creating Server Controller");
        serverController = new ServerController(serverModel);

        // Creating the server socket listener and passing the server controller
        logger.fine("Creating Server socket Listener");
        listenerSocket = new ListenerSocket(serverController);

        //Creating the server RMI listener and passing the server controller
        logger.fine("Creating Server RMI Listener");
        listenerRMI = new ListenerRMI(serverController);
    }





    //SERVER MANAGER METHODS
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

    public void stopServer(){
        //todo
    }





    //GETTERS
    public boolean isRunning() {
        return running;
    }
}
