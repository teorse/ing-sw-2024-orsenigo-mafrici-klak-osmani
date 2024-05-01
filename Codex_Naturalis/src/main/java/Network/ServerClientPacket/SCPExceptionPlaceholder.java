package Network.ServerClientPacket;

import Client.Controller.ClientController;

import java.io.Serial;

/**
 * The SCMExceptionPlaceholder class is an implementation of the ServerClientMessage interface.<br>
 * It represents a message sent from the server to the client to handle an exception.<br>
 * This class throws the specified runtime exception on the client side.
 */
public class SCPExceptionPlaceholder implements ServerClientPacket {
    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = 5619315912459844525L;
    private final RuntimeException exception;





    //CONSTRUCTOR
    /**
     * Constructs an SCMExceptionPlaceholder object with the specified runtime exception.
     *
     * @param exception The runtime exception to be thrown on the client side.
     */
    public SCPExceptionPlaceholder(RuntimeException exception){
        this.exception = exception;
    }





    //INTERFACE METHOD
    /**
     * Executes the message on the client side by throwing the specified runtime exception.
     *
     * @param clientController The ClientController object on the client side.
     */
    @Override
    public void execute(ClientController clientController) {
        throw exception;
    }
}
