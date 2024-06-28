package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

public class GameAlreadyStartedException extends LobbyException{
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
