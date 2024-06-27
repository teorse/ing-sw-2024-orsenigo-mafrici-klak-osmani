package it.polimi.ingsw.Exceptions.Network.RMI;

/**
 * Exception thrown when a client gets disconnected from the server.
 */
public class ClientDisconnectedException extends Exception{

    /**
     * Constructs a new ClientDisconnectedException with the specified detail message.
     *
     * @param message The detail message.
     */
    public ClientDisconnectedException(String message){
        super(message);
    }
}
