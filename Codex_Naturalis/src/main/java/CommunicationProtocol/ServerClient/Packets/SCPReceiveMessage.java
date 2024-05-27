package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
