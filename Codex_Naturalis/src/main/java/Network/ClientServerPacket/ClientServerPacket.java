package Network.ClientServerPacket;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The ClientServerPacket class represents a packet sent from the client to the server.
 * It encapsulates communication between the client and the server.
 */
public record ClientServerPacket(List<String> header, List<String> payload) implements Serializable {
    @Serial
    private static final long serialVersionUID = 3011854096212180959L;





    //CONSTRUCTOR

    /**
     * Constructs a ClientServerPacket with the specified header and payload.
     *
     * @param header  The list of strings representing the header of the packet.<br>
     *                The first position determines the execution layer (Server, Lobby, Game, etc.).<br>
     *                The second position identifies the command to be executed (Place_Card, Draw_Card, etc.).<br>
     * @param payload The list of strings representing the payload of the packet.<br>
     *                It carries the values for the parameters to be passed when executing the methods specified in the header.
     */
    public ClientServerPacket {
    }





    /**
     * Returns the string representation of the ClientServerPacket.
     *
     * @return A string representation of the ClientServerPacket, including its header and payload.
     */
    @Override
    public String toString() {
        StringBuilder result;
        result = new StringBuilder("Header: ");
        for (String head : header)
            result.append(head).append(" ");

        result.append("\n").append("Payload: ");

        for (String pay : payload)
            result.append(pay).append(" ");

        return result.toString();
    }
}
