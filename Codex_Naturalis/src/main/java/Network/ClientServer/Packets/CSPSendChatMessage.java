package Network.ClientServer.Packets;

import Client.Model.Records.ChatMessageRecord;
import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPSendChatMessage implements ClientServerPacket{

    private final ChatMessageRecord message;

    public CSPSendChatMessage(ChatMessageRecord message) {
        this.message = message;
    }

    @Override
    public void execute(ServerInputExecutor executor) {
        executor.sendChatMessage(message);
    }
}
