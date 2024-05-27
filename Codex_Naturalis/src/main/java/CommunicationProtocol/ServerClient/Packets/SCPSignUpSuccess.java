package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPSignUpSuccess implements ServerClientPacket{

    private final String username;

    public SCPSignUpSuccess(String username) {
        this.username = username;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.signUpSuccess(username);
    }
}
