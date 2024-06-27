package it.polimi.ingsw.Server.Network.ClientHandler;

import it.polimi.ingsw.Exceptions.Network.RMI.ClientDisconnectedException;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.ClientServerPacket;
import it.polimi.ingsw.CommunicationProtocol.NetworkConstants;
import it.polimi.ingsw.CommunicationProtocol.RMI.ClientRemoteInterfaces.ClientRemoteInterface;
import it.polimi.ingsw.CommunicationProtocol.RMI.ServerRemoteInterfaces.ClientHandlerRemoteInterface;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPConnectionAck;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ServerClientPacket;
import it.polimi.ingsw.Server.Controller.InputHandler.ClientInputHandler;
import it.polimi.ingsw.Server.Controller.InputHandler.InputHandler;
import it.polimi.ingsw.Utils.Utilities;

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
public class ClientHandlerRMI implements ClientHandler, ClientHandlerRemoteInterface {
    //ATTRIBUTES
    /**
     * The unique identifier for this client handler.
     */
    private final ClientRemoteInterface clientRemote;

    private boolean ping;

    private final InputHandler clientInputHandler;
    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a ClientHandlerRMI object with the specified ID, client IP address, and client ID of the client that requested this client handler.
     *
     * @param client     The remote object representing the client associated with this client handler.
     */
    public ClientHandlerRMI(ClientRemoteInterface client){
        logger = Logger.getLogger(ClientHandlerRMI.class.getName());
        logger.info("Initializing Client Handler RMI");

        this.clientRemote = client;
        clientInputHandler = new ClientInputHandler(this);
    }

    public ClientHandlerRemoteInterface getRemote(){
        try {
            logger.fine("exporting client handler object");
            return (ClientHandlerRemoteInterface) UnicastRemoteObject.exportObject(this, 0);
        }
        catch (RemoteException e) {
            String stackTrace = Utilities.StackTraceToString(e);
            logger.warning("RemoteException thrown while initializing ClientHandlerRMI for client: " + clientRemote + "\n" +
                    "Stacktrace:\n" + stackTrace);
            System.exit(666);
        }
        return null;
    }





    //RUN
    /**
     * Listens for incoming communication from the client and handles disconnections.<br>
     * Implements the heartbeat mechanism.
     */
    @Override
    public void run() {
        logger.info("Client Handler RMI "+ clientRemote +" is now listening for pings");
        //Setting up thread to handle the ping heartbeat mechanism to detect client disconnections.
        boolean receivedPing = true;

        try {
            while (receivedPing) {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //Every 0.5 seconds the thread checks if the client delivered a ping.
                //If no ping was delivered then receivedPing is false and the while loop is broken.
                receivedPing = ping;
                ping = false;
            }
            throw new ClientDisconnectedException("it/polimi/ingsw/Client " +clientRemote+" disconnected");
        }
        catch (ClientDisconnectedException e){
            logger.warning("No heartbeat detected from RMI Client: "+clientRemote);
            System.out.println("No heartbeat detected from client: "+clientRemote);
            clientInputHandler.clientDisconnectionProcedure();
        }
    }





    //CLIENT HANDLER METHODS
    /**
     * {@inheritDoc}
     * @param packet The packet to be sent to the client.
     */
    @Override
    public void sendPacket(ServerClientPacket packet) {
        logger.fine("Sending packet to client: "+clientRemote);
        try {
            clientRemote.receivePacket(packet);
        }
        catch (RemoteException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("RemoteException caught in Client Handler RMI while sending message to client: "+clientRemote+".\nName: "+e.getCause()+"\nStacktrace:\n"+stackTraceString);
        }
    }





    //REMOTE METHODS
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCSP(ClientServerPacket packet) throws RemoteException {
        logger.fine("Receiving packet from client: "+clientRemote);
        clientInputHandler.handleInput(packet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handShake() throws RemoteException {
        logger.info("Handshake called from client: "+clientRemote+"\nSending connection ack");
        sendPacket(new SCPConnectionAck("Welcome to the server, please Log in or Sign up."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ping() throws RemoteException {
        ping = true;
    }
}
