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





    //CONSTRUCTOR
    /**
     * Constructs a ListenerRMI object with the specified server controller and initializes the server RMI registry
     *
     * @param serverController The server controller associated with this RMI listener.
     */
    public ListenerRMI(ServerController serverController){
        this.serverController = serverController;

        //Locating the ip address and configuring the rmi server hostname
        //This configuration prevents the "host refused connection" error.
        String lanIP = LanIpFinder.getLAN_IP();
        System.setProperty("java.rmi.server.hostname", lanIP);

        System.out.println("My Ip address is: "+lanIP);

        //Create the server registry
        try {
            serverRegistry = LocateRegistry.createRegistry(NetworkConstants.RMIServerRegistryPort);
        }
        catch (RemoteException e){
            System.out.println("Problem during the server register creation: "+e);
        }
    }





    //RUN
    /**
     * Starts the RMI listener by exporting the RMI server listener connection and binding it to the registry.
     */
    @Override
    public void run() {
        try{
            //bind the connection object in the registry
            stub = (RMIServerListenerConnection) UnicastRemoteObject.exportObject(this, 0);
            serverRegistry.bind(NetworkConstants.RMIListenerStubName, stub);

            System.out.println("Listener added to registry");
        }
        catch (AlreadyBoundException | RemoteException e) {
            throw new RuntimeException("Error exporting or binding RMI object: " + e.getMessage(), e);
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

        String clientIpAddress = "";
        try {
            clientIpAddress = RemoteServer.getClientHost();
        }
        catch (ServerNotActiveException e){
            System.out.println(e);
        }

        System.out.println("Request arrived, creating new client handler for "+clientIpAddress+":"+clientID);

        //generate the id for the new client handler
        String id = Utilities.RandomStringGenerator(20);
        System.out.println("New id generated: "+id);

        //Initialize and start the new client handler
        try {
            ClientHandlerRMI clientHandler = new ClientHandlerRMI(id, clientIpAddress, clientID);
            clientHandler.setInputHandler(new ServerInputHandler(serverController, clientHandler));
            Thread thread = new Thread(clientHandler);
            thread.start();
            System.out.println("New client handler started with id: "+id);
        }
        catch (AlreadyBoundException e){
            //Ids are randomly generated, if a specific id is already found bound then a new one is generated
            //by repeating the whole if statement and going through all the steps again
            System.out.println("id "+id+" was already bound, generating new id");
        }

        System.out.println("Listening for client requests");

        //Return the client handler id to the client.
        return id;
    }
}
