package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPReceiveMessage implements ServerClientPacket{
    private final ChatMessageRecord chatMessage;

    public SCPReceiveMessage(ChatMessageRecord chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.receiveMessage(chatMessage);
    }
}
