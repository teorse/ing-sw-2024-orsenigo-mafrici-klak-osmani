package Network.RMI.ServerRemoteInterfaces;

import Network.ClientServer.Packets.ClientServerPacket;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMIClientHandlerConnection interface defines the remote methods that can be invoked
 * on the client handler by each client for handling communication with the server.
 */
public interface RMIClientHandlerConnection extends Remote {
    /**
     * Sends a ClientServerPacket from the client handler to the server.
     *
     * @param packet The ClientServerPacket to send to the server.
     * @throws RemoteException If a communication-related exception occurs during the remote method invocation.
     */
    void sendCSP(ClientServerPacket packet) throws RemoteException;
    /**
     * The client calls this method to signal that they are ready to interact with the client handler.<br>
     * The client handler when this method is called attempts to establish the connection in the other direction
     * (Server -> Client) completing the handshake.
     *
     * @throws RemoteException If a communication-related exception occurs during the remote method invocation.
     */
    void handShake() throws RemoteException;
    /**
     * Used by the client to ping the client handler, indicating that it (the client) is still connected.<br>
     * This method is used as an implementation of a heartbeat mechanism.
     *
     * @throws RemoteException If a communication-related exception occurs during the remote method invocation.
     */
    void ping()throws RemoteException;
}
