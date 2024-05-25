package Network.ServerClient.Packets;

import Client.Model.Records.ChatMessageRecord;
import Network.ServerClient.ServerMessageExecutor;

public class SCPReceiveMessage implements ServerClientPacket{
    private final ChatMessageRecord chatMessage;

    public SCPReceiveMessage(ChatMessageRecord chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.receiveMessage(chatMessage);
    }
}
