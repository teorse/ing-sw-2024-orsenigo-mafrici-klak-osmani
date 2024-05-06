package Exceptions.Server;

import Server.Network.ClientHandler.ClientHandler;

/**
 * Thrown when a user tries to create a new lobby with a name that is already taken.
 */
public class LobbyNameAlreadyTakenException extends ServerException{
    private final ClientHandler source;
    private final String lobbyName;

    public LobbyNameAlreadyTakenException(ClientHandler source, String lobbyName, String message) {
        super(message);
        this.source = source;
        this.lobbyName = lobbyName;
    }

    @Override
    public String toString() {
        return "LobbyNameAlreadyTakenException{" +
                "source=" + source +
                ", lobbyName='" + lobbyName + '\'' +
                '}';
    }
}
