package Server.Network.Listener;

import Network.LanIpFinder;
import Network.NetworkConstants;
import Network.RMI.ServerRemoteInterfaces.RMIServerListenerConnection;
import Server.Controller.InputHandler.ServerInputHandler;
import Server.Controller.ServerController;
import Server.Network.ClientHandler.ClientHandlerRMI;
import Utils.Utilities;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * ListenerRMI class is responsible for listening to incoming RMI connection requests from clients.
 * When a request is received, it creates a ClientHandler to further handle the client's connection.
 */
public class ListenerRMI implements Runnable, RMIServerListenerConnection{
    //ATTRIBUTES
    private final ServerController serverController;
    /**
     * The stub representing this RMI server listener remote object.
     */
    private static RMIServerListenerConnection stub;
    private static Registry serverRegistry;

    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a ListenerRMI object with the specified server controller and initializes the server RMI registry
     *
     * @param serverController The server controller associated with this RMI listener.
     */
    public ListenerRMI(ServerController serverController){
        logger = Logger.getLogger(ListenerRMI.class.getName());
        logger.fine("Initializing RMI Listener");

        this.serverController = serverController;

        //Locating the ip address and configuring the rmi server hostname
        //This configuration prevents the "host refused connection" error.
        String lanIP = LanIpFinder.getLAN_IP();
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
            stub = (RMIServerListenerConnection) UnicastRemoteObject.exportObject(this, 0);
            serverRegistry.bind(NetworkConstants.RMIListenerStubName, stub);

            logger.info("Listener added to registry, server is listening on RMI");
            System.out.println("Server is Listening on RMI...");
        }
        catch (AlreadyBoundException | RemoteException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("Error exporting or binding RMI object: " + e.getMessage()+"\nStacktrace:\n"+ stackTraceString);
        }
    }





    //REMOTE METHODS
    /**
     * Creates and adds to the RMI register a new Client Handler and returns its register id.
     *
     * @param clientID The ID of the client requesting a new client handler.
     * @return The ID of the new client handler.
     */
    @Override
    public synchronized String getNewClientHandler(String clientID){
        logger.info("New client request received on RMI");

        String clientIpAddress = "";
        try {
            clientIpAddress = RemoteServer.getClientHost();

            logger.info("Client ip is: "+clientIpAddress+":"+clientID);
            System.out.println("New client request received on RMI from: " + clientIpAddress+":"+clientID);
        }
        catch (ServerNotActiveException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("ServerNotActiveException caught in Listener RMI getNewClientHandler method" +
                    "while attempting to retrieve client ip.\n" +
                    "Stacktrace:\n"+ stackTraceString);
        }

        System.out.println("Creating new RMI client handler");

        //generate the id for the new client handler
        String id = Utilities.RandomStringGenerator(20);
        logger.fine("New id generated for client handler is: "+id);

        //Initialize and start the new client handler
        try {
            logger.info("Creating new RMI client handler for: "+clientIpAddress+":"+clientID);
            ClientHandlerRMI clientHandler = new ClientHandlerRMI(id, clientIpAddress, clientID);

            logger.fine("Setting client Handler's input handler");
            clientHandler.setInputHandler(new ServerInputHandler(clientHandler, serverController));


            Thread thread = new Thread(clientHandler);
            thread.start();
            System.out.println("Client handler started");
        }
        catch (AlreadyBoundException e){
            //Ids are randomly generated, if a specific id is already found bound then a new one is generated
            //by repeating the whole if statement and going through all the steps again
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AlreadyBoundException caught in Listener RMI getNewClientHandler method.\n" +
                    "client handler id "+id+" was already bound, generating new id\n" +
                    "stacktrace:\n"+ stackTraceString);
        }

        System.out.println("Server is Listening on RMI...");
        logger.info("Server is listening on RMI");

        //Return the client handler id to the client.
        return id;
    }
}
