package it.polimi.ingsw.CommunicationProtocol;

/**
 * The NetworkConstants class contains commonly used shared values by the networking side of the application.
 */
public final class NetworkConstants {
    /**
     * The port number for the server socket listener.
     */
    public static final int ServerSocketListenerPort = 50000;
    /**
     * The port number for the RMI registry on servers.
     */
    public static final int RMIServerRegistryPort = 50001;
    /**
     * The port number for the RMI registry on clients.
     */
    public static final int RMIClientRegistryPort = 50002;
    /**
     * The name of the RMI listener stub on the server.
     */
    public static final String RMIListenerStubName = "ServerRMIListener";
    /**
     * The directory name for storing RMI client handler remote objects on the server's registry.
     */
    public static final String RMIClientHandlerDirectory = "RMIClientHandlers/";
    /**
     * The directory name for storing RMI client connector remote objects on the client's registry.
     */
    public static final String RMIClientConnectorDirectory = "RMIClientConnectors/";


    public static final int ServerSocketTimeOut = 10000;
}
