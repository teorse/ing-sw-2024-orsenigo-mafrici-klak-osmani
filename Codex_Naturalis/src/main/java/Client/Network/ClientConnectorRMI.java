package Client.Network;

import Client.Controller.ClientController;
import Network.ClientServer.Packets.ClientServerPacket;
import Network.LanIpFinder;
import Network.NetworkConstants;
import Network.RMI.ClientRemoteInterfaces.RMIClientConnector;
import Network.RMI.ServerRemoteInterfaces.RMIClientHandlerConnection;
import Network.RMI.ServerRemoteInterfaces.RMIServerListenerConnection;
import Network.ServerClient.Packets.ServerClientPacket;
import Utils.Utilities;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The ClientConnectorRMI class is an implementation of the ClientConnector interface using RMI.<br>
 * It is responsible for establishing a connection with the server, listening for packets from the server,
 * and sending packets to the server.
 */
public class ClientConnectorRMI implements ClientConnector, RMIClientConnector {
    //ATTRIBUTES
    /**
     * The stub representing this client handler's RMI connection.
     */
    private final RMIClientHandlerConnection ClientHandlerStub;
    /**
     * The stub representing this client connector's RMI object.
     */
    private RMIClientConnector stub;
    /**
     * The client's RMI register (the server looks it up to communicate with the client).
     */
    private final Registry clientRegistry;
    private Registry serverRegistry;
    /**
     * The unique identifier for this client connector.<br>
     * This identifier is the one used to bind it in the client RMI register
     */
    private final String id;

    private final ClientController controller;





    //CONSTRUCTOR
    /**
     * Constructs a ClientConnectorRMI object with the specified server IP address and client controller and
     * then starts the local RMI register and adds itself to it.<br>
     * Then it connects to the server's RMI listener from which it obtains the id for the client handler,
     * after which it connects to the client handler and confirms the connection.
     *
     * @param serverIp    The IP address of the server to connect to.
     * @param controller  The client controller associated with this client connector.
     */
    public ClientConnectorRMI(String serverIp, ClientController controller){
        System.out.println("Initializing server handler");
        this.controller = controller;
        //Setting up client RMI registry
        //If the registry does not already exist then create a new registry.
        Registry clientRegistry1;
        try {
            clientRegistry1 = LocateRegistry.getRegistry(NetworkConstants.RMIClientRegistryPort);

            //test the registry to see if it actually exists.
            //If the registry exists it will throw NotBoundException
            //If the registry does not exist it will throw RemoteException
            try {
                clientRegistry1.lookup("Anything");
            }
            catch (NotBoundException e) {
                System.out.println("Local registry detected");
            }
        }
        catch (RemoteException e) {
            System.out.println("No registry detected, creating new registry");
            try {

                //Locating the ip address and configuring the rmi client hostname
                //This configuration prevents the "host refused connection" error.
                String ipAddr = LanIpFinder.getLAN_IP();
                System.setProperty("java.rmi.server.hostname", ipAddr);
                System.out.println("My Ip address is: "+ipAddr);

                //Creating new register.
                clientRegistry1 = LocateRegistry.createRegistry(NetworkConstants.RMIClientRegistryPort);

            } catch (RemoteException ex) {
                System.out.println("Could not even create a new registry :(");
                throw new RuntimeException();
            }
        }
        clientRegistry = clientRegistry1;


        //Bind client to local client registry by generating random client id
        String tempID = "";
        boolean clientIsBound = false;
        //While loop runs until client is successfully bound to the register, if not each iteration
        //of the while loop a new id is generated and a new attempt to bind is made.
        while(!clientIsBound) {

            //id is a final field, so tempID is used inside the loop to allow id to be final
            tempID = Utilities.RandomStringGenerator(20);
            try {
                //Bind this serverHandler to the register.
                stub = (RMIClientConnector) UnicastRemoteObject.exportObject(this, 0);
                clientRegistry.bind(NetworkConstants.RMIClientConnectorDirectory + tempID, stub);
                clientIsBound = true;

            } catch (RemoteException e) {
                System.out.println("Couldn't bind client to registry, Remote Exception thrown");
                throw new RuntimeException(e);
            } catch (AlreadyBoundException e) {
                System.out.println("Client id was already bound, trying with a different id");
                throw new RuntimeException(e);
            }
        }
        id = tempID;



        String clientHandlerID = getClientHandlerID(serverIp);
        System.out.println("Client handler id received: "+clientHandlerID);

        ClientHandlerStub = connectToClientHandler(clientHandlerID);
    }





    //HELPER METHODS
    /**
     * Obtains the client handler ID from the server.
     *
     * @param serverIp The IP address of the server.
     * @return The ID of the client handler.
     */
    private String getClientHandlerID(String serverIp) {
        try {
            //Requesting dedicated connection from the server
            System.out.println("Trying to connect to server");
            serverRegistry = LocateRegistry.getRegistry(serverIp, NetworkConstants.RMIServerRegistryPort);
            RMIServerListenerConnection serverListenerStub = (RMIServerListenerConnection) serverRegistry.lookup(NetworkConstants.RMIListenerStubName);
            System.out.println("Connection successful");

            return serverListenerStub.getNewClientHandler(id);

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connects to the client handler with the given ID on the server.
     *
     * @param clientHandlerID The ID of the client handler.
     * @return The client handler stub.
     */
    private RMIClientHandlerConnection connectToClientHandler(String clientHandlerID) {
        try {
            //Connect to the newly created clientHandler
            System.out.println("Connecting to client handler");
            RMIClientHandlerConnection clientHandlerStub = (RMIClientHandlerConnection) serverRegistry.lookup(NetworkConstants.RMIClientHandlerDirectory + clientHandlerID);
            System.out.println("Client Handler Stub obtained, attempting handshake with client handler.");
            clientHandlerStub.handShake();
            System.out.println("HandShake successful");

            return clientHandlerStub;

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }





    //RUN
    /**
     * Pings the client handler to signal that this client is still alive and connected.<br>
     * Handles possible disconnections.
     */
    @Override
    public void run() {
        boolean serverAlive = true;

        //While loop sends pings to the server every 0.1 seconds.
        try {
            while (serverAlive) {
                try {
                    Thread.sleep(100);
                    ClientHandlerStub.ping();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    //If remote exception is thrown then the connection to the server has died and
                    //the while loop is broken
                    System.out.println("The server just died, RIP");
                    serverAlive = false;
                }
            }
        }
        finally {
            //finally remove this server handler from the registry
            try {
                clientRegistry.unbind(NetworkConstants.RMIClientConnectorDirectory +id);
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
    }





    //INTERFACE METHODS
    /**
     * {@inheritDoc}
     *
     * @param packet The packet to be sent to the server.
     */
    @Override
    public void sendPacket(ClientServerPacket packet) {
        try {
            ClientHandlerStub.sendCSP(packet);
        }
        catch (RemoteException e){
            System.out.println("Could not send packet to the server");
        }
    }





    //REMOTE METHODS
    /**
     * {@inheritDoc}
     * @param received The ServerClientPacket received from the client.
     */
    @Override
    public void receivePacket(ServerClientPacket received) {
        received.execute(controller);
    }
}
