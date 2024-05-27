package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

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
