package Network.RMI.ServerRemoteInterfaces;

import Network.RMI.ClientRemoteInterfaces.ClientRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMIServerListenerConnection interface defines the remote methods that can be invoked
 * on the server's RMI listener by client connections.
 */
public interface ServerListenerRemoteInterface extends Remote {
    /**
     * Creates and adds to the RMI register a new Client Handler and returns its register id.
     *
     * @param clientRemote      The remote object representing the client requesting a new client handler.
     * @return                  The remote object representing the new client handler.
     * @throws RemoteException  If a communication-related exception occurs during the remote method invocation.
     */
    ClientHandlerRemoteInterface getNewClientHandler(ClientRemoteInterface clientRemote) throws RemoteException;
}
