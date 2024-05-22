package Server.Network.ClientHandler;

import Exceptions.Network.RMI.ClientDisconnectedException;
import Network.ClientServer.Packets.ClientServerPacket;
import Network.NetworkConstants;
import Network.RMI.ClientRemoteInterfaces.RMIClientConnector;
import Network.RMI.ServerRemoteInterfaces.RMIClientHandlerConnection;
import Network.ServerClient.Packets.SCPConnectionAck;
import Network.ServerClient.Packets.ServerClientPacket;
import Server.Controller.InputHandler.InputHandler;
import Utils.Utilities;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * ClientHandlerRMI class represents the RMI implementation of the ClientHandler interface.
 * It handles communication with a client over an RMI connection.
 */
public class ClientHandlerRMI implements ClientHandler, Runnable, RMIClientHandlerConnection {
    //ATTRIBUTES
    /**
     * The stub representing this client handler's RMI remote object.
     */
    private RMIClientHandlerConnection stub;
    /**
     * The unique identifier for this client handler.
     */
    private final String id;
    private final Registry serverRegistry;
    /**
     * The IP address of the client associated with this client handler.<br>
     * Used to establish the other side of the connection, Server -> Client.
     */
    private final String clientIPAddress;
    /**
     * The ID by which the RMI object representing the client is stored in the client's register.<br>
     * Used to establish the other side of the connection, Server -> Client.
     */
    private final String clientID;
    private RMIClientConnector clientStub;

    private boolean ping;

    private InputHandler serverInputHandler;
    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a ClientHandlerRMI object with the specified ID, client IP address, and client ID of the client that requested this client handler.
     *
     * @param id                The unique identifier for this client handler.
     * @param clientIPAddress   The IP address of the client associated with this client handler.
     * @param clientID          The client ID associated with this client handler.
     * @throws AlreadyBoundException If the client handler is already bound in the registry.
     */
    public ClientHandlerRMI(String id, String clientIPAddress, String clientID) throws AlreadyBoundException, RemoteException {

        logger = Logger.getLogger(ClientHandlerRMI.class.getName());
        logger.info("Initializing Client Handler RMI");

        this.id = id;
        this.clientIPAddress = clientIPAddress;
        this.clientID = clientID;

        try{
            //Locates the server register and binds itself in it.
            stub = (RMIClientHandlerConnection) UnicastRemoteObject.exportObject(this, 0);
            serverRegistry = LocateRegistry.getRegistry(NetworkConstants.RMIServerRegistryPort);

            logger.info("Binding this Client Handler to registry");
            serverRegistry.bind(NetworkConstants.RMIClientHandlerDirectory+id, stub);
            logger.info("Client handler "+id+" successfully bound to registry");
        }
        catch (RemoteException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("RemoteException caught in Client Handler RMI Constructor while binding object to register.\nStacktrace:\n"+stackTraceString);
            throw new RemoteException(e.getMessage());
        }
    }





    //RUN
    /**
     * Listens for incoming communication from the client and handles disconnections.<br>
     * Implements the heartbeat mechanism.
     */
    @Override
    public void run() {
        logger.info("Client Handler RMI "+id+" is now listening for pings");
        //Setting up thread to handle the ping heartbeat mechanism to detect client disconnections.
        boolean receivedPing = true;

        try {
            while (receivedPing) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //Every 0.5 seconds the thread checks if the client delivered a ping.
                //If no ping was delivered then receivedPing is false and the while loop is broken.
                receivedPing = ping;
                ping = false;
            }
            throw new ClientDisconnectedException("Client "+clientIPAddress+":"+clientID+" disconnected");
        }
        catch (ClientDisconnectedException e){
            logger.warning("No heartbeat detected from RMI Client: "+clientIPAddress+":"+clientID);
            System.out.println("No heartbeat detected from client: "+clientIPAddress+":"+clientID);
            serverInputHandler.clientDisconnectionProcedure();
        }
        finally {
            //Removing the client handler from the registry to clean up
            logger.fine("Executing 'finally' block in client handler, proceeding to remove from registry");
            try {
                logger.info("Unbinding RMI client handler "+id);
                serverRegistry.unbind(NetworkConstants.RMIClientHandlerDirectory+id);
            }
            catch (RemoteException ex) {
                String stackTraceString = Utilities.StackTraceToString(ex);
                logger.warning("RemoteException caught in Client Handler RMI while unbinding client handler.\nStacktrace:\n"+stackTraceString);
            }
            catch (NotBoundException ex) {
                String stackTraceString = Utilities.StackTraceToString(ex);
                logger.warning("NotBoundException caught in Client Handler RMI while unbinding client handler.\nStacktrace:\n"+stackTraceString);
            }
        }
    }





    //CLIENT HANDLER METHODS
    /**
     * {@inheritDoc}
     * @param packet The packet to be sent to the client.
     */
    @Override
    public void sendPacket(ServerClientPacket packet) {
        logger.fine("Sending packet to client: "+clientIPAddress+":"+clientID);
        try {
            clientStub.receivePacket(packet);
        }
        catch (RemoteException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("RemoteException caught in Client Handler RMI while sending message to client: "+clientIPAddress+":"+clientID+".\nStacktrace:\n"+stackTraceString);
        }
    }

    /**
     * {@inheritDoc}
     * @param inputHandler The InputHandler object to be set for the client.
     */
    @Override
    public void setInputHandler(InputHandler inputHandler) {
        this.serverInputHandler = inputHandler;
    }





    //REMOTE METHODS
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCSP(ClientServerPacket packet) throws RemoteException {
        logger.fine("Receiving packet from client: "+clientIPAddress+":"+clientID);
        serverInputHandler.handleInput(packet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handShake() throws RemoteException {
        logger.info("Client "+clientIPAddress+":"+clientID+" initiated handshake with client handler "+id);
        logger.fine("Attempting to close handshake with client "+clientIPAddress+":"+clientID);
        Registry clientRegistry = LocateRegistry.getRegistry(clientIPAddress, NetworkConstants.RMIClientRegistryPort);

        try {
            clientStub = (RMIClientConnector) clientRegistry.lookup(NetworkConstants.RMIClientConnectorDirectory + clientID);
            logger.info("Client Handler "+id+" confirms connection to client "+clientIPAddress+":"+clientID);

            logger.fine("Sending connection ack to client "+clientIPAddress+":"+clientID);
            sendPacket(new SCPConnectionAck("Welcome to the server, please Log in or Sign up."));
        }
        catch (NotBoundException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("NotBoundException caught in Client Handler RMI while handshaking with client handler.\nStacktrace:\n"+stackTraceString);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ping() throws RemoteException {
        ping = true;
    }
}
