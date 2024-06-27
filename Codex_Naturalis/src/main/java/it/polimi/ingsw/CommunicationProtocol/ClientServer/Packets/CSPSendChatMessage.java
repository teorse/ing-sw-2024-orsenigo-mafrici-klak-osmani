package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to send a chat message.
 * This packet encapsulates the data of a chat message record and sends it to the server.
 */
public class CSPSendChatMessage implements ClientServerPacket{

    private final ChatMessageRecord message;

    /**
     * Constructs a CSPSendChatMessage object with the specified chat message record.
     *
     * @param message The chat message record to be sent to the server.
     */
    public CSPSendChatMessage(ChatMessageRecord message) {
        this.message = message;
    }

    /**
     * Executes the operation on the server to process sending a chat message.
     * This method invokes the server's executor to handle the request for sending the chat message record.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.sendChatMessage(message);
    }
}
