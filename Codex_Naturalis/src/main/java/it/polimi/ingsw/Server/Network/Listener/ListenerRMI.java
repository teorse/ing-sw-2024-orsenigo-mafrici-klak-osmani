package it.polimi.ingsw.Server.Network.Listener;

import it.polimi.ingsw.CommunicationProtocol.LanIpFinder;
import it.polimi.ingsw.CommunicationProtocol.NetworkConstants;
import it.polimi.ingsw.CommunicationProtocol.RMI.ClientRemoteInterfaces.ClientRemoteInterface;
import it.polimi.ingsw.CommunicationProtocol.RMI.ServerRemoteInterfaces.ClientHandlerRemoteInterface;
import it.polimi.ingsw.CommunicationProtocol.RMI.ServerRemoteInterfaces.ServerListenerRemoteInterface;
import it.polimi.ingsw.Server.Controller.InputHandler.ClientInputHandler;
import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandlerRMI;
import it.polimi.ingsw.Utils.Utilities;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.logging.Logger;

/**
 * ListenerRMI class is responsible for listening to incoming RMI connection requests from clients.
 * When a request is received, it creates a ClientHandler to further handle the client's connection.
 */
public class ListenerRMI implements Runnable, ServerListenerRemoteInterface, Unreferenced {
    //ATTRIBUTES
    private final ServerController serverController;
    /**
     * The stub representing this RMI server listener remote object.
     */
    private static ServerListenerRemoteInterface stub;
    private static Registry serverRegistry;

    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a ListenerRMI object and initializes the server RMI registry
     */
    public ListenerRMI(){
        logger = Logger.getLogger(ListenerRMI.class.getName());
        logger.fine("Initializing RMI Listener");

        serverController = ServerController.getInstance();

        //Locating the ip address and configuring the rmi server hostname
        //This configuration prevents the "host refused connection" error.
        String lanIP = LanIpFinder.getInstance().getLAN_IP();
        System.setProperty("java.rmi.server.hostname", lanIP);

        System.out.println("Server Ip address is: "+lanIP);
        logger.info("Server Ip address is: "+lanIP);

        //Create the server registry
        try {
            serverRegistry = LocateRegistry.createRegistry(NetworkConstants.RMIServerRegistryPort);
            logger.fine("RMI Listener initialized");
        }
        catch (RemoteException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("Remote Exception caught in Listener RMI constructor during the server register creation:\n"+ stackTraceString);
            System.exit(666);
        }
    }





    //RUN
    /**
     * Starts the RMI listener by exporting the RMI server listener connection and binding it to the registry.
     */
    @Override
    public void run() {
        logger.info("RMI Listener thread started");
        try{
            //bind the connection object in the registry
            stub = (ServerListenerRemoteInterface) UnicastRemoteObject.exportObject(this, 0);
            serverRegistry.bind(NetworkConstants.RMIListenerStubName, stub);

            logger.info("Listener added to registry, server is listening on RMI");
            System.out.println("Server is Listening on RMI...");
        }
        catch (AlreadyBoundException | RemoteException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("Error exporting or binding RMI object: " + e.getMessage()+"\nStacktrace:\n"+ stackTraceString);
        }


        

    }

    @Override
    public void unreferenced(){
        try {
            logger.warning("Attempt to garbage collect RMI Listener, unreferenced method called.");
            logger.info("Rebinding listener to register.");
            serverRegistry.rebind(NetworkConstants.RMIListenerStubName, stub);
        }
        catch (RemoteException e){
            String stackTrace = Utilities.StackTraceToString(e);
            logger.warning("Exception "+e.getMessage()+ " caught while trying to re-bind the Server RMI Listener.\nStacktrace:\n"+stackTrace);
            System.exit(666);
        }
    }

    @SuppressWarnings("removal")
    @Override
    protected void finalize(){
        //todo remove this method if through further testing the listener rmi will not be garbage collected anymore.
        logger.warning("ListenerRMI is being garbage collected, bye bye :)");
    }





    //REMOTE METHODS
    /**
     * Creates and adds to the RMI register a new Client Handler and returns its register id.
     *
     * @param client    The ID of the client requesting a new client handler.
     * @return          The ID of the new client handler.
     */
    @Override
    public synchronized ClientHandlerRemoteInterface getNewClientHandler(ClientRemoteInterface client){
        logger.info("New client request received on RMI");
        System.out.println("New client request received on RMI");

        //Initialize and start the new client handler
        logger.info("Creating new RMI client handler for: " + client);
        ClientHandlerRMI clientHandler = new ClientHandlerRMI(client);


        Thread thread = new Thread(clientHandler);
        thread.start();
        System.out.println("Client handler started");

        System.out.println("Server is Listening on RMI...");
        logger.info("Server is listening on RMI");

        //Return the client handler  to the client.
        return clientHandler;
    }
}
