package it.polimi.ingsw.Exceptions.Server;

/**
 * Base exception class for server exceptions
 */
public class ServerException extends Exception{
    public ServerException(String message){
        super(message);
    }
}
