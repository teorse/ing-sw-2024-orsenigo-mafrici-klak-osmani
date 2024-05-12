package Exceptions.Network.RMI;

public class ClientDisconnectedException extends Exception{
    public ClientDisconnectedException(String message){
        super(message);
    }
}
