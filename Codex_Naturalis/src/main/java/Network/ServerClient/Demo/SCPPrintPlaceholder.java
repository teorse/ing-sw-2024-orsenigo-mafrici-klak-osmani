package Network.ServerClient.Demo;

import Network.ServerClient.Packets.ServerClientPacket;
import Network.ServerMessageExecutor;

import java.io.Serial;

/**
 * The SCMPrintPlaceholder class is a demo implementation of the ServerClientMessage interface.<br>
 * It represents a message sent from the server to the client to print a message in the console.<br>
 * This is a placeholder implementation of the System.out.println() functionality.
 */
public class SCPPrintPlaceholder implements ServerClientPacket {
    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -2706594822261290009L;
    private final String text;





    //CONSTRUCTOR
    /**
     * Constructs an SCMPrintPlaceholder object with the specified text.
     *
     * @param text The text to be printed on the client side.
     */
    public SCPPrintPlaceholder(String text){
        this.text = text;
    }





    //INTERFACE METHOD

    /**
     * {@inheritDoc}
     * @param clientController The ClientController object on the client side to execute the message.
     */
    @Override
    public void execute(ServerMessageExecutor clientController) {
        System.out.println(text);
    }
}
