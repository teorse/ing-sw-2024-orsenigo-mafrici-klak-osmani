package Network.RMI.ServerRemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMIServerListenerConnection interface defines the remote methods that can be invoked
 * on the server's RMI listener by client connections.
 */
public interface RMIServerListenerConnection extends Remote {
    /**
     * Creates and adds to the RMI register a new Client Handler and returns its register id.
     *
     * @param clientID The ID of the client requesting a new client handler.
     * @return The ID of the new client handler.
     * @throws RemoteException If a communication-related exception occurs during the remote method invocation.
     */
    String getNewClientHandler(String clientID) throws RemoteException;
}
