package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPSendChatMessage implements ClientServerPacket{

    private final ChatMessageRecord message;

    public CSPSendChatMessage(ChatMessageRecord message) {
        this.message = message;
    }

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.sendChatMessage(message);
    }
}
