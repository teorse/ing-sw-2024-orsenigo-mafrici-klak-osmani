package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to deliver a chat message.
 * Contains the {@code ChatMessageRecord} object representing the chat message.
 * Upon receiving this packet, the client invokes {@code receiveMessage} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPReceiveMessage implements ServerClientPacket{
    private final ChatMessageRecord chatMessage;

    /**
     * Constructs a {@code SCPReceiveMessage} instance with the specified {@code ChatMessageRecord}.
     *
     * @param chatMessage The chat message record containing the details of the received message.
     */
    public SCPReceiveMessage(ChatMessageRecord chatMessage) {
        this.chatMessage = chatMessage;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code receiveMessage} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method delivers the chat message to the client for display or processing.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.receiveMessage(chatMessage);
    }
}
