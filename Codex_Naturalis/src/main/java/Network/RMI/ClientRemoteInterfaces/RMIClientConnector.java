package Network.RMI.ClientRemoteInterfaces;

import Network.ServerClientPacket.ServerClientPacket;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMIClientConnector interface defines the remote methods that can be invoked
 * on the client side by the client handler to interact with the client.
 */
public interface RMIClientConnector extends Remote {
    /**
     * Delivers a ServerClientPacket from the server to the client side for further handling.
     *
     * @param packet The ServerClientPacket received from the client.
     * @throws RemoteException If a communication-related exception occurs during the remote method invocation.
     */
    void receivePacket(ServerClientPacket packet) throws RemoteException;
}
