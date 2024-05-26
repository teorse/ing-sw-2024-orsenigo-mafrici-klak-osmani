package Client.Network;

import Client.Controller.ClientController;
import Network.ClientServer.Packets.ClientServerPacket;
import Network.NetworkConstants;
import Network.RMI.ClientRemoteInterfaces.ClientRemoteInterface;
import Network.RMI.ServerRemoteInterfaces.ClientHandlerRemoteInterface;
import Network.RMI.ServerRemoteInterfaces.ServerListenerRemoteInterface;
import Network.ServerClient.Packets.ServerClientPacket;
import Network.ServerClient.ServerMessageExecutor;
import Utils.Utilities;
import jdk.jshell.execution.Util;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * The ClientConnectorRMI class is an implementation of the ClientConnector interface using RMI.<br>
 * It is responsible for establishing a connection with the server, listening for packets from the server,
 * and sending packets to the server.
 */
public class ClientConnectorRMI implements ClientConnector, ClientRemoteInterface {
    //ATTRIBUTES
    /**
     * The thisRemote representing this client handler's RMI connection.
     */
    private final ClientHandlerRemoteInterface ServerRemote;
    /**
     * The thisRemote representing this client connector's RMI object.
     */
    private final ClientRemoteInterface thisRemote;
    private final ServerMessageExecutor controller;
    private final Logger logger;





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
    public ClientConnectorRMI(String serverIp, ClientController controller) throws RemoteException {
        logger = Logger.getLogger(ClientConnectorRMI.class.getName());
        logger.info("Initializing Client Connector RMI");

        this.controller = controller;

        //Exporting the remote object that the server will use to connect back to this client.
        thisRemote = (ClientRemoteInterface) UnicastRemoteObject.exportObject(this, 0);

        //Connecting to the RMI server
        ServerRemote = getClientHandler(serverIp);
        logger.info("Client handler received");

        ServerRemote.handShake();

        Thread pinger = getPingerThread();
        pinger.start();
    }

    //HELPER METHOD
    /**
     * Obtains the client handler ID from the server.
     *
     * @param serverIp The IP address of the server.
     * @return The ID of the client handler.
     */
    private ClientHandlerRemoteInterface getClientHandler(String serverIp) throws ConnectException {
        try {
            //Requesting dedicated connection from the server
            logger.info("Trying to connect to server");

            //Using the socket factory to generate a socket with a reasonable connection timeout to avoid
            //waiting for 5+ minutes if the entered ip address for the server is not reachable
            RMIClientSocketFactory RMIClientSocketFactory = new RMIClientSocketFactory();
            Registry serverRegister = LocateRegistry.getRegistry(serverIp, NetworkConstants.RMIServerRegistryPort, RMIClientSocketFactory);
            ServerListenerRemoteInterface serverListenerStub = (ServerListenerRemoteInterface) serverRegister.lookup(NetworkConstants.RMIListenerStubName);
            logger.fine("Connected to server RMI listener, proceeding to request dedicated client handler.");

            //Requesting and returning client handler remote object from the server
            return serverListenerStub.getNewClientHandler(thisRemote);

        }
        catch(ConnectException e){
            //Thrown when the server does not respond, possibly wrong ip address.
            throw new ConnectException(e.getMessage());
        }
        catch (RemoteException | NotBoundException e) {
            //todo add better exception handling
            throw new RuntimeException(e);
        }
    }





    //THREAD
    /**
     * Returns a thread that when started pings the client handler to signal that this client is still alive and connected.<br>
     * Handles possible disconnections.
     */
    private Thread getPingerThread(){

        return new Thread(() -> {

            boolean serverAlive = true;
            //While loop sends pings to the server every 0.1 seconds.
            while (serverAlive) {
                try {
                    Thread.sleep(100);
                    ServerRemote.ping();
                }
                catch (InterruptedException e) {
                    String stackTrace = Utilities.StackTraceToString(e);
                    logger.warning("InterruptedException thrown in the pinger thread of the ClientConnectorRMI.\nStacktrace:\n"+stackTrace);
                    //todo add method in client to handle server death.
                }
                catch (RemoteException e) {
                    //If remote exception is thrown then the connection to the server has died and
                    //the while loop is broken
                    //todo add method in client to handle server death.
                    System.out.println("The server just died, RIP");
                    serverAlive = false;
                }
            }
        });
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
            ServerRemote.sendCSP(packet);
        }
        catch (RemoteException e){
            System.out.println("Could not send packet to the server");
            String stackTrace = Utilities.StackTraceToString(e);
            logger.warning("Remote Exception in Client while sending packet to Server Through RMI"+
                    "\nStacktrace: "+stackTrace+
                    "\nMessage type: "+packet.getClass().getSimpleName());
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